package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.NoticeService;
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
@RequestMapping("/notice/")
public class NoticeController {

    @Autowired
    public NoticeService noticeService;
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
        Page<Notice> notice =  this.noticeService.getNotice(branchId, searchParam, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Notice(), notice, "branch");
        this.moduleController.pageDataModel(model, request, "Notice List", "/notice/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Notice notice, Model model, String message) {
        this.moduleController.formModel(model, "Notice form", notice, "/notice/save", message);
        return "forms/notice";
    }

    @GetMapping("update/{noticeId}")
    public String editForm(@PathVariable("noticeId") Long noticeId, HttpSession session, Model model) {
        Notice notice = this.getVerifiedNotice(session, noticeId);
        this.moduleController.formModel(model, "Notice form", notice, "/notice/save", null);
        return "forms/notice";
    }

    @PostMapping("save")
    public String save(@Valid Notice notice, BindingResult result, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long branchId = (Long) session.getAttribute("branchId");
            notice.setBranch(new Branch(branchId));
            this.noticeService.save(notice);
            message = "Notice entry successful";
            notice = new Notice();
        }
        return this.form(session, notice, model, message);
    }

    @GetMapping("delete/{noticeId}")
    public String delete(@PathVariable("noticeId") Long noticeId, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Long branchId = (Long) session.getAttribute("branchId");
        Integer count = this.noticeService.delete(noticeId, branchId);
        if (count > 0) {
            return this.listByBranch(request, model, "Deleted successfully");
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long noticeId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Notice notice = this.noticeService.getVerifiedNotice(noticeId, branchId);
        Map<String, Object> data = this.dataService.getViewData(notice);
        this.moduleController.displayModel(model, "notice details", data, null);
        return "pages/details";
    }

    private Notice getVerifiedNotice(HttpSession session, Long noticeId) {
        Long branchId = (Long) session.getAttribute("branchId");
        return this.noticeService.getVerifiedNotice(noticeId, branchId);
    }

}
