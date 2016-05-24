package ru.urvanov.keystore.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.controller.domain.CodeName;
import ru.urvanov.keystore.controller.domain.CodeNamesResponse;
import ru.urvanov.keystore.controller.domain.ListResponse;
import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.Sex;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.domain.UserListItem;
import ru.urvanov.keystore.domain.UserListParameters;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody ListResponse<UserListItem> list(
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/user/list started.");
        ListResponse<UserListItem> result = new ListResponse<>();
        UserListParameters params = new UserListParameters();
        params.setUserId(userDetailsImpl.getUser().getId());
        List<UserListItem> info = userService.list(params);
        result.setInfo(info);

        UserListParameters countParams = new UserListParameters();
        countParams.setUserId(userDetailsImpl.getUser().getId());
        result.setTotalRecords(userService.countList(countParams).longValue());
        result.setSuccess(true);
        logger.info("/user/list finished.");
        return result;
    }

    public static class UserJson extends SimpleResponse {

        /**
         * 
         */
        private static final long serialVersionUID = 3530799141806089457L;

        private String id;
        private String fullName;
        private Sex sex;
        private String userName;
        private String phone;
        private String post;
        private Date birthdate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Sex getSex() {
            return sex;
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        @Override
        public String toString() {
            return "UserJson userName=" + userName;
        }
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<UserJson> edit(
            @RequestParam(value = "id") Long id,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/user/edit started.");
        User user = userService.findById(id);
        UserJson info = new UserJson();
        info.setId(String.valueOf(user.getId()));
        info.setFullName(user.getFullName());
        info.setUserName(user.getUserName());
        info.setSex(user.getSex());
        info.setPhone(user.getPhone());
        info.setPost(user.getPost());
        info.setBirthdate(user.getBirthdate());
        ObjectResponse<UserJson> result = new ObjectResponse<>();
        result.setInfo(info);
        result.setSuccess(true);
        logger.info("/user/edit finished.");
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse save(@RequestBody UserJson userJson,
            BindingResult bindingResult,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws UserNameNotUniqueException {
        logger.info("/user/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        Long clientId = userDetailsImpl.getUser().getClient().getId();
        User user = null;
        if (userJson.getId() == null || userJson.getId().length() == 0) {
            user = new User();
            user.setPassword(bcryptEncoder.encode(RandomStringUtils
                    .randomAlphanumeric(10)));
        } else {
            user = userService.findById(Long.valueOf(userJson.getId()));
        }

        user.setEnabled(true);
        user.setActivated(false);
        Map<UserAccessCode, UserAccess> userAccesses = new HashMap<UserAccessCode, UserAccess>();
        user.setUserAccesses(userAccesses);
        Set<Authority> authorities = new HashSet<Authority>();
        Authority authority = new Authority();
        authority.setUser(user);
        authority.setAuthority("ROLE_CLIENT");
        authorities.add(authority);
        user.setAuthorities(authorities);
        user.setClient(clientService.findById(clientId));
        user.setUserName(userJson.getUserName());
        user.setFullName(userJson.getFullName());
        user.setPhone(userJson.getPhone());
        user.setPost(userJson.getPost());
        user.setBirthdate(userJson.getBirthdate());
        user.setSex(userJson.getSex());

        userService.save(user);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/user/save finished.");
        return result;
    }

    @RequestMapping(value = "/sexes", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse sexes() {
        logger.info("/user/sexes started.");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < Sex.values().length; n++) {
            result.getInfo().add(
                    new CodeName(Sex.values()[n].name(), messageSource
                            .getMessage("ru.urvanov.keystore.domain.Sex."
                                    + Sex.values()[n], null, null)));
        }
        result.setSuccess(true);
        logger.info("/user/sexes finished.");
        return result;
    }

    @RequestMapping(value = "/block", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse block(
            @RequestParam(value = "id") Long id,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/user/block started.");
        userService.block(userService.findById(id));
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/user/block finished.");
        return result;
    }

    @RequestMapping(value = "/access", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody UserAccessResponse access(
            @RequestParam(value = "id") Long id,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        // try {
        logger.info("/user/access started.");
        User fullUser = userService.findFullById(id);
        Map<UserAccessCode, UserAccess> userAccesses = userService
                .getUserAccesses(fullUser);
        Map<String, Boolean> info = new HashMap<String, Boolean>();
        for (Entry<UserAccessCode, UserAccess> entry : userAccesses.entrySet()) {
            info.put(entry.getKey().name(), entry.getValue().getAccess());
        }
        UserAccessResponse result = new UserAccessResponse();
        result.setSuccess(true);
        result.setInfo(info);
        for (Authority authority : fullUser.getAuthorities()) {
            result.getAuthorities().add(authority.getAuthority());
        }
        logger.info("/user/access finished.");
        return result;
        // } catch (Exception ex) {
        // logger.error("/user/access failed.", ex);
        // UserAccessResponse result = new UserAccessResponse();
        // result.setMessage(messageSource.getMessage(
        // "ru.urvanov.keystore.internal_error", null, null));
        // return result;
        // }
    }

    public static class UserAccessResponse extends SimpleResponse {

        /**
         * 
         */
        private static final long serialVersionUID = -5617372968384885467L;

        private Map<String, Boolean> info = new HashMap<String, Boolean>();

        private List<String> authorities = new ArrayList<>();

        public Map<String, Boolean> getInfo() {
            return info;
        }

        public void setInfo(Map<String, Boolean> info) {
            this.info = info;
        }

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

    }

    @RequestMapping(value = "/saveAccess", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse saveAccess(
            @RequestBody SaveAccessJson saveAccessJson,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws UserNameNotUniqueException {
        logger.info("/user/saveAccess started.");
        Long id = Long.valueOf(saveAccessJson.getId());
        User user = userService.findFullById(id);

        Map<UserAccessCode, UserAccess> mapUserAccesses = user
                .getUserAccesses();
        for (Entry<UserAccessCode, Boolean> entry : saveAccessJson.getInfo()
                .entrySet()) {
            UserAccess userAccess = mapUserAccesses.get(entry.getKey());
            if (userAccess == null) {
                userAccess = new UserAccess();
                userAccess.setCode(entry.getKey());
                userAccess.setUser(user);
            }
            userAccess.setAccess(entry.getValue());
            mapUserAccesses.put(entry.getKey(), userAccess);
        }
        userService.save(user);

        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/user/saveAccess finished.");
        return result;
    }

    public static class SaveAccessJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -5819045313018839758L;
        private String id;
        private Map<UserAccessCode, Boolean> info = new HashMap<UserAccessCode, Boolean>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Map<UserAccessCode, Boolean> getInfo() {
            return info;
        }

        public void setInfo(Map<UserAccessCode, Boolean> info) {
            this.info = info;
        }

    }
}
