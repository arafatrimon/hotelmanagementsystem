package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Employee;
import com.idb.hmis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.EmployeeService;
import com.idb.hmis.service.UserService;
import com.idb.hmis.utils.DataService;
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
@RequestMapping("/employee/")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    UserService userService;
    @Autowired
    ModuleController moduleController;
    @Autowired
    PageController pageController;
    @Autowired
    DataService dataService;
    private final static String[] IGNORABLES = {"branch", "gender", "birthDate", "email", "presentAddress", "permanentAddress", "isActive"};

    @GetMapping("inactive")
    public String listInactiveEmployees(HttpServletRequest request, Model model, String message) {
        return this.listEmployees(request, model, false, message);
    }

    @GetMapping("active")
    public String listActiveEmployees(HttpServletRequest request, Model model, String message) {
        model.addAttribute("deactivation", true);
        return this.listEmployees(request, model, true, message);
    }

    private String listEmployees(HttpServletRequest request, Model model, boolean isActive, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Employee> employees;
        if (searchParam == null || searchParam.isEmpty()) {
            employees = this.employeeService.getEmployees(branchId, isActive, pageable);
        } else {
            employees = this.employeeService.searchEmployees(branchId, searchParam, isActive, pageable);
        }
        Map<String, Object> data = this.dataService.getPageData(new Employee(), employees, IGNORABLES);
        this.moduleController.dateFilterModel(model, request, "Employee List", "/employee/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("manager/inactive")
    public String listPreviousManagers(HttpServletRequest request, Model model, String message) {
        return this.listManagers(request, model, false, message);
    }

    @GetMapping("manager/active")
    public String listActiveManagers(HttpServletRequest request, Model model, String message) {
        model.addAttribute("accounts", true);
        return this.listManagers(request, model, true, message);
    }

    public String listManagers(HttpServletRequest request, Model model, boolean isActive, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Employee> employees;
        if (searchParam == null || searchParam.isEmpty()) {
            employees = this.employeeService.getManagers(branchId, isActive, pageable);
        } else {
            employees = this.employeeService.searchManagers(branchId, searchParam, isActive, pageable);
        }
        Map<String, Object> data = this.dataService.getPageData(new Employee(), employees, IGNORABLES);
        this.moduleController.dateFilterModel(model, request, "Employee List", "/employee/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping(value = {"form", "form/{identity}"})
    public String form(@PathVariable(required = false) String identity, HttpSession session, Model model) {
        String module = "employee form";
        if ("manager".equals(identity)) {
            module = "register manager";
        }
        this.moduleController.formModel(model, module, new Employee(), "/employee/save", null);
        return "forms/employee";
    }

    @GetMapping("update/{employeeId}")
    public String editForm(@PathVariable("employeeId") Long employeeId, HttpSession session, Model model) {
        String module = "update employee";
        Employee verifiedEmployee = this.getVerifiedEmployee(session, employeeId);
        if (verifiedEmployee == null) {
            return "redirect:/user/invalidate";
        } else if (verifiedEmployee.getUsername() != null) {
            module = "update manager";
        }
        this.moduleController.formModel(model, module, verifiedEmployee, "/employee/save", null);
        return "forms/employee";
    }

    @GetMapping("account/{username}")
    public String accountUpdateForm(@PathVariable("username") String username, HttpSession session, Model model) {
        User user = this.userService.findByUsername(username);
        this.moduleController.formModel(model, "update account info", user, "/user/update", null);
        return "forms/user";
    }

    @GetMapping("delete/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") Long employeeId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer result = this.employeeService.delete(employeeId, branchId);
        switch (result) {
            case 1:
                return this.listEmployees(request, model, true, "Employee data deleted. Failed to delete photo file.");
            case 11:
                return this.listEmployees(request, model, true, "Successfully deleted employee data.");
            default:
                return "redirect:/user/invalidate";
        }
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long employeeId, HttpSession session, Model model) {
        Employee employee = this.getVerifiedEmployee(session, employeeId);
        Map<String, Object> data = this.dataService.getViewData(employee);
        this.moduleController.displayModel(model, "employee details", data, null);
        return "pages/details";
    }

    @PostMapping("save")
    public String save(@Valid Employee employee, BindingResult result, @RequestParam("image") MultipartFile photo, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long employeeId = employee.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            if (employeeId == null && employee.getUsername() != null) {
                User user = this.getUserFromRequest(request);
                message = this.userService.register(user, "MANAGER");
            } else {
                message = "1";
            }
            if (message.equals("1")) {
                employee.setBranch(new Branch(branchId));
                message = this.employeeService.save(employee, photo);
                if (message.endsWith("successfully")) {
                    employee = (employeeId != null) ? null : new Employee();
                } else if (message.startsWith("Invalid")) {
                    return "redirect:/user/invalidate";
                }
            }
        }
        this.moduleController.formModel(model, "employee form", employee, "/employee/save", message);
        return "forms/employee";
    }

    @GetMapping("upload/{employeeId}")
    public String uploadForm(@PathVariable("employeeId") Long employeeId, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Employee Photo", "/employee/upload", employeeId, null);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam("id") Long employeeId,
            @RequestParam("image") MultipartFile photo,
            HttpSession session,
            Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        String message = this.employeeService.updatePhoto(photo, employeeId, branchId);
        Long sessionEmployeeId = (Long) session.getAttribute("userId");
        if (!message.endsWith("failed") && employeeId.equals(sessionEmployeeId)) {
            session.setAttribute("userPhoto", message);
             return "redirect:/empty-model/Updated successfully";
        }
        return "redirect:details/" + employeeId;
    }

    @GetMapping("deactivate/{employeeId}")
    public String deactivate(@PathVariable("employeeId") Long employeeId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        String message = this.employeeService.deactivate(employeeId, branchId);
        return this.listActiveEmployees(request, model, message);
    }

    private Employee getVerifiedEmployee(HttpSession session, Long employeeId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.employeeService.getVerifiedEmployee(employeeId, branchId);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        return user;
    }
}
