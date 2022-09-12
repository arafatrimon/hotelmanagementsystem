/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Deposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface DepositService {

    String save(Deposit deposit);

    Page<Deposit> getByBranch(Long  branchId, Pageable pageable);

    Page<Deposit> searchInBranch(Long  branchId, String searchParam, Pageable pageable);

    Page<Deposit> filter(Long  branchId, String searchParam, String startDate, String endDate, Pageable page);

    Deposit getVerifiedDeposit(Long depositId, Long branchId);

    Integer delete(Long depositId, Long branchId);
}
