package com.idb.hmis.service;

import com.idb.hmis.entity.ElectricityBills;
import com.idb.hmis.entity.Branch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElectricityBillsService {

    ElectricityBills save(ElectricityBills electricityBills);

    Page<ElectricityBills> getByBranch(Long  branchId, Pageable pageable);

    Page<ElectricityBills> searchInBranch(Long  branchId, String searchParam, Pageable pageable);

    List<String> getMeterNumbers(Long branchId);

    ElectricityBills getVerifiedElectricityBills(Long billsId, Long branchId);

    Page<ElectricityBills> filter(Long  branchId, String searchParam, String filterText, String startDate, String endDate, Pageable page);

    Integer delete(Long billsId, Long branchId);
}
