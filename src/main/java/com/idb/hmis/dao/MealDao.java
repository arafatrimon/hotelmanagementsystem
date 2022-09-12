package com.idb.hmis.dao;

import com.idb.hmis.entity.Meal;
import com.idb.hmis.entity.Student;
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
public interface MealDao extends JpaRepository<Meal, Long> {

    final String SEARCH_CONSTRAINT = "(m.student.studentId LIKE %:param% OR m.student.name LIKE %:param% OR m.breakfast LIKE %:param% OR m.lunch LIKE %:param% OR "
            + "m.supper LIKE %:param% OR m.rate LIKE %:param% OR m.date LIKE %:param%)";
    final String DATE_CONSTRAINT = "m.date = CURDATE()";
    final String FIND_BY_BRANCH_QUERY = "SELECT m FROM Meal m WHERE m.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_TODAYS_MEALS = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
    final String SEARCH_TODAYS_MEAL = FIND_TODAYS_MEALS + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Meal> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Meal> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_TODAYS_MEALS)
    Page<Meal> findTodaysMeals(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_TODAYS_MEAL)
    Page<Meal> searchTodaysMeals(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT m FROM Meal m WHERE m.id = :id AND m.branch.id = :branch")
    Meal findVerifiedMeal(
            @Param("id") Long mealId,
            @Param("branch") Long branchId);

    @Query("SELECT NEW Student(s.id, s.studentId, s.name) FROM Student s "
            + "WHERE s.branch.id = :id AND s.id NOT IN("
            + "SELECT m.student.id from Meal m where m.branch.id = :id AND m.date = CURDATE()"
            + ")")
    List<Student> findUnhandledStudents(@Param("id") Long branchId);

    @Query("SELECT NEW Student(s.id, s.studentId, s.name) FROM Student s WHERE s.id = ?1 and s.branch.id = ?2")
    Student findStudent(Long studentId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Meal m WHERE m.id = ?1 AND m.branch.id = ?2")
    Integer delete(Long mealId, Long branchId);

    @Query("SELECT CASE WHEN COUNT(m.student.id)> 0 THEN true ELSE false END FROM Meal m "
            + "WHERE m.student.id = :student AND m.date = CURDATE()")
    boolean alreadyExists(@Param("student") Long studentId);
}
