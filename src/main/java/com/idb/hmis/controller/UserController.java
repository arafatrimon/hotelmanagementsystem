package com.idb.hmis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.entity.User;
import com.idb.hmis.service.AdminService;
import com.idb.hmis.service.EmployeeService;
import com.idb.hmis.service.StudentService;
import com.idb.hmis.service.UserService;
import com.idb.hmis.utils.DataService;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ModuleController moduleController;

    @GetMapping("update")
    public String updateForm(HttpSession session, Model model) {
        String username = this.getUsername();
        User currentUser = this.userService.findByUsername(username);
        if (currentUser == null) {
            return "redirect:invalidate";
        }
        this.moduleController.formModel(model, "account update", currentUser, "/user/update", null);
        return "forms/user";
    }

    @PostMapping("update")
    public String updateUser(@RequestParam("oldPassword") String oldPassword, User user, Model model) {
        String message = null;
        String output = this.userService.update(user, oldPassword);
        switch (output) {
            case "00":
                message = "Email or phone already in use. Please enter a new email or leave the field empty";
                break;
            case "01":
                message = "Invalid password";
                break;
            case "11":
                this.moduleController.simpleModel(model, "Account update info", "Account updated successfully");
                return "pages/details";
        }
        this.moduleController.formModel(model, "account update", user, "/user/update", message);
        return "forms/user";
    }

    @GetMapping("invalidate")
    public String invalidate(Model model, HttpSession session) {
        String username = this.getUsername();
        this.userService.deactivate(username);
        return "redirect:/logout";
    }

    @GetMapping("profile")
    public String profile(Model model, HttpSession session) {
        String username = this.getUsername();
        String role = (String) session.getAttribute("userRole");
        Object profile = null;
        if (role.equals("ROLE_ADMIN")) {
            profile = this.adminService.getByUsername(username);
        } else if (role.equals("ROLE_MANAGER")) {
            profile = this.employeeService.getByUsername(username);
        } else if (role.equals("ROLE_STUDENT")) {
            profile = this.studentService.getByUsername(username);
        }
        Map<String, Object> data = this.dataService.getViewData(profile, "username", "isActive");
        this.moduleController.displayModel(model, "Profle view", data, null);
        return "pages/details";
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
