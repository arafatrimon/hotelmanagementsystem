package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Room;
import com.idb.hmis.entity.Seat;
import com.idb.hmis.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.SeatService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/seat/")
public class SeatController {

    @Autowired
    public SeatService seatService;
    @Autowired
    public RoomService roomService;
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
        Page<Seat> seat = this.seatService.getSeats(branchId, searchParam, pageable);
        Map<String, Object> data = this.dataService.getPageData(new Seat(), seat, "branch", "photo");
        this.moduleController.pageDataModel(model, request, "Seat List", "/seat/", data, message);
        return "pages/pagination-without-filter";
    }

    @GetMapping("form")
    public String form(HttpSession session, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List rooms = this.roomService.getRoomNumbers(branchId);
        this.moduleController.multiChoiceFormModel(model, "seat form", new Seat(new Room()), "/seat/save", rooms, message);
        return "forms/seat";
    }

    @GetMapping("update/{seatId}")
    public String editForm(@PathVariable("seatId") Long seatId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<Room> rooms = this.roomService.getRoomNumbers(branchId);
        Seat seat = this.seatService.getVerifiedSeat(seatId, branchId);
        if (seat == null) {
            return "redirect:/user/invalidate";
        }
        this.moduleController.multiChoiceFormModel(model, "seat form", seat, "/seat/save", rooms, null);
        return "forms/seat";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long seatId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Seat seat = this.seatService.getVerifiedSeat(seatId, branchId);
        Map<String, Object> data = this.dataService.getViewData(seat);
        this.moduleController.displayModel(model, "seat details", data, null);
        return "pages/details";
    }

    @GetMapping("delete/{seatId}")
    public String delete(@PathVariable("seatId") Long seatId, HttpServletRequest request, Model model) {
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        Integer result = this.seatService.delete(seatId, branchId);
        String message = null;
        switch (result) {
            case 0:
                return "redirect:/user/invalidate";
            case 1:
                message = "Seat data deleted. Something went wrong when updating seat count in room";
                break;
            case 10:
                message = "Seat data deleted. Something went wrong when deleting photo file";
                break;
            case 11:
                message = "Seat data deleted successfully";
        }
        return this.listByBranch(request, model, message);
    }

    @PostMapping("save")
    public String save(@Valid Seat seat, BindingResult result, @RequestParam("image") MultipartFile photo, HttpServletRequest request, Model model) {
        String message = "Please provide all information correctly";
        HttpSession session = request.getSession();
        if (!result.hasErrors()) {
            Long seatId = seat.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            seat.setBranch(new Branch(branchId));
            message = this.seatService.save(seat, photo);
            if (message.endsWith("successfully")) {
                if (seatId != null) {
                    return "redirect:details/" + seatId;
                }
                seat = new Seat();
            } else if (message.startsWith("Invalid")) {
                return "redirect:/user/invalidate";
            }
        }
        return this.form(session, model, message);
    }

    @GetMapping("upload/{seatId}")
    public String uploadForm(@PathVariable("seatId") Long seatId, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Seat Photo", "/seat/upload", seatId, null);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam("id") Long seatId,
            @RequestParam("image") MultipartFile photo,
            HttpServletRequest request,
            Model model) {
        Long branchId = (Long) request.getSession().getAttribute("branchId");
        String message = this.seatService.updatePhoto(photo, seatId, branchId);
        return this.listByBranch(request, model, message);
    }

}
