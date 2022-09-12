package com.idb.hmis.dao;

import com.idb.hmis.entity.Attendance;
import com.idb.hmis.entity.Student;
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
public interface AttendanceDao extends JpaRepository<Attendance, Long> {

    final String SEARCH_CONSTRAINT = "(a.student.studentId LIKE %:param% OR a.student.name LIKE %:param% OR a.isPresent LIKE %:param% OR a.date LIKE %:param%)";
    final String FIND_BY_BRANCH_QUERY = "SELECT a FROM Attendance a WHERE a.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Attendance> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Attendance> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT a FROM Attendance a WHERE a.id = :id AND a.branch.id = :branch")
    Attendance findVerifiedAttendance(
            @Param("id") Long attendanceId,
            @Param("branch") Long branchId);

    @Query("SELECT case when count(s.id)> 0 then true else false end FROM Student s WHERE s.id = :student AND s.branch.id = :branch")
    Boolean studentExists(
            @Param("student") Long studentId,
            @Param("branch") Long branchId);

//    @Query("SELECT a FROM Attendance a WHERE a.id = :id AND a.student.id = :student AND a.branch.id = :branch")
//    Attendance findVerifiedAttendance(
//            @Param("id") Long attendanceId,
//            @Param("student") Long studentId,
//            @Param("branch") Long branchId);

    @Query("SELECT NEW Student(s.id, s.studentId, s.name) FROM Student s "
            + "WHERE s.branch.id = :branch AND s.id NOT IN("
            + "SELECT a.student.id from Attendance a where a.branch.id = :branch AND a.date = CURDATE()"
            + ")")
    List<Student> findUnhandledStudents(@Param("branch") Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Attendance a WHERE a.id = ?1 AND a.branch.id = ?2")
    Integer delete(Long attendanceId, Long branchId);

    @Query("SELECT CASE WHEN COUNT(a.student.id)> 0 THEN true ELSE false END FROM Attendance a "
            + "WHERE a.student.id = :student AND  a.branch.id = :branch AND a.date = CURDATE()")
    boolean alreadyExists(
            @Param("student") Long studentId,
            @Param("branch") Long branchId
    );
}
