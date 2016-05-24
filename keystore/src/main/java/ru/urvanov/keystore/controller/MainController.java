package ru.urvanov.keystore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.UserAccess;
import ru.urvanov.keystore.domain.UserAccessCode;
import ru.urvanov.keystore.domain.UserDetailsImpl;

@Controller
@RequestMapping(value = "/main")
public class MainController {

    private static final Logger logger = LoggerFactory
            .getLogger(MainController.class);

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/checkSession", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody SimpleResponse checkSession() {
        logger.info("/main/checkSession started.");
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/main/checkSession finished.");
        return result;
    }
    
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public @ResponseBody UserInfoResponse userFullName() {
        logger.info("/main/userInfo started.");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
        UserInfoResponse result = new UserInfoResponse();
        result.setSuccess(true);
        result.setFullName(userDetailsImpl.getUser().getFullName());
        for (GrantedAuthority ga : userDetailsImpl.getAuthorities()) {
            result.getAuthorities().add(ga.getAuthority());
        }

        for (UserAccess userAccess : userDetailsImpl.getUser()
                .getUserAccesses().values()) {
            result.getAccesses().put(userAccess.getCode(),
                    userAccess.getAccess());
        }

        logger.info("/main/userInfo finished");
        return result;
    }

    public static class UserInfoResponse extends SimpleResponse {

        /**
         * 
         */
        private static final long serialVersionUID = -8359898079902009489L;

        private String fullName;

        private List<String> authorities = new ArrayList<String>();

        private Map<UserAccessCode, Boolean> accesses = new HashMap<UserAccessCode, Boolean>();

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public List<String> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<String> authorities) {
            this.authorities = authorities;
        }

        public Map<UserAccessCode, Boolean> getAccesses() {
            return accesses;
        }

        public void setAccesses(Map<UserAccessCode, Boolean> accesses) {
            this.accesses = accesses;
        }

    }

}
