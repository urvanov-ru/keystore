package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictActionType;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.DictActionService;
import ru.urvanov.keystore.service.DictServiceTypeService;

@Controller
@RequestMapping(value = "/dictAction")
public class DictActionController {
    private static final Logger logger = LoggerFactory
            .getLogger(DictActionController.class);

    @Autowired
    private DictActionService dictActionService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DictServiceTypeService dictServiceTypeService;
    
    @Autowired
    private ClientService clientService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody ListResponse<DictActionListItem> list() {
        logger.info("/dictAction/list started.");
        ListResponse<DictActionListItem> result = new ListResponse<>();
        List<DictAction> lst = dictActionService.findAll();
        List<DictActionListItem> info = new ArrayList<>();
        result.setInfo(info);
        for (DictAction da : lst) {
            DictActionListItem dictActionListItem = new DictActionListItem();
            dictActionListItem.setId(String.valueOf(da.getId()));
            dictActionListItem.setName(da.getName());
            dictActionListItem.setDictActionTypeName(messageSource.getMessage(
                    "ru.urvanov.keystore.domain.DictActionType."
                            + da.getDictActionType(), null, null));
            dictActionListItem.setDateBegin(da.getDateBegin());
            dictActionListItem.setDateEnd(da.getDateEnd());
            dictActionListItem.setDescription(da.getDescription());
            dictActionListItem.setForNewClients(da.getForNewClients());
            dictActionListItem.setDictServiceTypeName(da.getDictServiceType()
                    .getName());
            info.add(dictActionListItem);
        }
        result.setTotalRecords(Long.valueOf(result.getInfo().size()));
        result.setSuccess(true);
        logger.info("/dictAction/list finished.");
        return result;
    }

    public static class DictActionListItem implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = -8896329630546579177L;

        private String id;
        private String name;
        private String description;
        private Date dateBegin;
        private Date dateEnd;
        private String dictServiceTypeName;
        private String dictActionTypeName;
        private Boolean forNewClients;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getDateBegin() {
            return dateBegin;
        }

        public void setDateBegin(Date dateBegin) {
            this.dateBegin = dateBegin;
        }

        public Date getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getDictServiceTypeName() {
            return dictServiceTypeName;
        }

        public void setDictServiceTypeName(String dictServiceTypeName) {
            this.dictServiceTypeName = dictServiceTypeName;
        }

        public String getDictActionTypeName() {
            return dictActionTypeName;
        }

        public void setDictActionTypeName(String dictActionTypeName) {
            this.dictActionTypeName = dictActionTypeName;
        }

        public Boolean getForNewClients() {
            return forNewClients;
        }

        public void setForNewClients(Boolean forNewClients) {
            this.forNewClients = forNewClients;
        }

        @Override
        public String toString() {
            return "DictActionJson id=" + id + ", name=" + name
                    + ", description=" + description;
        }

    }

    @RequestMapping(value = "/dictActionTypes", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse dictActionTypes() {
        logger.info("/dictAction/dictActionTypes started");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < DictActionType.values().length; n++) {
            result.getInfo().add(
                    new CodeName(DictActionType.values()[n].name(),
                            messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.DictActionType."
                                            + DictActionType.values()[n], null,
                                    null)));
        }
        result.setSuccess(true);
        logger.info("/dictAction/dictActionTypes finished.");
        return result;
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<DictActionJson> edit(
            @RequestParam(value = "id") Long id) {
        logger.info("/dictAction/edit started.");
        DictAction dictAction = dictActionService.findById(id);
        if (dictAction == null) {
            ObjectResponse<DictActionJson> result = new ObjectResponse<>();
            result.setMessage("РђРєС†РёСЏ СЃ id=" + id + " РЅРµ РЅР°Р№РґРµРЅР°.");
            return result;
        }
        ObjectResponse<DictActionJson> result = new ObjectResponse<>();
        DictActionJson info = new DictActionJson();
        result.setInfo(info);
        info.setId(String.valueOf(dictAction.getId()));
        info.setName(dictAction.getName());
        info.setDescription(dictAction.getDescription());
        info.setDictServiceTypeId(dictAction.getDictServiceType() == null ? null
                : String.valueOf(dictAction.getDictServiceType().getId()));
        info.setDictServiceTypeName(dictAction.getDictServiceType() == null ? null
                : dictAction.getDictServiceType().getName());
        info.setDictActionType(dictAction.getDictActionType());
        info.setDateBegin(dictAction.getDateBegin());
        info.setDateEnd(dictAction.getDateEnd());
        info.setForNewClients(dictAction.getForNewClients());
        result.setSuccess(true);
        logger.info("/dictAction/edit finished.");
        return result;
    }

    public static class DictActionJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 6618075563714316426L;
        private String id;
        @NotNull
        @Size(min = 1, max = 100)
        private String name;

        @NotNull
        @Size(min = 1, max = 1024)
        private String description;

        private Date dateBegin;

        private Date dateEnd;

        @NotNull
        @Size(min = 1)
        private String dictServiceTypeId;

        private String dictServiceTypeName;

        @NotNull
        private DictActionType dictActionType;

        @NotNull
        private Boolean forNewClients;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getDateBegin() {
            return dateBegin;
        }

        public void setDateBegin(Date dateBegin) {
            this.dateBegin = dateBegin;
        }

        public Date getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getDictServiceTypeId() {
            return dictServiceTypeId;
        }

        public void setDictServiceTypeId(String dictServiceTypeId) {
            this.dictServiceTypeId = dictServiceTypeId;
        }

        public String getDictServiceTypeName() {
            return dictServiceTypeName;
        }

        public void setDictServiceTypeName(String dictServiceTypeName) {
            this.dictServiceTypeName = dictServiceTypeName;
        }

        public DictActionType getDictActionType() {
            return dictActionType;
        }

        public void setDictActionType(DictActionType dictActionType) {
            this.dictActionType = dictActionType;
        }

        public Boolean getForNewClients() {
            return forNewClients;
        }

        public void setForNewClients(Boolean forNewClients) {
            this.forNewClients = forNewClients;
        }

    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid DictActionJson dictActionJson,
            BindingResult bindingResult) {
        logger.info("/dictAction/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        DictAction dictAction = null;
        if (dictActionJson.getId() == null || "".equals(dictActionJson.getId())) {
            dictAction = new DictAction();
        } else {
            dictAction = dictActionService.findById(Long.valueOf(dictActionJson
                    .getId()));
        }
        dictAction.setName(dictActionJson.getName());
        dictAction.setDescription(dictActionJson.getDescription());
        dictAction.setDictServiceType(dictServiceTypeService.getReference(Long
                .valueOf(dictActionJson.getDictServiceTypeId())));
        dictAction.setDictActionType(dictActionJson.getDictActionType());
        dictActionService.save(dictAction);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/dictAction/save finished.");
        return result;
    }

    @RequestMapping(value = "/exportXls/**", method = RequestMethod.GET)
    public String exportXls(Model model) {
        model.addAttribute("dataSource", dictActionService.findAll());
        return "jasperDictActionList";
    }
    
    
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public @ResponseBody ListResponse<DictActionListItem> select() {
        logger.info("/dictAction/select started.");
        ListResponse<DictActionListItem> result = new ListResponse<>();
        List<DictAction> lst = dictActionService.findAll();
        List<DictActionListItem> info = new ArrayList<>();
        result.setInfo(info);
        for (DictAction da : lst) {
            DictActionListItem dictActionJson = new DictActionListItem();
            dictActionJson.setId(String.valueOf(da.getId()));
            dictActionJson.setName(da.getName());
            dictActionJson.setDictActionTypeName(messageSource.getMessage(
                    "ru.urvanov.keystore.domain.DictActionType."
                            + da.getDictActionType(), null, null));
            dictActionJson.setDateBegin(da.getDateBegin());
            dictActionJson.setDateEnd(da.getDateEnd());
            dictActionJson.setDescription(da.getDescription());
            dictActionJson.setForNewClients(da.getForNewClients());
            dictActionJson.setDictServiceTypeName(da.getDictServiceType()
                    .getName());
            info.add(dictActionJson);
        }
        result.setSuccess(true);
        logger.info("/dictAction/select finished.");
        return result;
    }
    
    @RequestMapping(value = "/linkClientDictAction", method = {
            RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody ListResponse<DictActionListItem> linkClientDictAction(
            @RequestParam(value = "clientId") Long clientId,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/dictServiceType/linkClientDictAction started.");
        ListResponse<DictActionListItem> result = new ListResponse<>();
        clientService
                .findFullById(clientId)
                .getLinkClientDictActions()
                .stream()
                .forEach(
                        (da) -> {
                            DictActionListItem r = new DictActionListItem();
                            r.setId(String.valueOf(da.getId()));
                            r.setName(da.getName());
                            r.setDictActionTypeName(messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.DictActionType."
                                            + da.getDictActionType(), null, null));
                            r.setDateBegin(da.getDateBegin());
                            r.setDateEnd(da.getDateEnd());
                            r.setDescription(da.getDescription());
                            r.setForNewClients(da.getForNewClients());
                            r.setDictServiceTypeName(da.getDictServiceType()
                                    .getName());
                            result.getInfo().add(r);
                        });
        result.setSuccess(true);
        logger.info("/dictAction/linkClientDictAction finished.");
        return result;
    }
    
    @RequestMapping(value="/start", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody SimpleResponse start(@RequestParam(value="id") Long id, @RequestParam(value="dateEnd") Date dateEnd) {
        logger.info("/dictAction/start started.");
        try {
            dictActionService.start(id, dateEnd);
        } catch (IOException e) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage("Нет базового ключа для пакета, выбранного по акции.");
            return result;
        }
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/dictAction/start finished.");
        return result;
    }
}
