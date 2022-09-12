package com.idb.hmis.dao;

import com.idb.hmis.entity.Student;
import com.idb.hmis.entity.StudentPayment;
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
public interface StudentPaymentDao extends JpaRepository<StudentPayment, Long> {

    final String SEARCH_CONSTRAINT = "(p.student.studentId LIKE %:param% OR p.student.name LIKE %:param% OR p.voucherNo LIKE %:param% OR p.feeType LIKE %:param% "
            + "OR p.paymentMethod LIKE %:param% OR p.transactionNo LIKE %:param% OR p.month LIKE %:param% OR p.amount LIKE %:param% "
            + "OR p.paid LIKE %:param% OR p.due LIKE %:param% OR p.date LIKE %:param% OR p.year LIKE %:param%)";
    final String FIND_BY_BRANCH_QUERY = "SELECT p FROM StudentPayment p WHERE p.student.id IN(SELECT s.id from Student s WHERE s.branch.id = :branch AND s.isActive = :active)";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<StudentPayment> findByBranch(@Param("branch") Long branchId, @Param("active") boolean isActive, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<StudentPayment> searchInBranch(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("active") boolean isActive,
            Pageable page);

    @Query("SELECT p FROM StudentPayment p WHERE p.id = ?1 and p.student.branch.id = ?2")
    StudentPayment findVerifiedStudentPayment(Long studentPaymentId, Long branchId);

    @Query("SELECT case when count(s.id)> 0 then true else false end FROM Student s WHERE s.id = :student AND s.branch.id = :branch")
    Boolean studentExists(
            @Param("student") Long studentId,
            @Param("branch") Long branchId);

    @Query("SELECT case when count(s.id)> 0 then true else false end FROM StudentPayment s WHERE s.student.id = :student AND s.month = :month AND s.year = :year")
    Boolean alreadyExists(
            @Param("student") Long studentId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT NEW Student(s.id, s.studentId, s.name, s.monthlyFee) FROM Student s WHERE s.branch.id = :id AND s.isActive = true")
    List<Student> findStudents(@Param("id") Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM StudentPayment p WHERE p.id = ?1 and p.student.branch.id = ?2")
    Integer delete(Long studentPaymentId, Long branchId);
}
