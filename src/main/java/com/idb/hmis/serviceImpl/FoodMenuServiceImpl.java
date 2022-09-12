package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.FoodMenuDao;
import com.idb.hmis.entity.FoodMenu;
import com.idb.hmis.service.FoodMenuService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class FoodMenuServiceImpl implements FoodMenuService {

    @Autowired
    FoodMenuDao foodMenuDao;

    @Override
    public String save(FoodMenu foodMenu) {
        String days = foodMenu.getDays();
        Long branchId = foodMenu.getBranch().getId();
        String message = null;
        if (foodMenu.getId() != null) {
            return this.update(foodMenu);
        }
        FoodMenu existingFoodMenu = this.foodMenuDao.alreadyExists(days, branchId);
        if (existingFoodMenu != null) {
            message = "Menu for " + existingFoodMenu.getDays() + " already listed";
        } else {
            this.foodMenuDao.save(foodMenu);
            message = "Food menu for " + foodMenu.getDays() + " saved successfully";
        }
        return message;
    }

    @Override
    public Integer delete(Long foodMenuId, Long branchId) {
        return this.foodMenuDao.delete(foodMenuId, branchId);
    }

    @Override
    public List<FoodMenu> getByBranchId(Long id) {
        return this.foodMenuDao.findByBranchId(id);
    }

    @Override
    public FoodMenu getVerifiedFoodMenu(Long foodMenuId, Long branchId) {
        return this.foodMenuDao.findVerifiedFoodMenu(foodMenuId, branchId);
    }
    
    private String update(FoodMenu foodMenu){
        Long foodMenuId = foodMenu.getId();
        Long branchId = foodMenu.getBranch().getId();
        String message = "Invalid data found. Failed to update.";
        FoodMenu verifiedFoodMenu = this.getVerifiedFoodMenu(foodMenuId, branchId);
        if (verifiedFoodMenu != null) {
            this.foodMenuDao.save(foodMenu);
            message = "Updated successfully";
        }
        return message;
    }

}
