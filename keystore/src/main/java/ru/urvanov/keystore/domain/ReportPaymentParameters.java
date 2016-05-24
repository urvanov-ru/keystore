package ru.urvanov.keystore.domain;

import java.io.Serializable;
import java.util.Date;


public class ReportPaymentParameters implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6338003266961198848L;
    public static final Integer REPORT_MODE_YEAR = 1;
    public static final Integer REPORT_MODE_QUARTER = 2;
    public static final Integer REPORT_MODE_MONTH = 3;
    public static final Integer REPORT_MODE_WEEK = 4;
    public static final Integer REPORT_MODE_DAY = 5;
    
    private Long userId;
    private Date dateBegin;
    private Date dateEnd;
    private Integer reportMode;
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
    @Override
    public String toString() {
        return "ReportPaymentParameters [userId=" + userId + ", dateBegin="
                + dateBegin + ", dateEnd=" + dateEnd + ", reportMode="
                + reportMode + "]";
    }
    
    
    

}
