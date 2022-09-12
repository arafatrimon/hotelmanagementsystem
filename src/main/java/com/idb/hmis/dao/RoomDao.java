package com.idb.hmis.dao;

import com.idb.hmis.entity.Room;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.branch.id = :id")
    List<Room> findByBranchId(@Param("id") Long id);

    @Query("SELECT NEW Room(r.id, r.roomNo) FROM Room r WHERE r.branch.id = :id")
    List<Room> findRoomNumbers(@Param("id") Long id);

    @Query("SELECT case when count(r.roomNo)> 0 then true else false end FROM Room r WHERE r.roomNo = :roomNo and r.branch.id = :id")
    Boolean alreadyExists(@Param("roomNo") String roomNo, @Param("id") Long branchId);

    @Query("SELECT r FROM Room r WHERE r.id = ?1 and r.branch.id = ?2")
    Room findVerifiedRoom(Long roomId, Long branchId);

    @Query("SELECT r.photo FROM Room r WHERE r.id = ?1 and r.branch.id = ?2")
    String findPhoto(Long roomId, Long branchId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Room SET seat_count = seat_count + ? where id = ? AND branch = ?", nativeQuery = true)
    Integer updateSeatCount(Integer count, Long roomId, Long branchId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Room SET photo = ? where id = ? AND branch = ?", nativeQuery = true)
    Integer updatePhoto(String photo, Long roomId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Room r WHERE r.id = ?1 and r.branch.id = ?2")
    Integer delete(Long roomId, Long branchId);
}
