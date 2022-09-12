/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.FoodMenu;
import java.util.List;

/**
 *
 * @author Yasir Araphat
 */
public interface FoodMenuService {

    String save(FoodMenu foodMenu);
    
    List<FoodMenu> getByBranchId(Long branchId);

    FoodMenu getVerifiedFoodMenu(Long foodMenuId, Long branchId);

    Integer delete(Long foodMenuId, Long branchId);

}
