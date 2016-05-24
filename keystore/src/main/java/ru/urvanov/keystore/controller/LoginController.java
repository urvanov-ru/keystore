package ru.urvanov.keystore.controller;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.controller.domain.CaptchaResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientType;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.exception.ClientNameNotUniqueException;
import ru.urvanov.keystore.exception.InvalidActivationKeyException;
import ru.urvanov.keystore.exception.NoSuchUserException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.GlobalSettingsService;
import ru.urvanov.keystore.service.UserService;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory
            .getLogger(LoginController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleMailMessage templateMessage;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${recaptcha.public.key}")
    private String publicKey;

    @Value("${recaptcha.private.key}")
    private String privateKey;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = { "/security/loginfailed" }, method = RequestMethod.GET)
    public String loginFailed(
            @RequestParam(value = "error", required = false) String error,
            Model model) {
        String errorMessageCode = "";
        if ("captcha".equals(error)) {
            errorMessageCode = "ru.urvanov.keystore.invalid_captcha";
        } else {
            errorMessageCode = "ru.urvanov.keystore.invalid_username_or_password";
        }
        ReCaptcha c = ReCaptchaFactory.newReCaptcha(publicKey, privateKey,
                false);
        model.addAttribute("captcha", c.createRecaptchaHtml(null, null));
        model.addAttribute("errorMessageCode", errorMessageCode);
        return "security/loginfailed";
    }

    @RequestMapping(value = { "security/captchaurl" }, method = RequestMethod.GET)
    public @ResponseBody CaptchaResponse captchaUrl() {
        CaptchaResponse result = new CaptchaResponse();
        result.setSuccess(true);
        ReCaptcha c = ReCaptchaFactory.newReCaptcha(publicKey, privateKey,
                false);
        result.setCaptchaHtml(c.createRecaptchaHtml(null, null));
        return result;
    }

    @RequestMapping(value = { "/security/captcha" }, method = RequestMethod.GET)
    public String captcha(Model model) {
        ReCaptcha c = ReCaptchaFactory.newReCaptcha(publicKey, privateKey,
                false);
        model.addAttribute("captcha", c.createRecaptchaHtml(null, null));
        return "security/captcha";
    }

    @RequestMapping(value = { "/security/register" }, method = RequestMethod.POST)
    public @ResponseBody SimpleResponse register(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "clientType") ClientType clientType,
            @RequestParam(value = "contactPersonName") String contactPersonName,
            @RequestParam(value = "contactPersonEmail") String contactPersonEmail,
            @RequestParam(value = "contactPersonPhone") String contactPersonPhone,
            @RequestParam(value = "recaptcha_response_field") String recaptcha_response_field,
            @RequestParam(value = "recaptcha_challenge_field") String recaptcha_challenge_field,
            @RequestParam(value = "password1") String password1,
            @RequestParam(value = "password2") String password2) {
        try {
            if (name == null || clientType == null || contactPersonName == null
                    || contactPersonEmail == null || contactPersonPhone == null
                    || recaptcha_response_field == null
                    || recaptcha_challenge_field == null || password1 == null
                    || password2 == null) {
                logger.info("security/register invalid params");
                SimpleResponse result = new SimpleResponse();
                result.setMessage(messageSource.getMessage(
                        "ru.urvanov.keystore.invalid_params", null, null));
                return result;
            }
            SimpleResponse result = new SimpleResponse();

            if (!password1.equals(password2)) {
                result.setSuccess(false);
                result.setMessage(messageSource.getMessage(
                        "ru.urvanov.keystore.passwords_are_different", null,
                        null));
            }
            Client client = new Client();
            client.setName(name);
            client.setClientType(clientType);
            client.setContactPersonName(contactPersonName);
            client.setContactPersonEmail(contactPersonEmail);
            client.setContactPersonPhone(contactPersonPhone);
            client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
            
            client.setActive(true);
            client.setDictClientGroup(globalSettingsService.read()
                    .getDefaultDictClientGroup());
            client.setAuthority("ROLE_CLIENT");
            clientService.save(client, password1);

            result.setSuccess(true);
            return result;
        } catch (ClientNameNotUniqueException cnnue) {
            logger.error("/security/register failed.", cnnue);
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Клиент с таким названием уже есть в базе.");
            return result;
        } catch (UserNameNotUniqueException unnue) {
            logger.error("/security/register failed.", unnue);
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Пользователь с таким e-mail уже есть в базе.");
            return result;
        } catch (MailException me) {
            logger.error("/security/register failed.", me);
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Не удалось отправить письмо на указанный email.");
            return result;
        }
    }

    @RequestMapping(value = "/security/repeatActivationLetter", method = {
            RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody SimpleResponse repeatActivationLetter(
            @RequestParam(value = "userName") String userName)
            throws UserNameNotUniqueException {
        try {
            logger.info("/security/repeatActivationLetter started.");
            User user = userDao.findByUserName(userName.toLowerCase());
            if (user == null) {
                SimpleResponse result = new SimpleResponse();
                result.setSuccess(false);
                result.setMessage("Пользователь с таким логином не найден.");
                return result;
            }
            userService.sendActivationLetter(user);
            SimpleResponse result = new SimpleResponse();
            result.setSuccess(true);
            logger.info("/security/repeatActivationLetter failed.");
            return result;
        } catch (MailException me) {
            logger.error("/security/register failed.", me);
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Не удалось отправить письмо на указанный email.");
            return result;
        }
    }

    @RequestMapping(value = "/security/sendpassword", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse sendPassword(
            @RequestParam(value = "userName") String userName)
            throws UserNameNotUniqueException {

        logger.info("/security/sendpassword started.");
        SimpleResponse result = new SimpleResponse();
        try {
            userService.sendPassword(userName);
        } catch (MailException mailException) {
            result.setMessage("Не удалось отправить письмо на указанный e-mail.");
        } catch (NoSuchUserException noSuchUserException) {
            result.setMessage("Пользователь с таким e-mail (" + userName
                    + ") не найден.");
        }
        result.setSuccess(true);
        logger.info("/security/sendpassword finished.");
        return result;
    }

    @RequestMapping(value = "/security/changepassword", method = {
            RequestMethod.POST, RequestMethod.GET })
    public @ResponseBody SimpleResponse changePassword(
            @RequestParam(value = "newPassword") String newPassword,
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "key") String key) {
        logger.info("/security/changepassword started.");
        if (newPassword == null || newPassword.equals("") || id == null
                || key == null) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.invalid_params", null, null));
            return result;
        }
        try {
            userService.changePassword(id, newPassword, key);
        } catch (NoSuchUserException nsue) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Пользователь не найден");
            return result;
        }
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/security/changepassword finished.");
        return result;

    }

    @RequestMapping(value = "/security/accountactivation", method = RequestMethod.GET)
    public @ResponseBody SimpleResponse accountActivation(
            @RequestParam("key") String key, @RequestParam("id") Long id)
            throws UserNameNotUniqueException {
        logger.info("/security/accountactivation started.");

        try {
            userService.activate(id, key);
            SimpleResponse result = new SimpleResponse();
            result.setSuccess(true);
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.your_account_activated", null, null));
            logger.info("/security/accountactivation finished.");
            return result;
        } catch (InvalidActivationKeyException iaue) {
            logger.info("/security/accountactivation finished.");
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.invalid_activation_url", null, null));
            return result;
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

}
