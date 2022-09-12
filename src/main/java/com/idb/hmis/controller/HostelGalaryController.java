package com.idb.hmis.controller;

import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.HostelGalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.HostelGalaryService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/hostelgalary/")
public class HostelGalaryController {

    @Autowired
    public HostelGalaryService hostelGalaryService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping
    public String listByHostel(HttpServletRequest request, Model model, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long hostelId = (Long) request.getSession().getAttribute("hostelId");
        Page<HostelGalary> photos = this.hostelGalaryService.getHostelGalary(hostelId, searchParam, pageable);
        this.moduleController.galaryModel(model, request, "Hostel Galary", "/hostelgalary/", photos, message);
        return "pages/galary";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model, String message) {
        this.moduleController.formModel(model, "Upload Hostel Photo", null, "/hostelgalary/save", message);
        return "pages/multiple-uploader";
    }

    @GetMapping("update/{hostelGalaryId}")
    public String editForm(@PathVariable("hostelGalaryId") Long hostelGalaryId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        HostelGalary hostelGalary = this.hostelGalaryService.getVerifiedHostelGalary(hostelGalaryId, hostelId);
        this.moduleController.formModel(model, "Update Hostel Photo", hostelGalary, "/hostelgalary/update", null);
        return "pages/uploader";
    }

    @PostMapping("save")
    public String save(@RequestParam("photos") List<MultipartFile> files, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        String message = this.hostelGalaryService.save(files, hostelId);
        return this.form(session, model, message);
    }

    @PostMapping("update")
    public String update(@RequestBody HostelGalary galary, @RequestParam("image") MultipartFile file, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        galary.setHostel(new Hostel(hostelId));
        String message = this.hostelGalaryService.update(galary, file);
        return this.form(session, model, message);
    }

    @GetMapping("delete/{hostelGalaryId}")
    public String delete(@PathVariable("hostelGalaryId") Long hostelGalaryId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long hostelId = (Long) session.getAttribute("hostelId");
        Integer count = this.hostelGalaryService.delete(hostelGalaryId, hostelId);
        if (count > 0) {
            return this.listByHostel(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long hostelGalaryId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        HostelGalary hostelGalary = this.hostelGalaryService.getVerifiedHostelGalary(hostelGalaryId, hostelId);
        Map<String, Object> data = this.dataService.getViewData(hostelGalary);
        this.moduleController.displayModel(model, "hostelGalary details", data, null);
        return "pages/details";
    }

}
