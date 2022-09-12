package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.BazarItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.BazarItemsService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/bazaritems/")
public class BazarItemsController {

    @Autowired
    public BazarItemsService bazarItemsService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public DataService dataService;

    @GetMapping
    public String listBazarItemss(HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<BazarItems> bazarItemses = this.bazarItemsService.getByBranchId(branchId);
        Map<String, Object> data = this.dataService.getTableData(new BazarItems(), bazarItemses);
        this.moduleController.displayModel(model, "bazarItems list", data, null);
        return "pages/data-table";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model, String message) {
        this.moduleController.formModel(model, "bazar items form", new BazarItems(), "/bazaritems/save", message);
        return "forms/bazaritems";
    }

    @GetMapping("update/{bazarItemsId}")
    public String editForm(@PathVariable("bazarItemsId") Long bazarItemsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        BazarItems verifiedBazarItems = this.bazarItemsService.getVerifiedBazarItems(bazarItemsId, branchId);
        this.moduleController.formModel(model, "bazar items form", verifiedBazarItems, "/bazaritems/save", null);
        return "forms/bazaritems";
    }

    @GetMapping("delete/{bazarItemsId}")
    public String delete(@PathVariable("bazarItemsId") Long bazarItemsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        this.bazarItemsService.delete(bazarItemsId, branchId);
        return this.listBazarItemss(session, model);
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long bazarItemsId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        BazarItems bazarItems = this.bazarItemsService.getVerifiedBazarItems(bazarItemsId, branchId);
        Map<String, Object> data = this.dataService.getViewData(bazarItems);
        this.moduleController.displayModel(model, "bazarItems details", data, null);
        return "pages/details";
    }

    @PostMapping("save")
    public String save(@Valid BazarItems bazarItems, BindingResult result, HttpSession session, Model model) {
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            bazarItems.setBranch(new Branch(branchId));
            if (bazarItems.getId() != null) {
                return this.update(bazarItems, model, session);
            }
            String message;
            BazarItems existingBazarItems = this.bazarItemsService.alreadyExists(bazarItems);
            if (existingBazarItems != null) {
                message = "'" + bazarItems.getName() + "' already exists in selected branch";
                return this.form(session, model, message);
            }
            this.bazarItemsService.save(bazarItems);
            message = "Item added successfully";
            return this.form(session, model, message);
        }
        this.moduleController.formModel(model, "bazar items form", bazarItems, "/bazaritems/save", null);
        return "forms/bazaritems";
    }

    private String update(BazarItems bazarItems, Model model, HttpSession session) {
        Long bazarItemsid = bazarItems.getId();
        Long branchId = bazarItems.getBranch().getId();
        BazarItems verifiedBazarItems = this.bazarItemsService.getVerifiedBazarItems(bazarItemsid, branchId);
        if (verifiedBazarItems != null) {
            this.bazarItemsService.save(bazarItems);
            String message = "Update successful";
            return this.form(session, model, message);
        }
        return "redirect:/user/invalidate";
    }

}
