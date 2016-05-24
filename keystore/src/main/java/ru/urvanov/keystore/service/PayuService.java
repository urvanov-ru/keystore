package ru.urvanov.keystore.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface PayuService {

    String hashMd5(String... strings) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException;

}
