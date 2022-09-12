package com.idb.hmis.dao;

import com.idb.hmis.entity.Employee;
import com.idb.hmis.entity.Salary;
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
public interface SalaryDao extends JpaRepository<Salary, Long> {

    final String SEARCH_CONSTRAINT = "(s.employee.name LIKE %:param% OR s.month LIKE %:param% OR s.year LIKE %:param% "
            + "OR s.amount LIKE %:param% OR s.date LIKE %:param%)";
//    final String DATE_CONSTRAINT = "(s.date BETWEEN :start AND :end)";
    final String FIND_BY_BRANCH_QUERY = "SELECT s FROM Salary s WHERE s.employee.branch.id = :branch AND s.employee.isActive = :active";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
//    final String FIND_BY_DATE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
//    final String SEARCH_BY_DATE_QUERY = FIND_BY_DATE_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Salary> findByBranchId(
            @Param("branch") Long id,
            @Param("active") Boolean isActive,
            Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Salary> searchInBranch(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("active") Boolean isActive,
            Pageable page);
//
//    @Query(FIND_BY_DATE_QUERY)
//    Page<Salary> findByDate(
//            @Param("branch") Long branchId,
//            @Param("start") Date startDate,
//            @Param("end") Date endDate,
//            Pageable page);
//
//    @Query(SEARCH_BY_DATE_QUERY)
//    Page<Salary> searchByDate(
//            @Param("branch") Long branchId,
//            @Param("param") String searchParam,
//            @Param("start") Date startDate,
//            @Param("end") Date endDate,
//            Pageable page);

    @Query("SELECT s FROM Salary s WHERE s.id = :id and s.employee.branch.id = :branch")
    Salary findVerifiedSalary(
            @Param("id") Long salaryId,
            @Param("branch") Long branchId);

    @Query("SELECT case when count(e.id)> 0 then true else false end FROM Employee e WHERE e.id = :employee AND e.branch.id = :branch")
    Boolean employeeExists(
            @Param("employee") Long employeeId,
            @Param("branch") Long branchId);

//    @Query("SELECT NEW Employee(e.id, e.name, e.salary) FROM Employee e WHERE e.branch.id = :branchId AND e.id NOT IN("
//            + "SELECT s.employee.id from Salary s WHERE s.month = MONTH(CURDATE()) AND s.year = YEAR(CURDATE()))")
    @Query("SELECT NEW Employee(e.id, e.name, e.salary) FROM Employee e WHERE e.branch.id = :branchId AND e.isActive = true")
    List<Employee> findLiteEmployeeData(@Param("branchId") Long branchId);

    @Query("SELECT SUM(amount) AS total FROM Salary b WHERE b.date BETWEEN ?1 and ?2 and b.employee.id IN(SELECT e.id from Employee e WHERE e.branch.id = ?3)")
    Long findAmount(String startDate, String endDate, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Salary s WHERE s.id = ?1 and s.employee.id in(SELECT e.id FROM Employee e WHERE e.branch.id = ?2)")
    Integer delete(Long salaryId, Long branchId);

    @Query("SELECT case when count(s.id)> 0 then true else false end FROM Salary s WHERE s.employee.id = :employee AND s.month = :month AND s.year = :year")
    Boolean alreadyExists(
            @Param("employee") Long employeeId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

}
