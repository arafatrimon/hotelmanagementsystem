package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Attendance;
import com.idb.hmis.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.AttendanceService;
import com.idb.hmis.utils.DataService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/attendance/")
public class AttendanceController {

    @Autowired
    public AttendanceService attendanceService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping
    public String listByBranch(HttpServletRequest request, Model model, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Attendance> attendances = this.attendanceService.getAttendanceList(branchId, searchParam, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Attendance(), attendances, "branch");
        this.moduleController.pageDataModel(model, request, "Attendance List", "/attendance/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Attendance attendance, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<Student> students = this.attendanceService.getUnhandledStudents(branchId);
        this.moduleController.multiChoiceFormModel(model, "Attendance form", attendance, "/attendance/save", students, message);
        return "forms/attendance";
    }

    @GetMapping("update/{attendanceId}")
    public String editForm(@PathVariable("attendanceId") Long attendanceId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Attendance attendance = this.attendanceService.getVerifiedAttendance(attendanceId, branchId);
        List<Student> students = new ArrayList();
        students.add(attendance.getStudent());
        this.moduleController.multiChoiceFormModel(model, "Attendance form", attendance, "/attendance/save", students, null);
        return "forms/attendance";
    }

    @PostMapping("save")
    public String save(@Valid Attendance attendance, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long attendanceId = attendance.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            attendance.setBranch(new Branch(branchId));
            message = this.attendanceService.save(attendance);
            if (message == null) {
                return "redirect:/user/invalidate";
            }
            if (attendanceId != null) {
                return "redirect:";
            } else if (message.endsWith("successful")) {
                attendance = new Attendance();
            }
        }
        return this.form(session, attendance, model, message);
    }

    @GetMapping("delete/{attendanceId}")
    public String delete(@PathVariable("attendanceId") Long attendanceId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.attendanceService.delete(attendanceId, branchId);
        if (count > 0) {
           return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long attendanceId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Attendance attendance = this.attendanceService.getVerifiedAttendance(attendanceId, branchId);
        Map<String, Object> data = this.dataService.getViewData(attendance);
        this.moduleController.displayModel(model, "attendance details", data, null);
        return "pages/details";
    }


}
