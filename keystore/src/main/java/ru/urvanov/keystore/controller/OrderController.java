package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import javax.sql.DataSource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.KeyActivationMode;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderListItem;
import ru.urvanov.keystore.domain.OrderListParameters;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.propertyeditor.MillisecondsDateEditor;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.DictServiceTypeService;
import ru.urvanov.keystore.service.KeyService;
import ru.urvanov.keystore.service.OrderService;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    private static final Logger logger = LoggerFactory
            .getLogger(OrderController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DictServiceTypeService dictServiceTypeService;

    @Autowired
    private KeyService keyService;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private ClientService clientService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MillisecondsDateEditor());
    }

    @RequestMapping(value = "/orderStatuses", method = RequestMethod.GET)
    public @ResponseBody CodeNamesResponse orderStatuses() {
        logger.info("/order/orderStatuses started");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < OrderStatus.values().length; n++) {
            result.getInfo().add(
                    new CodeName(OrderStatus.values()[n].name(), messageSource
                            .getMessage(
                                    "ru.urvanov.keystore.domain.OrderStatus."
                                            + OrderStatus.values()[n], null,
                                    null)));
        }
        result.setSuccess(true);
        logger.info("/order/orderStatuses finished.");
        return result;
    }

    @RequestMapping(value = "/keyActivationModes", method = {
            RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody CodeNamesResponse keyActivationModes() {
        logger.info("/order/keyActivationModes started.");
        CodeNamesResponse result = new CodeNamesResponse();
        for (int n = 0; n < KeyActivationMode.values().length; n++) {
            result.getInfo().add(
                    new CodeName(KeyActivationMode.values()[n].name(),
                            messageSource.getMessage(
                                    "ru.urvanov.keystore.domain.KeyActivationMode."
                                            + KeyActivationMode.values()[n],
                                    null, null)));
        }
        result.setSuccess(true);
        logger.info("/order/keyActivationModes finished.");
        return result;
    }

    public static class OrderSaveParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -4950241910922887199L;
        private Long id;

        @NotNull
        private Long dictServiceTypeId;

        @NotNull
        private KeyActivationMode keyActivationMode;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getDictServiceTypeId() {
            return dictServiceTypeId;
        }

        public void setDictServiceTypeId(Long dictServiceTypeId) {
            this.dictServiceTypeId = dictServiceTypeId;
        }

        public KeyActivationMode getKeyActivationMode() {
            return keyActivationMode;
        }

        public void setKeyActivationMode(KeyActivationMode keyActivationMode) {
            this.keyActivationMode = keyActivationMode;
        }

    }

    @RequestMapping(value = "/save", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse save(
            @RequestBody @Valid OrderSaveParam orderSaveParam,
            BindingResult bindingResult) throws IOException {
        logger.info("/order/save started.");
        if (bindingResult.hasErrors()) {
            SimpleResponse result = new SimpleResponse();
            result.setMessage(messageSource.getMessage(
                    "ru.urvanov.keystore.check_form", null, null));
            return result;
        }
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication
                .getPrincipal();
        Order order = null;
        if (orderSaveParam.getId() == null) {
            order = new Order();
            order.setCreatedAt(new Date());
            order.setCreatedBy(userDetailsImpl.getUser());
        } else {
            order = orderService.findFullById(orderSaveParam.getId());
            if (order == null) {
                SimpleResponse result = new SimpleResponse();
                result.setMessage("Заказ не найден.");
                return result;
            }
        }
        DictServiceType dictServiceType = dictServiceTypeService
                .findById(orderSaveParam.getDictServiceTypeId());
        order.setDictServiceType(dictServiceType);
        order.setKeyActivationMode(orderSaveParam.getKeyActivationMode());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderService.save(order);

        

        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);
        logger.info("/order/save finished.");
        return result;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.POST })
    public @ResponseBody ListResponse<OrderListItem> list(
            @RequestParam(value = "clientName", required = false) String clientNameFilter,
            @RequestParam(value = "status", required = false) OrderStatus statusFilter,
            @RequestParam(value = "createdAtBegin", required = false) Date createdAtBeginFilter,
            @RequestParam(value = "createdAtEnd", required = false) Date createdAtEndFilter) {
        logger.info("/order/list started.");
        logger.debug("clientName=" + clientNameFilter);
        logger.debug("status=" + statusFilter);
        logger.debug("createdAtBegin=" + createdAtBeginFilter);
        logger.debug("createdAtEnd=" + createdAtEndFilter);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication
                .getPrincipal();
        Long userId = userDetailsImpl.getUser().getId();

        ListResponse<OrderListItem> result = new ListResponse<>();

        OrderListParameters param = new OrderListParameters();
        param.setUserId(userId);
        param.setClientName(clientNameFilter);
        param.setStatus(statusFilter);
        param.setCreatedAtBegin(createdAtBeginFilter);
        param.setCreatedAtEnd(createdAtEndFilter);
        result.setInfo(orderService.list(param));

        OrderListParameters countParam = new OrderListParameters();
        countParam.setUserId(userId);
        result.setTotalRecords(orderService.countList(countParam)
                .longValueExact());

        result.setSuccess(true);
        logger.info("/order/list finished.");
        return result;
    }

    @RequestMapping(value = "/exportXls/OrderList", method = RequestMethod.GET)
    public String exportXls(Model model,
            @CurrentUser UserDetailsImpl userDetailsImpl) {
        model.addAttribute("dataSource", dataSource);
        model.addAttribute("userId", userDetailsImpl.getUser().getId());
        return "jasperOrderList";
    }

    @RequestMapping(value = "/cancel", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody SimpleResponse cancel(
            @RequestParam(value = "orderId") Long orderId) {
        logger.info("/order/cancel started.");
        orderService.cancelOrder(orderService.findById(orderId));
        SimpleResponse result = new SimpleResponse();
        result.setSuccess(true);

        logger.info("/order/cancel finished.");
        return result;
    }
    
    @RequestMapping(value="/payBack", method = {RequestMethod.GET,
            RequestMethod.POST})
    public @ResponseBody SimpleResponse payBack(
            @RequestParam(value = "orderId") Long orderId) {
        logger.info("/order/payBack started.");
        SimpleResponse result = new SimpleResponse();
        orderService.payBackOrder(orderService.findById(orderId));
        result.setSuccess(true);
        logger.info("/order/payBack finished.");
        return result;
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.GET)
    public String printInvoice(@RequestParam(value = "orderId") Long orderId,
            Model model, @CurrentUser UserDetailsImpl userDetailsImpl) {
        logger.info("/order/invoice started...");
        Order order = orderService.findById(orderId);
        Client client= clientService.findService();
        model.addAttribute("dataSource", dataSource);
        model.addAttribute("orderId", orderId);
        model.addAttribute("receiverJuridicalPersonName", client.getJuridicalPersonName());
        model.addAttribute("receiverJuridicalPersonAddress",
                client.getJuridicalPersonAddress());
        model.addAttribute("receiverPhone", client
                .getContactPersonPhone());
        model.addAttribute("receiverItn", client.getItn());
        model.addAttribute("receiverIec", client.getIec());
        model.addAttribute("receiverBankName", client.getBankName());
        model.addAttribute("receiverAccount", client.getAccount());
        model.addAttribute("receiverCorrAccount", client.getCorrAccount());
        model.addAttribute("receiverBic", client.getBic());
        model.addAttribute("payerJuridicalPersonName", order.getCreatedBy()
                .getClient().getJuridicalPersonName());
        model.addAttribute("orderId", order.getId());

        logger.info("/order/invoice finished...");
        return "jasperInvoice";
    }
}
