package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Seat;
import com.idb.hmis.entity.Student;
import com.idb.hmis.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.StudentService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/student/")
public class StudentController {

    @Autowired
    public StudentService studentService;
    @Autowired
    public SeatService seatService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;
    private static final String[] IGNORABLES = {"branch", "nameBangla", "fatherName", "motherName", "parentOccupation", "presentAddress",
        "permanentAddress", "localGuardian", "relWithGuardian", "guardianAddress", "localGuardianPhone", "email", "sscGpa", "sscGroup",
        "sscPassYear", "school", "presentInstitute", "classTeacherName", "classTeacherPhone", "gender", "dateOfBirth", "nationality",
        "passportNo", "motherPhone", "isActive"};

    @GetMapping("inactive")
    public String listPreviousStudents(HttpServletRequest request, Model model, String message) {
        return this.listStudents(request, model, false, message);
    }

    @GetMapping("active")
    public String listActiveStudents(HttpServletRequest request, Model model, String message) {
        return this.listStudents(request, model, true, message);
    }

    private String listStudents(HttpServletRequest request, Model model, boolean isActive, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Student> students;
        if (searchParam == null || searchParam.isEmpty()) {
            students = this.studentService.getStudents(branchId, isActive, pageable);
        } else {
            students = this.studentService.searchStudents(branchId, searchParam, isActive, pageable);
        }
        Map<String, Object> data = this.dataService.getPageData(new Student(), students, IGNORABLES);
        String pageLink = (isActive) ? "/student/active" : "/student/inactive";
        this.moduleController.pageDataModel(model, request, "Student List", pageLink, data, message);
        model.addAttribute("deactivation", true);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Student student, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<Seat> emptySeats = this.studentService.getEmptySeats(branchId);
        this.moduleController.multiChoiceFormModel(model, "Student form", student, "/student/save", emptySeats, message);
        return "forms/student";
    }

    @GetMapping("update/{studentId}")
    public String editForm(@PathVariable("studentId") Long studentId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Student student = this.studentService.getVerifiedStudent(studentId, branchId);
        List<Seat> emptySeats = this.studentService.getEmptySeats(branchId);
        this.moduleController.multiChoiceFormModel(model, "Student form", student, "/student/save", emptySeats, null);
        return "forms/student";
    }

    @PostMapping("save")
    public String save(@Valid Student student, BindingResult result, @RequestParam("image") MultipartFile photo, HttpSession session, Model model) {
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long studentId = student.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            student.setBranch(new Branch(branchId));
            message = this.studentService.save(student, photo);
            if (message.endsWith("successfully")) {
                student = (studentId != null) ? null : new Student();
            } else if (message.startsWith("Invalid")) {
                return "redirect:/user/invalidate";
            }
        }
        return this.form(session, student, model, message);
    }

    @GetMapping("upload/{studentId}")
    public String uploadForm(@PathVariable("studentId") Long studentId, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Student Photo", "/student/upload", studentId, null);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam("id") Long studentId,
            @RequestParam("image") MultipartFile photo,
            HttpSession session,
            Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        String message = this.studentService.updatePhoto(photo, studentId, branchId);
        Long sessionStudentId = (Long) session.getAttribute("userId");
        if (!message.endsWith("failed") && studentId.equals(sessionStudentId)) {
            session.setAttribute("userPhoto", message);
             return "redirect:/studentpanel/Updated successfully";
        }
        this.moduleController.simpleModel(model, "Student update", message);
        return "pages/details";
    }

    @GetMapping("delete/{studentId}")
    public String delete(@PathVariable("studentId") Long studentId, HttpServletRequest request, Model model) {
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Integer result = this.studentService.delete(studentId, branchId);
        switch (result) {
            case 1:
                return this.listActiveStudents(request, model, "Student data deleted. Failed to delete photo file.");
            case 11:
                return this.listActiveStudents(request, model, "Student data deleted successfully");
            case 100:
                return this.listActiveStudents(request, model, "Failed to release seat. Student data could not be deleted. Please try again later");
            default:
                return "redirect:/user/invalidate";
        }
    }

    @GetMapping("deactivate/{studentId}")
    public String deactivate(@PathVariable("studentId") Long studentId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        String message = this.studentService.deactivate(studentId, branchId);
        return this.listActiveStudents(request, model, message);
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long studentId, HttpSession session, Model model) {
        Student student = this.getVerifiedStudent(session, studentId);
        Map<String, Object> data = this.dataService.getViewData(student);
        this.moduleController.displayModel(model, "student details", data, null);
        return "pages/details";
    }

    private Student getVerifiedStudent(HttpSession session, Long studentId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.studentService.getVerifiedStudent(studentId, branchId);
    }
}
