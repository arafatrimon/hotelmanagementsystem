package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.SummaryDao;
import com.idb.hmis.service.SummaryService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    SummaryDao summaryDao;
    @Autowired
    private ResourceLoader resourceLoader;
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    public Map<String, Object> getBranchSummary(Long branchId) {
        Map<String, Object> summary = new LinkedHashMap<>();
        String startDate, endDate;
        int year, month;
        Calendar calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH) + 1;
        startDate = year + "-1-1";
        endDate = year + "-" + month + "-31";
        getTodaysData(summary, branchId);
        getMonthlyIncomeCostProfitData(summary, startDate, endDate, month, branchId);
        startDate = year + "-" + month + "-1";
        getCategorizedIncomeAndCostData(summary, startDate, endDate, branchId);
        getMonthNames(summary, month);
        return summary;
    }

    @Override
    public JasperPrint getReport(String reportType, Integer year, Long hostelId, Long branchId) throws Exception {
        Map<String, Object> params = new HashMap();
        String startDate, endDate, path, pathString;
        startDate = year + "-1-1";
        endDate = year + "-12-31";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hmis", "root", "")) {
            params.put("start_date", startDate);
            params.put("end_date", endDate);
            if (hostelId != null) {
                params.put("hostel", hostelId);
                pathString = (reportType.equals("is")) ? 
                        "classpath:/static/reports/hostel_report.jasper" : 
                        "classpath:/static/reports/hostel_balance_sheet.jasper";
            } else {
                params.put("branch", branchId);
                pathString = (reportType.equals("is")) ? 
                        "classpath:/static/reports/branch_report.jasper" : 
                        "classpath:/static/reports/branch_balance_sheet.jasper";
            }
            path = resourceLoader.getResource(pathString).getURI().getPath();
            return JasperFillManager.fillReport(path, params, connection);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void getTodaysData(Map<String, Object> summary, Long branchId) {
        Integer totalStudents, todayMeals, todayBazar, emptySeats;
        totalStudents = this.summaryDao.countCurrentStudents(branchId);
        todayMeals = this.summaryDao.countTodaysMeal(branchId);
        todayBazar = this.summaryDao.todaysBazar(branchId);
        emptySeats = this.summaryDao.countEmptySeats(branchId);
        summary.put("totalStudents", totalStudents);
        summary.put("todayMeals", (todayMeals != null) ? todayMeals : 0);
        summary.put("todayBazar", (todayBazar != null) ? todayBazar : 0);
        summary.put("emptySeats", emptySeats);
    }

    private void getCategorizedIncomeAndCostData(Map<String, Object> summary, String startDate, String endDate, Long branchId) {
        summary.put("categorizedIncomeValues", "[" + this.summaryDao.getCategorizedIncomeValues(startDate, endDate, branchId) + "]");
        summary.put("categorizedCostValues", "[" + this.summaryDao.countCategorizedCostValues(startDate, endDate, branchId) + "]");
    }

    private void getMonthlyIncomeCostProfitData(Map<String, Object> summary, String startDate, String endDate, int month, Long branchId) {
        String incomes = "[", costs = "[", profits = "[";
        Object[][] monthlyIncomes = this.summaryDao.getMonthlyIncomeValues(startDate, endDate, branchId);
        Object[][] monthlyCosts = this.summaryDao.getMonthlyCostValues(startDate, endDate, branchId);
        int[] incomeValues = new int[month];
        int[] costValues = new int[month];
        int index = 0, incLength = monthlyIncomes.length, costLength = monthlyCosts.length;
        for (int i = 0; i < month; i++) {
            if (i < incLength) {
                index = indexOf(monthlyIncomes[i][0], months);
                incomeValues[index] = ((Double) monthlyIncomes[i][1]).intValue();
            }
            if (i < costLength) {
                index = indexOf(monthlyCosts[i][0], months);
                costValues[index] = ((Double) monthlyCosts[i][1]).intValue();
            }
        }
        for (int i = 0; i < month - 1; i++) {
            incomes += incomeValues[i] + ",";
            costs += costValues[i] + ",";
            profits += (incomeValues[i] - costValues[i]) + ",";
        }
        incomes += incomeValues[month - 1] + "]";
        costs += costValues[month - 1] + "]";
        profits += (incomeValues[month - 1] - costValues[month - 1]) + "]";
        summary.put("monthlyIncomes", incomes);
        summary.put("monthlyCosts", costs);
        summary.put("profitValues", profits);
    }

    private void getMonthNames(Map<String, Object> summary, int currentMonth) {
        String monthNames = "['";
        for (int i = 0; i < currentMonth - 1; i++) {
            monthNames += months[i] + "','";
        }
        monthNames += months[currentMonth - 1] + "']";
        summary.put("months", monthNames);
    }

    private int indexOf(Object value, Object[] list) {
        int length = list.length;
        for (int i = 0; i < length; i++) {
            if (list[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

}
