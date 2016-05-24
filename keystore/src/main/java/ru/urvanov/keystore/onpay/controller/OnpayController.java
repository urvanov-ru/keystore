package ru.urvanov.keystore.onpay.controller;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.dao.OrderDao;
import ru.urvanov.keystore.domain.Authority;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.OrderStatus;
import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.PaymentMethod;
import ru.urvanov.keystore.domain.PaymentStatus;
import ru.urvanov.keystore.domain.PaymentType;
import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.service.OrderService;
import ru.urvanov.keystore.service.PaymentService;

import com.fasterxml.jackson.annotation.JsonProperty;

@Controller
public class OnpayController {

    private static final Logger logger = LoggerFactory
            .getLogger(OnpayController.class);

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private PaymentService paymentService;

    @Value("${onpay.secretKey}")
    private String onpaySecretKey;

    
    @RequestMapping(value = "/check", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody MerchantResponse check(
            @RequestBody CheckParam checkParam, BindingResult bindingResult)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            
            logger.info("/onpay/check started.");
            MerchantResponse result = new MerchantResponse();
            Long orderId = Long.valueOf(checkParam.getPayFor());

            switch (checkParam.getType()) {
            case "check":
                if ((checkParam.getWay().equals("RUR") || checkParam.getWay().equals("TST"))
                        //&& checkParam.getMode().equals("fix")
                        && checkSignature(checkParam)) {
                    boolean resultStatus = orderService.checkPayAmountById(orderId, checkParam.getAmount());
                    result.setStatus(resultStatus);
                    result.setPayFor(checkParam.getPayFor());
                    result.setSignature(calculateSha1("check;" + result.getStatus()
                            + ";" + result.getPayFor() + ";" + onpaySecretKey));
                }
                break;
            case "pay":
                if ((checkParam.getPayment().getWay().equals("RUR") || checkParam.getPayment().getWay().equals("TST"))
                        && checkSignature(checkParam)) {
                    User user = new User();
                    user.setActivated(true);
                    Authority authority = new Authority();
                    authority.setAuthority("ROLE_PAY");
                    authority.setUser(user);
                    user.getAuthorities().add(authority);
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getPassword(),
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                    paymentService.createPaymentForOrder(orderId, PaymentType.ORDER_PAYMENT, checkParam.getBalance().getAmount(),
                            checkParam.getPayment().getAmount().subtract(checkParam.getBalance().getAmount()), null);
                    result.setStatus(true);
                    result.setPayFor(checkParam.getPayFor());
                    result.setSignature(calculateSha1("pay;" + result.getStatus()
                            + ";" + result.getPayFor() + ";" + onpaySecretKey));
                }
                break;
            }
            
            logger.info("/onpay/check finished.");
            return result;
        } catch (Exception e) {
            logger.error("/onpay/check failed.", e);
            MerchantResponse result = new MerchantResponse();
            result.setStatus(false);
            result.setPayFor(checkParam.getPayFor());
            result.setSignature(calculateSha1("check;" + result.getStatus()
                    + ";" + result.getPayFor() + ";" + onpaySecretKey));
            return result;
        }
    }

    private String calculateSha1(String str) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest messageDiggest = MessageDigest.getInstance("SHA1");
        byte[] md5Bytes = messageDiggest.digest(str.getBytes("UTF-8"));
        return (new HexBinaryAdapter()).marshal(md5Bytes).toLowerCase();
    }

    private boolean checkSignature(CheckParam checkParam)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ROOT);
        DecimalFormat decimalFormat = new DecimalFormat("0.0#", dfs);
        String str;
        String calculatedSignature;
        switch (checkParam.getType()) {
        case "check":
            str = checkParam.getType() + ";" + checkParam.getPayFor() + ";"
                    + decimalFormat.format(checkParam.getAmount()) + ";"
                    + checkParam.getWay() + ";" + checkParam.getMode() + ";"
                    + onpaySecretKey;
            logger.info("The string for signature : {}.", str);
            calculatedSignature = calculateSha1(str);
            logger.info("Calculated signature : {}.", calculatedSignature);
            logger.info("checkSignature : {}", checkParam.getSignature());
            return calculatedSignature.toUpperCase().equals(checkParam.getSignature().toUpperCase());
        case "pay":
            str = "pay;" + checkParam.getPayFor() + ";"
                    + decimalFormat.format(checkParam.getPayment().getAmount())
                    + ";" + checkParam.getPayment().getWay() + ";"
                    + decimalFormat.format(checkParam.getBalance().getAmount())
                    + ";" + checkParam.getBalance().getWay() + ";"
                    + onpaySecretKey;
            logger.info("The string for signature : {}.", str);
            calculatedSignature = calculateSha1(str);
            logger.info("Calculated signature : {}.", calculatedSignature);
            logger.info("checkSignature : {}", checkParam.getSignature());
            return calculatedSignature.toUpperCase().equals(checkParam.getSignature().toUpperCase());
        default:
            throw new IllegalArgumentException("Not supported type.");
        }
    }

    public static class CheckParamUser implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -5160935990009999056L;
        private String email;
        private String phone;
        private String note;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        @Override
        public String toString() {
            return "CheckParamUser [email=" + email + ", phone=" + phone
                    + ", note=" + note + "]";
        }

    }

    public static class CheckParamPayment implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 2165295830581528682L;
        private int id;
        @JsonProperty("date_time")
        private String dateTime;
        private BigDecimal amount;
        private String way;
        private BigDecimal rate;
        @JsonProperty("release_at")
        private String releaseAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public String getReleaseAt() {
            return releaseAt;
        }

        public void setReleaseAt(String releaseAt) {
            this.releaseAt = releaseAt;
        }

        @Override
        public String toString() {
            return "CheckParamPayment [id=" + id + ", dateTime=" + dateTime
                    + ", amount=" + amount + ", way=" + way + ", rate=" + rate
                    + ", releaseAt=" + releaseAt + "]";
        }

    }

    public static class CheckParamBalance implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 3153919327554052143L;
        private BigDecimal amount;
        @JsonProperty("way")
        private String way;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        @Override
        public String toString() {
            return "CheckParamBalance [amount=" + amount + ", way=" + way + "]";
        }

    }

    public static class CheckParamOrder implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 3865870837014982987L;
        @JsonProperty("from_amount")
        private BigDecimal fromAmount;
        @JsonProperty("from_way")
        private String fromWay;
        @JsonProperty("to_amount")
        private BigDecimal toAmount;
        @JsonProperty("to_way")
        private String toWay;

        public BigDecimal getFromAmount() {
            return fromAmount;
        }

        public void setFromAmount(BigDecimal fromAmount) {
            this.fromAmount = fromAmount;
        }

        public String getFromWay() {
            return fromWay;
        }

        public void setFromWay(String fromWay) {
            this.fromWay = fromWay;
        }

        public BigDecimal getToAmount() {
            return toAmount;
        }

        public void setToAmount(BigDecimal toAmount) {
            this.toAmount = toAmount;
        }

        public String getToWay() {
            return toWay;
        }

        public void setToWay(String toWay) {
            this.toWay = toWay;
        }

        @Override
        public String toString() {
            return "CheckParamOrder [fromAmount=" + fromAmount + ", fromWay="
                    + fromWay + ", toAmount=" + toAmount + ", toWay=" + toWay
                    + "]";
        }

    }

    public static class CheckParam implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 2570267294786314095L;

        private String type;

        @JsonProperty("pay_for")
        private String payFor;
        private BigDecimal amount;
        private String way;
        private String mode;
        private String signature;
        private CheckParamUser user;
        private CheckParamPayment payment;
        private CheckParamBalance balance;
        private CheckParamOrder order;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPayFor() {
            return payFor;
        }

        public void setPayFor(String payFor) {
            this.payFor = payFor;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public CheckParamUser getUser() {
            return user;
        }

        public void setUser(CheckParamUser user) {
            this.user = user;
        }

        public CheckParamPayment getPayment() {
            return payment;
        }

        public void setPayment(CheckParamPayment payment) {
            this.payment = payment;
        }

        public CheckParamBalance getBalance() {
            return balance;
        }

        public void setBalance(CheckParamBalance balance) {
            this.balance = balance;
        }

        public CheckParamOrder getOrder() {
            return order;
        }

        public void setOrder(CheckParamOrder order) {
            this.order = order;
        }

        @Override
        public String toString() {
            return "CheckParam [type=" + type + ", payFor=" + payFor
                    + ", amount=" + amount + ", way=" + way + ", mode=" + mode
                    + ", signature=" + signature + "]";
        }

    }

    public static class MerchantResponse implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -2806146777421866950L;
        private boolean status;
        @JsonProperty("pay_for")
        private String payFor;
        private String signature;

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getPayFor() {
            return payFor;
        }

        public void setPayFor(String payFor) {
            this.payFor = payFor;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        @Override
        public String toString() {
            return "MerchantResponse [status=" + status + ", payFor=" + payFor
                    + ", signature=" + signature + "]";
        }

    }


    public void setOnpaySecretKey(String onpaySecretKey) {
        this.onpaySecretKey = onpaySecretKey;
    }
    
    
    
}
