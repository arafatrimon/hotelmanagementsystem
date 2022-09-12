package com.idb.hmis.service;

import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {

    String save(Employee employee, MultipartFile photo);

    String updatePhoto(MultipartFile photo, Long employeeId, Long branchId);

    Employee createTempManager(String username, Branch branch);

    Employee getVerifiedEmployee(Long employeeId, Long branchId);

    Page<Employee> getEmployees(Long branchId, boolean isActive, Pageable pageable);

    Page<Employee> searchEmployees(Long branchId, String searchParam, boolean isActive, Pageable pageable);

    Page<Employee> getManagers(Long branchId, boolean isActive, Pageable pageable);

    Page<Employee> searchManagers(Long branchId, String searchParam, boolean isActive, Pageable pageable);

    Employee getByUsername(String username);

    String deactivate(Long employeeId, Long branchId);

    Integer delete(Long employeeId, Long branchId);

}
