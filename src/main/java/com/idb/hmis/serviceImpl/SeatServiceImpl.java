package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.RoomDao;
import com.idb.hmis.dao.SeatDao;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Seat;
import com.idb.hmis.service.SeatService;
import com.idb.hmis.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    SeatDao seatDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    FileService fileService;

    @Override
    public String save(Seat seat, MultipartFile photo) {
        Long roomId = seat.getRoom().getId();
        Long branchId = seat.getBranch().getId();
        if (seat.getId() != null) {
            return this.update(seat, photo);
        }
        boolean alreadyExists = this.seatDao.alreadyExists(seat.getSeatNo(), roomId, branchId);
        if (!alreadyExists) {
            String photoTitle = this.fileService.store(photo);
            seat.setPhoto(photoTitle);
            seat.setStudentId("empty");
            this.seatDao.save(seat);
            Integer count = this.roomDao.updateSeatCount(1, roomId, branchId);
            if (count == 1) {
                return "Seat saved successfully";
            }
            return "Seat saved successfully. Could not update seat count in room";
        }
        return "Failed to save seat. Seat already exists in room";
    }

    @Override
    public String updatePhoto(MultipartFile photo, Long seatId, Long branchId) {
        String photoTitle = this.fileService.store(photo);
        if (photoTitle == null) {
            return "Something went wrong. Update failed.";
        }
        String existingPhoto = this.seatDao.findPhoto(seatId, branchId);
        Integer count = this.seatDao.updatePhoto(photoTitle, seatId, branchId);
        if (count == 1) {
            this.fileService.delete(existingPhoto);
        }
        return "Profile photo updated successfully";
    }

    private String update(Seat seat, MultipartFile photo) {
        Long seatId = seat.getId();
        Long branchId = seat.getBranch().getId();
        Seat verifiedSeat = this.seatDao.findVerifiedSeat(seatId, branchId);
        if (verifiedSeat == null) {
            return "Invalid data found. Failed to update.";
        }
        String previousPhoto = verifiedSeat.getPhoto();
        String newPhoto = fileService.store(photo);
        if (newPhoto == null) {
            seat.setPhoto(previousPhoto);
        } else {
            seat.setPhoto(newPhoto);
            this.fileService.delete(previousPhoto);
        }
        this.seatDao.save(seat);
        return "Seat data updated successfully";
    }

    @Override
    public Integer delete(Long seatId, Long branchId) {
        Long roomId = this.seatDao.findRoomId(seatId, branchId);
        String photo = this.seatDao.findPhoto(seatId, branchId);
        if (roomId != null) {
            Integer count = this.seatDao.delete(seatId, branchId);
            if (count == 1) {
                boolean photoDeleted = this.fileService.delete(photo);
                count = this.roomDao.updateSeatCount(-1, roomId, branchId);
                return (count == 0) ? 1 : (photoDeleted) ? 11 : 10;
            }
        }
        return 0;
    }

    @Override
    public Page<Seat> getSeats(Long  branchId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.seatDao.findByBranchId(branchId, pageable);
        }
        return this.seatDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Seat getVerifiedSeat(Long seatId, Long branchId) {
        return this.seatDao.findVerifiedSeat(seatId, branchId);
    }

    @Override
    public Integer bookSeat(String studentId, Long seatId, Long branchId) {
        return this.seatDao.bookSeat(studentId, seatId, branchId);
    }
}
