/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Room;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface RoomService {

    String save(Room room, MultipartFile photo);

    String updatePhoto(MultipartFile photo, Long roomId, Long branchId);

    List<Room> getByBranchId(Long branchId);

    List<Room> getRoomNumbers(Long branchId);

    Room getVerifiedRoom(Long roomId, Long branchId);

    Integer delete(Long roomId, Long branchId);

}
