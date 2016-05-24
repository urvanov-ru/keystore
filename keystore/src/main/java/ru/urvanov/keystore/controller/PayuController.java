package ru.urvanov.keystore.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.service.OrderService;
import ru.urvanov.keystore.service.PaymentService;
import ru.urvanov.keystore.service.PayuService;

@Controller
@RequestMapping(value = "/payu")
public class PayuController {

    private static final Logger logger = LoggerFactory
            .getLogger(PayuController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PayuService payuService;

    @Value("${payu.secretKey}")
    private String secretKey;

    @Value("${payu.merchant}")
    private String merchant;

    @Value("${payu.testOrder}")
    private String testOrder;

    @Value("${payu.luUrl}")
    private String luUrl;
    
    @Value("${payu.irnUrl}")
    private String irnUrl;

    @RequestMapping(value = "loadLu", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<LoadLUResponse> loadLU(
            @RequestParam(value = "orderId") Long orderId,
            @RequestParam(value = "payMethod") String payMethod) {
        logger.info("payu/loadLU started.");
        ObjectResponse<LoadLUResponse> result = new ObjectResponse<>();
        LoadLUResponse info = new LoadLUResponse();
        result.setInfo(info);
        Order order = orderService.findById(orderId);
        info.setMerchant(merchant);
        info.setOrderRef("" + order.getId());
        SimpleDateFormat orderDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        info.setOrderDate(orderDateFormat.format(new Date()));
        info.setOrderPName(order.getDictServiceType().getName());
        info.setOrderPCode(String.valueOf(order.getDictServiceType().getId()));
        info.setOrderPrice(String.valueOf(order.getDictServiceType()
                .getAmount()));
        info.setPayMethod(payMethod);
        info.setOrderQty("1");
        info.setOrderVat("19");
        info.setOrderShipping("0");
        info.setPricesCurrency("RUB");
        info.setTestOrder(testOrder);
        info.setLuUrl(luUrl);
        result.setSuccess(true);
        logger.info("payu/loadLU finished.");
        return result;
    }

    public final class LoadLUResponse implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -2367666082505269652L;
        private String merchant;
        private String orderRef;
        private String orderDate;
        private String orderPName;
        private String orderPCode;
        private String orderPrice;
        private String payMethod;
        private String orderQty;
        private String orderVat;
        private String orderShipping;
        private String pricesCurrency;
        private String testOrder;
        private String luUrl;

        public String getMerchant() {
            return merchant;
        }

        public void setMerchant(String merchant) {
            this.merchant = merchant;
        }

        public String getOrderRef() {
            return orderRef;
        }

        public void setOrderRef(String orderRef) {
            this.orderRef = orderRef;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderPName() {
            return orderPName;
        }

        public void setOrderPName(String orderPName) {
            this.orderPName = orderPName;
        }

        public String getOrderPCode() {
            return orderPCode;
        }

        public void setOrderPCode(String orderPCode) {
            this.orderPCode = orderPCode;
        }

        public String getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(String orderPrice) {
            this.orderPrice = orderPrice;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public String getOrderQty() {
            return orderQty;
        }

        public void setOrderQty(String orderQty) {
            this.orderQty = orderQty;
        }

        public String getOrderVat() {
            return orderVat;
        }

        public void setOrderVat(String orderVat) {
            this.orderVat = orderVat;
        }

        public String getOrderShipping() {
            return orderShipping;
        }

        public void setOrderShipping(String orderShipping) {
            this.orderShipping = orderShipping;
        }

        public String getPricesCurrency() {
            return pricesCurrency;
        }

        public void setPricesCurrency(String pricesCurrency) {
            this.pricesCurrency = pricesCurrency;
        }

        public String getTestOrder() {
            return testOrder;
        }

        public void setTestOrder(String testOrder) {
            this.testOrder = testOrder;
        }

        public String getLuUrl() {
            return luUrl;
        }

        public void setLuUrl(String luUrl) {
            this.luUrl = luUrl;
        }

        public String getOrderHash() throws InvalidKeyException,
                UnsupportedEncodingException, NoSuchAlgorithmException {
            return payuService.hashMd5(merchant, orderRef, orderDate,
                    orderPName, orderPCode, orderPrice, orderQty, orderVat,
                    orderShipping, pricesCurrency, payMethod);
        }
    }

    @RequestMapping(value = "loadIrn", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<LoadIrnResponse> loadIrn(
            @RequestParam(value = "orderId") Long orderId)
            throws InvalidKeyException, UnsupportedEncodingException,
            NoSuchAlgorithmException {
        logger.info("/payu/loadIrn started.");
        ObjectResponse<LoadIrnResponse> result = new ObjectResponse<>();
        LoadIrnResponse info= new LoadIrnResponse();
        result.setInfo(info);
        Order order = orderService.findById(orderId);
        info.setMerchant(merchant);
        info.setOrderRef(order.getPayuRef());
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat
                .getInstance(Locale.ROOT);
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.getDecimalFormatSymbols().setDecimalSeparator('.');
        info.setOrderAmount(decimalFormat.format(orderService
                .calculateRemainder(order)));
        info.setOrderCurrency("RUB");
        Date irnDate = new Date();
        info.setIrnDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(irnDate));
        info.setOrderHash(payuService.hashMd5(info.getMerchant(),
                info.getOrderRef(), info.getOrderAmount(),
                info.getOrderCurrency(), info.getIrnDate()));
        info.setIrnUrl(irnUrl);
        result.setSuccess(true);
        logger.info("/payu/loadIrn finished.");
        return result;
    }

    public class LoadIrnResponse implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 2983205617747127300L;

        private String merchant;
        private String orderRef;
        private String orderAmount;
        private String orderCurrency;
        private String irnDate;
        private String orderHash;
        private String irnUrl;

        public String getMerchant() {
            return merchant;
        }

        public void setMerchant(String merchant) {
            this.merchant = merchant;
        }

        public String getOrderRef() {
            return orderRef;
        }

        public void setOrderRef(String orderRef) {
            this.orderRef = orderRef;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderCurrency() {
            return orderCurrency;
        }

        public void setOrderCurrency(String orderCurrency) {
            this.orderCurrency = orderCurrency;
        }

        public String getIrnDate() {
            return irnDate;
        }

        public void setIrnDate(String irnDate) {
            this.irnDate = irnDate;
        }

        public String getOrderHash() {
            return orderHash;
        }

        public void setOrderHash(String orderHash) {
            this.orderHash = orderHash;
        }

        public String getIrnUrl() {
            return irnUrl;
        }

        public void setIrnUrl(String irnUrl) {
            this.irnUrl = irnUrl;
        }

    }
}
