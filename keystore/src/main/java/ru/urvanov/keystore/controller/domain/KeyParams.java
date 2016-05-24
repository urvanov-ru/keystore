package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyParams implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7232653656823235768L;

    private static final Logger logger = LoggerFactory
            .getLogger(KeyParams.class);

    private String dictServiceTypeId;
    private String kind;
    private Date dateBegin;
    private Date dateEnd;
    private String password;
    private Boolean ethnosImportEnabled;
    private Boolean ethnosExportEnabled;
    private Boolean xlsEnabled;
    private Boolean officeEnabled;
    private Boolean csvEnabled;
    private Boolean xlsxEnabled;
    private Integer questionnaireLimit;
    private Integer qLimitPerDay;
    private Integer qLimitPerWeek;
    private Integer qLimitPerMonth;
    private Integer devicesLimit;

    public String getDictServiceTypeId() {
        return dictServiceTypeId;
    }

    public void setDictServiceTypeId(String dictServiceTypeId) {
        this.dictServiceTypeId = dictServiceTypeId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEthnosImportEnabled() {
        return ethnosImportEnabled;
    }

    public void setEthnosImportEnabled(Boolean ethnosImportEnabled) {
        this.ethnosImportEnabled = ethnosImportEnabled;
    }

    public Boolean getEthnosExportEnabled() {
        return ethnosExportEnabled;
    }

    public void setEthnosExportEnabled(Boolean ethnosExportEnabled) {
        this.ethnosExportEnabled = ethnosExportEnabled;
    }

    public Boolean getXlsEnabled() {
        return xlsEnabled;
    }

    public void setXlsEnabled(Boolean xlsEnabled) {
        this.xlsEnabled = xlsEnabled;
    }

    public Boolean getOfficeEnabled() {
        return officeEnabled;
    }

    public void setOfficeEnabled(Boolean officeEnabled) {
        this.officeEnabled = officeEnabled;
    }

    public Boolean getCsvEnabled() {
        return csvEnabled;
    }

    public void setCsvEnabled(Boolean csvEnabled) {
        this.csvEnabled = csvEnabled;
    }

    public Boolean getXlsxEnabled() {
        return xlsxEnabled;
    }

    public void setXlsxEnabled(Boolean xlsxEnabled) {
        this.xlsxEnabled = xlsxEnabled;
    }

    public Integer getQuestionnaireLimit() {
        return questionnaireLimit;
    }

    public void setQuestionnaireLimit(Integer questionnaireLimit) {
        this.questionnaireLimit = questionnaireLimit;
    }

    public Integer getqLimitPerDay() {
        return qLimitPerDay;
    }

    public void setqLimitPerDay(Integer qLimitPerDay) {
        this.qLimitPerDay = qLimitPerDay;
    }

    public Integer getqLimitPerWeek() {
        return qLimitPerWeek;
    }

    public void setqLimitPerWeek(Integer qLimitPerWeek) {
        this.qLimitPerWeek = qLimitPerWeek;
    }

    public Integer getqLimitPerMonth() {
        return qLimitPerMonth;
    }

    public void setqLimitPerMonth(Integer qLimitPerMonth) {
        this.qLimitPerMonth = qLimitPerMonth;
    }

    public Integer getDevicesLimit() {
        return devicesLimit;
    }

    public void setDevicesLimit(Integer devicesLimit) {
        this.devicesLimit = devicesLimit;
    }

    public void toLog() {
        logger.debug("KeyParams");
        logger.debug("dictServiceTypeId=" + dictServiceTypeId);
        logger.debug("kind=" + kind);
        logger.debug("dateBegin=" + dateBegin);
        logger.debug("dateEnd=" + dateEnd);
        logger.debug("password=" + password);
        logger.debug("ethnosImportEnabled=" + ethnosImportEnabled);
        logger.debug("ethnosExportEnabled=" + ethnosExportEnabled);
        logger.debug("xlsEnabled=" + xlsEnabled);
        logger.debug("officeEnabled=" + officeEnabled);
        logger.debug("csvEnabled=" + csvEnabled);
        logger.debug("xlsxEnabled=" + xlsxEnabled);
        logger.debug("questionnaireLimit=" + questionnaireLimit);
        logger.debug("qLimitPerDay=" + qLimitPerDay);
        logger.debug("qLimitPerWeek=" + qLimitPerWeek);
        logger.debug("qLimitPerMonth=" + qLimitPerMonth);
        logger.debug("devicesLimit=" + devicesLimit);
    }

}
