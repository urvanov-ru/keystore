package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.sql.DataSource;
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
import ru.urvanov.keystore.controller.domain.SimpleResponse;
import ru.urvanov.keystore.domain.PaymentListItem;
import ru.urvanov.keystore.domain.PaymentListParameters;
import ru.urvanov.keystore.domain.PaymentMethod;
import ru.urvanov.keystore.domain.PaymentStatus;
import ru.urvanov.keystore.domain.PaymentType;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.PaymentService;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Controller
@RequestMapping(value = "/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory
            .getLogger(PaymentController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private DataSource dataSource;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/paymentTypes", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse paymentTypes() {
        logger.info("/payment/paymentTypes started");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < PaymentType.values().length; n++) {
            result.getInfo().add(
                    new CodeName(PaymentType.values()[n].name(), messageSource
                            .getMessage(
                                    "ru.urvanov.keystore.domain.PaymentType."
                                            + PaymentType.values()[n], null,
                                    null)));
        }
        logger.info("/payment/paymentTypes finished.");
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/paymentStatuses", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse paymentStatuses() {
        logger.info("/payment/paymentStatuses started.");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < PaymentStatus.values().length; n++) {
            result.getInfo().add(
                    new CodeName(PaymentStatus.values()[n].name(),
                            messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.PaymentStatus."
                                            + PaymentStatus.values()[n], null,
                                    null)));
        }
        result.setSuccess(true);
        logger.info("/payment/paymentStatuses finished.");
        return result;
    }

    @RequestMapping(value = "/paymentMethods", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse paymentMethods() {
        logger.info("/payment/paymentMethods started.");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < PaymentMethod.values().length; n++) {
            result.getInfo().add(
                    new CodeName(PaymentMethod.values()[n].name(),
                            messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.PaymentMethod."
                                            + PaymentMethod.values()[n], null,
                                    null)));
        }
        result.setSuccess(true);
        logger.info("/payment/paymentMethods finished.");
        return result;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ListResponse<PaymentListItem> list(
            @CurrentUser UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "clientName", required = false) String clientNameFilter,
            @RequestParam(value = "paymentType", required = false) PaymentType paymentTypeFilter,
            @RequestParam(value = "status", required = false) PaymentStatus statusFilter,
            @RequestParam(value = "method", required = false) PaymentMethod methodFilter,
            @RequestParam(value = "createdAtBegin", required = false) Date createdAtBeginFilter,
            @RequestParam(value = "createdAtEnd", required = false) Date createdAtEndFilter,
            @RequestParam(value = "orderId", required = false) Long orderIdFilter) {
        logger.info("/payment/list started.");
        logger.debug("clientName={}.", clientNameFilter);
        logger.debug("paymentType={}.", paymentTypeFilter);
        logger.debug("status={}.", statusFilter);
        logger.debug("method={}.", methodFilter);
        logger.debug("createdAtBegin={}.", createdAtBeginFilter);
        logger.debug("createdAtEnd={}.", createdAtEndFilter);
        logger.debug("orderId={}.", orderIdFilter);

        ListResponse<PaymentListItem> result = new ListResponse<>();

        PaymentListParameters param = new PaymentListParameters();
        param.setUserId(userDetailsImpl.getUser().getId());
        param.setClientName(clientNameFilter);
        param.setPaymentType(paymentTypeFilter);
        param.setStatus(statusFilter);
        param.setMethod(methodFilter);
        param.setCreatedAtBegin(createdAtBeginFilter);
        param.setCreatedAtEnd(createdAtEndFilter);
        param.setOrderId(orderIdFilter);
        result.setInfo(paymentService.list(param));

        PaymentListParameters countParam = new PaymentListParameters();
        countParam.setUserId(userDetailsImpl.getUser().getId());
        result.setTotalRecords(paymentService.countList(countParam)
                .longValueExact());

        result.setSuccess(true);
        logger.info("/payment/list finished.");
        return result;
    }
    
    
    public static class PaymentCorrect implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 8348134441682255321L;
        @JsonSerialize(using=ToStringSerializer.class)
        @NotNull
        private Long parentPaymentId;
        
        @NotNull
        private BigDecimal amountWithoutCommission;
        
        @NotNull
        private BigDecimal amountOfCommission;
        
        @NotNull
        @Size(min=1)
        private String comment;
        public Long getParentPaymentId() {
            return parentPaymentId;
        }
        public void setParentPaymentId(Long parentPaymentId) {
            this.parentPaymentId = parentPaymentId;
        }
        public BigDecimal getAmountWithoutCommission() {
            return amountWithoutCommission;
        }
        public void setAmountWithoutCommission(BigDecimal amountWithoutCommission) {
            this.amountWithoutCommission = amountWithoutCommission;
        }
        public BigDecimal getAmountOfCommission() {
            return amountOfCommission;
        }
        public void setAmountOfCommission(BigDecimal amountOfCommission) {
            this.amountOfCommission = amountOfCommission;
        }
        public String getComment() {
            return comment;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }
        @Override
        public String toString() {
            return "PaymentCorrect [parentPaymentId=" + parentPaymentId
                    + ", amountWithoutCommission=" + amountWithoutCommission
                    + ", amountOfCommission=" + amountOfCommission
                    + ", comment=" + comment + "]";
        }
        
    }
    
    @RequestMapping(value="/correct", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody SimpleResponse correct(@RequestBody PaymentCorrect paymentCorrect,
            BindingResult bindingResult) throws NumberFormatException, IOException {
        logger.info("/payment/correct started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        paymentService.createCorrectPayment(paymentCorrect.getParentPaymentId(),
                paymentCorrect.getAmountWithoutCommission(),
                paymentCorrect.getAmountOfCommission(),
                paymentCorrect.getComment());
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/payment/correct finished.");
        return result;
    }
    
    @RequestMapping(value="/complete", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody SimpleResponse complete(@RequestParam(value="paymentId") Long paymentId){
        logger.info("/payment/complete started.");
        paymentService.complete(paymentId);
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/payment/complete finished.");
        return result;
    }
    
    @RequestMapping(value = "/exportXls/PaymentList", method = RequestMethod.GET)
    public String exportXls(Model model,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        model.addAttribute("dataSource", dataSource);
        model.addAttribute("userId", userDetailsImpl.getUser().getId());
        return "jasperPaymentList";
    }
}
