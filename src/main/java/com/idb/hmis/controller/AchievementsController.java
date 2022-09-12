package com.idb.hmis.controller;

import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.Achievements;
import com.idb.hmis.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.AchievementsService;
import com.idb.hmis.service.BranchService;
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

@Controller
@RequestMapping("/achievements/")
public class AchievementsController {

    @Autowired
    public AchievementsService achievementsService;
    @Autowired
    public BranchService branchService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping
    public String listByHostel(HttpServletRequest request, Model model, String message){
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long hostelId = (Long) request.getSession().getAttribute("hostelId");
        Page<Achievements> achievements =  this.achievementsService.getAchievements(hostelId, searchParam, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Achievements(), achievements, "hostel");
        this.moduleController.pageDataModel(model, request, "Achievements List", "/achievements/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Achievements achievements, Model model, String message) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        List<Branch> branches = this.branchService.getByHostelId(hostelId);
        this.moduleController.multiChoiceFormModel(model, "Achievements form", achievements, "/achievements/save", branches, message);
        return "forms/achievements";
    }

    @GetMapping("update/{achievementsId}")
    public String editForm(@PathVariable("achievementsId") Long achievementsId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        Achievements achievements = this.achievementsService.getVerifiedAchievements(achievementsId, hostelId);
        List<Branch> branches = this.branchService.getByHostelId(hostelId);
        this.moduleController.multiChoiceFormModel(model, "Achievements form", achievements, "/achievements/save", branches, null);
        return "forms/achievements";
    }

    @PostMapping("save")
    public String save(@Valid Achievements achievements, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long hostelId = (Long) session.getAttribute("hostelId");
            achievements.setHostel(new Hostel(hostelId));
            this.achievementsService.save(achievements);
            message = "Achievements entry successful";
            achievements = new Achievements();
        }
        return this.form(session, achievements, model, message);
    }

    @GetMapping("delete/{achievementsId}")
    public String delete(@PathVariable("achievementsId") Long achievementsId, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long hostelId = (Long) session.getAttribute("hostelId");
        Integer count = this.achievementsService.delete(achievementsId, hostelId);
        if (count > 0) {
            return this.listByHostel(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long achievementsId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        Achievements achievements = this.achievementsService.getVerifiedAchievements(achievementsId, hostelId);
        Map<String, Object> data = this.dataService.getViewData(achievements);
        this.moduleController.displayModel(model, "achievements details", data, null);
        return "pages/details";
    }

}
