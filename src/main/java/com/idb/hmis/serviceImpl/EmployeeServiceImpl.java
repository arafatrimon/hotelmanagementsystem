package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.EmployeeDao;
import com.idb.hmis.dao.UserDao;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.idb.hmis.service.EmployeeService;
import com.idb.hmis.utils.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    UserDao userDao;
    @Autowired
    FileService fileService;

    @Override
    public String save(Employee employee, MultipartFile photo) {
        if (employee.getId() != null) {
            return this.update(employee, photo);
        }
        String message = this.alreadyExists(employee);
        if (message == null) {
            if (photo == null || photo.isEmpty()) {
                employee.setPhoto(null);
            } else {
                String photoTitle = this.fileService.store(photo);
                employee.setPhoto(photoTitle);
            }
            this.employeeDao.save(employee);
            return "Employee data saved successfully";
        }
        return message;
    }

    @Override
    public Employee getVerifiedEmployee(Long employeeId, Long branchId) {
        return this.employeeDao.findVerifiedEmployee(employeeId, branchId);
    }

    @Override
    public Employee getByUsername(String username) {
        return this.employeeDao.findByUsername(username);
    }

    @Override
    public Employee createTempManager(String username, Branch branch) {
        Employee manager = new Employee(username, branch);
        return this.employeeDao.save(manager);
    }

    @Override
    public Page<Employee> getEmployees(Long branchId, boolean isActive, Pageable pageable) {
        return this.employeeDao.findEmployees(branchId, isActive, pageable);
    }

    @Override
    public Page<Employee> searchEmployees(Long branchId, String searchParam, boolean isActive, Pageable pageable) {
        return this.employeeDao.searchEmployees(branchId, searchParam, isActive, pageable);
    }

    @Override
    public Page<Employee> getManagers(Long branchId, boolean isActive, Pageable pageable) {
        return this.employeeDao.findManagers(branchId, isActive, pageable);
    }

    @Override
    public Page<Employee> searchManagers(Long branchId, String searchParam, boolean isActive, Pageable pageable) {
        return this.employeeDao.searchManagers(branchId, searchParam, isActive, pageable);
    }

    @Override
    public String deactivate(Long employeeId, Long branchId) {
        Employee employee = this.employeeDao.findVerifiedEmployee(employeeId, branchId);
        if(employee == null) return "No such employee found";
        Integer userCount = 1;
        userCount = (employee.getUsername() != null) ? this.userDao.deactivate(employee.getUsername()) : userCount;
        Integer employeeCount = (userCount > 0) ? this.employeeDao.deactivate(employeeId, branchId) : 0;
        return (employeeCount > 0) ? "Successfully diactivated employee" : "Something went wrong. Please try again or contact with authority.";
    }

    private String alreadyExists(Employee employee) {
        if (this.employeeDao.existsByPhone(employee.getPhone())) {
            return "Failed to register. Phone number already used";
        }
        if (this.employeeDao.existsByNationalId(employee.getNationalId())) {
            return "Failed to register. NID number already used";
        }
        if (this.employeeDao.existsByEmail(employee.getEmail())) {
            return "Failed to register. Email address already used";
        }
        if (employee.getUsername() != null && this.employeeDao.existsByUsername(employee.getUsername())) {
            return "Failed to register. Given username already exists";
        }
        return null;
    }

    @Override
    public Integer delete(Long employeeId, Long branchId) {
        Integer count = 1;
        Employee employee = this.employeeDao.findVerifiedEmployee(employeeId, branchId);
        if (employee.getUsername() != null) {
            count = this.userDao.deleteByUsername(employee.getUsername());
        }
        count = (count == 1) ? this.employeeDao.delete(employeeId, branchId) : 0;
        if (count == 1) {
            boolean photoDeleted = this.fileService.delete(employee.getPhoto());
            return (photoDeleted) ? 11 : 1;
        }
        return 0;
    }

    @Override
    public String updatePhoto(MultipartFile photo, Long employeeId, Long branchId) {
        String photoTitle = this.fileService.store(photo);
        if (photoTitle == null) {
            return "Something went wrong. Update failed";
        }
        String existingPhoto = this.employeeDao.findPhoto(employeeId, branchId);
        Integer count = this.employeeDao.updatePhoto(photoTitle, employeeId, branchId);
        if (count == 1) {
            this.fileService.delete(existingPhoto);
        }
        return photoTitle;
    }

    private String update(Employee employee, MultipartFile photo) {
        Long employeeId = employee.getId();
        Long branchId = employee.getBranch().getId();
        Employee verifiedEmployee = this.employeeDao.findVerifiedEmployee(employeeId, branchId);
        if (verifiedEmployee == null) {
            return "Invalid data found. Failed to update.";
        }
        String previousPhoto = verifiedEmployee.getPhoto();
        String newPhoto = fileService.store(photo);
        if (newPhoto == null) {
            employee.setPhoto(previousPhoto);
        } else {
            employee.setPhoto(newPhoto);
            this.fileService.delete(previousPhoto);
        }
        employee.setUsername(verifiedEmployee.getUsername());
        this.employeeDao.save(employee);
        return "Employee data updated successfully";
    }

}
