package com.idb.hmis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.SummaryService;
import java.io.OutputStream;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/report/")
public class ReportController {

    @Autowired
    public SummaryService summaryService;
    @Autowired
    public ModuleController moduleController;

    @GetMapping("income_statement")
    public String incomeStatement() {
        return "pages/income-statement";
    }
    @GetMapping("balance_sheet")
    public String balanceSheet() {
        return "pages/balance-sheet";
    }

    @PostMapping("generate")
    public void getncomeStatement(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long hostelId = null;
        Long branchId = null;
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        String scope, reportType, yearString;
        scope = request.getParameter("scope");
        reportType = request.getParameter("type");
        yearString = request.getParameter("year");
        if (scope.equals("branch")) {
            branchId = (Long) request.getSession().getAttribute("branchId");
        } else {
            hostelId = (Long) request.getSession().getAttribute("hostelId");
        }
        if (!yearString.isEmpty()) {
            year = Integer.parseInt(yearString);
        }
        this.sendPdfReports(response, reportType, year, hostelId, branchId);
    }

    private void sendPdfReports(HttpServletResponse response, String reportType, Integer year, Long hostelId, Long branchId) throws Exception {
        response.setContentType("application/pdf");
        OutputStream out = response.getOutputStream();
        JasperPrint jasperPrint = summaryService.getReport(reportType, year, hostelId, branchId);
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }

//    private void downloadPdfReports(HttpServletResponse response, Long year, Long hostelId, Long branchId)
//            throws Exception {
//        response.setContentType("application/x-download");
//        response.setHeader("Content-Disposition", String.format("attachment; filename=\"report.pdf\""));
//        OutputStream out = response.getOutputStream();
//        JasperPrint jasperPrint =  summaryService.getReport(year, hostelId, branchId);
//        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
//    }

}
