package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.controller.domain.CodeName;
import ru.urvanov.keystore.controller.domain.CodeNamesResponse;
import ru.urvanov.keystore.controller.domain.IdName;
import ru.urvanov.keystore.controller.domain.IdNamesResponse;
import ru.urvanov.keystore.controller.domain.KeyParams;
import ru.urvanov.keystore.controller.domain.KeyParamsResponse;
import ru.urvanov.keystore.controller.domain.ListResponse;
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.DictServiceTypeStatus;
import ru.urvanov.keystore.domain.KeyData;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.DictServiceTypeService;
import ru.urvanov.keystore.service.KeyService;

@Controller
@RequestMapping(value = "/dictServiceType")
public class DictServiceTypeController {

    private static final Logger logger = LoggerFactory
            .getLogger(DictServiceTypeController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DictServiceTypeService dictServiceTypeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private KeyService keyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody ListResponse<DictServiceTypeResponse> list() {
        logger.info("/dictServiceType/list started.");
        ListResponse<DictServiceTypeResponse> result = new ListResponse<>();
        dictServiceTypeService
                .findAll()
                .stream()
                .forEach(
                        (dst) -> {
                            DictServiceTypeResponse r = new DictServiceTypeResponse();
                            r.setId(dst.getId());
                            r.setName(dst.getName());
                            r.setDescription(dst.getDescription());
                            r.setStatusName(messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.DictServiceTypeStatus."
                                            + dst.getStatus().name(), null,
                                    null));
                            r.setAmount(dst.getAmount());
                            r.setAmount30Days(dst.getAmount30Days());
                            result.getInfo().add(r);
                        });
        result.setTotalRecords((long) result.getInfo().size());
        result.setSuccess(true);
        logger.info("/dictServiceType/list finished.");
        return result;
    }

    public class DictServiceTypeResponse {
        private Long id;
        private String name;
        private String description;
        private String statusName;
        private BigDecimal amount;
        private BigDecimal amount30Days;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
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

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getAmount30Days() {
            return amount30Days;
        }

        public void setAmount30Days(BigDecimal amount30Days) {
            this.amount30Days = amount30Days;
        }

        @Override
        public String toString() {
            return "DictServiceTypeResult id=" + id + ", name=" + name;
        }
    }

    @RequestMapping(value = "/dictServiceTypeStatuses", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse dictServiceTypeStatuses() {
        logger.info("/dictServiceType/dictServiceTypeStatuses started.");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < DictServiceTypeStatus.values().length; n++) {
            result.getInfo()
                    .add(new CodeName(
                            DictServiceTypeStatus.values()[n].name(),
                            messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.DictServiceTypeStatus."
                                            + DictServiceTypeStatus.values()[n],
                                    null, null)));
        }
        result.setSuccess(true);
        logger.info("/dictServiceType/dictServiceTypeStatuses finished.");
        return result;
    }

    @RequestMapping(value = "/keySave", method = RequestMethod.POST)
    public @ResponseBody SimpleResponse keySave(@RequestBody KeyParams keyParams)
             {
        logger.info("dictServiceType/keySave started.");
        keyParams.toLog();

        KeyData keyData = new KeyData();

        DictServiceType dictServiceType = dictServiceTypeService.findById(Long
                .valueOf(keyParams.getDictServiceTypeId()));

        String stringKey = keyService.generateKey(keyData);

        dictServiceType.setBaseKey(stringKey);

        dictServiceTypeService.save(dictServiceType);

        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("dictServiceType/keySave finished.");
        return result;
    }

    @RequestMapping(value = "/keyEdit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody KeyParamsResponse keyEdit(
            @RequestParam(value = "dictServiceTypeId") Long dictServiceTypeId,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws IOException {
        logger.info("/dictServiceType/keyEdit started.");
        KeyParamsResponse result = new KeyParamsResponse();
        if (userDetailsImpl.getAuthorities().stream()
                .filter(g -> g.getAuthority().equals("ROLE_SERVICE")).count() == 0) {
            result.setMessage("ru.urvanov.keystore.forbidden");
            return result;
        }
        DictServiceType dictServiceType = dictServiceTypeService
                .findById(dictServiceTypeId);
        if (dictServiceType == null) {
            logger.error("/dictServiceType/keyEdit failed. Not found.");
            result.setMessage("РџР°РєРµС‚ РЅРµ РЅР°Р№РґРµРЅ.");
            return result;
        }
        String baseKey = dictServiceType.getBaseKey();

        if (baseKey != null && !baseKey.isEmpty()) {
            KeyData keyData = new KeyData();// parse from baseKey

            KeyParams keyParams = new KeyParams();
            result.setKeyParams(keyParams);
            //keyParams.setKind(keyData.getKind());
            //keyParams.setDateBegin(keyData.getBeginDate());
            //keyParams.setDateEnd(keyData.getEndDate());
            //keyParams.setPassword(keyData.getPassword());
            //keyParams.setEthnosImportEnabled(keyData.isEthnosImportEnabled());
            //keyParams.setEthnosExportEnabled(keyData.isEthnosExportEnabled());
            //keyParams.setXlsEnabled(keyData.isXlsEnabled());
            //keyParams.setOfficeEnabled(keyData.isOfficeEnabled());
            //keyParams.setCsvEnabled(keyData.isCsvEnabled());
            //keyParams.setXlsxEnabled(keyData.isXlsxEnabled());
            //keyParams.setQuestionnaireLimit(keyData.getQuestionnaireLimit());
            //keyParams.setqLimitPerDay(keyData.getqLimitPerDay());
            //keyParams.setqLimitPerWeek(keyData.getqLimitPerWeek());
            //keyParams.setqLimitPerMonth(keyData.getqLimitPerMonth());
            //keyParams.setDevicesLimit(keyData.getDevicesLimit());
        }
        result.setSuccess(true);
        logger.info("/dictServiceType/keyEdit finished.");
        return result;
    }

    public class DictServiceTypeEditResponse extends SimpleResponse {
        /**
         * 
         */
        private static final long serialVersionUID = -6692885927194226768L;
        public DictServiceType dictServiceType;

        public DictServiceType getDictServiceType() {
            return dictServiceType;
        }

        public void setDictServiceType(DictServiceType dictServiceType) {
            this.dictServiceType = dictServiceType;
        }

    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody DictServiceTypeEditResponse edit(
            @RequestParam(value = "id") Long id) {
        logger.info("/dictServiceType/edit started.");
        DictServiceTypeEditResponse result = new DictServiceTypeEditResponse();
        result.setDictServiceType(dictServiceTypeService.findById(id));
        result.setSuccess(true);
        logger.info("/dictServiceType/edit finished.");
        return result;
    }

    @RequestMapping(value = "/save", method = { RequestMethod.POST })
    public @ResponseBody SimpleResponse save(
            @RequestBody DictServiceType dictServiceType) {
        logger.info("/dictServiceType/save started.");
        SimpleResponse result = new SimpleResponse();
        DictServiceType dct = dictServiceType.getId() == null ? new DictServiceType()
                : dictServiceTypeService.findById(dictServiceType.getId());

        dct.setName(dictServiceType.getName());
        dct.setDescription(dictServiceType.getDescription());
        dct.setAmount(dictServiceType.getAmount());
        dct.setAmount30Days(dictServiceType.getAmount30Days());
        dct.setStatus(dictServiceType.getStatus());
        dictServiceTypeService.save(dct);
        result.setSuccess(true);
        logger.info("/dictServiceType/save finished.");
        return result;
    }

    @RequestMapping(value = "/query", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody IdNamesResponse query(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "start") Integer start,
            @RequestParam(value = "limit") Integer limit) {
        logger.info("/dictServiceType/query started.");
        IdNamesResponse result = new IdNamesResponse();
        List<DictServiceType> lst = dictServiceTypeService.findByName(query,
                start, limit);
        List<IdName> idNames = new ArrayList<IdName>();
        lst.stream().forEachOrdered((item) -> {
            idNames.add(new IdName(item.getId(), item.getName()));
        });
        result.setInfo(idNames);
        result.setSuccess(true);
        logger.info("/dictServiceType/query finished.");
        return result;
    }

    @RequestMapping(value = "/select", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ListResponse<DictServiceTypeResponse> select(
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/dictServiceType/select started.");
        ListResponse<DictServiceTypeResponse> result = new ListResponse<>();
        Stream<DictServiceType> stream = null;
        boolean isService = false;
        for (GrantedAuthority ga : userDetailsImpl.getAuthorities()) {
            if (ga.getAuthority().equals("ROLE_SERVICE"))
                isService = true;
        }
        if (isService) {
            stream = dictServiceTypeService.findAll().stream();
        } else {
            stream = dictServiceTypeService.findByClient(
                    clientService.findFullById(userDetailsImpl.getUser()
                            .getClient().getId())).stream();
        }
        stream.forEach((dst) -> {
            DictServiceTypeResponse r = new DictServiceTypeResponse();
            r.setId(dst.getId());
            r.setName(dst.getName());
            r.setDescription(dst.getDescription());
            r.setStatusName(messageSource.getMessage(
                    "ru.urvanov.keystore.domain.DictServiceTypeStatus."
                            + dst.getStatus().name(), null, null));
            r.setAmount(dst.getAmount());
            r.setAmount30Days(dst.getAmount30Days());
            result.getInfo().add(r);
        });
        result.setSuccess(true);
        logger.info("/dictServiceType/select finished.");
        return result;
    }

    @RequestMapping(value = "/linkClientDictServiceType", method = {
            RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody ListResponse<DictServiceTypeResponse> linkClientDictServiceType(
            @RequestParam(value = "clientId") Long clientId,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/dictServiceType/linkClientDictServiceType started.");
        ListResponse<DictServiceTypeResponse> result = new ListResponse<>();
        clientService
                .findFullById(clientId)
                .getLinkClientDictServiceTypes()
                .stream()
                .forEach(
                        (dst) -> {
                            DictServiceTypeResponse r = new DictServiceTypeResponse();
                            r.setId(dst.getId());
                            r.setName(dst.getName());
                            r.setDescription(dst.getDescription());
                            r.setStatusName(messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.DictServiceTypeStatus."
                                            + dst.getStatus().name(), null,
                                    null));
                            r.setAmount(dst.getAmount());
                            r.setAmount30Days(dst.getAmount30Days());
                            result.getInfo().add(r);
                        });
        result.setSuccess(true);
        logger.info("/dictServiceType/linkClientDictServiceType finished.");
        return result;
    }
}
