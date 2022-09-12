package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.BranchGalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.BranchGalaryService;
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
@RequestMapping("/branchgalary/")
public class BranchGalaryController {

    @Autowired
    public BranchGalaryService branchGalaryService;
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
        Page<BranchGalary> photos = this.branchGalaryService.getBranchGalary(branchId, searchParam, pageable);
        this.moduleController.galaryModel(model, request, "Branch Galary", "/branchgalary/", photos, message);
        return "pages/galary";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model, String message) {
        this.moduleController.formModel(model, "Upload Branch Photo", null, "/branchgalary/save", message);
        return "pages/multiple-uploader";
    }

    @GetMapping("update/{branchGalaryId}")
    public String editForm(@PathVariable("branchGalaryId") Long branchGalaryId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        BranchGalary branchGalary = this.branchGalaryService.getVerifiedBranchGalary(branchGalaryId, branchId);
        this.moduleController.formModel(model, "Update Branch Photo", branchGalary, "/branchgalary/update", null);
        return "pages/uploader";
    }

    @PostMapping("save")
    public String save(@RequestParam("photos") List<MultipartFile> files, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        String message = this.branchGalaryService.save(files, branchId);
        return this.form(session, model, message);
    }

    @PostMapping("update")
    public String update(@RequestBody BranchGalary galary, @RequestParam("image") MultipartFile file, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        galary.setBranch(new Branch(branchId));
        String message = this.branchGalaryService.update(galary, file);
        return this.form(session, model, message);
    }

    @GetMapping("delete/{branchGalaryId}")
    public String delete(@PathVariable("branchGalaryId") Long branchGalaryId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.branchGalaryService.delete(branchGalaryId, branchId);
        if (count > 0) {
            return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long branchGalaryId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        BranchGalary branchGalary = this.branchGalaryService.getVerifiedBranchGalary(branchGalaryId, branchId);
        Map<String, Object> data = this.dataService.getViewData(branchGalary);
        this.moduleController.displayModel(model, "branchGalary details", data, null);
        return "pages/details";
    }

}
