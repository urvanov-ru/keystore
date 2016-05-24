package ru.urvanov.keystore.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.urvanov.keystore.annotation.CurrentUser;
import ru.urvanov.keystore.controller.domain.ListResponse;
import ru.urvanov.keystore.domain.ReportActivityData;
import ru.urvanov.keystore.domain.ReportActivityParameters;
import ru.urvanov.keystore.domain.ReportPaymentData;
import ru.urvanov.keystore.domain.ReportPaymentParameters;
import ru.urvanov.keystore.domain.UserDetailsImpl;
import ru.urvanov.keystore.report.ActivityReport;
import ru.urvanov.keystore.report.ClientActivityReport;
import ru.urvanov.keystore.report.PaymentReport;
import ru.urvanov.keystore.service.ClientService;
import ru.urvanov.keystore.service.ReportService;

@Controller
@RequestMapping(value = "/report")
public class ReportController {

    private static final Logger logger = LoggerFactory
            .getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private ClientService clientService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @RequestMapping(value = "/activity", method = { RequestMethod.GET,
            RequestMethod.POST })
    public void activity(@CurrentUser UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "dateBegin") Date dateBegin,
            @RequestParam(value = "dateEnd") Date dateEnd,
            @RequestParam(value = "reportMode") Integer reportMode,
            @RequestParam(value = "clientId", required = false) Long clientId,
            HttpServletResponse httpServletResponse) throws JRException,
            IOException {
        logger.info("/report/activity started.");
        ReportActivityParameters param = new ReportActivityParameters();
        param.setUserId(userDetailsImpl.getUser().getId());
        param.setDateBegin(dateBegin);
        param.setDateEnd(dateEnd);
        param.setReportMode(reportMode);
        param.setClientId(clientId);
        if (clientId != null) {
            param.setClientName(clientService.findById(clientId).getName());
        }
        List<ReportActivityData> data = reportService.activity(param);
        logger.info("data.size = {}.", data.size());
        byte[] reportBytes = null;
        if (param.getClientId() == null) {
            ActivityReport activityReport = new ActivityReport();
            activityReport.setParameters(param);
            activityReport.setData(data);
            reportBytes = activityReport.generate();
        } else {
            ClientActivityReport activityReport = new ClientActivityReport();
            activityReport.setParameters(param);
            activityReport.setData(data);
            reportBytes = activityReport.generate();
        }
        httpServletResponse
                .setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition",
                "attachment;filename=report_activity.xlsx");
        httpServletResponse.getOutputStream().write(reportBytes);
        httpServletResponse.flushBuffer();
        logger.info("/report/activity finished.");
    }
    
    @RequestMapping(value = "/activityJson", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ListResponse<ReportActivityData> activityJson(@CurrentUser UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "dateBegin") Date dateBegin,
            @RequestParam(value = "dateEnd") Date dateEnd,
            @RequestParam(value = "reportMode") Integer reportMode,
            @RequestParam(value = "clientId", required = false) Long clientId,
            HttpServletResponse httpServletResponse) throws JRException,
            IOException {
        logger.info("/report/activityJson started.");
        ReportActivityParameters param = new ReportActivityParameters();
        param.setUserId(userDetailsImpl.getUser().getId());
        param.setDateBegin(dateBegin);
        param.setDateEnd(dateEnd);
        param.setReportMode(reportMode);
        param.setClientId(clientId);
        if (clientId != null) {
            param.setClientName(clientService.findById(clientId).getName());
        }
        List<ReportActivityData> data = reportService.activity(param);
        logger.info("data.size = {}.", data.size());
        ListResponse<ReportActivityData> result = new ListResponse<>();
        result.setSuccess(true);
        result.setInfo(data);
        logger.info("/report/activity finished.");
        return result;
    }

    @RequestMapping(value = "/payment", method = { RequestMethod.GET,
            RequestMethod.POST })
    public void payment(@CurrentUser UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "dateBegin") Date dateBegin,
            @RequestParam(value = "dateEnd") Date dateEnd,
            @RequestParam(value = "reportMode") Integer reportMode,
            HttpServletResponse httpServletResponse) throws JRException,
            IOException {
        logger.info("/report/payment started.");
        ReportPaymentParameters param = new ReportPaymentParameters();
        param.setUserId(userDetailsImpl.getUser().getId());
        param.setDateBegin(dateBegin);
        param.setDateEnd(dateEnd);
        param.setReportMode(reportMode);
        List<ReportPaymentData> data = reportService.payment(param);
        logger.info("data.size = {}.", data.size());
        byte[] reportBytes = null;
            PaymentReport paymentReport = new PaymentReport();
            paymentReport.setParameters(param);
            paymentReport.setData(data);
            reportBytes = paymentReport.generate();

        httpServletResponse
                .setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition",
                "attachment;filename=report_payment.xlsx");
        httpServletResponse.getOutputStream().write(reportBytes);
        httpServletResponse.flushBuffer();
        logger.info("/report/payment finished.");
    }


    @RequestMapping(value = "/paymentJson", method = { RequestMethod.GET,
            RequestMethod.POST })
    public @ResponseBody ListResponse<ReportPaymentData> paymentJson(@CurrentUser UserDetailsImpl userDetailsImpl,
            @RequestParam(value = "dateBegin") Date dateBegin,
            @RequestParam(value = "dateEnd") Date dateEnd,
            @RequestParam(value = "reportMode") Integer reportMode,
            HttpServletResponse httpServletResponse) throws JRException,
            IOException {
        logger.info("/report/paymentJson started.");
        ReportPaymentParameters param = new ReportPaymentParameters();
        param.setUserId(userDetailsImpl.getUser().getId());
        param.setDateBegin(dateBegin);
        param.setDateEnd(dateEnd);
        param.setReportMode(reportMode);
        List<ReportPaymentData> data = reportService.payment(param);
        logger.info("data.size = {}.", data.size());
        ListResponse<ReportPaymentData> result = new ListResponse<>();
        result.setInfo(data);
        result.setSuccess(true);
        logger.info("/report/paymentJson finished.");
        return result;
    }

}
