package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.RoomDao;
import com.idb.hmis.entity.Room;
import com.idb.hmis.service.RoomService;
import com.idb.hmis.utils.FileService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomDao roomDao;
    @Autowired
    FileService fileService;

    @Override
    public String save(Room room, MultipartFile photo) {
        if (room.getId() != null) {
            return this.update(room, photo);
        }
        boolean alreadyExists = this.roomDao.alreadyExists(room.getRoomNo(), room.getBranch().getId());
        if (!alreadyExists) {
            String photoTitle = this.fileService.store(photo);
            room.setPhoto(photoTitle);
            this.roomDao.save(room);
            return "Room data saved successfully";
        }
        return "Failed to save room. Room name already exists in branch.";
    }

    @Override
    public Integer delete(Long roomId, Long branchId) {
        Room room = this.roomDao.findVerifiedRoom(roomId, branchId);
        boolean photoDeleted = this.fileService.delete(room.getPhoto());
        int count = this.roomDao.delete(roomId, branchId);
        return (count == 0) ? 0 : (photoDeleted) ? 11 : 1;
    }

    @Override
    public String updatePhoto(MultipartFile photo, Long roomId, Long branchId) {
        String photoTitle = this.fileService.store(photo);
        if (photoTitle == null) {
            return "Something went wrong. Update failed.";
        }
        String existingPhoto = this.roomDao.findPhoto(roomId, branchId);
        Integer count = this.roomDao.updatePhoto(photoTitle, roomId, branchId);
        if (count == 1) {
            this.fileService.delete(existingPhoto);
        }
        return "Profile photo updated successfully";
    }

    private String update(Room room, MultipartFile photo) {
        Long roomId = room.getId();
        Long branchId = room.getBranch().getId();
        Room verifiedRoom = this.roomDao.findVerifiedRoom(roomId, branchId);
        if (verifiedRoom == null) {
            return "Invalid data found. Failed to update.";
        }
        String previousPhoto = verifiedRoom.getPhoto();
        String newPhoto = fileService.store(photo);
        if (newPhoto == null) {
            room.setPhoto(previousPhoto);
        } else {
            room.setPhoto(newPhoto);
            this.fileService.delete(previousPhoto);
        }
        this.roomDao.save(room);
        return "Room data updated successfully";
    }

    @Override
    public List<Room> getByBranchId(Long id) {
        return this.roomDao.findByBranchId(id);
    }

    @Override
    public Room getVerifiedRoom(Long roomId, Long branchId) {
        return this.roomDao.findVerifiedRoom(roomId, branchId);
    }

    @Override
    public List<Room> getRoomNumbers(Long branchId) {
        return this.roomDao.findRoomNumbers(branchId);
    }
}
