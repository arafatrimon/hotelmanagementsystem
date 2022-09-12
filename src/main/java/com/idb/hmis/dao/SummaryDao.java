package com.idb.hmis.dao;

import com.idb.hmis.entity.Deposit;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryDao extends JpaRepository<Deposit, Long> {

    @Query("SELECT COUNT(s.id) FROM Student s WHERE s.isActive = true and s.branch.id = :branch")
    Integer countCurrentStudents(@Param("branch") Long branchId);

    @Query("SELECT (SUM(m.breakfast)+SUM(m.lunch)+SUM(m.supper))  AS Total FROM Meal m WHERE m.date = CURDATE() AND m.branch.id = :branch")
    Integer countTodaysMeal(@Param("branch") Long branchId);

    @Query("SELECT SUM(total) AS total FROM Bazar b WHERE b.date = CURDATE() and b.branch.id = ?1")
    Integer todaysBazar(Long branchId);

    @Query("SELECT COUNT(s.id) FROM Seat s WHERE s.branch.id = :branch and s.studentId = 'empty'")
    Integer countEmptySeats(@Param("branch") Long branchId);

    @Query(value = "SELECT SUM(payment) AS student_payment, SUM(deposit) AS others FROM ( "
            + "SELECT SUM(amount) AS payment, 0 AS deposit FROM Student_Payment s WHERE s.date BETWEEN :from and :to and s.student IN("
            + "SELECT e.id from Student e WHERE e.branch = :branch) UNION ALL "
            + "SELECT 0 AS payment, SUM(amount) AS deposit FROM Deposit d WHERE d.date BETWEEN :from and :to and d.branch = :branch"
            + ") AS income_list", nativeQuery = true)
    String getCategorizedIncomeValues(@Param("from") String from, @Param("to") String to, @Param("branch") Long branchId);

    @Query(value = "SELECT SUM(bazar) AS bazars, SUM(ebill) AS ebills,  SUM(salary) AS salaries,  SUM(bill) AS bills,  SUM(cost) AS costs FROM("
            + "SELECT SUM(total) AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, 0 AS cost FROM Bazar b WHERE b.date BETWEEN :from and :to and b.branch = :branch "
            + "UNION ALL "
            + "SELECT 0 AS bazar, SUM(total) AS ebill, 0 AS salary, 0 AS bill, 0 AS cost FROM Electricity_Bills b  WHERE b.date BETWEEN :from and :to and b.branch = :branch "
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, SUM(amount) AS salary, 0 AS bill, 0 AS cost FROM Salary b WHERE b.date BETWEEN :from and :to and b.employee in (SELECT id FROM employee WHERE branch = :branch AND is_active = true) "
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, 0 AS salary, SUM(amount) AS bill, 0 AS cost FROM Bills b  WHERE b.date BETWEEN :from and :to and b.branch = :branch "
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, SUM(amount) AS cost FROM Cost b WHERE b.date BETWEEN :from and :to and b.branch = :branch"
            + ") AS income_list", nativeQuery = true)
    String countCategorizedCostValues(@Param("from") String startDate, @Param("to") String endDate, @Param("branch") Long branchId);

    @Query(value = "SELECT Month_name, (SUM(bazar) + SUM(ebill) + SUM(salary) +  SUM(bill) +  SUM(cost)) as total FROM( "
            + "SELECT MONTHNAME(date) AS Month_name, SUM(total) AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, 0 AS cost FROM Bazar b WHERE b.date BETWEEN :from and :to and b.branch = :branch GROUP BY MONTHNAME(date) "
            + "UNION ALL "
            + "SELECT MONTHNAME(date) AS Month_name, 0 AS bazar, SUM(total) AS ebill, 0 AS salary, 0 AS bill, 0 AS cost FROM Electricity_Bills b  WHERE b.date BETWEEN :from and :to and b.branch = :branch GROUP BY MONTHNAME(date) "
            + "UNION ALL "
            + "SELECT MONTHNAME(date) AS Month_name, 0 AS bazar, 0 AS ebill, SUM(amount) AS salary, 0 AS bill, 0 AS cost FROM Salary b WHERE b.date BETWEEN :from and :to and b.employee in (SELECT id FROM employee WHERE branch = :branch AND is_active = true) GROUP BY MONTHNAME(date) "
            + "UNION ALL "
            + "SELECT MONTHNAME(date) AS Month_name, 0 AS bazar, 0 AS ebill, 0 AS salary, SUM(amount) AS bill, 0 AS cost FROM Bills b  WHERE b.date BETWEEN :from and :to and b.branch = :branch GROUP BY MONTHNAME(date) "
            + "UNION ALL "
            + "SELECT MONTHNAME(date) AS Month_name, 0 AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, SUM(amount) AS cost FROM Cost b WHERE b.date BETWEEN :from and :to and b.branch = :branch GROUP BY MONTHNAME(date) "
            + ") AS income_list GROUP BY Month_Name ORDER BY str_to_date(Month_Name,'%M')", nativeQuery = true)
    Object[][] getMonthlyCostValues(@Param("from") String startDate, @Param("to") String endDate, @Param("branch") Long branchId);

    @Query(value = "SELECT Month_name, (SUM(fees) + SUM(deposits)) as total FROM( "
            + "SELECT MONTHNAME(date) AS Month_name, SUM(amount) AS fees, 0 AS deposits FROM Student_Payment b WHERE b.date BETWEEN :from and :to and student IN(SELECT e.id from Student e WHERE e.branch = :branch) GROUP BY MONTHNAME(date) "
            + "UNION ALL "
            + "SELECT MONTHNAME(date) AS Month_name, SUM(amount) AS fees, 0 AS deposits FROM Deposit b  WHERE b.date BETWEEN :from and :to and b.branch = :branch GROUP BY MONTHNAME(date)"
            + ") AS income_list GROUP BY Month_Name ORDER BY str_to_date(Month_Name,'%M')", nativeQuery = true)
    Object[][] getMonthlyIncomeValues(@Param("from") String startDate, @Param("to") String endDate, @Param("branch") Long branchId);

    @Query(value = "SELECT SUM(bazar) AS bazars, SUM(ebill) AS ebills,  SUM(salary) AS salaries,  SUM(bill) AS bills,  SUM(cost) AS costs, SUM(deposits) as deposit, SUM(fees) as fee, ((SUM(deposits)+SUM(fees))-(SUM(bazar)+SUM(ebill)+SUM(salary)+SUM(bill)+SUM(cost))) as profit "
            + "FROM("
            + "SELECT SUM(total) AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, 0 AS cost, 0 AS fees, 0 AS deposits FROM Bazar b WHERE b.date BETWEEN :from and :to and b.branch = :branch "
            + "UNION ALL "
            + "SELECT 0 AS bazar, SUM(total) AS ebill, 0 AS salary, 0 AS bill, 0 AS cost, 0 AS fees, 0 AS deposits FROM Electricity_Bills b  WHERE b.date BETWEEN :from and :to and b.branch = :branch "
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, SUM(amount) AS salary, 0 AS bill, 0 AS cost, 0 AS fees, 0 AS deposits FROM Salary b WHERE b.date BETWEEN :from and :to and b.employee in (SELECT id FROM employee WHERE branch = :branch AND is_active = true) "
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, 0 AS salary, SUM(amount) AS bill, 0 AS cost, 0 AS fees, 0 AS deposits FROM Bills b WHERE b.date BETWEEN :from and :to and b.branch = :branch "
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, SUM(amount) AS cost, 0 AS fees, 0 AS deposits FROM Cost b WHERE b.date BETWEEN :from and :to and b.branch = :branch"
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, 0 AS cost, SUM(amount) AS fees, 0 AS deposits FROM Student_Payment b WHERE b.date BETWEEN :from and :to and student IN(SELECT e.id from Student e WHERE e.branch = :branch)"
            + "UNION ALL "
            + "SELECT 0 AS bazar, 0 AS ebill, 0 AS salary, 0 AS bill, 0 AS cost, 0 AS fees, SUM(amount) AS deposits FROM Deposit b  WHERE b.date BETWEEN :from and :to and b.branch = :branch"
            + ") AS financial_statement", nativeQuery = true)
    Map<String, Long> getFinancialStatement(@Param("from") String startDate, @Param("to") String endDate, @Param("branch") Long branchId);

}
