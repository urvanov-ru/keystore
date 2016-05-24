package ru.urvanov.keystore.report;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.urvanov.keystore.domain.ReportPaymentData;
import ru.urvanov.keystore.domain.ReportPaymentParameters;
import ru.urvanov.keystore.domain.UserDetailsImpl;

public class PaymentReport {

    private static final Logger logger = LoggerFactory.getLogger(ActivityReport.class);
    
    private UserDetailsImpl userDetailsImpl;
    private ReportPaymentParameters reportPaymentParameters;
    private String columnHeaderFormat = "";
    private List<ReportPaymentData> data;
    private NumberFormat moneyFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("ru", "RU", "")));
    private NumberFormat numberFormat = new DecimalFormat("0", new DecimalFormatSymbols(new Locale("ru", "RU", "")));
    

    public byte[] generate() throws JRException, IOException {
        logger.debug("generate activityReport started.");
        logger.debug("dateBegin = {}.", this.reportPaymentParameters.getDateBegin());
        logger.debug("dateEnd = {}.", this.reportPaymentParameters.getDateEnd());
        logger.debug("reportMode = {}.", this.reportPaymentParameters.getReportMode());
        switch (reportPaymentParameters.getReportMode()) {
        case 1:
            // по годам
            columnHeaderFormat = "%1$tY";
            break;
        case 2:
            // по кварталам
            columnHeaderFormat = "%1$td.%1$tm-%2$td.%2$tm.%2$tY";
            break;
        case 3:
            // по месяцам
            columnHeaderFormat = "%1$tm.%1$tY";
            break;
        case 4:
            // по неделям
            columnHeaderFormat = "%1$td-%2$td.%2$tm.%2$tY";
            break;
        case 5:
            // по дням
            columnHeaderFormat = "%1$td.%1$tm.%1$tY";
            break;
        }
        JasperDesign jasperDesign = createActivityDesign(data);
        JasperReport jasperReport = JasperCompileManager
                .compileReport(jasperDesign);
        JRDataSource jrDataSource = prepareDataSource(data);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reportDate", new Date());
        params.put("dateBegin", this.reportPaymentParameters.getDateBegin());
        params.put("dateEnd", this.reportPaymentParameters.getDateEnd());
        params.put("reportMode", this.reportPaymentParameters.getReportMode());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                params, jrDataSource);
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            exporter.exportReport();

            return os.toByteArray();
        }
    }

    private JRDataSource prepareDataSource(List<ReportPaymentData> data) {
        class DictServiceTypePayment {
            private Long dictServiceTypeId;
            private String dictServiceTypeName;
            private Map<String, String> cumulativeProfit = new LinkedHashMap<>();
            private Map<String, String> profit = new LinkedHashMap<>();
            private Map<String, String> orderPaymentAmount = new LinkedHashMap<>();
            private Map<String, String> payBackAmount = new LinkedHashMap<>();
            private Map<String, String> activeKeys = new LinkedHashMap<>();
            private Map<String, String> newKeys = new LinkedHashMap<>();
            private Map<String, String> expiredKeys = new LinkedHashMap<>();
            private Map<String, String> canceledKeys = new LinkedHashMap<>();
            private BigDecimal profitSummary = new BigDecimal("0.00");
            private BigDecimal orderPaymentAmountSummary = new BigDecimal("0.00");
            private BigDecimal payBackAmountSummary = new BigDecimal("0.00");
            private Long activeKeysSummary = 0L;
            private Long newKeysSummary = 0L;
            private Long expiredKeysSummary = 0L;
            private Long canceledKeysSummary = 0L;
            public Long getDictServiceTypeId() {
                return dictServiceTypeId;
            }
            public void setDictServiceTypeId(Long dictServiceTypeId) {
                this.dictServiceTypeId = dictServiceTypeId;
            }
            public String getDictServiceTypeName() {
                return dictServiceTypeName;
            }
            public void setDictServiceTypeName(String dictServiceTypeName) {
                this.dictServiceTypeName = dictServiceTypeName;
            }
            
            public Map<String, String> getCumulativeProfit() {
                return cumulativeProfit;
            }
            public Map<String, String> getProfit() {
                return profit;
            }
            public Map<String, String> getOrderPaymentAmount() {
                return orderPaymentAmount;
            }
            public Map<String, String> getPayBackAmount() {
                return payBackAmount;
            }
            public Map<String, String> getActiveKeys() {
                return activeKeys;
            }
            public Map<String, String> getNewKeys() {
                return newKeys;
            }
            public Map<String, String> getExpiredKeys() {
                return expiredKeys;
            }
            public Map<String, String> getCanceledKeys() {
                return canceledKeys;
            }
            public BigDecimal getProfitSummary() {
                return profitSummary;
            }
            public void setProfitSummary(BigDecimal profitSummary) {
                this.profitSummary = profitSummary;
            }
            public BigDecimal getOrderPaymentAmountSummary() {
                return orderPaymentAmountSummary;
            }
            public void setOrderPaymentAmountSummary(BigDecimal orderPaymentAmountSummary) {
                this.orderPaymentAmountSummary = orderPaymentAmountSummary;
            }
            public BigDecimal getPayBackAmountSummary() {
                return payBackAmountSummary;
            }
            public void setPayBackAmountSummary(BigDecimal payBackAmountSummary) {
                this.payBackAmountSummary = payBackAmountSummary;
            }
            
            public Long getActiveKeysSummary() {
                return activeKeysSummary;
            }
            public void setActiveKeysSummary(Long activeKeysSummary) {
                this.activeKeysSummary = activeKeysSummary;
            }
            public Long getNewKeysSummary() {
                return newKeysSummary;
            }
            public void setNewKeysSummary(Long newKeysSummary) {
                this.newKeysSummary = newKeysSummary;
            }
            public Long getExpiredKeysSummary() {
                return expiredKeysSummary;
            }
            public void setExpiredKeysSummary(Long expiredKeysSummary) {
                this.expiredKeysSummary = expiredKeysSummary;
            }
            public Long getCanceledKeysSummary() {
                return canceledKeysSummary;
            }
            public void setCanceledKeysSummary(Long canceledKeysSummary) {
                this.canceledKeysSummary = canceledKeysSummary;
            }
            
        }
        
        Map<Long, DictServiceTypePayment> tempData = new TreeMap<>();
        
        for (ReportPaymentData rpd : data) {
            Long dictServiceTypeId = rpd.getDictServiceTypeId() == null ? 0 : rpd.getDictServiceTypeId();
            DictServiceTypePayment dstp = null;
            if (!tempData.containsKey(dictServiceTypeId)) {
                dstp = new DictServiceTypePayment();
                tempData.put(dictServiceTypeId, dstp);
            } else
                dstp = tempData.get(dictServiceTypeId);
            String columnHeader = String.format(columnHeaderFormat, rpd.getDateBegin(), rpd.getDateEnd());
            dstp.getProfit().put(columnHeader, moneyFormat.format(rpd.getProfit()));
            dstp.getOrderPaymentAmount().put(columnHeader, moneyFormat.format(rpd.getOrderPaymentAmount()));
            dstp.getPayBackAmount().put(columnHeader, moneyFormat.format(rpd.getPayBackAmount()));
            dstp.getActiveKeys().put(columnHeader, numberFormat.format(rpd.getActiveKeys()));
            dstp.getNewKeys().put(columnHeader, numberFormat.format(rpd.getNewKeys()));
            dstp.getExpiredKeys().put(columnHeader, numberFormat.format(rpd.getExpiredKeys()));
            dstp.getCanceledKeys().put(columnHeader, numberFormat.format(rpd.getCanceledKeys()));
            
            dstp.setProfitSummary(dstp.getProfitSummary().add(rpd.getProfit()));
            dstp.getCumulativeProfit().put(columnHeader, moneyFormat.format(dstp.getProfitSummary()));
            dstp.setOrderPaymentAmountSummary(dstp.getOrderPaymentAmountSummary().add(rpd.getOrderPaymentAmount()));
            dstp.setPayBackAmountSummary(dstp.getPayBackAmountSummary().add(rpd.getPayBackAmount()));
            dstp.setActiveKeysSummary(dstp.getActiveKeysSummary() + rpd.getActiveKeys());
            dstp.setNewKeysSummary(dstp.getNewKeysSummary() + rpd.getNewKeys());
            dstp.setExpiredKeysSummary(dstp.getExpiredKeysSummary() + rpd.getExpiredKeys());
            dstp.setCanceledKeysSummary(dstp.getCanceledKeysSummary() + rpd.getCanceledKeys());
            dstp.setDictServiceTypeId(dictServiceTypeId);
            dstp.setDictServiceTypeName(rpd.getDictServiceTypeName());
        }
        
        
        List<Map<String, ?>> preparedData = new ArrayList<Map<String, ?>>(9);
        
        for (Entry<Long, DictServiceTypePayment> entry : tempData.entrySet()) {
            Map<String, Object> cumulativeProfitMap = new LinkedHashMap<>();
            Map<String, Object> profitMap = new LinkedHashMap<>();
            Map<String, Object> orderPaymentAmountMap = new LinkedHashMap<>();
            Map<String, Object> payBackAmountMap = new LinkedHashMap<>();
            Map<String, Object> activeKeysMap = new LinkedHashMap<>();
            Map<String, Object> newKeysMap = new LinkedHashMap<>();
            Map<String, Object> expiredKeysMap = new LinkedHashMap<>();
            Map<String, Object> canceledKeysMap = new LinkedHashMap<>();
            DictServiceTypePayment dstp = entry.getValue();
            String dictServiceTypeName = dstp.getDictServiceTypeName() == null ? "Сводная информация" : dstp.getDictServiceTypeName();
            final String firstColumn = "_";
            final String secondColumn = "__";
            final String summaryColumn = "summary";
            
            cumulativeProfitMap.put(firstColumn, "");
            profitMap.put(firstColumn, "1");
            orderPaymentAmountMap.put(firstColumn, "1.1");
            payBackAmountMap.put(firstColumn, "1.2");
            activeKeysMap.put(firstColumn, "2");
            newKeysMap.put(firstColumn, "2.1");
            expiredKeysMap.put(firstColumn, "2.2");
            canceledKeysMap.put(firstColumn, "2.3");
            
            cumulativeProfitMap.put(secondColumn, dictServiceTypeName);
            profitMap.put(secondColumn, dstp.getDictServiceTypeId() == null ? "Общий доход" : "Доход по пакету");
            orderPaymentAmountMap.put(secondColumn, "Поступления");
            payBackAmountMap.put(secondColumn, "Возвраты");
            activeKeysMap.put(secondColumn, "Активных ключей");
            newKeysMap.put(secondColumn, "Приобретено");
            expiredKeysMap.put(secondColumn, "Истекло");
            canceledKeysMap.put(secondColumn, "Вернули");
            
            cumulativeProfitMap.putAll(dstp.getCumulativeProfit());
            profitMap.putAll(dstp.getProfit());
            orderPaymentAmountMap.putAll(dstp.getOrderPaymentAmount());
            payBackAmountMap.putAll(dstp.getPayBackAmount());
            activeKeysMap.putAll(dstp.getActiveKeys());
            newKeysMap.putAll(dstp.getNewKeys());
            expiredKeysMap.putAll(dstp.getExpiredKeys());
            canceledKeysMap.putAll(dstp.getCanceledKeys());
            
            cumulativeProfitMap.put(summaryColumn, moneyFormat.format(dstp.getProfitSummary()));
            profitMap.put(summaryColumn, moneyFormat.format(dstp.getProfitSummary()));
            orderPaymentAmountMap.put(summaryColumn, moneyFormat.format(dstp.getOrderPaymentAmountSummary()));
            payBackAmountMap.put(summaryColumn, moneyFormat.format(dstp.getPayBackAmountSummary()));
            activeKeysMap.put(summaryColumn, numberFormat.format(dstp.getActiveKeysSummary()));
            newKeysMap.put(summaryColumn, numberFormat.format(dstp.getNewKeysSummary()));
            expiredKeysMap.put(summaryColumn, numberFormat.format(dstp.getExpiredKeysSummary()));
            canceledKeysMap.put(summaryColumn, numberFormat.format(dstp.getCanceledKeysSummary()));
            
            preparedData.add(cumulativeProfitMap);
            preparedData.add(profitMap);
            preparedData.add(orderPaymentAmountMap);
            preparedData.add(payBackAmountMap);
            preparedData.add(activeKeysMap);
            preparedData.add(newKeysMap);
            preparedData.add(expiredKeysMap);
            preparedData.add(canceledKeysMap);
        }
        
        return new JRMapCollectionDataSource(preparedData);
    }

    private JasperDesign createActivityDesign(List<ReportPaymentData> data2)
            throws JRException {
        JRDesignStaticText staticText = null;
        JRDesignTextField textField = null;
        JRDesignBand band = null;
        JRDesignExpression expression = null;
        // JRDesignFrame frame = null;
        // JRDesignLine line = null;
        JRDesignField field = null;
        JRDesignConditionalStyle conditionalStyle = null;
        JRLineBox lineBox = null;
        // JRPropertyExpression propertyExpression = null;

        int x;
        int y;
        final int ROW_HEIGHT = 11;
        final int COLUMN_WIDTH = 100;

        Set<String> columnNames = new LinkedHashSet<String>();
        for (ReportPaymentData rad : data2) {
            columnNames.add(String.format(columnHeaderFormat, rad.getDateBegin(), rad.getDateEnd()));
        }
        
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("report_activity");
        jasperDesign.setPageWidth(1000);
        jasperDesign.setPageHeight(500);
        jasperDesign.setColumnWidth(COLUMN_WIDTH);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(40);
        jasperDesign.setRightMargin(40);
        jasperDesign.setTopMargin(40);
        jasperDesign.setBottomMargin(40);
        jasperDesign.setIgnorePagination(true);

        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Tahoma_Normal");
        normalStyle.setDefault(true);
        normalStyle.setFontName("Tahoma");
        normalStyle.setFontSize(8.5f);
        jasperDesign.addStyle(normalStyle);

        JRDesignStyle italicStyle = new JRDesignStyle();
        italicStyle.setName("Tahoma_Italic");
        italicStyle.setFontName("Tahoma");
        italicStyle.setFontSize(8.5f);
        italicStyle.setItalic(true);
        jasperDesign.addStyle(italicStyle);

        JRDesignStyle columnHeaderStyle = new JRDesignStyle();
        columnHeaderStyle.setName("Column_Header");
        columnHeaderStyle.setFontName("Tahoma");
        columnHeaderStyle.setFontSize(8.5f);
        columnHeaderStyle.setBold(true);
        columnHeaderStyle.setBackcolor(new Color(47, 117, 181));
        columnHeaderStyle.setForecolor(new Color(255, 255, 255));
        columnHeaderStyle.setFill(FillEnum.SOLID);
        columnHeaderStyle.setMode(ModeEnum.OPAQUE);
        lineBox = columnHeaderStyle.getLineBox();
        lineBox.getTopPen().setLineColor(new Color(155, 194, 230));
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineColor(new Color(155, 194, 230));
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineColor(new Color(155, 194, 230));
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineColor(new Color(155, 194, 230));
        lineBox.getLeftPen().setLineWidth(0.5f);
        jasperDesign.addStyle(columnHeaderStyle);

        JRDesignStyle columnHeaderCenterStyle = new JRDesignStyle();
        columnHeaderCenterStyle.setName("Column_Center_Header");
        columnHeaderCenterStyle.setFontName("Tahoma");
        columnHeaderCenterStyle.setFontSize(8.5f);
        columnHeaderCenterStyle.setBold(true);
        columnHeaderCenterStyle.setBackcolor(new Color(47, 117, 181));
        columnHeaderCenterStyle.setForecolor(new Color(255, 255, 255));
        columnHeaderCenterStyle.setFill(FillEnum.SOLID);
        columnHeaderCenterStyle.setMode(ModeEnum.OPAQUE);
        columnHeaderCenterStyle
                .setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        lineBox = columnHeaderCenterStyle.getLineBox();
        lineBox.getTopPen().setLineColor(new Color(155, 194, 230));
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineColor(new Color(155, 194, 230));
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineColor(new Color(155, 194, 230));
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineColor(new Color(155, 194, 230));
        lineBox.getLeftPen().setLineWidth(0.5f);
        jasperDesign.addStyle(columnHeaderCenterStyle);

        JRDesignStyle detailsStyle = new JRDesignStyle();
        detailsStyle.setName("Details_Style");
        detailsStyle.setFontName("Tahoma");
        detailsStyle.setFontSize(8.5f);
        detailsStyle.setMode(ModeEnum.OPAQUE);
        detailsStyle.setFill(FillEnum.SOLID);
        lineBox = detailsStyle.getLineBox();
        lineBox.getTopPen().setLineColor(new Color(155, 194, 230));
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineColor(new Color(155, 194, 230));
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineColor(new Color(155, 194, 230));
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineColor(new Color(155, 194, 230));
        lineBox.getLeftPen().setLineWidth(0.5f);
        JRDesignConditionalStyle clientsDetailsStyle = new JRDesignConditionalStyle();
        clientsDetailsStyle.setBackcolor(new Color(221, 235, 247));
        expression = new JRDesignExpression();
        expression.setText("!$F{_}.isEmpty() && !$F{_}.contains(\".\");");
        clientsDetailsStyle.setConditionExpression(expression);
        detailsStyle.addConditionalStyle(clientsDetailsStyle);
        JRDesignConditionalStyle cumulativeProfitStyle = new JRDesignConditionalStyle();
        cumulativeProfitStyle.setBackcolor(new Color(142, 169, 219));
        expression = new JRDesignExpression();
        expression.setText("$F{_}.isEmpty();");
        cumulativeProfitStyle.setConditionExpression(expression);
        detailsStyle.addConditionalStyle(cumulativeProfitStyle);
        jasperDesign.addStyle(detailsStyle);
        
        JRDesignStyle detailsCenterStyle = new JRDesignStyle();
        detailsCenterStyle.setName("Details_Clone_Style");
        detailsCenterStyle.setFontName("Tahoma");
        detailsCenterStyle.setFontSize(8.5f);
        detailsCenterStyle.setMode(ModeEnum.OPAQUE);
        detailsCenterStyle.setFill(FillEnum.SOLID);
        detailsCenterStyle
                .setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        lineBox = detailsCenterStyle.getLineBox();
        lineBox.getTopPen().setLineColor(new Color(155, 194, 230));
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineColor(new Color(155, 194, 230));
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineColor(new Color(155, 194, 230));
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineColor(new Color(155, 194, 230));
        lineBox.getLeftPen().setLineWidth(0.5f);
        conditionalStyle = new JRDesignConditionalStyle();
        conditionalStyle.setBackcolor(new Color(221, 235, 247));
        expression = new JRDesignExpression();
        expression.setText("!$F{_}.isEmpty() && !$F{_}.contains(\".\");");
        conditionalStyle.setConditionExpression(expression);
        detailsCenterStyle.addConditionalStyle(conditionalStyle);
        conditionalStyle = new JRDesignConditionalStyle();
        conditionalStyle.setBackcolor(new Color(142, 169, 219));
        expression = new JRDesignExpression();
        expression.setText("$F{_}.isEmpty();");
        conditionalStyle.setConditionExpression(expression);
        detailsCenterStyle.addConditionalStyle(conditionalStyle);
        jasperDesign.addStyle(detailsCenterStyle);

        JRDesignStyle detailsSummaryStyle = new JRDesignStyle();
        detailsSummaryStyle.setName("Details_Summary_Style");
        detailsSummaryStyle.setFontName("Tahoma");
        detailsSummaryStyle.setFontSize(8.5f);
        detailsSummaryStyle.setMode(ModeEnum.OPAQUE);
        detailsSummaryStyle.setFill(FillEnum.SOLID);
        detailsSummaryStyle.setBackcolor(new Color(255, 230, 153));
        detailsSummaryStyle
                .setHorizontalTextAlign(HorizontalTextAlignEnum.RIGHT);
        lineBox = detailsSummaryStyle.getLineBox();
        lineBox.getTopPen().setLineColor(new Color(155, 194, 230));
        lineBox.getTopPen().setLineWidth(0.5f);
        lineBox.getRightPen().setLineColor(new Color(155, 194, 230));
        lineBox.getRightPen().setLineWidth(0.5f);
        lineBox.getBottomPen().setLineColor(new Color(155, 194, 230));
        lineBox.getBottomPen().setLineWidth(0.5f);
        lineBox.getLeftPen().setLineColor(new Color(155, 194, 230));
        lineBox.getLeftPen().setLineWidth(0.5f);
        conditionalStyle = new JRDesignConditionalStyle();
        conditionalStyle.setBackcolor(new Color(189, 215, 238));
        expression = new JRDesignExpression();
        expression.setText("!$F{_}.isEmpty() && !$F{_}.contains(\".\");");
        conditionalStyle.setConditionExpression(expression);
        detailsSummaryStyle.addConditionalStyle(conditionalStyle);
        conditionalStyle = new JRDesignConditionalStyle();
        conditionalStyle.setBackcolor(new Color(142, 169, 219));
        expression = new JRDesignExpression();
        expression.setText("$F{_}.isEmpty();");
        conditionalStyle.setConditionExpression(expression);
        detailsSummaryStyle.addConditionalStyle(conditionalStyle);
        jasperDesign.addStyle(detailsSummaryStyle);

        JRDesignParameter userIdParameter = new JRDesignParameter();
        userIdParameter.setName("userId");
        userIdParameter.setValueClass(java.lang.Long.class);
        jasperDesign.addParameter(userIdParameter);

        JRDesignParameter dateBeginParameter = new JRDesignParameter();
        dateBeginParameter.setName("dateBegin");
        dateBeginParameter.setValueClass(java.util.Date.class);
        jasperDesign.addParameter(dateBeginParameter);

        JRDesignParameter dateEndParameter = new JRDesignParameter();
        dateEndParameter.setName("dateEnd");
        dateEndParameter.setValueClass(java.util.Date.class);
        jasperDesign.addParameter(dateEndParameter);

        JRDesignParameter reportModeParameter = new JRDesignParameter();
        reportModeParameter.setName("reportMode");
        reportModeParameter.setValueClass(java.lang.Integer.class);
        jasperDesign.addParameter(reportModeParameter);

        JRDesignParameter reportDateParameter = new JRDesignParameter();
        reportDateParameter.setName("reportDate");
        reportDateParameter.setValueClass(java.util.Date.class);
        jasperDesign.addParameter(reportDateParameter);
        


        field = new JRDesignField();
        field.setName("_");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);
        field = new JRDesignField();
        field.setName("__");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);
        for (String columnName : columnNames) {
            field = new JRDesignField();
            field.setName(columnName);
            field.setValueClass(java.lang.String.class);
            jasperDesign.addField(field);
        }
        field = new JRDesignField();
        field.setName("summary");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);

        // Title
        band = new JRDesignBand();
        band.setHeight(ROW_HEIGHT * 2 + 20);
        JRDesignStaticText titleText = new JRDesignStaticText();
        x = 0;
        y = 0;
        titleText.setFontName("Tahoma");
        titleText.setFontSize(12.0f);
        titleText.setForecolor(new Color(31, 78, 120));
        titleText.setBold(true);
        titleText.setX(x);
        titleText.setY(y);
        titleText.setWidth(600);
        titleText.setHeight(20);
        titleText.setText("Финансовые показатели KeyStore");
        band.addElement(titleText);
        y += titleText.getHeight();

        staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(100);
        staticText.setHeight(ROW_HEIGHT);
        staticText.setText("Сформирован");
        staticText.setStyle(normalStyle);
        x += staticText.getWidth();
        band.addElement(staticText);

        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(100);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(italicStyle);
        expression = new JRDesignExpression();
        expression.setText("$P{reportDate}");
        textField.setExpression(expression);
        textField.setPattern("dd.MM.yyyy");
        y += textField.getHeight();
        x = 0;
        band.addElement(textField);

        staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(100);
        staticText.setHeight(ROW_HEIGHT);
        staticText.setText("За период");
        x += staticText.getWidth();
        band.addElement(staticText);

        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(100);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(italicStyle);
        expression = new JRDesignExpression();
        expression.setText("$P{dateBegin}");
        textField.setExpression(expression);
        textField.setPattern("dd.MM.yyyy");
        x += textField.getWidth();
        band.addElement(textField);

        staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(30);
        staticText.setHeight(ROW_HEIGHT);
        staticText.setText("по");
        x += staticText.getWidth();
        band.addElement(staticText);

        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(100);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(italicStyle);
        expression = new JRDesignExpression();
        expression.setText("$P{dateEnd}");
        textField.setExpression(expression);
        textField.setPattern("dd.MM.yyyy");
        x += textField.getWidth();
        band.addElement(textField);
        
        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(y);
        textField.setWidth(100);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(italicStyle);
        expression = new JRDesignExpression();
        expression.setText("$P{reportMode} == 1 ? \"по годам\" : $P{reportMode} == 2 ? \"по кварталам\" : $P{reportMode} == 3 ? \"по месяцам\" : $P{reportMode} == 4 ? \"по неделям\" : $P{reportMode} == 5 ? \"по дням\" : \"неизвестно\"");
        textField.setExpression(expression);
        x += textField.getWidth();
        band.addElement(textField);
        
        jasperDesign.setTitle(band);

        band = new JRDesignBand();
        jasperDesign.setPageHeader(band);

        // column header
        band = new JRDesignBand();
        band.setHeight(ROW_HEIGHT);
        x = 0;
        staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(0);
        staticText.setWidth(30);
        staticText.setHeight(ROW_HEIGHT);
        staticText.setStyle(columnHeaderStyle);
        staticText.setText("№");
        band.addElement(staticText);
        x += staticText.getWidth();
        staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(0);
        staticText.setWidth(200);
        staticText.setHeight(ROW_HEIGHT);
        staticText.setStyle(columnHeaderStyle);
        staticText.setText("Показатель");
        band.addElement(staticText);
        x += staticText.getWidth();
        for (String columnName : columnNames) {
            staticText = new JRDesignStaticText();
            staticText.setX(x);
            staticText.setY(0);
            staticText.setWidth(COLUMN_WIDTH);
            staticText.setHeight(ROW_HEIGHT);
            staticText.setStyle(columnHeaderCenterStyle);
            staticText.setText(columnName);
            band.addElement(staticText);
            x += staticText.getWidth();
        }
        staticText = new JRDesignStaticText();
        staticText.setX(x);
        staticText.setY(0);
        staticText.setWidth(COLUMN_WIDTH);
        staticText.setHeight(ROW_HEIGHT);
        staticText.setStyle(columnHeaderCenterStyle);
        staticText.setText("Итого");
        band.addElement(staticText);
        x += staticText.getWidth();
        jasperDesign.setColumnHeader(band);

        // Detail
        band = new JRDesignBand();
        band.setHeight(ROW_HEIGHT);
        x = 0;
        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(0);
        textField.setWidth(30);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(detailsStyle);
        expression = new JRDesignExpression();
        expression.setText("$F{_}");
        textField.setExpression(expression);
        band.addElement(textField);
        x += textField.getWidth();
        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(0);
        textField.setWidth(200);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(detailsStyle);
        expression = new JRDesignExpression();
        expression.setText("$F{__}");
        textField.setExpression(expression);
        band.addElement(textField);
        x += textField.getWidth();
        for (String columnName : columnNames) {
            textField = new JRDesignTextField();
            textField.setX(x);
            textField.setY(0);
            textField.setWidth(COLUMN_WIDTH);
            textField.setHeight(ROW_HEIGHT);
            textField.setStyle(detailsCenterStyle);
            expression = new JRDesignExpression();
            expression
                    .setText("$F{" + columnName + "}");
            textField.setExpression(expression);
            band.addElement(textField);
            x += textField.getWidth();
        }
        textField = new JRDesignTextField();
        textField.setX(x);
        textField.setY(0);
        textField.setWidth(COLUMN_WIDTH);
        textField.setHeight(ROW_HEIGHT);
        textField.setStyle(detailsSummaryStyle);
        expression = new JRDesignExpression();
        expression.setText("$F{summary}");
        textField.setExpression(expression);
        band.addElement(textField);
        x += textField.getWidth();
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);

        // Column footer
        band = new JRDesignBand();
        band.setHeight(ROW_HEIGHT);
        jasperDesign.setColumnFooter(band);

        band = new JRDesignBand();
        jasperDesign.setPageFooter(band);

        band = new JRDesignBand();
        jasperDesign.setSummary(band);

        return jasperDesign;
    }

    public UserDetailsImpl getUserDetailsImpl() {
        return userDetailsImpl;
    }

    public void setUserDetailsImpl(UserDetailsImpl userDetailsImpl) {
        this.userDetailsImpl = userDetailsImpl;
    }

    public List<ReportPaymentData> getData() {
        return data;
    }

    public void setData(List<ReportPaymentData> data) {
        this.data = data;
    }

    public void setParameters(
            ReportPaymentParameters reportPaymentParameters) {
        this.reportPaymentParameters = reportPaymentParameters;
    }

    


}
