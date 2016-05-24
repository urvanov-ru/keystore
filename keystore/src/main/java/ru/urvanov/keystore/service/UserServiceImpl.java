package ru.urvanov.keystore.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserListItem;
import ru.urvanov.keystore.domain.UserListParameters;
import ru.urvanov.keystore.exception.InvalidActivationKeyException;
import ru.urvanov.keystore.exception.NoSuchUserException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory
            .getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private SimpleMailMessage templateMessage;

    @Value("${application.url}")
    private String applicationUrl;

    @Autowired
    private MailService mailService;

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findFullById(Long id) {
        return userDao.findFullById(id);
    }

    @Override
    public User getReference(Long id) {
        return userDao.getReference(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(User user) throws UserNameNotUniqueException {
        boolean newUser = user.getId() == null;
        if (!userDao.checkUserNameUnique(user))
            throw new UserNameNotUniqueException();
        userDao.save(user);
        if (newUser) {
            user = findById(user.getId());
            sendActivationLetter(user);
            mailService.sendNotificationNewUser(user);
        } else {
            mailService.sendNotificationChangeUser(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findFullByUserName(String username) {
        return userDao.findFullByUserName(username);
    }

    @Transactional
    @Override
    public List<UserListItem> list(UserListParameters params) {
        return userDao.list(params);
    }

    @Transactional
    @Override
    public BigInteger countList(UserListParameters params) {
        return userDao.countList(params);
    }

    @Override
    public void sendActivationLetter(User user)
            throws UserNameNotUniqueException {
        String contactPersonEmail = user.getUserName();
        byte[] hash = bcryptEncoder.encode("" + Math.random()).getBytes();

        // converting byte array to Hexadecimal String
        StringBuilder sb = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            sb.append(String.format("%02x", b & 0xff));
        }
        String key = sb.toString();

        user.setActivationKey(key);
        save(user);

        // Create a thread safe "copy" of the template message and customize
        // it
        // May be I should read mail template from database???
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(contactPersonEmail);
        msg.setText("Уважаемый " + user.getFullName()
                + ", пожалуйста, воспользуйтесь ссылкой " + applicationUrl
                + "/site/resources/sencha/index.html?type=accountactivation"
                + "&key=" + key + "&id=" + user.getId()
                + " для активации аккаунта");
        this.mailSender.send(msg);
    }

    @Transactional
    @Override
    public void block(User user) {
        user.setEnabled(false);
        try {
            save(user);
            mailService.sendNotificationBlockUser(user);
        } catch (UserNameNotUniqueException e) {
            logger.error("blockById failed. id=" + user.getId(), e);
        }
    }

    @Transactional
    @Override
    public Map<UserAccessCode, UserAccess> getUserAccesses(User user) {
        return findFullById(user.getId()).getUserAccesses();
    }

    @Transactional
    @Override
    public void activate(Long id, String key)
            throws InvalidActivationKeyException {
        User user = userDao.findById(id);
        if (user != null && user.getActivationKey() != null
                && user.getActivationKey().equals(key)) {
            user.setActivationKey(null);
            user.setActivated(true);
            userDao.save(user);
        } else {
            throw new InvalidActivationKeyException();
        }
    }

    @Override
    public void sendPassword(String userName) throws NoSuchUserException {
        User user = userDao.findByUserName(userName);
        if (user == null) {
            throw new NoSuchUserException(userName);
        }
        byte[] hash = bcryptEncoder.encode("" + Math.random()).getBytes();

        // converting byte array to Hexadecimal String
        StringBuilder sb = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            sb.append(String.format("%02x", b & 0xff));
        }
        String changePasswordKey = sb.toString();

        user.setChangePasswordKey(changePasswordKey);
        userDao.save(user);

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(userName);
        msg.setText("Уважаемый" + " " + user.getFullName()
                + ", пожалуйста, воспользуйтесь ссылкой " + applicationUrl
                + "/site/resources/sencha/index.html?type=changepassword"
                + "&key=" + changePasswordKey + "&id=" + user.getId()
                + " для задания нового пароля.");
        this.mailSender.send(msg);

    }

    @Override
    public void changePassword(Long id, String newPassword, String key)
            throws NoSuchUserException {
        User user = userDao.findById(id);
        if (user == null) {
            throw new NoSuchUserException();
        }
        if (user.getChangePasswordKey().equals(key)) {
            user.setChangePasswordKey(null);
            user.setPassword(bcryptEncoder.encode(newPassword));
            userDao.save(user);
        }
    }

}
