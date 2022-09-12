package com.idb.hmis.controller;

import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.HostelInfo;
import com.idb.hmis.service.HostelInfoService;
import com.idb.hmis.utils.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/hostelinfo/")
public class HostelInfoController {

    @Autowired
    public HostelInfoService hostelInfoService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public DataService dataService;
    private static final String[] IGNORABLES = {"hostelGalaries", "galary", "foodmenus", "achievementses"};

    @GetMapping
    public String getHostelInfo(HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        HostelInfo hostelInfo = this.hostelInfoService.getByHostel(hostelId);
        return this.viewInfo(hostelInfo, model);
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        HostelInfo hostelInfo = this.hostelInfoService.getByHostel(hostelId);
        this.moduleController.formModel(model, "hostel details", hostelInfo, "/hostelinfo/save", null);
        return "forms/hostel-info";
    }

    @GetMapping("update/{id}")
    public String editForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        HostelInfo hostelInfo = this.hostelInfoService.getByHostel(hostelId);
        this.moduleController.formModel(model, "update hostel details", hostelInfo, "/hostelinfo/save", null);
        return "forms/hostel-info";
    }

    @PostMapping("save")
    public String save(@ModelAttribute HostelInfo hostelInfo, HttpSession session, Model model) {
        Long hostelId = (Long) session.getAttribute("hostelId");
        HostelInfo verifiedHostelInfo = this.hostelInfoService.getByHostel(hostelId);
        if (verifiedHostelInfo != null) {
            hostelInfo.setId(verifiedHostelInfo.getId());
        }
        hostelInfo.setHostel(new Hostel(hostelId));
        hostelInfo = this.hostelInfoService.save(hostelInfo);
        return this.viewInfo(hostelInfo, model);
    }

    @GetMapping("details/{id}")
    public String viewInfo(HostelInfo hostelInfo, Model model) {
        Map<String, Object> data = this.dataService.getViewData(hostelInfo, IGNORABLES);
        this.moduleController.displayModel(model, "hostel details", data, null);
        return "pages/details";
    }

}
