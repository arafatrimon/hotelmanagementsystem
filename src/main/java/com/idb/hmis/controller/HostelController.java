package com.idb.hmis.controller;

import com.idb.hmis.entity.Admin;
import com.idb.hmis.entity.Hostel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.BranchService;
import com.idb.hmis.service.HostelService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/hostel/")
public class HostelController {

    @Autowired
    public BranchService branchService;
    @Autowired
    public HostelService hostelService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public DataService dataService;

    @GetMapping("display")
    public String displayHostels(HttpSession session, Model model) {
        Long adminId = (Long) session.getAttribute("userId");
        List<Hostel> hostels = this.hostelService.getByAdminId(adminId);
        this.moduleController.selectionModel(model, "home", hostels, "Please add new hostels or select a hostel for managerial operations.");
        return "dashboard";
    }

    @GetMapping("{hostelId}")
    public String selectHostel(@PathVariable("hostelId") Long hostelId, HttpSession session, Model model) {
        Hostel hostel = this.getVerifiedHostel(session, hostelId);
        if (hostel != null) {
            session.setAttribute("hostelId", hostelId);
            session.setAttribute("hostelName", hostel.getName());
            session.setAttribute("hostelLogo", hostel.getLogo());
            session.removeAttribute("branchId");
            session.removeAttribute("branchName");
            return "redirect:/branch/display";
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping
    public String listHostels(HttpSession session, Model model, String message) {
        Long adminId = (Long) session.getAttribute("userId");
        List<Hostel> hostels = this.hostelService.getByAdminId(adminId);
        Map<String, Object> data = this.dataService.getTableData(new Hostel(), hostels, "admin");
        this.moduleController.displayModel(model, "hostel list", data, message);
        return "pages/data-table";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model, String message) {
        this.moduleController.formModel(model, "hostel form", new Hostel(), "/hostel/save", message);
        return "forms/hostel";
    }

    @GetMapping("update/{hostelId}")
    public String editForm(@PathVariable("hostelId") Long hostelId, HttpSession session, Model model) {
        Hostel hostel = this.getVerifiedHostel(session, hostelId);
        this.moduleController.formModel(model, "hostel form", hostel, "/hostel/save", null);
        return "forms/hostel";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long hostelId, HttpSession session, Model model) {
        Long adminId = (Long) session.getAttribute("userId");
        Hostel hostel = this.hostelService.getVerifiedHostel(hostelId, adminId);
        Map<String, Object> data = this.dataService.getViewData(hostel);
        this.moduleController.displayModel(model, "hostel details", data, null);
        return "pages/details";
    }

    private Hostel getVerifiedHostel(HttpSession session, Long hostelId) {
        Long adminId = (Long) session.getAttribute("userId");
        return this.hostelService.getVerifiedHostel(hostelId, adminId);
    }

    @GetMapping("delete/{hostelId}")
    public String delete(@PathVariable("hostelId") Long hostelId, HttpSession session, Model model) {
        Long adminId = (Long) session.getAttribute("userId");
        Integer result = this.hostelService.delete(hostelId, adminId);
        Long sessionHostelId = (Long) session.getAttribute("hostel");
        if (hostelId.equals(sessionHostelId)) {
            session.removeAttribute("hostelId");
            session.removeAttribute("hostelName");
            session.removeAttribute("hostelLogo");
        }
        switch (result) {
            case 1:
                return this.listHostels(session, model, "Hostel data deleted. Failed to delete logo file.");
            case 11:
                return this.listHostels(session, model, "Hostel data deleted successfully");
            default:
                return "redirect:/user/invalidate";
        }
    }

    @PostMapping("save")
    public String save(@Valid Hostel hostel, BindingResult result, @RequestParam("image") MultipartFile photo, HttpSession session, Model model) throws Exception{
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long hostelId = hostel.getId();
            Long adminId = (Long) session.getAttribute("userId");
            hostel.setAdmin(new Admin(adminId));
            message = this.hostelService.save(hostel, photo);
            if (message.endsWith("successfully")) {
                if (hostelId != null) {
                    Hostel sessionHostel = (Hostel) session.getAttribute("hostel");
                    if (sessionHostel != null && sessionHostel.getId().equals(hostelId)) {
                        session.setAttribute("hostelId", hostelId);
                        session.setAttribute("hostelName", hostel.getName());
                        session.setAttribute("hostelLogo", hostel.getLogo());
                    }
                    return this.listHostels(session, model, message);
                }
                hostel = new Hostel();
            } else if (message.startsWith("Invalid")) {
                return "redirect:/user/invalidate";
            }
        }
        this.moduleController.formModel(model, "hostel form", hostel, "/hostel/save", message);
        return "forms/hostel";
    }

    @GetMapping("upload/{hostelId}")
    public String uploadForm(@PathVariable("hostelId") Long hostelId, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Hostel Photo", "/hostel/upload", hostelId, null);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam("id") Long hostelId,
            @RequestParam("image") MultipartFile photo,
            HttpSession session,
            Model model) {
        Long adminId = (Long) session.getAttribute("userId");
        String message = this.hostelService.updateLogo(photo, hostelId, adminId);
        Long sessionHostelId = (Long) session.getAttribute("hostelId");
        if (!message.endsWith("failed") && hostelId.equals(sessionHostelId)) {
            session.setAttribute("hostelLogo", message);
            message = "Update successful";
        }
        return this.listHostels(session, model, message);
    }
}
