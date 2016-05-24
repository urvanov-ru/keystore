package ru.urvanov.keystore.controller;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientType;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.exception.ClientNameNotUniqueException;
import ru.urvanov.keystore.exception.UserNameNotUniqueException;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.ClientService;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
    private static final Logger logger = LoggerFactory
            .getLogger(AccountController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientService clientService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<ClientJson> edit(
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/account/edit started.");
        Long id = userDetailsImpl.getUser().getClient().getId();
        ObjectResponse<ClientJson> result = new ObjectResponse<ClientJson>();
        Client client = clientService.findById(id);
        ClientJson info = new ClientJson();
        result.setInfo(info);
        info.setName(client.getName());
        info.setClientType(client.getClientType());
        info.setContactPersonEmail(client.getContactPersonEmail());
        info.setName(client.getName());
        info.setContactPersonName(client.getContactPersonName());
        info.setContactPersonPhone(client.getContactPersonPhone());
        info.setJuridicalPersonName(client.getJuridicalPersonName());
        info.setItn(client.getItn());
        info.setIec(client.getIec());
        info.setUniqueId(client.getUniqueId());
        info.setJuridicalPersonAddress(client.getJuridicalPersonAddress());
        info.setBankName(client.getBankName());
        info.setAccount(client.getAccount());
        info.setCorrAccount(client.getCorrAccount());
        info.setBic(client.getBic());

        result.setSuccess(true);
        logger.info("/account/edit finished.");
        return result;
    }

    public static class ClientJson implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = -3708636706019763832L;

        @Column
        @Size(min = 1, max = 1024)
        @NotNull
        private String name;

        @Enumerated(EnumType.STRING)
        @Column(name = "client_type")
        @NotNull
        private ClientType clientType;

        private String uniqueId;

        @Column(name = "contact_person_name")
        @Size(min = 1, max = 255)
        @NotNull
        private String contactPersonName;

        @Column(name = "contact_person_email")
        @Size(min = 1, max = 50)
        @NotNull
        private String contactPersonEmail;

        @Size(min = 1, max = 50)
        @NotNull
        @Column(name = "contact_person_phone")
        private String contactPersonPhone;

        @Column(name = "itn")
        @Size(max = 12)
        private String itn;

        @Size(min = 9, max = 9)
        @Column(name = "iec")
        private String iec;

        @Size(max = 1024)
        @Column(name = "juridical_person_name")
        private String juridicalPersonName;
        
        @Size(max=1024)
        private String juridicalPersonAddress;
        
        @Size(max=1024)
        private String bankName;
        
        @Size(max=20, min=20)
        private String account;
        
        @Size(max=20, min=20)
        private String corrAccount;
        
        @Size(max=9, min=9)
        private String bic;

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

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
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

        public String getJuridicalPersonName() {
            return juridicalPersonName;
        }

        public void setJuridicalPersonName(String juridicalPersonName) {
            this.juridicalPersonName = juridicalPersonName;
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

    @RequestMapping(value = "/save", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid ClientJson clientJson,
            BindingResult bindingResult,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws ClientNameNotUniqueException, UserNameNotUniqueException {
        logger.info("/account/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        Long id = userDetailsImpl.getUser().getClient().getId();
        Client savedClient = clientService.findById(id);
        savedClient.setClientType(clientJson.getClientType());
        savedClient.setContactPersonEmail(clientJson.getContactPersonEmail());
        savedClient.setName(clientJson.getName());
        savedClient.setContactPersonName(clientJson.getContactPersonName());
        savedClient.setContactPersonPhone(clientJson.getContactPersonPhone());
        savedClient.setJuridicalPersonName(clientJson.getJuridicalPersonName());
        savedClient.setItn(clientJson.getItn());
        savedClient.setIec(clientJson.getIec());
        savedClient.setJuridicalPersonAddress(clientJson.getJuridicalPersonAddress());
        savedClient.setBankName(clientJson.getBankName());
        savedClient.setAccount(clientJson.getAccount());
        savedClient.setCorrAccount(clientJson.getCorrAccount());
        savedClient.setBic(clientJson.getBic());
        clientService.save(savedClient);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/account/save finished.");
        return result;
    }
    
    @RequestMapping(value = "/changePassword", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody SimpleResponse changePassword(@RequestParam(value = "newPassword") String newPassword,
            @CurrentUser UserDetailsImpl userDetailsImpl){
        logger.info("/account/changePassword started.");
        clientService.changePassword(clientService.findById(userDetailsImpl.getUser().getClient().getId()), newPassword);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/account/changePassword finished.");
        return result;
    }
}
