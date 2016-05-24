package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
import ru.urvanov.keystore.domain.Key;
import ru.urvanov.keystore.domain.KeyListItem;
import ru.urvanov.keystore.domain.KeyListParameters;
import ru.urvanov.keystore.domain.KeyStatus;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.KeyService;

@Controller
@RequestMapping(value = "/key")
public class KeyController {

    private static final Logger logger = LoggerFactory
            .getLogger(KeyController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private KeyService keyService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/keyStatuses", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse keyStatuses() {
        logger.info("/key/keyStatuses");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < KeyStatus.values().length; n++) {
            result.getInfo()
                    .add(new CodeName(
                            KeyStatus.values()[n].name(),
                            messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.KeyStatus."
                                            + KeyStatus.values()[n], null, null)));
        }
        result.setSuccess(true);
        logger.info("/key/keyStatuses");
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public @ResponseBody ListResponse<KeyListItem> list(
            @RequestParam(value = "clientName", required = false) String clientNameFilter,
            @RequestParam(value = "status", required = false) KeyStatus statusFilter,
            @RequestParam(value = "activeOnDate", required = false) Date activeOnDateFilter,
            @RequestParam(value = "orderId", required = false) Long orderIdFilter) {
        logger.info("/key/list started.");
        logger.debug("clientName=" + clientNameFilter);
        logger.debug("keyStatus=" + statusFilter);
        logger.debug("status=" + statusFilter);
        logger.debug("activeOnDate=" + activeOnDateFilter);
        logger.debug("orderId=" + orderIdFilter);

        ListResponse<KeyListItem> result = new ListResponse<>();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
        Long userId = userDetailsImpl.getUser().getId();

        KeyListParameters param = new KeyListParameters();
        param.setUserId(userId);
        param.setClientName(clientNameFilter);
        param.setStatus(statusFilter);
        param.setActiveOnDate(activeOnDateFilter);
        param.setOrderId(orderIdFilter);
        result.setInfo(keyService.list(param));

        KeyListParameters countParam = new KeyListParameters();
        countParam.setUserId(userId);
        result.setTotalRecords(keyService.countList(countParam)
                .longValueExact());

        result.setSuccess(true);
        logger.info("/key/list finished.");
        return result;
    }

    @RequestMapping(value = "/activate", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse activate(
            @RequestParam(value = "id") Long id,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws  IOException {
        logger.info("/key/activate started.");
        Key key = keyService.findById(id);
        Long keyClientId = key.getClient().getId();
        if (!userDetailsImpl.getUser().getClient().getId().equals(keyClientId)) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.forbidden", null, null));
            return result;
        }
        if (key.getStatus() == KeyStatus.CREATED) {
            keyService.activateAndSaveKey(key);
        }
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/key/activate finished.");
        return result;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = "application/octet-stream")
    public @ResponseBody byte[] download(@RequestParam(value = "id") Long id,
            HttpServletResponse httpServletResponse,
            @CurrentUser UserDetailsImpl userDetailsImpl) throws IOException {
        logger.info("/key/download started.");
        Key key = keyService.findById(id);
        Long keyClientId = key.getClient().getId();
        if (!userDetailsImpl.getUser().getClient().getId().equals(keyClientId)) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        // httpServletResponse.setContentType("application/octet-stream");
        String fileName = "file.key";
        logger.info("fileName=" + fileName);
        httpServletResponse.setHeader(
                "Content-Disposition",
                "attachment; filename*=UTF-8''"
                        + URLEncoder.encode(fileName, "UTF-8") + "");
        logger.info("/key/key finished.");
        return key.getCode().getBytes("UTF-8");
    }

    @RequestMapping(value = "/guid", method = { RequestMethod.POST,
            RequestMethod.GET })
    public @ResponseBody ObjectResponse<String> guid() {
        logger.info("/key/guid started.");
        ObjectResponse<String> result = new ObjectResponse<String>();
        result.setInfo(UUID.randomUUID().toString().toUpperCase());
        logger.info("/key/guid finished.");
        result.setSuccess(true);
        return result;
    }
    
    @RequestMapping(value = "/delete", method = {RequestMethod.POST,
            RequestMethod.GET})
    public @ResponseBody SimpleResponse delete(
            @RequestParam(value = "id") Long id,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/key/delete started.");
        Key key = keyService.findById(id);
        keyService.delete(key);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/key/delete finished.");
        return result;
    }
}
