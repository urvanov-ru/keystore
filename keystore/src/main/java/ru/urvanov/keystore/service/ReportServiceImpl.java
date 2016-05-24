package ru.urvanov.keystore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.keystore.dao.ReportDao;
import ru.urvanov.keystore.domain.ReportActivityData;
import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.ReportPaymentData;
import ru.urvanov.keystore.domain.ReportPaymentParameters;


@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Override
    @Transactional
    public List<ReportActivityData> activity(ReportActivityParameters reportActivityParam) {
        return reportDao.activity(reportActivityParam);
    }

    @Override
    @Transactional
    public List<ReportPaymentData> payment(
            ReportPaymentParameters reportPaymentParam) {
        return reportDao.payment(reportPaymentParam);
    }

}
