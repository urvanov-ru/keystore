package ru.urvanov.keystore.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ru.urvanov.keystore.domain.ReportActivityData;
import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.ReportPaymentData;
import ru.urvanov.keystore.domain.ReportPaymentParameters;

public interface ReportService {
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    List<ReportActivityData> activity(ReportActivityParameters reportActivityParam);
    
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_SERVICE')")
    List<ReportPaymentData> payment(ReportPaymentParameters reportPaymentParam);
}
