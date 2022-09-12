package com.idb.hmis.dao;

import com.idb.hmis.entity.Employee;
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
public interface EmployeeDao extends JpaRepository<Employee, Long> {

    final String SEARCH_CONSTRAINT = "(e.name LIKE %:param% OR e.birthDate LIKE %:param% OR e.phone LIKE %:param% OR "
            + "e.nationalId LIKE %:param% OR e.designation LIKE %:param% OR e.salary LIKE %:param%)";
    final String ACTIVE_CONSTRAINT = "e.isActive = :active";
    final String MANAGER_CONSTRAINT = "e.username IS NOT NULL";
    final String EMPLOYEE_CONSTRAINT = "e.username IS NULL";
    final String FIND_BY_BRANCH_QUERY = "SELECT e FROM Employee e WHERE e.branch.id = :branch";
    final String FIND_MANAGER_QUERY = FIND_BY_BRANCH_QUERY + " AND " + MANAGER_CONSTRAINT + " AND " + ACTIVE_CONSTRAINT;
    final String SEARCH_MANAGER_QUERY = FIND_MANAGER_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_EMPLOYEE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + EMPLOYEE_CONSTRAINT + " AND " + ACTIVE_CONSTRAINT;
    final String SEARCH_EMPLOYEE_QUERY = FIND_EMPLOYEE_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_EMPLOYEE_QUERY)
    Page<Employee> findEmployees(@Param("branch") Long branchId, @Param("active") boolean isActive, Pageable page);

    @Query(SEARCH_EMPLOYEE_QUERY)
    Page<Employee> searchEmployees(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("active") boolean isActive,
            Pageable page);

    @Query(FIND_MANAGER_QUERY)
    Page<Employee> findManagers(@Param("branch") Long branchId, @Param("active") boolean isActive, Pageable page);

    @Query(SEARCH_MANAGER_QUERY)
    Page<Employee> searchManagers(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("active") boolean isActive,
            Pageable page);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByNationalId(String nationalId);

    boolean existsByEmail(String email);

    Employee findByUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.username = null AND e.branch.id = :branchId")
    List<Employee> findEmployeesByBranch(@Param("branchId") Long branchId);

    @Query("SELECT e FROM Employee e WHERE e.username != null AND e.branch.id = :branchId")
    List<Employee> findManagersByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT e FROM Employee e WHERE e.id = :id AND e.username = :username AND e.branch.id = :branchId")
    Employee findVerifiedEmployee(@Param("id") Long id, @Param("username") String username, @Param("branchId") Long branchId);

    @Query("SELECT e FROM Employee e WHERE e.id = :employeeId AND e.branch.id = :branchId")
    Employee findVerifiedEmployee(@Param("employeeId") Long employeeId, @Param("branchId") Long branchId);

    @Query("SELECT e.username FROM Employee e WHERE e.id = :employeeId AND e.branch.id = :branchId")
    Long findusername(@Param("employeeId") Long employeeId, @Param("branchId") Long branchId);

    @Query("SELECT e.photo FROM Employee e WHERE e.id = :employeeId AND e.branch.id = :branchId")
    String findPhoto(@Param("employeeId") Long employeeId, @Param("branchId") Long branchId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Employee SET photo = ? where id = ? AND branch = ?", nativeQuery = true)
    Integer updatePhoto(String photo, Long employeeId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Employee e WHERE e.id = ?1 and e.branch.id = ?2")
    Integer delete(Long employeeId, Long branchId);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.isActive = false WHERE e.id = :employeeId and e.branch.id = :branchId")
    Integer deactivate(@Param("employeeId") Long employeeId, @Param("branchId") Long branchId);
}
