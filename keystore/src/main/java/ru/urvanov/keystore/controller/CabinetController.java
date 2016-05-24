package ru.urvanov.keystore.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;
import ru.urvanov.keystore.domain.LinkUserDictEventNotification;
import ru.urvanov.keystore.domain.Sex;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.service.DictEventService;
import ru.urvanov.keystore.service.UserService;

@Controller
@RequestMapping(value = "/cabinet")
public class CabinetController {

    private static final Logger logger = LoggerFactory
            .getLogger(CabinetController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DictEventService dictEventService;

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public @ResponseBody ObjectResponse<CabinetJson> cabinet(
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/cabinet/edit started.");
        ObjectResponse<CabinetJson> result = new ObjectResponse<>();
        CabinetJson info = new CabinetJson();
        result.setInfo(info);
        Long userId = userDetailsImpl.getUser().getId();
        User user = userService.findFullById(userId);
        info.setFullName(user.getFullName());
        info.setUserName(user.getUserName());
        info.setPhone(user.getPhone());
        info.setBirthdate(user.getBirthdate());
        info.setPost(user.getPost());
        info.setSex(user.getSex());
        for (Entry<DictEvent, LinkUserDictEventNotification> entry : user
                .getLinkUserDictEventNotifications().entrySet()) {
            info.getNotifications().put(entry.getKey().getCode(),
                    entry.getValue().getAllowNotification());
        }
        result.setSuccess(true);
        logger.info("/cabinet/edit finished.");
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid CabinetJson cabinetJson,
            BindingResult bindingResult,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws UserNameNotUniqueException {
        logger.info("/cabinet/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        Long userId = userDetailsImpl.getUser().getId();
        User user = userService.findFullById(userId);
        user.setFullName(cabinetJson.getFullName());
        user.setUserName(cabinetJson.getUserName());
        user.setPhone(cabinetJson.getPhone());
        user.setBirthdate(cabinetJson.getBirthdate());
        user.setPost(cabinetJson.getPost());
        user.setSex(cabinetJson.getSex());
        Map<DictEvent, LinkUserDictEventNotification> notifications = user
                .getLinkUserDictEventNotifications();
        for (Entry<DictEventCode, Boolean> entry : cabinetJson
                .getNotifications().entrySet()) {
            DictEvent dictEvent = dictEventService.findByCode(entry.getKey());
            LinkUserDictEventNotification luden = notifications.get(dictEvent);
            if (luden == null) {
                luden = new LinkUserDictEventNotification();
                luden.setUser(user);
                luden.setDictEvent(dictEvent);
            }
            luden.setAllowNotification(entry.getValue());
            notifications.put(dictEvent, luden);
        }
        userService.save(user);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/cabinet/save finished.");
        return result;
    }

    public static class CabinetJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 3329791236107906908L;
        private String id;

        @Size(max = 1024)
        private String fullName;

        @NotNull
        @Size(max = 50)
        private String userName;

        @Size(max = 50)
        private String phone;

        private Sex sex;
        private Date birthdate;

        @Size(max = 50)
        private String post;

        private Map<DictEventCode, Boolean> notifications = new HashMap<DictEventCode, Boolean>();

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

        public Sex getSex() {
            return sex;
        }

        public void setSex(Sex sex) {
            this.sex = sex;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public Map<DictEventCode, Boolean> getNotifications() {
            return notifications;
        }

        public void setNotifications(Map<DictEventCode, Boolean> notifications) {
            this.notifications = notifications;
        }

        @Override
        public String toString() {
            return "CabinetJson id=" + id + ", fullName=" + fullName
                    + ", userName=" + userName;
        }

    }
}
