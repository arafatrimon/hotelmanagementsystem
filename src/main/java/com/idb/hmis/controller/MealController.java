package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Meal;
import com.idb.hmis.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.MealService;
import com.idb.hmis.utils.DataService;
import java.util.ArrayList;
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
@RequestMapping("/meal/")
public class MealController {

    @Autowired
    public MealService mealService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public PageController pageController;
    @Autowired
    public DataService dataService;

    @GetMapping
    public String listByBranch(HttpServletRequest request, Model model) {
        return this.listMeals(request, model, false, null);
    }

    @GetMapping("today")
    public String listTodaysMeals(HttpServletRequest request, Model model) {
        return this.listMeals(request, model, true, null);
    }

    public String listMeals(HttpServletRequest request, Model model, boolean isToday, String message) {
        String searchParam = request.getParameter("searchParam");
        Pageable pageable = this.pageController.getPageable(request);
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Page<Meal> meals = this.mealService.getMealList(branchId, searchParam, isToday, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Meal(), meals, "branch");
        this.moduleController.pageDataModel(model, request, "Meal List", "/meal/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Meal meal, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<Student> students = this.mealService.getUnhandledStudents(branchId);
        this.moduleController.multiChoiceFormModel(model, "Meal form", meal, "/meal/save", students, message);
        return "forms/meal";
    }

    @GetMapping("update/{mealId}")
    public String editForm(@PathVariable("mealId") Long mealId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Meal meal = this.mealService.getVerifiedMeal(mealId, branchId);
        List<Student> students = new ArrayList();
        students.add(meal.getStudent());
        this.moduleController.multiChoiceFormModel(model, "Meal form", meal, "/meal/save", students, null);
        return "forms/meal";
    }

    @PostMapping("save")
    public String save(@Valid Meal meal, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            meal.setBranch(new Branch(branchId));
            message = this.mealService.save(meal);
            meal = new Meal();
        }
        return this.form(session, meal, model, message);
    }

    @GetMapping("delete/{mealId}")
    public String delete(@PathVariable("mealId") Long mealId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Boolean isToday = this.mealService.delete(mealId, branchId);
        if (isToday != null) {
            return this.listMeals(request, model, isToday, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long mealId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Meal meal = this.mealService.getVerifiedMeal(mealId, branchId);
        Map<String, Object> data = this.dataService.getViewData(meal);
        this.moduleController.displayModel(model, "meal details", data, null);
        return "pages/details";
    }

}
