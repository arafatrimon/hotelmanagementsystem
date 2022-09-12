package com.idb.hmis.dao;

import com.idb.hmis.entity.Student;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {

    final String SEARCH_CONSTRAINT = "(s.seat LIKE %:param% OR s.name LIKE %:param% OR s.phone LIKE %:param% OR "
            + "s.presentInstitute LIKE %:param% OR s.currentSubject LIKE %:param% OR s.batchNo LIKE %:param% OR s.bloodGroup LIKE %:param%)";
    final String ACTIVE_CONSTRAINT = "s.isActive = :active";
    final String FIND_BY_BRANCH_QUERY = "SELECT s FROM Student s WHERE s.branch.id = :branch";
//    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_ACTIVE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + ACTIVE_CONSTRAINT;
    final String SEARCH_IN_ACTIVE_QUERY = FIND_BY_ACTIVE_QUERY + " AND " + SEARCH_CONSTRAINT;
//
//    @Query(FIND_BY_BRANCH_QUERY)
//    Page<Student> findByBranch(@Param("branch") Long branchId, Pageable page);
//
//    @Query(SEARCH_IN_BRANCH_QUERY)
//    Page<Student> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_BY_ACTIVE_QUERY)
    Page<Student> findStudents(@Param("branch") Long branchId, @Param("active") boolean isActive, Pageable page);

    @Query(SEARCH_IN_ACTIVE_QUERY)
    Page<Student> searchStudents(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("active") boolean isActive,
            Pageable page);

    @Query("select case when count(s.studentId)> 0 then true else false end from Student s where "
            + "s.studentId = :student AND s.branch.id = :branch AND s.isActive = true")
    boolean existsByStudentId(@Param("student") String studentId, @Param("branch") Long branchId);

    @Query("select case when count(s.phone)> 0 then true else false end from Student s where "
            + "s.phone = :phone AND s.branch.id = :branch AND s.isActive = true")
    boolean existsByPhone(@Param("phone") String phone, @Param("branch") Long branchId);

    boolean existsByNationalId(String nationalId);

    boolean existsByPassportNo(String passportNo);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.id = ?1 and s.branch.id = ?2")
    Student findVerifiedStudent(Long studentId, Long branchId);

    Student findByUsername(String username);

    @Query(value = "SELECT photo FROM Student WHERE id = ? and branch = ?", nativeQuery = true)
    String findPhoto(Long studentId, Long branchId);

    @Query("SELECT s.seat.id FROM Student s WHERE s.id = ?1 and s.branch.id = ?2")
    Long findSeat(Long studentId, Long branchId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Student SET photo = ? where id = ? AND branch = ?", nativeQuery = true)
    Integer updatePhoto(String photo, Long studentId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Student s WHERE s.id = ?1 and s.branch.id = ?2")
    Integer delete(Long studentId, Long branchId);

    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.isActive = false WHERE s.id = ?1 and s.branch.id = ?2")
    Integer deactivate(Long studentId, Long branchId);
}
