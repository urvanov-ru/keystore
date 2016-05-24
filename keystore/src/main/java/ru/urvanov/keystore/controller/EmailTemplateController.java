package ru.urvanov.keystore.controller;

import java.io.Serializable;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.controller.domain.ListResponse;
import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.service.DictEventService;

@Controller
@RequestMapping(value = "/emailTemplate")
public class EmailTemplateController {

    private static final Logger logger = LoggerFactory
            .getLogger(EmailTemplateController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DictEventService dictEventService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody ListResponse<EmailTemplateListItem> list() {
        logger.info("/emailTemplate/list started.");

        ListResponse<EmailTemplateListItem> result = new ListResponse<>();
        dictEventService
                .findAll()
                .stream()
                .forEach(
                        dictEvent -> {
                            EmailTemplateListItem item = new EmailTemplateListItem();
                            item.setId(String.valueOf(dictEvent.getId()));
                            item.setCodeName(messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.DictEventCode."
                                            + dictEvent.getCode().name(), null,
                                    null));
                            result.getInfo().add(item);
                        });
        result.setTotalRecords((long) result.getInfo().size());
        result.setSuccess(true);
        logger.info("/emailTemplate/list finished.");
        return result;
    }

    public static final class EmailTemplateListItem implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -3639907315821379521L;

        private String id;
        private String codeName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        @Override
        public String toString() {
            return "EmailTemplateListItem id=" + id + ", codeName=" + codeName;
        }
    }

    public static final class EmailTemplateJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1172767264746282115L;

        private String id;
        private String codeName;

        @NotNull
        @Size(min = 1, max = 1024)
        private String title;

        @NotNull
        @Size(min = 1)
        private String body;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCodeName() {
            return codeName;
        }

        public void setCodeName(String codeName) {
            this.codeName = codeName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        @Override
        public String toString() {
            return "EmailTemplateJson id=" + id + ", title = " + title
                    + ", body=" + body;
        }
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<EmailTemplateJson> edit(
            @RequestParam(value = "id") Long id) {
        logger.info("/emailTemplate/edit started.");
        DictEvent dictEvent = dictEventService.findById(id);
        if (dictEvent == null) {
            ObjectResponse<EmailTemplateJson> result = new ObjectResponse<>();
            result.setMessage("Событие с id=" + id + " не найдено.");
            return result;
        }
        ObjectResponse<EmailTemplateJson> result = new ObjectResponse<>();
        EmailTemplateJson info = new EmailTemplateJson();
        result.setInfo(info);

        info.setId(String.valueOf(dictEvent.getId()));
        info.setCodeName(messageSource.getMessage(
                "ru.urvanov.keystore.domain.DictEventCode."
                        + dictEvent.getCode().name(), null, null));
        info.setTitle(dictEvent.getTitle());
        info.setBody(dictEvent.getBody());
        result.setSuccess(true);
        logger.info("/emailTemplate/edit finished.");
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid EmailTemplateJson emailTemplateJson,
            BindingResult bindingResult) {
        logger.info("/emailTemplate/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        Long id = Long.valueOf(emailTemplateJson.getId());
        DictEvent dictEvent = dictEventService.findById(id);
        if (dictEvent == null) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Событие с id=" + id + " не найдено.");
            return result;
        }
        dictEvent.setTitle(emailTemplateJson.getTitle());
        dictEvent.setBody(emailTemplateJson.getBody());
        dictEventService.save(dictEvent);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/emailTemplate/save finished.");
        return result;

    }

}
