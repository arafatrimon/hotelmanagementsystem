package com.idb.hmis.serviceImpl;

import com.google.gson.Gson;
import com.idb.hmis.dao.StudentPaymentDao;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.StudentPayment;
import com.idb.hmis.service.StudentPaymentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class StudentPaymentServiceImpl implements StudentPaymentService {

    @Autowired
    StudentPaymentDao studentPaymentDao;

    @Override
    public String save(StudentPayment studentPayment, Long branchId) {
        boolean studentExists = this.studentPaymentDao.studentExists(studentPayment.getStudent().getId(), branchId);
        if (!studentExists) {
            return null;
        }
        if (studentPayment.getId() != null) {
            return this.update(studentPayment, branchId);
        }
        Boolean exists = this.studentPaymentDao.alreadyExists(studentPayment.getStudent().getId(), studentPayment.getMonth(), studentPayment.getYear());
        if (exists) {
            return "Student already paid for given month and year";
        }
        this.studentPaymentDao.save(studentPayment);
        return "StudentPayment entry successful";
    }

    public String update(StudentPayment studentPayment,  Long branchId) {
        StudentPayment verifiedStudentPayment = this.studentPaymentDao.findVerifiedStudentPayment(studentPayment.getId(), branchId);
        if (verifiedStudentPayment != null) {
            this.studentPaymentDao.save(studentPayment);
            return "Update successful";
        }
        return null;
    }

    @Override
    public Page<StudentPayment> getByBranch(Long branchId, boolean isActive, Pageable pageable) {
        return this.studentPaymentDao.findByBranch(branchId, isActive, pageable);
    }

    @Override
    public Page<StudentPayment> searchInBranch(Long branchId, String searchParam, boolean isActive, Pageable pageable) {
        return this.studentPaymentDao.searchInBranch(branchId, searchParam, isActive, pageable);
    }

    @Override
    public StudentPayment getVerifiedStudentPayment(Long studentPaymentId, Long branchId) {
        return this.studentPaymentDao.findVerifiedStudentPayment(studentPaymentId, branchId);
    }

    @Override
    public Integer delete(Long studentPaymentId, Long branchId) {
        return this.studentPaymentDao.delete(studentPaymentId, branchId);
    }

    @Override
    public String getUnpaidStudents(Long branchId) {
        List students = this.studentPaymentDao.findStudents(branchId);
        Gson g = new Gson();
        String gsonEmployees = g.toJson(students);
        return gsonEmployees;
    }

}
