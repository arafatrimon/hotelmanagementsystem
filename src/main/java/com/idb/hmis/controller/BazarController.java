package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Bazar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.BazarService;
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
@RequestMapping("/bazar/")
public class BazarController {

    @Autowired
    public BazarService bazarService;
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
        Page<Bazar> bazar = null;
        if (searchParam == null || searchParam.isEmpty()) {
            bazar = this.bazarService.getByBranch(branchId, pageable);
        } else {
            bazar = this.bazarService.searchInBranch(branchId, searchParam, pageable);
        }
        List types = this.bazarService.getBazarItemNames(branchId);
        Map<String, Object> data = this.dataService.getPageData(new Bazar(), bazar, "branch");
        this.moduleController.filterPageModel(model, request, "Bazar List", "/bazar/", types, true, data, message);
        return "pages/pagination-with-filter";
    }

    @GetMapping("filter")
    public String listByFilter(HttpServletRequest request, Model model) {
        String searchParam = request.getParameter("searchParam");
        String filterText = request.getParameter("filterText");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Pageable pageable = this.pageController.getPageable(request);
        Page<Bazar> bazar = this.bazarService.filter(branchId, searchParam, filterText, startDate, endDate, pageable);
        List types = this.bazarService.getBazarItemNames(branchId);
        Map<String, Object> data = this.dataService.getPageData(new Bazar(), bazar, "branch");
        this.moduleController.filterPageModel(model, request, "Filtered  Bazar List", "/bazar/filter", types, true, data, null);
        return "pages/pagination-with-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Bazar bazar, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List types = this.bazarService.getBazarItemNames(branchId);
        this.moduleController.multiChoiceFormModel(model, "Bazar form", bazar, "/bazar/save", types, message);
        return "forms/bazar";
    }

    @GetMapping("update/{bazarId}")
    public String editForm(@PathVariable("bazarId") Long bazarId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Bazar bazar = this.getVerifiedBazar(session, bazarId);
        List types = this.bazarService.getBazarItemNames(branchId);
        this.moduleController.multiChoiceFormModel(model, "Bazar form", bazar, "/bazar/save", types, null);
        return "forms/bazar";
    }

    @PostMapping("save")
    public String save(@Valid Bazar bazar, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            bazar.setBranch(new Branch(branchId));
            if (bazar.getId() != null) {
                return this.update(bazar, request, model);
            }
            this.bazarService.save(bazar);
            message = "Bazar entry successful";
            bazar = new Bazar();
        }
        return this.form(session, bazar, model, message);
    }

    public String update(Bazar bazar, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Bazar verifiedBazar = this.getVerifiedBazar(session, bazar.getId());
        if (verifiedBazar != null) {
            this.bazarService.save(bazar);
            return "redirect:";

        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("delete/{bazarId}")
    public String delete(@PathVariable("bazarId") Long bazarId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.bazarService.delete(bazarId, branchId);
        if (count > 0) {
           return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long bazarId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Bazar bazar = this.bazarService.getVerifiedBazar(bazarId, branchId);
        Map<String, Object> data = this.dataService.getViewData(bazar);
        this.moduleController.displayModel(model, "bazar details", data, null);
        return "pages/details";
    }

    private Bazar getVerifiedBazar(HttpSession session, Long bazarId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.bazarService.getVerifiedBazar(bazarId, branchId);
    }

}
