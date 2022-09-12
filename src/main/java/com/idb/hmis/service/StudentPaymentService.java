/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.StudentPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface StudentPaymentService {

    String save(StudentPayment studentPayment, Long branchId);

    Page<StudentPayment> getByBranch(Long branchId, boolean isActive, Pageable pageable);

    Page<StudentPayment> searchInBranch(Long branchId, String searchParam, boolean isActive, Pageable pageable);

    String getUnpaidStudents(Long branchId);

    StudentPayment getVerifiedStudentPayment(Long studentPaymentId, Long branchId);

    Integer delete(Long studentPaymentId, Long branchId);
}
