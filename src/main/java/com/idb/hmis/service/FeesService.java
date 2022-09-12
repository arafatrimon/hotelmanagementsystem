/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Fees;
import com.idb.hmis.entity.Branch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface FeesService {

    Fees save(Fees fees);

    Page<Fees> getFees(Long  branchId, String searchParam, Pageable pageable);

    List<String> getFeesTypes(Long branchId);

    Fees getVerifiedFees(Long feesId, Long branchId);

    Integer delete(Long feesId, Long branchId);
}
