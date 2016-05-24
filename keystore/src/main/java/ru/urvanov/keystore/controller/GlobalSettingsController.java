package ru.urvanov.keystore.controller;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.DictClientGroup;
import ru.urvanov.keystore.domain.GlobalSettings;
import ru.urvanov.keystore.service.DictClientGroupService;
import ru.urvanov.keystore.service.GlobalSettingsService;

@Controller
@RequestMapping(value = "/globalSettings")
public class GlobalSettingsController {

    private static final Logger logger = LoggerFactory
            .getLogger(GlobalSettingsController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private DictClientGroupService dictClientGroupService;

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<SettingsJson> edit() {
        logger.info("/settings/edit started.");
        ObjectResponse<SettingsJson> result = new ObjectResponse<>();
        SettingsJson info = new SettingsJson();
        result.setInfo(info);

        GlobalSettings globalSettings = globalSettingsService.read();
        DictClientGroup defaultDictClientGroup = globalSettings
                .getDefaultDictClientGroup();
        if (defaultDictClientGroup != null) {
            info.setDictClientGroupId(String.valueOf(defaultDictClientGroup
                    .getId()));
            info.setDictClientGroupName(defaultDictClientGroup.getName());
        }
        info.setSessionStoreDays(globalSettings.getSessionStoreDays());
        result.setSuccess(true);
        logger.info("/settings/edit finished.");
        return result;
    }

    public static class SettingsJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -7488057987822864308L;

        @NotNull
        private String dictClientGroupId;

        private String dictClientGroupName;

        @NotNull
        @Min(1)
        @Max(Integer.MAX_VALUE)
        private Integer sessionStoreDays;

        public String getDictClientGroupId() {
            return dictClientGroupId;
        }

        public void setDictClientGroupId(String dictClientGroupId) {
            this.dictClientGroupId = dictClientGroupId;
        }

        public String getDictClientGroupName() {
            return dictClientGroupName;
        }

        public void setDictClientGroupName(String dictClientGroupName) {
            this.dictClientGroupName = dictClientGroupName;
        }

        public Integer getSessionStoreDays() {
            return sessionStoreDays;
        }

        public void setSessionStoreDays(Integer sessionStoreDays) {
            this.sessionStoreDays = sessionStoreDays;
        }

        @Override
        public String toString() {
            return "SettingsJson dictClientGroupId=" + dictClientGroupId
                    + ", sessionStoreDays=" + sessionStoreDays;
        }
    }

    @RequestMapping(value = "/save", method = { RequestMethod.POST })
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid SettingsJson settingsJson,
            BindingResult bindingResult) {
        logger.info("/settings/save started.");
        GlobalSettings globalSettings = globalSettingsService.read();
        globalSettings
                .setDefaultDictClientGroup(dictClientGroupService
                        .getReference(Long.valueOf(settingsJson
                                .getDictClientGroupId())));
        globalSettings.setSessionStoreDays(settingsJson.getSessionStoreDays());
        globalSettingsService.save(globalSettings);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/settings/save finished.");
        return result;
    }
}
