package com.idb.hmis.service;

import com.idb.hmis.entity.Meal;
import com.idb.hmis.entity.Student;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealService {

    String save(Meal meal);

    Page<Meal> getMealList(Long  branchId, String searchParam, boolean isToday, Pageable pageable);

    List<Student> getUnhandledStudents(Long branchId);

    Meal getVerifiedMeal(Long mealId, Long branchId);

    Boolean delete(Long mealId, Long branchId);
}
