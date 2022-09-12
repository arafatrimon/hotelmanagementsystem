package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.DepositService;
import com.idb.hmis.utils.DataService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/deposit/")
public class DepositController {

    @Autowired
    public DepositService depositService;
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
        Page<Deposit> deposit = null;
        if (searchParam == null || searchParam.isEmpty()) {
            deposit = this.depositService.getByBranch(branchId, pageable);
        } else {
            deposit = this.depositService.searchInBranch(branchId, searchParam, pageable);
        }
        Map<String, Object> data = this.dataService.getPageData(new Deposit(), deposit, "branch", "username");
        this.moduleController.dateFilterModel(model, request, "Deposit List", "/deposit/", data, message);
        return "pages/pagination-with-date";
    }

    @GetMapping("filter")
    public String listByFilter(HttpServletRequest request, Model model, String message) {
        String searchParam = request.getParameter("searchParam");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Pageable pageable = this.pageController.getPageable(request);
        Page<Deposit> deposit = this.depositService.filter(branchId, searchParam, startDate, endDate, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Deposit(), deposit, "branch", "username");
        this.moduleController.dateFilterModel(model, request, "Deposit List", "/deposit/", data, message);
        return "pages/pagination-with-date";
    }

    @GetMapping("form")
    public String form(HttpSession session, Deposit deposit, Model model, String message) {
        this.moduleController.formModel(model, "Deposit form", deposit, "/deposit/save", message);
        return "forms/deposit";
    }

    @GetMapping("update/{depositId}")
    public String editForm(@PathVariable("depositId") Long depositId, HttpSession session, Model model) {
        Deposit deposit = this.getVerifiedDeposit(session, depositId);
        this.moduleController.formModel(model, "Deposit form", deposit, "/deposit/save", null);
        return "forms/deposit";
    }

    @PostMapping("save")
    public String save(@Valid Deposit deposit, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = null;
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            deposit.setBranch(new Branch(branchId));
            message = this.depositService.save(deposit);
            if (message.endsWith("successfully")) {
                deposit = new Deposit();
            } else if(message.startsWith("Invalid")){
                return "redirect:/user/invalidate";
            }
        }
        return this.form(session, deposit, model, message);
    }

    @GetMapping("delete/{depositId}")
    public String delete(@PathVariable("depositId") Long depositId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.depositService.delete(depositId, branchId);
        if (count > 0) {
            return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long depositId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Deposit deposit = this.depositService.getVerifiedDeposit(depositId, branchId);
        Map<String, Object> data = this.dataService.getViewData(deposit);
        this.moduleController.displayModel(model, "deposit details", data, null);
        return "pages/details";
    }

    private Deposit getVerifiedDeposit(HttpSession session, Long depositId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.depositService.getVerifiedDeposit(depositId, branchId);
    }

}
