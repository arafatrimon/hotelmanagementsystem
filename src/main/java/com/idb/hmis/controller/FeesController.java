package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Fees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.FeesService;
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
@RequestMapping("/fees/")
public class FeesController {

    @Autowired
    public FeesService feesService;
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
        Page<Fees> fees =  this.feesService.getFees(branchId, searchParam, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Fees(), fees, "branch");
        this.moduleController.pageDataModel(model, request, "Fees List", "/fees/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Fees fees, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List types = this.feesService.getFeesTypes(branchId);
        this.moduleController.multiChoiceFormModel(model, "Fees form", fees, "/fees/save", types, message);
        return "forms/fees";
    }

    @GetMapping("update/{feesId}")
    public String editForm(@PathVariable("feesId") Long feesId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Fees fees = this.getVerifiedFees(session, feesId);
        List types = this.feesService.getFeesTypes(branchId);
        this.moduleController.multiChoiceFormModel(model, "Fees form", fees, "/fees/save", types, null);
        return "forms/fees";
    }

    @PostMapping("save")
    public String save(@Valid Fees fees, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            fees.setBranch(new Branch(branchId));
            if (fees.getId() != null) {
                return this.update(fees, request, model);
            }
            this.feesService.save(fees);
            message = "Fees entry successful";
            fees = new Fees();
        }
        return this.form(session, fees, model, message);
    }

    public String update(Fees fees, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Fees verifiedFees = this.getVerifiedFees(session, fees.getId());
        if (verifiedFees != null) {
            this.feesService.save(fees);
            return "redirect:";
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("delete/{feesId}")
    public String delete(@PathVariable("feesId") Long feesId, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.feesService.delete(feesId, branchId);
        if (count > 0) {
            return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long feesId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Fees fees = this.feesService.getVerifiedFees(feesId, branchId);
        Map<String, Object> data = this.dataService.getViewData(fees);
        this.moduleController.displayModel(model, "fees details", data, null);
        return "pages/details";
    }

    private Fees getVerifiedFees(HttpSession session, Long feesId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.feesService.getVerifiedFees(feesId, branchId);
    }

}
