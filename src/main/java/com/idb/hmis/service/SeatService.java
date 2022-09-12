/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface SeatService {

    String save(Seat seat, MultipartFile photo);

    String updatePhoto(MultipartFile photo, Long seatId, Long branchId);

    Page<Seat> getSeats(Long  branchId, String searchParam, Pageable pageable);

//    List<Seat> getSeatsByRoom(Long roomId, Long branchId);

    Seat getVerifiedSeat(Long seatId, Long branchId);
    
    Integer bookSeat(String studentId, Long seatId, Long branchId);

    Integer delete(Long seatId, Long branchId);
}
