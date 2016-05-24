package ru.urvanov.keystore.test;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class FakeMailSenderImpl implements MailSender {

    public FakeMailSenderImpl() {
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
    }

}
