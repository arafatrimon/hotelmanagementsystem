package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.FoodMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.FoodMenuService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/foodmenu/")
public class FoodMenuController {

    @Autowired
    public FoodMenuService foodMenuService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public DataService dataService;
    
//    private final String[] DAYS = {"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
//    @GetMapping
//    public String listFoodMenus(HttpSession session, Model model) {
//        Long branchId = (Long) session.getAttribute("branchId");
//        List<FoodMenu> foodMenues = this.foodMenuService.getByBranchId(branchId);
//        Map<String, Object> data = this.dataService.getFieldBasedData(foodMenues, "time", "Days", DAYS);
//        this.moduleController.simpleModel(model, "Food Menu", null);
//        model.addAttribute("columnBasedData", data);
//        return "pages/data-table";
//    }

    @GetMapping
    public String listFoodMenus(HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<FoodMenu> foodMenues = this.foodMenuService.getByBranchId(branchId);
        Map<String, Object> data = this.dataService.getTableData(new FoodMenu(), foodMenues, "branch");
        this.moduleController.displayModel(model, "Food Menu", data, null);
        return "pages/data-table";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model, String message) {
        this.moduleController.formModel(model, "Menu entry form", new FoodMenu(), "/foodmenu/save", message);
        return "forms/foodmenu";
    }

    @GetMapping("update/{foodMenuId}")
    public String editForm(@PathVariable("foodMenuId") Long foodMenuId, HttpSession session, Model model) {
        FoodMenu verifiedFoodMenu = this.getVerifiedFoodMenu(foodMenuId, session);
        this.moduleController.formModel(model, "Menu update form", verifiedFoodMenu, "/foodmenu/save", null);
        return "forms/foodmenu";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long foodMenuId, HttpSession session, Model model) {
        FoodMenu foodMenu = this.getVerifiedFoodMenu(foodMenuId, session);
        Map<String, Object> data = this.dataService.getViewData(foodMenu);
        this.moduleController.displayModel(model, "foodMenu details", data, null);
        return "pages/details";
    }

    @GetMapping("delete/{foodMenuId}")
    public String delete(@PathVariable("foodMenuId") Long foodMenuId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        this.foodMenuService.delete(foodMenuId, branchId);
        return this.listFoodMenus(session, model);
    }

    @PostMapping("save")
    public String save(@Valid FoodMenu foodMenu, BindingResult result, HttpSession session, Model model) {
        String message = "Please provide all the information correctly";
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            foodMenu.setBranch(new Branch(branchId));
            message = this.foodMenuService.save(foodMenu);
            return this.form(session, model, message);
        }
        this.moduleController.formModel(model, "foodMenu form", foodMenu, "/foodmenu/save", message);
        return "forms/foodmenu";
    }

    private FoodMenu getVerifiedFoodMenu(Long foodMenuId, HttpSession session) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.foodMenuService.getVerifiedFoodMenu(foodMenuId, branchId);
    }

}
