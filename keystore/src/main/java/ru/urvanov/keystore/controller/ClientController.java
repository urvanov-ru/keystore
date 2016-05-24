package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
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
import org.springframework.web.servlet.ModelAndView;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.controller.domain.CodeName;
import ru.urvanov.keystore.controller.domain.CodeNamesResponse;
import ru.urvanov.keystore.controller.domain.IdName;
import ru.urvanov.keystore.controller.domain.IdNamesResponse;
import ru.urvanov.keystore.controller.domain.ListResponse;
import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListItem;
import ru.urvanov.keystore.domain.ClientListParameters;
import ru.urvanov.keystore.domain.ClientType;
import ru.urvanov.keystore.domain.DictClientGroup;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.exception.ClientNameNotUniqueException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.DictClientGroupService;
import ru.urvanov.keystore.service.MailService;

@RequestMapping(value = "/client")
@Controller
public class ClientController {

    private static final Logger logger = LoggerFactory
            .getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DictClientGroupService dictClientGroupService;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private DataSource dataSource;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody ListResponse<ClientListItem> list(
            @RequestParam(value = "active", required = false) Boolean activeFilter,
            @RequestParam(value = "dictClientGroupId", required = false) Long dictClientGroupIdFilter,
            @RequestParam(value = "activeBegin", required = false) Date activeBeginFilter,
            @RequestParam(value = "activeEnd", required = false) Date activeEndFilter,
            @RequestParam(value = "dictActionId", required = false) Long dictActionId,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/client/list started.");
        logger.debug("active=" + activeFilter);
        logger.debug("dictClientGroupIdFilter=" + dictClientGroupIdFilter);
        logger.debug("activeBegin=" + activeBeginFilter);
        logger.debug("activeEnd=" + activeEndFilter);
        ClientListParameters params = new ClientListParameters();
        params.setUserId(userDetailsImpl.getUser().getId());
        params.setActive(activeFilter);
        params.setDictClientGroupId(dictClientGroupIdFilter);
        params.setActiveBegin(activeBeginFilter);
        params.setActiveEnd(activeEndFilter);
        params.setDictActionId(dictActionId);
        ListResponse<ClientListItem> result = new ListResponse<>();
        result.setInfo(clientService.list(params));
        ClientListParameters countParams = new ClientListParameters();
        countParams.setUserId(userDetailsImpl.getUser().getId());
        result.setTotalRecords(clientService.countList(countParams).longValue());
        result.setSuccess(true);
        logger.info("/client/list finished.");
        return result;
    }

    @RequestMapping(value = "/clientTypes", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse clientTypes() {
        logger.info("/client/clientTypes started");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < ClientType.values().length; n++) {
            result.getInfo().add(
                    new CodeName(ClientType.values()[n].name(), messageSource
                            .getMessage(
                                    "ru.urvanov.keystore.domain.ClientType."
                                            + ClientType.values()[n], null,
                                    null)));
        }
        result.setSuccess(true);
        logger.info("/client/clientTypes finished.");
        return result;
    }

    @RequestMapping(value = "/dictClientGroups", method = RequestMethod.GET)
    public @ResponseBody IdNamesResponse dictClientGroups() {
        logger.info("/client/dictClientGroups started.");
        IdNamesResponse result = new IdNamesResponse();
        for (DictClientGroup dcg : dictClientGroupService.findAll()) {
            result.getInfo().add(new IdName(dcg.getId(), dcg.getName()));
        }
        result.setSuccess(true);
        logger.info("/client/dictClientGroups finished.");
        return result;
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<ClientJson> edit(
            @RequestParam(value = "id") Long id) {
        logger.info("/client/edit started.");
        ObjectResponse<ClientJson> result = new ObjectResponse<>();
        Client client = clientService.findById(id);
        ClientJson info = new ClientJson();
        result.setInfo(info);
        info.setClientType(client.getClientType());
        info.setContactPersonEmail(client.getContactPersonEmail());
        info.setContactPersonName(client.getContactPersonName());
        info.setContactPersonPhone(client.getContactPersonPhone());
        info.setId(String.valueOf(client.getId()));
        info.setIec(client.getIec());
        info.setItn(client.getItn());
        info.setName(client.getName());
        info.setJuridicalPersonName(client.getJuridicalPersonName());
        info.setDictClientGroupId(String.valueOf(client.getDictClientGroup()
                .getId()));
        info.setDictClientGroupName(client.getDictClientGroup().getName());
        info.setJuridicalPersonAddress(client.getJuridicalPersonAddress());
        info.setBankName(client.getBankName());
        info.setAccount(client.getAccount());
        info.setCorrAccount(client.getCorrAccount());
        info.setBic(client.getBic());
        result.setSuccess(true);
        logger.info("/client/edit failed.");
        return result;
    }

    @RequestMapping(value = "/save", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid ClientJson clientJson,
            BindingResult bindingResult,
            @CurrentUser UserDetailsImpl userDetailsImpl) throws ClientNameNotUniqueException,
            UserNameNotUniqueException {
        logger.info("/client/save started.");
        Client client = null;

        if (clientJson.getId() == null || clientJson.getId().length() == 0) {
            client = new Client();
            client.setKeyActivationMode(KeyActivationMode.AUTOMATIC);
            client.setActive(true);
            client.setAuthority("ROLE_CLIENT");
        } else {
            client = clientService.findById(Long.valueOf(clientJson.getId()));
        }
        client.setClientType(clientJson.getClientType());
        client.setContactPersonEmail(clientJson.getContactPersonEmail());
        client.setContactPersonName(clientJson.getContactPersonName());
        client.setContactPersonPhone(clientJson.getContactPersonPhone());
        DictClientGroup dictClientGroup = dictClientGroupService.findById(Long
                .valueOf(clientJson.getDictClientGroupId()));
        client.setDictClientGroup(dictClientGroup);
        client.setIec(clientJson.getIec());
        client.setItn(clientJson.getItn());
        client.setJuridicalPersonName(clientJson.getJuridicalPersonName());
        client.setName(clientJson.getName());
        
        boolean isServiceUser = false;
        for (GrantedAuthority grantedAuthority : userDetailsImpl.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals("ROLE_SERVICE"))
                isServiceUser = true;
        }
        if (isServiceUser) {
            client.setJuridicalPersonAddress(clientJson.getJuridicalPersonAddress());
            client.setBankName(clientJson.getBankName());
            client.setAccount(clientJson.getAccount());
            client.setCorrAccount(clientJson.getCorrAccount());
            client.setBic(clientJson.getBic());
        }
        clientService.save(client);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/client/save finished.");
        return result;
    }

    public static final class ClientJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 2130612513612202228L;

        private String id;

        @NotNull
        @Size(min = 1, max = 1024)
        private String name;

        @NotNull
        private ClientType clientType;

        @Size(min = 1, max = 255)
        @NotNull
        private String contactPersonName;

        @Size(min = 1, max = 50)
        @NotNull
        private String contactPersonEmail;

        @Size(min = 1, max = 50)
        @NotNull
        private String contactPersonPhone;

        @Size(max = 1024)
        private String juridicalPersonName;

        @Size(max = 12)
        private String itn;

        @Size(min = 9, max = 9)
        private String iec;

        @NotNull
        @Size(min = 1)
        private String dictClientGroupId;
        private String dictClientGroupName;

        @Size(max = 1024)
        private String juridicalPersonAddress;

        @Size(max = 1024)
        private String bankName;

        @Size(max = 20, min = 20)
        private String account;

        @Size(max = 20, min = 20)
        private String corrAccount;

        @Size(max = 9, min = 9)
        private String bic;

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

        public ClientType getClientType() {
            return clientType;
        }

        public void setClientType(ClientType clientType) {
            this.clientType = clientType;
        }

        public String getContactPersonName() {
            return contactPersonName;
        }

        public void setContactPersonName(String contactPersonName) {
            this.contactPersonName = contactPersonName;
        }

        public String getContactPersonEmail() {
            return contactPersonEmail;
        }

        public void setContactPersonEmail(String contactPersonEmail) {
            this.contactPersonEmail = contactPersonEmail;
        }

        public String getContactPersonPhone() {
            return contactPersonPhone;
        }

        public void setContactPersonPhone(String contactPersonPhone) {
            this.contactPersonPhone = contactPersonPhone;
        }

        public String getJuridicalPersonName() {
            return juridicalPersonName;
        }

        public void setJuridicalPersonName(String juridicalPersonName) {
            this.juridicalPersonName = juridicalPersonName;
        }

        public String getItn() {
            return itn;
        }

        public void setItn(String itn) {
            this.itn = itn;
        }

        public String getIec() {
            return iec;
        }

        public void setIec(String iec) {
            this.iec = iec;
        }

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

        public String getJuridicalPersonAddress() {
            return juridicalPersonAddress;
        }

        public void setJuridicalPersonAddress(String juridicalPersonAddress) {
            this.juridicalPersonAddress = juridicalPersonAddress;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getCorrAccount() {
            return corrAccount;
        }

        public void setCorrAccount(String corrAccount) {
            this.corrAccount = corrAccount;
        }

        public String getBic() {
            return bic;
        }

        public void setBic(String bic) {
            this.bic = bic;
        }

    }

    @RequestMapping(value = "/block", method = { RequestMethod.POST })
    public @ResponseBody SimpleResponse block(@RequestBody IdsJson idsJson) {
        logger.info("/client/block started.");
        for (String id : idsJson.getIds()) {
            clientService.blockById(Long.valueOf(id));
        }
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/client/block finished.");
        return result;
    }

    public static final class IdsJson implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1990689168623467208L;
        private String[] ids = new String[0];

        public String[] getIds() {
            return ids;
        }

        public void setIds(String[] ids) {
            this.ids = ids;
        }

    }

    @RequestMapping(value = "/sendNotification", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse sendNotification(
            @RequestBody SendNotificationParam sendNotificationParam) {
        logger.info("/client/sendNotification started.");

        StringBuilder errorBuilder = new StringBuilder();
        SimpleResponse result = new SimpleResponse();
        Arrays.stream(sendNotificationParam.getIds())
                .map(str -> {
                    return Long.valueOf(str);
                })
                .map(id -> {
                    return clientService.findById(id);
                })
                .forEach(
                        client -> {
                            try {
                                mailService.sendNotification(client,
                                        sendNotificationParam.getTitle(),
                                        sendNotificationParam.getBody());
                            } catch (Exception ex) {
                                logger.error("sendNotification failed.", ex);
                                errorBuilder.append(client
                                        .getContactPersonEmail() + "; ");
                            }
                        });
        if (errorBuilder.length() == 0) {
            result.setSuccess(true);
            logger.info("/client/sendNotification finished.");
            return result;
        } else {
            errorBuilder.insert(0,
                    "Не удалось отправить уведомления на e-mail: ");
            result.setMessage(errorBuilder.toString());
            logger.info("/client/sendNotification finished with errors.");
            return result;
        }
    }

    public static final class SendNotificationParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -6305659062144518569L;
        private String[] ids = new String[0];
        private String title;
        private String body;

        public String[] getIds() {
            return ids;
        }

        public void setIds(String[] ids) {
            this.ids = ids;
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

    }

    @RequestMapping(value = "/exportXls/**", method = RequestMethod.GET)
    public ModelAndView export(@CurrentUser UserDetailsImpl userDetailsImpl)
            throws IOException, SQLException {
        logger.info("/client/export started.");
        if (userDetailsImpl.getAuthorities().stream()
                .filter(g -> g.getAuthority().equals("ROLE_SERVICE")).count() == 0) {
            return new ModelAndView("forbidden");
        }
        ModelAndView modelAndView = new ModelAndView("jasperClientList");
        modelAndView.addObject("userId", userDetailsImpl.getUser().getId());
        modelAndView.addObject("dataSource", dataSource);
        logger.info("/client/export finished.");
        return modelAndView;
    }

    @RequestMapping(value = "/giveDictServiceType", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse giveDictServiceType(
            @RequestBody GiveDictServiceTypeParam giveDictServiceTypeParam) {
        logger.info("/client/giveDictServiceType started.");
        for (Long dictServiceTypeId : giveDictServiceTypeParam
                .getDictServiceTypeIds()) {
            for (Long clientId : giveDictServiceTypeParam.getClientIds()) {
                Client client = clientService.findById(clientId);
                clientService.giveDictServiceType(client, dictServiceTypeId);
            }
        }
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/client/giveDictServiceType finished.");
        return result;
    }

    public static final class GiveDictServiceTypeParam implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 4067451418955514283L;
        private List<Long> dictServiceTypeIds = new ArrayList<Long>();

        private List<Long> clientIds = new ArrayList<Long>();

        public List<Long> getDictServiceTypeIds() {
            return dictServiceTypeIds;
        }

        public void setDictServiceTypeIds(List<Long> dictServiceTypeIds) {
            this.dictServiceTypeIds = dictServiceTypeIds;
        }

        public List<Long> getClientIds() {
            return clientIds;
        }

        public void setClientIds(List<Long> clientIds) {
            this.clientIds = clientIds;
        }

    }

    public static class RemoveLinkClientDictServiceTypeParam implements
            Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 137711566648275754L;
        private Long clientId;
        private List<Long> dictServiceTypeIds = new ArrayList<Long>();

        public Long getClientId() {
            return clientId;
        }

        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public List<Long> getDictServiceTypeIds() {
            return dictServiceTypeIds;
        }

        public void setDictServiceTypeIds(List<Long> dictServiceTypeIds) {
            this.dictServiceTypeIds = dictServiceTypeIds;
        }

    }

    @RequestMapping(value = "/removeLinkClientDictServiceType", method = {
            RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody SimpleResponse removeLinkClientDictServiceType(
            @RequestBody RemoveLinkClientDictServiceTypeParam param) {
        logger.info("/client/removeLinkClientDictServiceType started");
        Client client = clientService.findById(param.getClientId());

        clientService.removeDictServiceType(client, param
                .getDictServiceTypeIds().toArray(new Long[0]));
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/client/removeLinkClientDictServiceType finished.");
        return result;
    }

    @RequestMapping(value = "/giveDictAction", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse giveDictAction(
            @RequestBody GiveDictActionParam giveDictActionParam) {
        logger.info("/client/giveDictAction started.");
        for (Long dictActionId : giveDictActionParam.getDictActionIds()) {
            for (Long clientId : giveDictActionParam.getClientIds()) {
                Client client = clientService.findById(clientId);
                clientService.giveDictAction(client, dictActionId);
            }
        }
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/client/giveDictAction finished.");
        return result;
    }

    public static class GiveDictActionParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 63472251733576352L;
        private List<Long> clientIds = new ArrayList<Long>();
        private List<Long> dictActionIds = new ArrayList<Long>();

        public List<Long> getClientIds() {
            return clientIds;
        }

        public void setClientIds(List<Long> clientIds) {
            this.clientIds = clientIds;
        }

        public List<Long> getDictActionIds() {
            return dictActionIds;
        }

        public void setDictActionIds(List<Long> dictActionIds) {
            this.dictActionIds = dictActionIds;
        }

    }

    @RequestMapping(value = "/removeLinkClientDictAction", method = {
            RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody SimpleResponse removeLinkClientDictActione(
            @RequestBody RemoveLinkClientDictActionParam param) {
        logger.info("/client/removeLinkClientDictAction started");
        Client client = clientService.findById(param.getClientId());

        clientService.removeDictAction(client, param.getDictActionIds()
                .toArray(new Long[0]));
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/client/removeLinkClientDictAction finished.");
        return result;
    }

    public static class RemoveLinkClientDictActionParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 4896874058765812026L;
        private Long clientId;
        private List<Long> dictActionIds = new ArrayList<Long>();

        public Long getClientId() {
            return clientId;
        }

        public void setClientId(Long clientId) {
            this.clientId = clientId;
        }

        public List<Long> getDictActionIds() {
            return dictActionIds;
        }

        public void setDictActionIds(List<Long> dictActionIds) {
            this.dictActionIds = dictActionIds;
        }

    }
}
