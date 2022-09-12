package com.idb.hmis.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class ModuleController {

    public void simpleModel(Model model, String module, String message) {
        model.addAttribute("module", module);
        model.addAttribute("message", message);
    }

    public void displayModel(Model model, String module, Map<String, Object> data, String message) {
        this.simpleModel(model, module, message);
        model.addAttribute("data", data);
    }

    public void uploadModel(Model model, String module, String formAction, Number id, String message) {
        this.simpleModel(model, module, message);
        model.addAttribute("id", id);
        model.addAttribute("action", formAction);
    }
//
//    public void uploadModel(Model model, String module, String formAction, String type, Number id, String message) {
//        this.simpleModel(model, module, message);
//        model.addAttribute("id", id);
//        model.addAttribute("type", type);
//        model.addAttribute("action", formAction);
//    }

    public void selectionModel(Model model, String module, List entities, String message) {
        this.simpleModel(model, module, message);
        model.addAttribute("entities", entities);
    }

    public void formModel(Model model, String module, Object formObject, String formAction, String message) {
        this.simpleModel(model, module, message);
        model.addAttribute("entity", formObject);
        model.addAttribute("action", formAction);
    }

    public void multiChoiceFormModel(Model model, String module, Object formObject, String formAction, List multiChoiceList, String message) {
        this.formModel(model, module, formObject, formAction, message);
        model.addAttribute("mclist", multiChoiceList);
    }

    public void ngMCFormModel(Model model, String module, Object formObject, String formAction, String multiChoiceList, String message) {
        this.formModel(model, module, formObject, formAction, message);
        model.addAttribute("mclist", multiChoiceList);
    }

    public void ngMCFormModel(Model model, String module, Object formObject, String formAction, String multiChoiceList, String selectedItem, String message) {
        this.formModel(model, module, formObject, formAction, message);
        model.addAttribute("mclist", multiChoiceList);
        model.addAttribute("mcitem", selectedItem);
    }

    public void formAndTableModel(Model model, String module, Object formObject, String formAction,
            List multiChoiceList, Map<String, Object> data, String message) {
        this.multiChoiceFormModel(model, module, formObject, formAction, multiChoiceList, message);
        model.addAttribute("data", data);
    }

    public void pageModel(Model model, HttpServletRequest request, String module, String pageLink, String message) {
        String searchParam = request.getParameter("searchParam");
        String sortCriteria = request.getParameter("sortCriteria");
        String sortDirection = request.getParameter("sortDirection");
        int pageSize = 9;
        String size = request.getParameter("size");
        if (size != null && !size.isEmpty()) {
            pageSize = Integer.parseInt(size);
        }
        this.simpleModel(model, module, message);
        model.addAttribute("size", pageSize);
        model.addAttribute("pageLink", pageLink);
        model.addAttribute("searchParam", searchParam);
        model.addAttribute("sortCriteria", sortCriteria);
        model.addAttribute("sortDirection", sortDirection);
    }

    public void pageDataModel(Model model, HttpServletRequest request, String module, String pageLink, Map<String, Object> data, String message) {
        pageModel(model, request, module, pageLink, message);
        model.addAttribute("data", data);
    }

    public void galaryModel(Model model, HttpServletRequest request, String module, String pageLink, Page page, String message) {
        pageModel(model, request, module, pageLink, message);
        model.addAttribute("photos", page.getContent());
        model.addAttribute("pageIndex", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
    }

    public void dateFilterModel(Model model, HttpServletRequest request, String module, String pageLink,
            Map<String, Object> data, String message) {
        this.pageDataModel(model, request, module, pageLink, data, message);
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
    }

    public void filterPageModel(Model model, HttpServletRequest request, String module, String pageLink, List multiChoiceList,
            boolean mcIdExists, Map<String, Object> data, String message) {
        this.dateFilterModel(model, request, module, pageLink, data, message);
        String filterText = request.getParameter("filterText");
        model.addAttribute("filterText", filterText);
        model.addAttribute("mclist", multiChoiceList);
        model.addAttribute("mcIdExists", mcIdExists); // For checking if the items in the multiChoiceList contain id field or not
    }
}
