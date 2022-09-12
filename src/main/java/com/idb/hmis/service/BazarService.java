/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Bazar;
import com.idb.hmis.entity.BazarItems;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface BazarService {

    Bazar save(Bazar bazar);

    Page<Bazar> getByBranch(Long branchId, Pageable pageable);

    Page<Bazar> searchInBranch(Long branchId, String searchParam, Pageable pageable);

    List<BazarItems> getBazarItemNames(Long branchId);

    Bazar getVerifiedBazar(Long bazarId, Long branchId);

    Page<Bazar> filter(Long branchId, String searchParam, String filterText, String startDate, String endDate, Pageable page);

    Integer delete(Long bazarId, Long branchId);
}
