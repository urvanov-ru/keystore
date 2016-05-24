package ru.urvanov.keystore.dao;

import java.util.List;

import ru.urvanov.keystore.domain.ReportActivityData;
import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.ReportPaymentData;
import ru.urvanov.keystore.domain.ReportPaymentParameters;

public interface ReportDao {

    List<ReportActivityData> activity(ReportActivityParameters reportActivityParam);

    List<ReportPaymentData> payment(ReportPaymentParameters reportPaymentParam);

}
