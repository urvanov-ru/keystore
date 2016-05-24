package ru.urvanov.keystore.payu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.domain.PaymentType;
import ru.urvanov.keystore.service.OrderService;
import ru.urvanov.keystore.service.PaymentService;
import ru.urvanov.keystore.service.PayuService;

@Controller
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

    @Value("${payu.idnUrl}")
    private String idnUrl;

    @RequestMapping(value = "/ipn", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody String ipn(
            @RequestParam(value = "SALEDATE", required = false) String saleDate,
            @RequestParam(value = "REFNO", required = false) String refNo,
            @RequestParam(value = "REFNOEXT", required = false) String refNoExt,
            @RequestParam(value = "ORDERNO", required = false) String orderNo,
            @RequestParam(value = "ORDERSTATUS", required = false) String orderStatus,
            @RequestParam(value = "PAYMETHOD", required = false) String payMethod,
            @RequestParam(value = "FIRSTNAME", required = false) String firstName,
            @RequestParam(value = "LASTNAME", required = false) String lastName,
            @RequestParam(value = "ADDRESS1", required = false) String address1,
            @RequestParam(value = "CITY", required = false) String city,
            @RequestParam(value = "STATE", required = false) String state,
            @RequestParam(value = "ZIPCODE", required = false) String zipCode,
            @RequestParam(value = "COUNTRY", required = false) String country,
            @RequestParam(value = "PHONE", required = false) String phone,
            @RequestParam(value = "FAX", required = false) String fax,
            @RequestParam(value = "CUSTOMEREMAIL", required = false) String customerEmail,
            @RequestParam(value = "FIRSTNAME_D", required = false) String firstNameD,
            @RequestParam(value = "LASTNAME_D", required = false) String lastNameD,
            @RequestParam(value = "ADDRESS1_D", required = false) String address1D,
            @RequestParam(value = "CITY_D", required = false) String cityD,
            @RequestParam(value = "STATE_D", required = false) String stateD,
            @RequestParam(value = "ZIPCODE_D", required = false) String zipCodeD,
            @RequestParam(value = "COUNTRY_D", required = false) String countryD,
            @RequestParam(value = "PHONE_D", required = false) String phoneD,
            @RequestParam(value = "IPADDRESS", required = false) String ipAddress,
            @RequestParam(value = "CURRENCY", required = false) String currency,
            @RequestParam(value = "IPN_PID[]", required = false) String ipnPid,
            @RequestParam(value = "IPN_PNAME[]", required = false) String ipnPName,
            @RequestParam(value = "IPN_PCODE[]", required = false) String ipnPCode,
            @RequestParam(value = "IPN_INFO[]", required = false) String ipnInfo,
            @RequestParam(value = "IPN_QTY[]", required = false) String ipnQty,
            @RequestParam(value = "IPN_PRICE[]", required = false) String ipnPrice,
            @RequestParam(value = "IPN_VAT[]", required = false) String ipnVat,
            @RequestParam(value = "IPN_TOTAL[]", required = false) String ipnTotal,
            @RequestParam(value = "IPN_TOTALGENERAL", required = false) String ipnTotalGeneral,
            @RequestParam(value = "IPN_SHIPPING", required = false) String ipnShipping,
            @RequestParam(value = "IPN_COMMISSION", required = false) String ipnCommission,
            @RequestParam(value = "IPN_DATE", required = false) String ipnDate,
            @RequestParam(value = "HASH", required = false) String hash)
            throws InvalidKeyException, NoSuchAlgorithmException, ClientProtocolException, IOException {
        logger.info("/payu/ipn started.");
        String computedHash = payuService.hashMd5(saleDate, refNo, refNoExt,
                orderNo, orderStatus, payMethod, firstName, lastName, address1,
                city, state, zipCode, country, phone, fax, customerEmail,
                firstName, lastName, address1D, cityD, stateD, zipCode,
                countryD, phoneD, ipAddress, currency, ipnPid, ipnPName,
                ipnPCode, ipnInfo, ipnQty, ipnPrice, ipnVat, ipnTotal,
                ipnTotalGeneral, ipnShipping, ipnCommission, ipnDate);
        logger.debug("hashMd5 = {}", computedHash);
        String result = "";
        Long orderId = Long.valueOf(refNoExt);
        if (hash.toUpperCase().equals(computedHash.toUpperCase())) {
            if (orderStatus.equals("TEST") && testOrder.equals("TRUE")
                    || orderStatus.equals("COMPLETE")) {

                paymentService.createPaymentForOrder(orderId,
                        PaymentType.ORDER_PAYMENT);
                // TODO: Проверить, что ордер полностью оплачен.
                sendIdn(orderId, refNo, refNo);
                String d = new SimpleDateFormat("yyyyMMddHHmmss")
                        .format(new Date());
                result += "<EPAYMENT>" + d + "|"
                        + payuService.hashMd5(ipnPid, ipnPName, ipnDate, d)
                        + "</EPAYMENT>";
            } else if (orderStatus.equals("REVERSED")
                    || orderStatus.equals("REFUND")) {
                paymentService.createPaymentForOrder(orderId,
                        PaymentType.PAY_BACK);
            }
        } else {
            throw new IllegalStateException("incorrect hash");
        }
        logger.info("/payu/ipn finished.");
        return result;
    }

    private void sendIdn(Long orderId, String orderRef, String orderAmount)
            throws ClientProtocolException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        
        String idnDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());
        String orderCurrency = "RUB";
        String orderHash = payuService.hashMd5(merchant, orderRef, orderAmount,
                orderCurrency, idnDate);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(idnUrl);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("MERCHANT", merchant));
        nvps.add(new BasicNameValuePair("ORDER_REF", orderRef));
        nvps.add(new BasicNameValuePair("ORDER_AMOUNT", orderAmount));
        nvps.add(new BasicNameValuePair("ORDER_CURRENCY", orderCurrency));
        nvps.add(new BasicNameValuePair("IDN_DATE", idnDate));
        nvps.add(new BasicNameValuePair("ORDER_HASH", orderHash));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            long contentLength = entity.getContentLength();
            if (contentLength > 1000000)
                throw new IllegalStateException("content-length is too big.");
            byte[] buff = new byte[(int) contentLength];
            try (InputStream inputStream = entity.getContent()) {
                int pos = 0;
                int bytesReaded = 0;
                while ((bytesReaded = inputStream.read(buff, pos, buff.length
                        - pos)) != -1) {
                    pos += bytesReaded;
                }
            }
            String responseString = new String(buff);
            processIdnResponse(responseString, orderId, orderRef);
            EntityUtils.consume(entity);
        }
    }

    private void processIdnResponse(String responseString, Long orderId,
            String orderRefArg) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        final int ORDER_REF = 0;
        final int RESPONSE_CODE = 1;
        final int RESPONSE_MSG = 2;
        final int IDN_DATE = 3;
        final int ORDER_HASH = 4;

        int startEpayment = responseString.indexOf("<EPAYMENT>");
        int endEpayment = responseString.indexOf("</EPAYMENT>");
        responseString = responseString.substring(
                startEpayment + "<EPAYMENT>".length(), endEpayment);
        String[] responseValues = responseString.split("|");

        String orderRef = responseValues[ORDER_REF];
        String responseCode = responseValues[RESPONSE_CODE];
        String responseMsg = responseValues[RESPONSE_MSG];
        String idnDate = responseValues[IDN_DATE];
        String orderHash = responseValues[ORDER_HASH];
        logger.debug(
                "orderRef={}, responseCode={}, responseMsg={}, idnDate={}, orderHash={}",
                orderRef, responseCode, responseMsg, idnDate, orderHash);
        String computedHash = payuService.hashMd5(orderRef, responseCode,
                responseMsg, idnDate);
        if (!orderHash.toUpperCase().equals(computedHash.toUpperCase())) {
            throw new IllegalStateException("computedHash=" + computedHash
                    + " not equal to orderHash=" + orderHash);
        }
        if (!orderRef.equals(orderRefArg))
            throw new IllegalStateException("orderRef=" + orderRef
                    + ", not equal to orderRefArg=" + orderRefArg);
        logger.info("IDN to payu for order with id={} successfully sended. ",
                orderId);
    }
}
