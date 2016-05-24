package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.List;

import ru.urvanov.keystore.domain.Client;
import ru.urvanov.keystore.domain.ClientListItem;
import ru.urvanov.keystore.domain.ClientListParameters;

public interface ClientDao {
    Client findById(Long id);

    Client findFullById(Long id);

    Client getReference(Long id);

    void save(Client client);

    List<ClientListItem> list(ClientListParameters clientListParameters);

    boolean checkNameUnique(Client client);

    Client findByUniqueId(String uniqueId);

    BigInteger countList(ClientListParameters clientListParameters);

    Client findByAuthority(String authority);

    List<Client> findByDictActionId(Long dictActionId);
}
