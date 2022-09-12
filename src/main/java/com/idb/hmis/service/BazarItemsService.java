/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.BazarItems;
import java.util.List;

/**
 *
 * @author Yasir Araphat
 */
public interface BazarItemsService {

    BazarItems save(BazarItems bazarItems);

    List<BazarItems> getByBranchId(Long id);
    
//    List<BazarItems> getMCList(Long branchId);

    BazarItems getVerifiedBazarItems(Long bazarItemsId, Long branchId);

    Integer delete(Long bazarItemsId, Long branchId);

    BazarItems alreadyExists(BazarItems bazarItems);
}
