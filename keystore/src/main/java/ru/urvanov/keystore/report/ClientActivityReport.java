package ru.urvanov.keystore.report;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import ru.urvanov.keystore.domain.ReportActivityData;
import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.UserDetailsImpl;

public class ClientActivityReport {

    private static final Logger logger = LoggerFactory.getLogger(ClientActivityReport.class);
    
    private UserDetailsImpl userDetailsImpl;
    private ReportActivityParameters reportActivityParameters;
    private String columnHeaderFormat = "";
    private List<ReportActivityData> data;
    private Locale russianLocale = new Locale("ru", "RU", "");

    public byte[] generate() throws JRException, IOException {
        logger.debug("generate activityReport started.");
        logger.debug("dateBegin = {}.", this.reportActivityParameters.getDateBegin());
        logger.debug("dateEnd = {}.", this.reportActivityParameters.getDateEnd());
        logger.debug("reportMode = {}.", this.reportActivityParameters.getReportMode());
        logger.debug("clientId = {}.", this.reportActivityParameters.getClientId());
        switch (reportActivityParameters.getReportMode()) {
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
        params.put("dateBegin", this.reportActivityParameters.getDateBegin());
        params.put("dateEnd", this.reportActivityParameters.getDateEnd());
        params.put("reportMode", this.reportActivityParameters.getReportMode());
        params.put("clientId", this.reportActivityParameters.getClientId());
        params.put("clientName", this.reportActivityParameters.getClientName());
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

    private JRDataSource prepareDataSource(List<ReportActivityData> data) {
        List<Map<String, ?>> preparedData = new ArrayList<Map<String, ?>>(9);
        
        Map<String, Object> connectionsMap = new LinkedHashMap<String, Object>();
        Map<String, Object> sessionsTimeMap = new LinkedHashMap<String, Object>();

        preparedData.add(0, connectionsMap);
        preparedData.add(1, sessionsTimeMap);
        
        final String firstColumn = "_";
        final String secondColumn = "__";
        connectionsMap.put(firstColumn, "1");
        sessionsTimeMap.put(firstColumn, "2");
        
        connectionsMap.put(secondColumn, "Общее число подключений");
        sessionsTimeMap.put(secondColumn, "Общая длительность сессий");

        ReportActivityData summaryRad = new ReportActivityData();
        summaryRad.setConnections(new BigInteger("0"));
        summaryRad.setSessionsTime(new BigInteger("0"));
        for (ReportActivityData rad : data) {
            summaryRad.setConnections(summaryRad.getConnections().add(rad.getConnections()));
            summaryRad.setSessionsTime(summaryRad.getSessionsTime().add(
                    rad.getSessionsTime()));
            
            connectionsMap.put(String.format(russianLocale, columnHeaderFormat, rad.getDateBegin(), rad.getDateEnd()),
                    String.valueOf(rad.getConnections()));
            sessionsTimeMap.put(String.format(russianLocale, columnHeaderFormat, rad.getDateBegin(), rad.getDateEnd()),
                    numberToTimeString(rad.getSessionsTime()));
        }
        final String summaryColumn = "summary";
        connectionsMap.put(summaryColumn,
                String.valueOf(summaryRad.getConnections()));
        sessionsTimeMap.put(summaryColumn,
                numberToTimeString(summaryRad.getSessionsTime()));
        return new JRMapCollectionDataSource(preparedData);
    }

    private String numberToTimeString(BigInteger time) {
        BigInteger[] divideAndRemainder = time.divideAndRemainder(new BigInteger("60"));
        return String.format(russianLocale,"%d:%02d", divideAndRemainder[0], divideAndRemainder[1], Locale.ROOT);
    }

    private JasperDesign createActivityDesign(List<ReportActivityData> data)
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
        
        JRDesignParameter reportClientIdParameter = new JRDesignParameter();
        reportClientIdParameter.setName("clientId");
        reportClientIdParameter.setValueClass(java.lang.Long.class);
        jasperDesign.addParameter(reportClientIdParameter);
        
        JRDesignParameter reportClientNameParameter = new JRDesignParameter();
        reportClientNameParameter.setName("clientName");
        reportClientNameParameter.setValueClass(java.lang.String.class);
        jasperDesign.addParameter(reportClientNameParameter);

        field = new JRDesignField();
        field.setName("_");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);
        field = new JRDesignField();
        field.setName("__");
        field.setValueClass(java.lang.String.class);
        jasperDesign.addField(field);
        for (ReportActivityData rad : data) {
            field = new JRDesignField();
            field.setName(String.format(russianLocale, columnHeaderFormat, rad.getDateBegin(), rad.getDateEnd()));
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
        JRDesignTextField titleText = new JRDesignTextField();
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
        expression = new JRDesignExpression();
        expression.setText("\"Отчёт об активности \" +  $P{clientName}");
        titleText.setExpression(expression);
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
        for (ReportActivityData rad : data) {
            staticText = new JRDesignStaticText();
            staticText.setX(x);
            staticText.setY(0);
            staticText.setWidth(COLUMN_WIDTH);
            staticText.setHeight(ROW_HEIGHT);
            staticText.setStyle(columnHeaderCenterStyle);
            staticText.setText(String.format(russianLocale, columnHeaderFormat, rad.getDateBegin(), rad.getDateEnd()));
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
        for (ReportActivityData rad : data) {
            textField = new JRDesignTextField();
            textField.setX(x);
            textField.setY(0);
            textField.setWidth(COLUMN_WIDTH);
            textField.setHeight(ROW_HEIGHT);
            textField.setStyle(detailsCenterStyle);
            expression = new JRDesignExpression();
            expression
                    .setText("$F{" + String.format(russianLocale, columnHeaderFormat, rad.getDateBegin(), rad.getDateEnd()) + "}");
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


    public List<ReportActivityData> getData() {
        return data;
    }

    public void setData(List<ReportActivityData> data) {
        this.data = data;
    }

    public void setParameters(ReportActivityParameters param) {
        this.reportActivityParameters = param;
    }
    
    public ReportActivityParameters getReportActivityParameters() {
        return this.reportActivityParameters;
    }

}
