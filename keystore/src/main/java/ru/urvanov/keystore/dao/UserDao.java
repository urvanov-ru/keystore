package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import ru.urvanov.keystore.domain.User;
import ru.urvanov.keystore.domain.UserListItem;
import ru.urvanov.keystore.domain.UserListParameters;

public interface UserDao {
    User findById(Long id);

    User findFullById(Long id);

    User getReference(Long id);

    User findByUserName(String userName);

    void save(User user);

    User findFullByUserName(String username);

    boolean checkUserNameUnique(User user);

    List<UserListItem> list(UserListParameters params);

    BigInteger countList(UserListParameters params);

    List<User> findEnabledByClientId(Long id);

    List<User> findForNewClientNotification();

    List<User> findByTodayBirthday();
}
