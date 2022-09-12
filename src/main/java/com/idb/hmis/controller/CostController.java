package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Cost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.CostService;
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
@RequestMapping("/cost/")
public class CostController {

    @Autowired
    public CostService costService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping
    public String listByBranch(HttpServletRequest request, Model model, String message){
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Cost> cost = null;
        if (searchParam == null || searchParam.isEmpty()) {
            cost = this.costService.getByBranch(branchId, pageable);
        } else {
            cost = this.costService.searchInBranch(branchId, searchParam, pageable);
        }
        List types = this.costService.getCostTypes(branchId);
        Map<String, Object> data = this.dataService.getPageData(new Cost(), cost, "branch");
        this.moduleController.filterPageModel(model, request, "Cost List", "/cost/", types, false, data, message);
        return "pages/pagination-with-filter";
    }

    @GetMapping("filter")
    public String listByFilter(HttpServletRequest request, Model model){
        String searchParam = request.getParameter("searchParam");
        String filterText = request.getParameter("filterText");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Pageable pageable = this.pageController.getPageable(request);
        Page<Cost> cost = this.costService.filter(branchId, searchParam, filterText, startDate, endDate, pageable);
        List types = this.costService.getCostTypes(branchId);
        Map<String, Object> data = this.dataService.getPageData(new Cost(), cost, "branch");
        this.moduleController.filterPageModel(model, request, "Filtered  Cost List", "/cost/filter", types, false, data, null);
        return "pages/pagination-with-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Cost cost, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List types = this.costService.getCostTypes(branchId);
        this.moduleController.multiChoiceFormModel(model, "Cost form", cost, "/cost/save", types, message);
        return "forms/cost";
    }

    @GetMapping("update/{costId}")
    public String editForm(@PathVariable("costId") Long costId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Cost cost = this.getVerifiedCost(session, costId);
        List types = this.costService.getCostTypes(branchId);
        this.moduleController.multiChoiceFormModel(model, "Cost form", cost, "/cost/save", types, null);
        return "forms/cost";
    }

    @PostMapping("save")
    public String save(@Valid Cost cost, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            cost.setBranch(new Branch(branchId));
            if (cost.getId() != null) {
                return this.update(cost, request, model);
            }
            this.costService.save(cost);
            message = "Cost entry successful";
            cost = new Cost();
        }
        return this.form(session, cost, model, message);
    }

    public String update(Cost cost, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Cost verifiedCost = this.getVerifiedCost(session, cost.getId());
        if (verifiedCost != null) {
            this.costService.save(cost);
            return "redirect:";
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("delete/{costId}")
    public String delete(@PathVariable("costId") Long costId, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.costService.delete(costId, branchId);
        if (count > 0) {
            return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long costId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Cost cost = this.costService.getVerifiedCost(costId, branchId);
        Map<String, Object> data = this.dataService.getViewData(cost);
        this.moduleController.displayModel(model, "cost details", data, null);
        return "pages/details";
    }

    private Cost getVerifiedCost(HttpSession session, Long costId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.costService.getVerifiedCost(costId, branchId);
    }

}
