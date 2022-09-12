package com.idb.hmis.controller;

import com.idb.hmis.dao.RoleDao;
import com.idb.hmis.entity.Admin;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Employee;
import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.Role;
import com.idb.hmis.entity.Student;
import com.idb.hmis.entity.User;
import com.idb.hmis.service.AdminService;
import com.idb.hmis.service.EmployeeService;
import com.idb.hmis.service.StudentService;
import com.idb.hmis.service.UserService;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    UserService userservice;
    @Autowired
    RoleDao roleDao;
    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    public ModuleController moduleController;

    @GetMapping("/login")
    public String loginPage(String message, String error, String logout, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            message = "You have been logged out successfully";
        }
        List<Role> roles = this.roleDao.findAll();
        this.moduleController.multiChoiceFormModel(model, "login", new User(), "/login", roles, message);
        return "index";
    }

    @PostMapping(value = {"/loginsuccess", "/loginsuccess/{message}"})
    public String loginsuccess(@PathVariable(value = "message", required = false) String message, Model model, HttpSession session) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        String page = "";
        String roleAdmin = "ROLE_ADMIN";
        String roleManager = "ROLE_MANAGER";
        String roleStudent = "ROLE_STUDENT";
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(roleAdmin)) {
                Admin admin = this.adminService.getByUsername(username);
                if (admin != null) {
                    session.setAttribute("userName", admin.getName());
                    session.setAttribute("userRole", authority.getAuthority());
                    session.setAttribute("userId", admin.getId());
                    session.setAttribute("userPhoto", admin.getPhoto());
                    page = "redirect:/hostel/display";
                } else {
                    this.moduleController.formModel(model, "admin-register", new Admin(), "/admin/register", "Please complete your registration");
                    page = "index";
                }
            } else if (authority.getAuthority().equals(roleManager)) {
                Employee manager = this.employeeService.getByUsername(username);
                if (manager == null) {
                    page = "redirect:/user/invalidate";
                } else if (manager.getIsActive()) {
                    Branch branch = manager.getBranch();
                    Hostel hostel = branch.getHostel();
                    session.setAttribute("userName", manager.getName());
                    session.setAttribute("userRole", authority.getAuthority());
                    session.setAttribute("userId", manager.getId());
                    session.setAttribute("userPhoto", manager.getPhoto());
                    session.setAttribute("branchId", branch.getId());
                    session.setAttribute("branchName", branch.getName());
                    session.setAttribute("hostelId", hostel.getId());
                    session.setAttribute("hostelName", hostel.getName());
                    session.setAttribute("hostelLogo", hostel.getLogo());
                    page = "redirect:/branch/" + branch.getId();
                }
            } else if (authority.getAuthority().equals(roleStudent)) {
                Student student = this.studentService.getByUsername(username);
                Branch branch = student.getBranch();
                Hostel hostel = branch.getHostel();
                session.setAttribute("userName", student.getName());
                session.setAttribute("userRole", authority.getAuthority());
                session.setAttribute("userId", student.getId());
                session.setAttribute("branchId", branch.getId());
                session.setAttribute("branchName", branch.getName());
                session.setAttribute("hostelId", hostel.getId());
                session.setAttribute("hostelName", hostel.getName());
                session.setAttribute("userPhoto", student.getPhoto());
                session.setAttribute("hostelLogo", hostel.getLogo());
                page = "redirect:/studentpanel";
            } else {
                page = "redirect:/";
            }
        }
        return page;
    }

}
