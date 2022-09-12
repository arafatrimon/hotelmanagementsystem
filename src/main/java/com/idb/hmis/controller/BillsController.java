package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Bills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.BillsService;
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
@RequestMapping("/bills/")
public class BillsController {

    @Autowired
    public BillsService billsService;
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
        Page<Bills> bills = null;
        if (searchParam == null || searchParam.isEmpty()) {
            bills = this.billsService.getByBranch(branchId, pageable);
        } else {
            bills = this.billsService.searchInBranch(branchId, searchParam, pageable);
        }
        List types = this.billsService.getBillTypes(branchId);
        Map<String, Object> data = this.dataService.getPageData(new Bills(), bills, "branch");
        this.moduleController.filterPageModel(model, request, "Bills List", "/bills/", types, false, data, message);
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
        Page<Bills> bills = this.billsService.filter(branchId, searchParam, filterText, startDate, endDate, pageable);
        List types = this.billsService.getBillTypes(branchId);
        Map<String, Object> data = this.dataService.getPageData(new Bills(), bills, "branch");
        this.moduleController.filterPageModel(model, request, "Filtered  Bills List", "/bills/filter", types, false, data, null);
        return "pages/pagination-with-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Bills bills, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List types = this.billsService.getBillTypes(branchId);
        this.moduleController.multiChoiceFormModel(model, "Bills form", bills, "/bills/save", types, message);
        return "forms/bills";
    }

    @GetMapping("update/{billsId}")
    public String editForm(@PathVariable("billsId") Long billsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Bills bills = this.getVerifiedBills(session, billsId);
        List types = this.billsService.getBillTypes(branchId);
        this.moduleController.multiChoiceFormModel(model, "Bills form", bills, "/bills/save", types, null);
        return "forms/bills";
    }

    @PostMapping("save")
    public String save(@Valid Bills bills, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            bills.setBranch(new Branch(branchId));
            if (bills.getId() != null) {
                return this.update(bills, request, model);
            }
            this.billsService.save(bills);
            message = "Bills entry successful";
            bills = new Bills();
        }
        return this.form(session, bills, model, message);
    }

    public String update(Bills bills, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Bills verifiedBills = this.getVerifiedBills(session, bills.getId());
        if (verifiedBills != null) {
            this.billsService.save(bills);
            return "redirect:";
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("delete/{billsId}")
    public String delete(@PathVariable("billsId") Long billsId, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.billsService.delete(billsId, branchId);
        if (count > 0) {
            return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long billsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Bills bills = this.billsService.getVerifiedBills(billsId, branchId);
        Map<String, Object> data = this.dataService.getViewData(bills);
        this.moduleController.displayModel(model, "bills details", data, null);
        return "pages/details";
    }

    private Bills getVerifiedBills(HttpSession session, Long billsId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.billsService.getVerifiedBills(billsId, branchId);
    }

}
