package com.idb.hmis.serviceImpl;

import com.google.gson.Gson;
import com.idb.hmis.dao.SalaryDao;
import com.idb.hmis.entity.Salary;
import com.idb.hmis.service.SalaryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    SalaryDao salaryDao;

    @Override
    public String save(Salary salary, Long branchId) {
        boolean employeeExists = this.salaryDao.employeeExists(salary.getEmployee().getId(), branchId);
        if (!employeeExists) {
            return null;
        }
        if (salary.getId() != null) {
            return this.update(salary, branchId);
        }
        Boolean exists = this.salaryDao.alreadyExists(salary.getEmployee().getId(), salary.getMonth(), salary.getYear());
        if (exists) {
            return "Salary already paid for given month of the given year";
        }
        this.salaryDao.save(salary);
        return "Salary entry successful";
    }

    public String update(Salary salary, Long branchId) {
        Salary verifiedSalary = this.salaryDao.findVerifiedSalary(salary.getId(), branchId);
        if (verifiedSalary != null) {
            this.salaryDao.save(salary);
            return "Update successful";
        }
        return null;
    }

    @Override
    public Page<Salary> getSalaries(Long  branchId, String searchParam, Boolean isActive, Pageable pageable) {
        if (searchParam == null) {
            return this.salaryDao.findByBranchId(branchId, isActive, pageable);
        }
        return this.salaryDao.searchInBranch(branchId, searchParam, isActive, pageable);
    }

    @Override
    public Salary getVerifiedSalary(Long salaryId, Long branchId) {
        return this.salaryDao.findVerifiedSalary(salaryId, branchId);
    }

    @Override
    public Integer delete(Long salaryId, Long branchId) {
        return this.salaryDao.delete(salaryId, branchId);
    }

    @Override
    public String getUnpaidEmployees(Long branchId) {
        List employees = this.salaryDao.findLiteEmployeeData(branchId);
        Gson g = new Gson();
        String gsonEmployees = g.toJson(employees);
        return gsonEmployees;
    }

}
