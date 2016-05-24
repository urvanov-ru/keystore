package ru.urvanov.keystore.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ru.urvanov.keystore.domain.ReportActivityData;
import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.ReportPaymentData;
import ru.urvanov.keystore.domain.ReportPaymentParameters;

@Repository("reportDao")
public class ReportDaoImpl implements ReportDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ReportActivityData> activity(
            ReportActivityParameters reportActivityParam) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("report_activity");
        spc.addParameter("bigint", reportActivityParam.getUserId());
        spc.addParameter("date", reportActivityParam.getDateBegin());
        spc.addParameter("date", reportActivityParam.getDateEnd());
        spc.addParameter("integer", reportActivityParam.getReportMode());
        spc.addParameter("bigint", reportActivityParam.getClientId());
        return spc.getResultList(em, ReportActivityData.class);
    }

    @Override
    public List<ReportPaymentData> payment(
            ReportPaymentParameters reportPaymentParam) {
        StoredProcedureCall spc = new StoredProcedureCall();
        spc.setProcedureName("report_payment");
        spc.addParameter("bigint", reportPaymentParam.getUserId());
        spc.addParameter("date", reportPaymentParam.getDateBegin());
        spc.addParameter("date", reportPaymentParam.getDateEnd());
        spc.addParameter("integer", reportPaymentParam.getReportMode());
        return spc.getResultList(em, ReportPaymentData.class);
    }

}
