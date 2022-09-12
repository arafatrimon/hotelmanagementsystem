package com.idb.hmis.controller;

import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.BranchService;
import com.idb.hmis.service.SummaryService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/branch/")
public class BranchController {

    @Autowired
    public BranchService branchService;
    @Autowired
    private ModuleController moduleController;
    @Autowired
    private DataService dataService;
    @Autowired
    private SummaryService summaryService;

    @GetMapping("display")
    public String showBranches(HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        List<Branch> branches = this.branchService.getByHostelId(hostelId);
        this.moduleController.selectionModel(model, "select branch", branches,
                "Please select a branch or add new branches for managerial operations.");
        return "pages/branches";
    }

    @GetMapping("{branchId}")
    public String selectBranch(@PathVariable("branchId") Long branchId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Long hostelId = (Long) session.getAttribute("hostelId");
        Branch branch = null;
        if (request.isUserInRole("ROLE_MANAGER")) {
            branchId = (Long) session.getAttribute("branchId");
        }
        if (hostelId != null) {
            branch = this.branchService.getVerifiedBranch(branchId, hostelId);
        }
        if (branch != null) {
            if (request.isUserInRole("ROLE_ADMIN")) {
                session.setAttribute("branchId", branchId);
                session.setAttribute("branchName", branch.getName());
            }
            Map<String, Object> summary = summaryService.getBranchSummary(branchId);
            this.moduleController.simpleModel(model, "branch summary", null);
            model.addAttribute("summary", summary);
            return "pages/summary";
        }
        return "redirect:/user/invalidate";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model) {
        this.moduleController.formModel(model, "branch form", new Branch(), "/branch/save", null);
        return "forms/branch";
    }

    @GetMapping("update/{branchId}")
    public String editForm(@PathVariable("branchId") Long branchId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        Branch verifiedBranch = this.branchService.getVerifiedBranch(branchId, hostelId);
        this.moduleController.formModel(model, "branch form", verifiedBranch, "/branch/save", null);
        return "forms/branch";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long branchId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        Branch branch = this.branchService.getVerifiedBranch(branchId, hostelId);
        Map<String, Object> data = this.dataService.getViewData(branch);
        this.moduleController.displayModel(model, "branch details", data, null);
        return "pages/details";
    }

    @GetMapping
    public String listBranchs(HttpSession session, Model model, String message) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        List<Branch> branches = this.branchService.getByHostelId(hostelId);
        Map<String, Object> data = this.dataService.getTableData(new Branch(), branches, "hostel", "photo");
        this.moduleController.displayModel(model, "branch list", data, message);
        return "pages/data-table";
    }

    @GetMapping("delete/{branchId}")
    public String delete(@PathVariable("branchId") Long branchId, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        int result = this.branchService.delete(branchId, hostelId);
        Long sessionBranchId = (Long) session.getAttribute("branchId");
        if (sessionBranchId.equals(branchId)) {
            session.removeAttribute("branchId");
            session.removeAttribute("branchName");
        }
        switch (result) {
            case 1:
                return this.listBranchs(session, model, "Branch data deleted. Failed to delete photo file.");
            case 11:
                return this.listBranchs(session, model, "Branch data deleted successfully");
            default:
                return "redirect:/user/invalidate";
        }
    }

    @PostMapping("save")
    public String save(@Valid Branch branch, BindingResult result, @RequestParam("image") MultipartFile photo, HttpSession session, Model model) {
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long branchId = branch.getId();
            Long hostelId = (Long) session.getAttribute("hostelId");
            branch.setHostel(new Hostel(hostelId));
            message = this.branchService.save(branch, photo);
            if (message.endsWith("successfully")) {
                if (branchId != null) {
                    Long sessionBranchId = (Long) session.getAttribute("branchId");
                    if (branchId.equals(sessionBranchId)) {
                        session.setAttribute("branch", branch);
                    }
                    return this.listBranchs(session, model, message);
                }
                branch = new Branch();
            } else if (message.startsWith("Invalid")) {
                return "redirect:/user/invalidate";
            }
        }
        this.moduleController.formModel(model, "branch form", branch, "/branch/save", message);
        return "forms/branch";
    }

    @GetMapping("upload/{branchId}")
    public String uploadForm(@PathVariable("branchId") Long branchId, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Branch Photo", "/branch/upload", branchId, null);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam("id") Long branchId,
            @RequestParam("image") MultipartFile photo,
            HttpSession session,
            Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        String message = this.branchService.updatePhoto(photo, branchId, hostelId);
        Long sessionBranchId = (Long) session.getAttribute("branchId");
        if (!message.endsWith("failed") && branchId.equals(sessionBranchId)) {
            session.setAttribute("branchLogo", message);
            message = "Update successful";
        }
        return this.listBranchs(session, model, message);
    }
}
