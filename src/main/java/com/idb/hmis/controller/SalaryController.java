package com.idb.hmis.controller;

import com.google.gson.Gson;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Employee;
import com.idb.hmis.entity.Salary;
import com.idb.hmis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.SalaryService;
import com.idb.hmis.utils.DataService;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/salary/")
public class SalaryController {

    @Autowired
    public SalaryService salaryService;
    @Autowired
    public EmployeeService employeeService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping("active")
    public String listActiveEmployeeSalaries(HttpServletRequest request, Model model, String message) {
        return this.lisSalaries(request, model, true, message);
    }

    @GetMapping("inactive")
    public String listInactiveEmployeeSalaries(HttpServletRequest request, Model model, String message) {
        return this.lisSalaries(request, model, false, message);
    }

    private String lisSalaries(HttpServletRequest request, Model model, Boolean isActive, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Salary> salary = this.salaryService.getSalaries(branchId, searchParam, isActive, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Salary(), salary);
        String pageLink = (isActive) ? "/salary/active" : "/salary/inactive";
        this.moduleController.dateFilterModel(model, request, "Salary List", pageLink, data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Salary salary, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        String employees = this.salaryService.getUnpaidEmployees(branchId);
        this.moduleController.ngMCFormModel(model, "Salary form", salary, "/salary/save", employees, message);
        return "forms/salary";
    }

    @GetMapping("update/{salaryId}")
    public String editForm(@PathVariable("salaryId") Long salaryId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Salary salary = this.salaryService.getVerifiedSalary(salaryId, branchId);
        String selectedEmployee = null;
        if (salary != null) {
            Employee employee = salary.getEmployee();
            Gson g = new Gson();
            selectedEmployee = g.toJson(new Employee(employee.getId(), employee.getName(), employee.getSalary()));
        }
        String employees = Arrays.toString(new String[]{selectedEmployee});
        this.moduleController.ngMCFormModel(model, "Salary form", salary, "/salary/save", employees, selectedEmployee, null);
        return "forms/salary";
    }

    @PostMapping("save")
    public String save(@Valid Salary salary, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long salaryId = salary.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            message = this.salaryService.save(salary, branchId);
            if (message == null) {
                return "redirect:/user/invalidate";
            }
            if (salaryId != null) {
                return "redirect:active";
            } else if (message.endsWith("successful")) {
                salary = new Salary();
            }
        }
        return this.form(session, salary, model, message);
    }

    @GetMapping("delete/{salaryId}")
    public String delete(@PathVariable("salaryId") Long salaryId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Salary salary = this.salaryService.getVerifiedSalary(salaryId, branchId);
        if (salary != null) {
            boolean isActive = salary.getEmployee().getIsActive();
            this.salaryService.delete(salaryId, branchId);
            return this.lisSalaries(request, model, isActive, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long salaryId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Salary salary = this.salaryService.getVerifiedSalary(salaryId, branchId);
        Map<String, Object> data = this.dataService.getViewData(salary);
        this.moduleController.displayModel(model, "salary details", data, null);
        return "pages/details";
    }

}
