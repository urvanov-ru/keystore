package ru.urvanov.keystore.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("payuService")
public class PayuServiceImpl implements PayuService {

    private static final Logger logger = LoggerFactory.getLogger(PayuServiceImpl.class);
    
    @Value("${payu.secretKey}")
    private String secretKey;
    
    @Override
    public String hashMd5(String... strings) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuilder baseString = new StringBuilder();
        for (String str : strings) {
            if (str != null) {
                baseString.append(str.getBytes("UTF-8").length);
                baseString.append(str);
            }
        }
        logger.debug("baseString={}", baseString);
        return stringToHmacMd5(baseString.toString(), secretKey);
    }
    
    public static String stringToHmacMd5(String s, String keyString)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"),
                "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(key);

        byte[] bytes = mac.doFinal(s.getBytes("ASCII"));

        StringBuffer hash = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        return hash.toString();
    }
}
