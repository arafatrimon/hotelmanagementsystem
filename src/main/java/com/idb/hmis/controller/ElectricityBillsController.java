package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.ElectricityBills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.ElectricityBillsService;
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
@RequestMapping("/electricitybills/")
public class ElectricityBillsController {

    @Autowired
    public ElectricityBillsService electricityBillsService;
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
        Page<ElectricityBills> electricityBills;
        if (searchParam == null || searchParam.isEmpty()) {
            electricityBills = this.electricityBillsService.getByBranch(branchId, pageable);
        } else {
            electricityBills = this.electricityBillsService.searchInBranch(branchId, searchParam, pageable);
        }
        List types = this.electricityBillsService.getMeterNumbers(branchId);
        Map<String, Object> data = this.dataService.getPageData(new ElectricityBills(), electricityBills, "branch");
        this.moduleController.filterPageModel(model, request, "ElectricityBills List", "/electricitybills/", types, false, data, message);
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
        Page<ElectricityBills> electricityBills = this.electricityBillsService.filter(branchId, searchParam, filterText, 
                startDate, endDate, pageable);
        List types = this.electricityBillsService.getMeterNumbers(branchId);
        Map<String, Object> data = this.dataService.getPageData(new ElectricityBills(), electricityBills, "branch");
        this.moduleController.filterPageModel(model, request, "Filtered  ElectricityBills List", "/electricitybills/filter", 
                types, false, data, null);
        return "pages/pagination-with-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, ElectricityBills electricityBills, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List types = this.electricityBillsService.getMeterNumbers(branchId);
        this.moduleController.multiChoiceFormModel(model, "ElectricityBills form", electricityBills, "/electricitybills/save", 
                types, message);
        return "forms/electricityBills";
    }

    @GetMapping("update/{electricityBillsId}")
    public String editForm(@PathVariable("electricityBillsId") Long electricityBillsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        ElectricityBills electricityBills = this.getVerifiedElectricityBills(session, electricityBillsId);
        List types = this.electricityBillsService.getMeterNumbers(branchId);
        this.moduleController.multiChoiceFormModel(model, "ElectricityBills form", electricityBills, "/electricitybills/save", types, null);
        return "forms/electricityBills";
    }

    @PostMapping("save")
    public String save(@Valid ElectricityBills electricityBills, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            electricityBills.setBranch(new Branch(branchId));
            if (electricityBills.getId() != null) {
                return this.update(electricityBills, request, model);
            }
            this.electricityBillsService.save(electricityBills);
            message = "ElectricityBills entry successful";
            electricityBills = new ElectricityBills();
        }
        return this.form(session, electricityBills, model, message);
    }

    public String update(ElectricityBills electricityBills, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        ElectricityBills verifiedElectricityBills = this.getVerifiedElectricityBills(session, electricityBills.getId());
        if (verifiedElectricityBills != null) {
            this.electricityBillsService.save(electricityBills);
            return "redirect:";

        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("delete/{electricityBillsId}")
    public String delete(@PathVariable("electricityBillsId") Long electricityBillsId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.electricityBillsService.delete(electricityBillsId, branchId);
        if (count > 0) {
           return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long electricityBillsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        ElectricityBills electricityBills = this.electricityBillsService.getVerifiedElectricityBills(electricityBillsId, branchId);
        Map<String, Object> data = this.dataService.getViewData(electricityBills);
        this.moduleController.displayModel(model, "electricity Bills details", data, null);
        return "pages/details";
    }

    private ElectricityBills getVerifiedElectricityBills(HttpSession session, Long electricityBillsId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.electricityBillsService.getVerifiedElectricityBills(electricityBillsId, branchId);
    }

}
