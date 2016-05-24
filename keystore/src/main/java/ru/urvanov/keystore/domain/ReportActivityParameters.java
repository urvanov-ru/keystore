package ru.urvanov.keystore.domain;

import java.util.Date;

public class ReportActivityParameters {

    public static final Integer REPORT_MODE_YEAR = 1;
    public static final Integer REPORT_MODE_QUARTER = 2;
    public static final Integer REPORT_MODE_MONTH = 3;
    public static final Integer REPORT_MODE_WEEK = 4;
    public static final Integer REPORT_MODE_DAY = 5;
    
    private Long userId;
    private Date dateBegin;
    private Date dateEnd;
    private Integer reportMode;
    private Long clientId;
    private String clientName;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Date getDateBegin() {
        return dateBegin;
    }
    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }
    public Date getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
    public Integer getReportMode() {
        return reportMode;
    }
    public void setReportMode(Integer reportMode) {
        this.reportMode = reportMode;
    }
    
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    @Override
    public String toString() {
        return "ReportActivityParameters [userId=" + userId + ", dateBegin="
                + dateBegin + ", dateEnd=" + dateEnd + ", reportMode="
                + reportMode + ", clientId=" + clientId + ", clientName="
                + clientName + "]";
    }
    
    

}
