package com.idb.hmis.controller;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.idb.hmis.service.RoomService;
import com.idb.hmis.utils.DataService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/room/")
public class RoomController {

    @Autowired
    public RoomService roomService;
    @Autowired
    public ModuleController moduleController;
    @Autowired
    public DataService dataService;

    @GetMapping("form")
    public String form(HttpSession session, Model model) {
        this.moduleController.formModel(model, "room form", new Room(), "/room/save", null);
        return "forms/room";
    }

    @GetMapping("update/{roomId}")
    public String editForm(@PathVariable("roomId") Long roomId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Room verifiedRoom = this.roomService.getVerifiedRoom(roomId, branchId);
        this.moduleController.formModel(model, "room form", verifiedRoom, "/room/save", null);
        return "forms/room";
    }

    @GetMapping("details/{id}")
    public String details(@PathVariable("id") Long roomId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Room room = this.roomService.getVerifiedRoom(roomId, branchId);
        Map<String, Object> data = this.dataService.getViewData(room);
        this.moduleController.displayModel(model, "room details", data, null);
        return "pages/details";
    }

    @GetMapping
    public String listRooms(HttpSession session, Model model, String message) {
        Long branchId = (Long) session.getAttribute("branchId");
        List<Room> roomes = this.roomService.getByBranchId(branchId);
        Map<String, Object> data = this.dataService.getTableData(new Room(), roomes, "branch", "photo", "seats");
        this.moduleController.displayModel(model, "room list", data, message);
        return "pages/data-table";
    }

    @GetMapping("delete/{roomId}")
    public String delete(@PathVariable("roomId") Long roomId, HttpSession session, Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        Integer result = this.roomService.delete(roomId, branchId);
        switch (result) {
            case 1:
                return this.listRooms(session, model, "Room data deleted. Failed to delete photo file.");
            case 11:
                return this.listRooms(session, model, "Room data deleted successfully");
            default:
                return "redirect:/user/invalidate";
        }
    }

    @PostMapping("save")
    public String save(@Valid Room room, BindingResult result, @RequestParam("image") MultipartFile photo, HttpSession session, Model model) {
        String message = "Please provide all information correctly";
        if (!result.hasErrors()) {
            Long roomId = room.getId();
            Long branchId = (Long) session.getAttribute("branchId");
            room.setBranch(new Branch(branchId));
            message = this.roomService.save(room, photo);
            if (message.endsWith("successfully")) {
                if (roomId != null) {
                    return this.details(roomId, session, model);
                }
                room = new Room();
            } else if (message.startsWith("Invalid")) {
                return "redirect:/user/invalidate";
            }
        }
        this.moduleController.formModel(model, "room form", room, "/room/save", message);
        return "forms/room";
    }

    @GetMapping("upload/{roomId}")
    public String uploadForm(@PathVariable("roomId") Long roomId, HttpSession session, Model model) {
        this.moduleController.uploadModel(model, "Upload Room Photo", "/room/upload", roomId, null);
        return "pages/uploader";
    }

    @PostMapping("upload")
    public String upload(
            @RequestParam("id") Long roomId,
            @RequestParam("image") MultipartFile photo,
            HttpSession session,
            Model model) {
        Long branchId = (Long) session.getAttribute("branchId");
        String message = this.roomService.updatePhoto(photo, roomId, branchId);
        return this.listRooms(session, model, message);
    }

}
