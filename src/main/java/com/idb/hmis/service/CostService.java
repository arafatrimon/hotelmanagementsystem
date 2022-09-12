/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Cost;
import com.idb.hmis.entity.Branch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface CostService {

    Cost save(Cost cost);

    Page<Cost> getByBranch(Long  branchId, Pageable pageable);

    Page<Cost> searchInBranch(Long  branchId, String searchParam, Pageable pageable);

    List<String> getCostTypes(Long branchId);

    Cost getVerifiedCost(Long costId, Long branchId);

    Page<Cost> filter(Long  branchId, String searchParam, String filterText, String startDate, String endDate, Pageable page);

    Integer delete(Long costId, Long branchId);
}
