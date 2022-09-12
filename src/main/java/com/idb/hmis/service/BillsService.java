/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Bills;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface BillsService {

    Bills save(Bills bills);

    Page<Bills> getByBranch(Long  branchId, Pageable pageable);

    Page<Bills> searchInBranch(Long  branchId, String searchParam, Pageable pageable);

    List<String> getBillTypes(Long branchId);

    Bills getVerifiedBills(Long billsId, Long branchId);

    Page<Bills> filter(Long  branchId, String searchParam, String filterText, String startDate, String endDate, Pageable page);

    Integer delete(Long billsId, Long branchId);
}
