package ru.urvanov.keystore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import ru.urvanov.keystore.dao.DictEventDao;
import ru.urvanov.keystore.dao.UserDao;
import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.DictAction;
import ru.urvanov.keystore.domain.DictEvent;
import ru.urvanov.keystore.domain.DictEventCode;
import ru.urvanov.keystore.domain.DictServiceType;
import ru.urvanov.keystore.domain.LinkUserDictEventNotification;
import ru.urvanov.keystore.domain.Order;
import ru.urvanov.keystore.domain.Payment;
import ru.urvanov.keystore.domain.User;

@Service(value = "mailService")
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory
            .getLogger(MailServiceImpl.class);

    @Autowired
    private SimpleMailMessage templateMessage;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private DictEventDao dictEventDao;

    @Autowired
    private UserDao userDao;

    @Override
    public void sendNotification(Client client, String title, String body) {
        logger.debug("SendNotification client=" + client + ", title=" + title
                + ",body=" + body);
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(client.getContactPersonEmail());
        msg.setText(body);
        msg.setSubject(title);
        this.mailSender.send(msg);
    }

    private void sendMailWithoutException(String to, String title, String body) {
        try {
            logger.debug("sendMail to=" + to + ", title=" + title + ", body="
                    + body);
            SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
            msg.setTo(to);
            msg.setText(body);
            msg.setSubject(title);
            this.mailSender.send(msg);
        } catch (Exception ex) {
            logger.error("Failed to send mail to " + to);
        }
    }

    private void sendMailWithoutException(Long userId, DictEvent dictEvent,
            String title, String body) {
        User fullUser = userDao.findFullById(userId);
        LinkUserDictEventNotification luden = fullUser
                .getLinkUserDictEventNotifications().get(dictEvent);
        if (luden != null && luden.getAllowNotification())
            this.sendMailWithoutException(fullUser.getEmail(), title, body);
    }

    @Override
    public void sendNotificationNewClient(Client client, User user) {
        DictEvent dictEvent = dictEventDao
                .findByCode(DictEventCode.CLIENT_ADDED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User serviceUser : userDao.findForNewClientNotification()) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("newClient", client);
            variables.put("newUser", user);
            variables.put("user", serviceUser);
            sendMailWithoutException(serviceUser.getEmail(), title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationChangeClient(Client client) {
        DictEvent dictEvent = dictEventDao
                .findByCode(DictEventCode.CLIENT_CHANGED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(client.getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("changedClient", client);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationBlockClient(Client client) {
        DictEvent dictEvent = dictEventDao
                .findByCode(DictEventCode.CLIENT_BLOCKED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(client.getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("blockClient", client);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationNewUser(User userArg) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.USER_ADDED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(userArg.getClient()
                .getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("newUser", userArg);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationChangeUser(User user) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.USER_CHANGED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        sendMailWithoutException(user.getId(), dictEvent, title,
                evaluateSpEL(body, variables));
    }

    @Override
    public void sendNotificationBlockUser(User user) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.USER_BLOCKED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        sendMailWithoutException(user.getId(), dictEvent, title,
                evaluateSpEL(body, variables));
    }

    @Override
    public void sendNotificationNewOrder(Order order) {
        DictEvent dictEvent = dictEventDao
                .findByCode(DictEventCode.ORDER_CREATED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        User user = userDao.findById(order.getCreatedBy().getId());
        Map<String, Object> variables = new HashMap<>();
        variables.put("newOrder", order);
        variables.put("user", user);
        sendMailWithoutException(user.getId(), dictEvent, title,
                evaluateSpEL(body, variables));
    }

    @Override
    public void sendNotificationNewPayment(Payment payment) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.PAYED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(payment.getOrder()
                .getCreatedBy().getClient().getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("newPayment", payment);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationPayBack(Payment payment) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.PAY_BACK);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(payment.getOrder()
                .getCreatedBy().getClient().getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("payBackPayment", payment);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationCorrectingPayment(Payment payment) {
        DictEvent dictEvent = dictEventDao
                .findByCode(DictEventCode.PAY_CORRECTED);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(payment.getOrder()
                .getCreatedBy().getClient().getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("correctingPayment", payment);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationGivenDictAction(Client client,
            DictAction dictAction) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.ACTION);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(client.getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("givenClient", client);
            variables.put("givenDictAction", dictAction);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationGivenDictServiceType(Client client,
            DictServiceType dictServiceType) {
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.SERVICE);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : userDao.findEnabledByClientId(client.getId())) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("givenClient", client);
            variables.put("givenDictServiceType", dictServiceType);
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
    }

    @Override
    public void sendNotificationBirthday() {
        logger.info("sendNotificationBirthday started.");
        List<User> users = userDao.findByTodayBirthday();
        DictEvent dictEvent = dictEventDao.findByCode(DictEventCode.BIRTHDAY);
        String title = dictEvent.getTitle();
        String body = dictEvent.getBody();
        for (User user : users) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("user", user);
            sendMailWithoutException(user.getId(), dictEvent, title,
                    evaluateSpEL(body, variables));
        }
        logger.info("sendNotificationBirthday finished.");
    }

    private String evaluateSpEL(String text, Map<String, Object> variables) {
        Pattern pattern = Pattern.compile("\\$\\{(.+)\\}\\$");
        Matcher matcher = pattern.matcher(text);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (Entry<String, Object> entry : variables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        StringBuilder result = new StringBuilder();
        int trailIndex = 0;
        while (matcher.find()) {
            int group = matcher.groupCount() - 1;

            String program = matcher.group(group);
            program = program.substring(2, program.length() - 2);
            logger.debug("program = {}.", program);
            result.append(text.substring(trailIndex, matcher.start(group)));
            logger.debug("result={}", result);
            try {
                result.append(String.valueOf(parser.parseExpression(program)
                        .getValue(context)));
            } catch (Exception ex) {
                logger.error("Spell " + program +" evaluation failed.", ex);
                result.append(matcher.group(group));
            }
            logger.debug("result={}", result);
            trailIndex = matcher.end(group);
        }
        result.append(text.substring(trailIndex));
        logger.debug("result_at_last={}.", result);
        return result.toString();
    }
}
