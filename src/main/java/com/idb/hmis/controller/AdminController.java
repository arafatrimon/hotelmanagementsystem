package com.idb.hmis.controller;

import com.idb.hmis.entity.Admin;
import com.idb.hmis.entity.User;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.AdminService;
import com.idb.hmis.service.UserService;
import com.idb.hmis.utils.DataService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ModuleController moduleController;

    @GetMapping("register")
    public String registerForm(Model model) {
        this.moduleController.formModel(model, "register", new Admin(), "/admin/register", null);
        return "index";
    }

    @PostMapping("register")
    public String register(@Valid Admin admin, BindingResult result, @RequestParam("image") MultipartFile photo, HttpServletRequest request, Model model) {
        String message = "Please insert all the information correctly";
        if (!result.hasErrors()) {
            User user = this.getUserFromRequest(request);
            message = this.adminService.register(admin, user, photo);
            switch (message) {
                case "0":
                    message = "Something went wrong. Failed to save photo";
                    break;
                case "1":
                    message = "Registration successful";
                    break;
            }
        }
        this.moduleController.formModel(model, "register", admin, "/admin/register", message);
        return "index";
    }

//    @GetMapping("login")
//    public String login(HttpSession session, Model model) {
//        String username = this.getUsername();
//        Admin admin = this.adminService.getByUsername(username);
//        if (admin != null) {
//            session.setAttribute("profile", admin);
//            return "redirect:/hostel/display";
//        }
//        this.moduleController.formModel(model, "admin-register", new Admin(), "/admin/register",
//                "Please complete your registration");
//        return "index";
//    }
    @GetMapping
    public String getAdminInfo(HttpSession session, Model model) {
        Admin admin = this.adminService.getByUsername(this.getUsername());
        if (admin != null) {
            Map<String, Object> data = this.dataService.getViewData(admin, "username", "isActive");
            this.moduleController.displayModel(model, "Profle view", data, null);
            return "pages/details";
        }
        this.moduleController.formModel(model, "admin-register", new Admin(), "/admin/register",
                "Please complete your registration to login");
        return "index";
    }

    @GetMapping("update/{id}")
    public String updateForm(HttpSession session, Model model) {
        String username = this.getUsername();
        Admin admin = this.adminService.getByUsername(username);
        this.moduleController.formModel(model, "admin update", admin, "/admin/update", null);
        return "forms/admin";
    }

    @PostMapping("update")
    public String update(@Valid Admin admin, BindingResult result, @RequestParam("image") MultipartFile photo, HttpSession session, Model model) {
        if (!result.hasErrors()) {
            String username = this.getUsername();
            admin.setUsername(username);
            admin = this.adminService.update(admin, photo);
            session.setAttribute("userName", admin.getName());
            session.setAttribute("userId", admin.getId());
            session.setAttribute("userPhoto", admin.getPhoto());
            return "redirect:details/0";
        }
        this.moduleController.formModel(model, "admin-register", admin, "/admin/update",
                "Please insert all the information correctly");
        return "forms/admin";
    }

    @GetMapping("upload/{id}")
    public String uploadForm(String message, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Admin Photo", "/admin/upload", 0, message);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(@RequestParam("image") MultipartFile photo, HttpSession session, Model model) {
        String username = this.getUsername();
        String photoTitle = this.adminService.updatePhoto(photo, username);
        session.setAttribute("userPhoto", photoTitle);
        return "redirect:";
    }

    @GetMapping("details/{id}")
    public String details(HttpSession session, Model model) {
        String username = this.getUsername();
        Admin admin = this.adminService.getByUsername(username);
        Map<String, Object> data = this.dataService.getViewData(admin, "username");
        this.moduleController.displayModel(model, "admin details", data, null);
        return "pages/details";
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();

    }

    private User getUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        return user;
    }

}
