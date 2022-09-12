package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.MealDao;
import com.idb.hmis.entity.Meal;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Student;
import com.idb.hmis.service.MealService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    MealDao mealDao;

    @Override
    public String save(Meal meal) {
        Student student = this.mealDao.findStudent(meal.getStudent().getId(), meal.getBranch().getId());
        if (student == null) {
            return "Failed to update. Meal information is invalid";
        }
        if (meal.getId() != null) {
            return this.update(meal);
        }
        Long studentId = meal.getStudent().getId();
        Boolean mealExists = this.mealDao.alreadyExists(studentId);
        if (mealExists) {
            return "Meal info for the selected student already exists";
        }
        this.mealDao.save(meal);
        return "Meal for " + student + " saved successfully";
    }

    private String update(Meal meal) {
        Long mealId = meal.getId();
        Long branchId = meal.getBranch().getId();
        Meal verifiedMeal = this.mealDao.findVerifiedMeal(mealId, branchId);
        if (verifiedMeal != null) {
            this.mealDao.save(meal);
            return "Meal info updated successfully";
        }
        return "Failed to update. Meal information is invalid";
    }

    @Override
    public Page<Meal> getMealList(Long  branchId, String searchParam, boolean isToday, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            if (isToday) {
                return this.mealDao.findTodaysMeals(branchId, pageable);
            }
            return this.mealDao.findByBranchId(branchId, pageable);
        }
        if (isToday) {
            return this.mealDao.searchTodaysMeals(branchId, searchParam, pageable);
        }
        return this.mealDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Meal getVerifiedMeal(Long mealId, Long branchId) {
        return this.mealDao.findVerifiedMeal(mealId, branchId);
    }

    @Override
    public Boolean delete(Long mealId, Long branchId) {
        Meal meal = this.mealDao.findVerifiedMeal(mealId, branchId);
        if (meal != null) {
            Date mealDate = meal.getDate();
            Date today = new Date();
            this.mealDao.delete(mealId, branchId);
            return mealDate.equals(today);
        }
        return null;
    }

    @Override
    public List<Student> getUnhandledStudents(Long branchId) {
        return this.mealDao.findUnhandledStudents(branchId);
    }

}
