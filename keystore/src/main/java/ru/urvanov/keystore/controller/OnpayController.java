package ru.urvanov.keystore.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.controller.domain.ObjectResponse;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.service.OrderService;

@Controller
@RequestMapping(value = "/onpay")
public class OnpayController {

    private static final Logger logger = LoggerFactory
            .getLogger(OnpayController.class);

    @Value("${onpay.url}")
    private String onpayUrl;
    
    @Value("${onpay.urlSuccess}")
    private String onpayUrlSuccess;
    
    @Value("${onpay.urlFail}")
    private String onpayUrlFail;
    
    @Value("${onpay.secretKey}")
    private String onpaySecretKey;

    @Value("${application.url}")
    private String applicationUrl;
    
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/loadOnPay", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ObjectResponse<PayInfo> laodPay(
            @RequestParam(value = "orderId") Long orderId,
            @CurrentUser UserDetailsImpl userDetailsImpl)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        logger.info("/onpay/loadPay started.");

        Order order = orderService.findById(orderId);
        ObjectResponse<PayInfo> result = new ObjectResponse<>();
        PayInfo payInfo = new PayInfo();
        result.setInfo(payInfo);
        payInfo.setOnpayUrl(onpayUrl);
        DictServiceType dictServiceType = order.getDictServiceType();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
        DecimalFormat decimalFormat = new DecimalFormat("0.0#", dfs);
        String priceString = decimalFormat.format(dictServiceType.getAmount());
        payInfo.setPrice(priceString);
        payInfo.setPayFor(String.valueOf(order.getId()));
        payInfo.setUrlSuccess(applicationUrl + (applicationUrl.endsWith("/") ? "" : "/") + onpayUrlSuccess);
        payInfo.setUrlSuccessEnc(applicationUrl + (applicationUrl.endsWith("/") ? "" : "/") + onpayUrlSuccess);
        payInfo.setUrlFail(applicationUrl + (applicationUrl.endsWith("/") ? "" : "/") + onpayUrlFail);
        payInfo.setUrlFailEnc(applicationUrl + (applicationUrl.endsWith("/") ? "" : "/") + onpayUrlFail);
        payInfo.setUserEmail(userDetailsImpl.getUser().getEmail());
        payInfo.setUserPhone(userDetailsImpl.getUser().getPhone());
        payInfo.setLn("ru");
        payInfo.setDictServiceTypeName(dictServiceType.getName());
        payInfo.setOrderCreatedAt(order.getCreatedAt());
        payInfo.setAmount(dictServiceType.getAmount());
        MessageDigest messageDiggest = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = messageDiggest.digest(("fix;" + payInfo.getPrice()
                + ";RUR;" + payInfo.getPayFor() + ";yes;" + onpaySecretKey)
                .getBytes("UTF-8"));
        payInfo.setMd5((new HexBinaryAdapter()).marshal(md5Bytes));
        result.setSuccess(true);
        logger.info("/onpay/loadPay finished.");
        return result;
    }

    public class PayInfo implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -9011318248197566023L;
        private String onpayUrl;
        private String price;
        private String payFor;
        private String md5;
        private String urlSuccess;
        private String urlSuccessEnc;
        private String urlFail;
        private String urlFailEnc;
        private String userEmail;
        private String userPhone;
        private String note;
        private String ln;
        private String dictServiceTypeName;
        private Date orderCreatedAt;
        private BigDecimal amount;

        public String getOnpayUrl() {
            return onpayUrl;
        }

        public void setOnpayUrl(String onpayUrl) {
            this.onpayUrl = onpayUrl;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPayFor() {
            return payFor;
        }

        public void setPayFor(String payFor) {
            this.payFor = payFor;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getUrlSuccess() {
            return urlSuccess;
        }

        public void setUrlSuccess(String urlSuccess) {
            this.urlSuccess = urlSuccess;
        }

        public String getUrlSuccessEnc() {
            return urlSuccessEnc;
        }

        public void setUrlSuccessEnc(String urlSuccessEnc) {
            this.urlSuccessEnc = urlSuccessEnc;
        }

        public String getUrlFail() {
            return urlFail;
        }

        public void setUrlFail(String urlFail) {
            this.urlFail = urlFail;
        }

        public String getUrlFailEnc() {
            return urlFailEnc;
        }

        public void setUrlFailEnc(String urlFailEnc) {
            this.urlFailEnc = urlFailEnc;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getLn() {
            return ln;
        }

        public void setLn(String ln) {
            this.ln = ln;
        }

        public String getDictServiceTypeName() {
            return dictServiceTypeName;
        }

        public void setDictServiceTypeName(String dictServiceTypeName) {
            this.dictServiceTypeName = dictServiceTypeName;
        }

        public Date getOrderCreatedAt() {
            return orderCreatedAt;
        }

        public void setOrderCreatedAt(Date orderCreatedAt) {
            this.orderCreatedAt = orderCreatedAt;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "PayInfo [onpayUrl=" + onpayUrl + ", price=" + price
                    + ", payFor=" + payFor + ", md5=" + md5 + ", urlSuccess="
                    + urlSuccess + ", urlSuccessEnc=" + urlSuccessEnc
                    + ", urlFail=" + urlFail + ", urlFailEnc=" + urlFailEnc
                    + ", userEmail=" + userEmail + ", userPhone=" + userPhone
                    + ", note=" + note + ", ln=" + ln + "]";
        }

    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

}
