package com.idb.hmis.service;

import com.idb.hmis.entity.Salary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalaryService {

    String save(Salary salary, Long branchId);

    String getUnpaidEmployees(Long branchId);

    Page<Salary> getSalaries(Long  branchId, String searchParam, Boolean isActive, Pageable pageable);

    Salary getVerifiedSalary(Long salaryId, Long branchId);

    Integer delete(Long salaryId, Long branchId);
}
