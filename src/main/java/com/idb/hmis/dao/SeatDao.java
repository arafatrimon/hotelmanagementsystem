package com.idb.hmis.dao;

import com.idb.hmis.entity.Seat;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatDao extends JpaRepository<Seat, Long> {

    final String SEARCH_CONSTRAINT = "(s.room.roomNo LIKE %:param% OR s.seatNo LIKE %:param% OR s.monthlyRent LIKE %:param% OR "
            + "s.studentId LIKE %:param%)";
    final String FIND_BY_BRANCH_QUERY = "SELECT s FROM Seat s WHERE s.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Seat> findByBranchId(@Param("branch") Long branchId, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Seat> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT s FROM Seat s WHERE s.id = ?1 and s.branch.id = ?2")
    Seat findVerifiedSeat(Long seatId, Long branchId);

    @Query("SELECT s.photo FROM Seat s WHERE s.id = ?1 and s.branch.id = ?2")
    String findPhoto(Long seatId, Long branchId);

    @Query(value = "SELECT room FROM Seat WHERE id = ? and branch = ?", nativeQuery = true)
    Long findRoomId(Long seatId, Long branchId);

    @Query("SELECT case when count(s.seatNo)> 0 then true else false end FROM Seat s WHERE s.seatNo = ?1 and s.room.id = ?2 and s.branch.id = ?3")
    Boolean alreadyExists(String seatNo, Long roomId, Long branchId);
//
//    @Query("SELECT s FROM Seat s WHERE s.room.id = :roomId and s.branch.id = :branchId")
//    List<Seat> findByRoomAndBranch(@Param("roomId") Long roomId, @Param("branchId") Long branchId);

    @Query("SELECT NEW Seat(s.id, s.room.roomNo, s.seatNo, s.monthlyRent) FROM Seat s WHERE s.branch.id = :branch and s.studentId = 'empty'")
    List<Seat> findEmptySeats(@Param("branch") Long branchId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Seat SET photo = ? where id = ? AND branch = ?", nativeQuery = true)
    Integer updatePhoto(String photo, Long seatId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Seat s WHERE s.id = ?1 and s.branch.id = ?2")
    Integer delete(Long seatId, Long branchId);

    @Transactional
    @Modifying
    @Query("UPDATE Seat s SET s.studentId = :studentId WHERE s.id = :seatId AND s.branch.id = :branchId")
    Integer bookSeat(@Param("studentId") String studentId, @Param("seatId") Long seatId, @Param("branchId") Long branchId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Seat s SET s.student_id = 'empty' WHERE s.id = ? AND s.branch = ?", nativeQuery = true)
    Integer releaseSeat(Long seatId, Long branchId);
}
