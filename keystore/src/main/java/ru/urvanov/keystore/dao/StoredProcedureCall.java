package ru.urvanov.keystore.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ни JPA ни Hibernate на текущий момент не позволяют в своих
 * StoredProcedureQuery и подобных классах передавать null параметры в хранимые
 * процедуры. Этот класс позволяет.
 */
public class StoredProcedureCall {
    private static final Logger logger = LoggerFactory
            .getLogger(StoredProcedureCall.class);

    private String procedureName;
    private List<String> types = new ArrayList<String>();
    private List<Object> values = new ArrayList<Object>();

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    };

    public void addParameter(String type, Object value) {
        types.add(type);
        values.add(value);
    }

    private enum ExecuteType {
        SELECT_ALL, COUNT_ALL
    }

    private Query createQuery(EntityManager em, Class<?> resultType,
            ExecuteType executeType) {
        StringBuilder sql = new StringBuilder();
        switch (executeType) {
        case SELECT_ALL:
            sql.append("select * from ");
            break;
        case COUNT_ALL:
            sql.append("select count(*) from ");
            break;
        }

        sql.append(procedureName);
        sql.append("(");
        for (int n = 0; n < types.size(); n++) {
            String type = types.get(n);
            Object value = values.get(n);
            if (value == null) {
                sql.append(" cast (null as ");
                sql.append(type);
                sql.append(") ");

            } else {
                sql.append(" cast(? as ");
                sql.append(type);
                sql.append(") ");
            }
            if (n < types.size() - 1)
                sql.append(" , ");
        }
        sql.append(") ");
        String sqlString = sql.toString();
        logger.info("sqlString=" + sqlString);
        Query query = null;
        switch (executeType) {
        case SELECT_ALL:
            query = em.createNativeQuery(sqlString, resultType);
            break;
        case COUNT_ALL:
            query = em.createNativeQuery(sqlString);
            break;
        }
        int parameterIndex = 1;
        for (int n = 0; n < values.size(); n++) {
            Object value = values.get(n);
            if (value != null) {
                query.setParameter(parameterIndex, value);
                parameterIndex++;
            }
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getResultList(EntityManager em, Class<T> resultType) {
        Query query = createQuery(em, resultType, ExecuteType.SELECT_ALL);
        return query.getResultList();
    }

    public <T> BigInteger getCount(EntityManager em, Class<T> resultType) {
        Query query = createQuery(em, resultType, ExecuteType.COUNT_ALL);
        return (BigInteger) query.getSingleResult();
    }
}
