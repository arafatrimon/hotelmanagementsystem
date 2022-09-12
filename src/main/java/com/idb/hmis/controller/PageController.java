package com.idb.hmis.controller;

import com.idb.hmis.service.SummaryService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("/")
public class PageController {

    @Autowired
    public ModuleController moduleController;
    @Autowired
    public SummaryService summaryService;

    @GetMapping("home")
    public String home() {
        return "index";
    }

    @GetMapping("dashboard")
    public String dashboard(HttpSession session, Model model) {
        this.moduleController.simpleModel(model, "home", "Create new hostel or select existing one for managerial operation");
        return "redirect:/hostel/display";
    }

    @GetMapping(value = "access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("module", "Access denied");
        return "pages/access-denied";
    }

    @GetMapping(value = "empty-model/{message}")
    public String emptyModel(@PathVariable() String message, Model model) {
        model.addAttribute("module", message);
        return "pages/access-denied";
    }

    public Pageable getPageable(HttpServletRequest request) {
        int page = 0;
        int size = 10;
        String rpage = request.getParameter("page");
        String rsize = request.getParameter("size");
        String sortCriteria = request.getParameter("sortCriteria");
        String sortDirection = request.getParameter("sortDirection");
        if (rpage != null && !rpage.isEmpty()) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        if (rsize != null && !rsize.isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        if (sortCriteria == null || sortCriteria.isEmpty()) {
            sortCriteria = "id";
        }
        if (sortDirection == null || sortDirection.isEmpty()) {
            sortDirection = "DESC";
        }
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortCriteria));
    }
}
