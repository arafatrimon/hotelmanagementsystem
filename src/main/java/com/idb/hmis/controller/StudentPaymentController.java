package com.idb.hmis.controller;

import com.google.gson.Gson;
import com.idb.hmis.entity.Student;
import com.idb.hmis.entity.StudentPayment;
import com.idb.hmis.service.StudentPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.StudentService;
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
@RequestMapping("/studentpayment/")
public class StudentPaymentController {

    @Autowired
    public StudentPaymentService studentPaymentService;
    @Autowired
    public StudentService studentService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping("active")
    public String listActivePayments(HttpServletRequest request, Model model, String message) {
        return this.listPayments(request, model, true, message);
    }

    @GetMapping("inactive")
    public String listPreviousPayments(HttpServletRequest request, Model model, String message) {
        return this.listPayments(request, model, false, message);
    }

    private String listPayments(HttpServletRequest request, Model model, boolean isActive, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<StudentPayment> studentPayments;
        if (searchParam == null || searchParam.isEmpty()) {
            studentPayments = this.studentPaymentService.getByBranch(branchId, isActive, pageable);
        } else {
            studentPayments = this.studentPaymentService.searchInBranch(branchId, searchParam, isActive, pageable);
        }
        Map<String, Object> data = this.dataService.getPageData(new StudentPayment(), studentPayments, "branch");
        String pageLink = (isActive) ? "/studentpayment/active" : "/studentpayment/inactive";
        this.moduleController.pageDataModel(model, request, "Student's Payment List", pageLink, data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, StudentPayment studentPayment, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        String students = this.studentPaymentService.getUnpaidStudents(branchId);
        this.moduleController.ngMCFormModel(model, "StudentPayment form", studentPayment, "/studentpayment/save", students, message);
        return "forms/studentpayment";
    }

    @GetMapping("update/{studentPaymentId}")
    public String editForm(@PathVariable("studentPaymentId") Long studentPaymentId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        StudentPayment studentPayment = this.studentPaymentService.getVerifiedStudentPayment(studentPaymentId, branchId);
        String selectedStudent = null;
        if (studentPayment != null) {
            Student student = studentPayment.getStudent();
            Gson g = new Gson();
            selectedStudent = g.toJson(new Student(student.getId(), student.getStudentId(), student.getName(), student.getMonthlyFee()));
        }
        String students = Arrays.toString(new String[]{selectedStudent});
        this.moduleController.ngMCFormModel(model, "StudentPayment form", studentPayment, "/studentpayment/save", students, selectedStudent, null);
        return "forms/studentpayment";
    }

    @PostMapping("save")
    public String save(@Valid StudentPayment studentPayment, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long studentPaymentId = studentPayment.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            message = this.studentPaymentService.save(studentPayment, branchId);
            if (message == null) {
                return "redirect:/user/invalidate";
            }
            if (studentPaymentId != null) {
                return "redirect:active";
            } else if (message.endsWith("successful")) {
                studentPayment = new StudentPayment();
            }
        }
        return this.form(session, studentPayment, model, message);
    }

    @GetMapping("delete/{studentpaymentId}")
    public String delete(@PathVariable("studentpaymentId") Long studentPaymentId, HttpServletRequest request, Model model) {
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Integer count = this.studentPaymentService.delete(studentPaymentId, branchId);
        if (count > 0) {
            return this.listPayments(request, model, true, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long studentPaymentId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        StudentPayment studentPayment = this.studentPaymentService.getVerifiedStudentPayment(studentPaymentId, branchId);
        Map<String, Object> data = this.dataService.getViewData(studentPayment);
        this.moduleController.displayModel(model, "studentpayment details", data, null);
        return "pages/details";
    }

}
