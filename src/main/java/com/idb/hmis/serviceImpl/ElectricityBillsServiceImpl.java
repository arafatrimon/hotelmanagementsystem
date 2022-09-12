package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.ElectricityBillsDao;
import com.idb.hmis.entity.ElectricityBills;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.service.ElectricityBillsService;

@Service
public class ElectricityBillsServiceImpl implements ElectricityBillsService {

    @Autowired
    ElectricityBillsDao electricityBillsDao;

    @Override
    public ElectricityBills save(ElectricityBills electricityBills) {
        return this.electricityBillsDao.save(electricityBills);
    }

    @Override
    public Page<ElectricityBills> getByBranch(Long  branchId, Pageable pageable) {
        return this.electricityBillsDao.findByBranchId(branchId, pageable);
    }

    @Override
    public Page<ElectricityBills> searchInBranch(Long  branchId, String searchParam, Pageable pageable) {
        return this.electricityBillsDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Page<ElectricityBills> filter(Long  branchId, String searchParam, String filterText, String startDate, String endDate, Pageable pageableable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        boolean noSearchParam = searchParam == null || searchParam.isEmpty();
        try {
            if (startDate == null || startDate.isEmpty()) {
                start = sdf.parse("1971-01-01");
            } else {
                start = sdf.parse(startDate);
            }
            if (endDate == null || endDate.isEmpty()) {
                end = new Date();
            } else {
                end = sdf.parse(endDate);
            }
        } catch (ParseException e) {
        }
        if (filterText == null || filterText.isEmpty()) {
            if (noSearchParam) {
                return this.electricityBillsDao.findByDate(branchId, start, end, pageableable);
            }
            return this.electricityBillsDao.searchByDate(branchId, searchParam, start, end, pageableable);
        } else {
            if (noSearchParam) {
                return this.electricityBillsDao.filter(branchId, filterText, start, end, pageableable);
            }
            return this.electricityBillsDao.searchInFilter(branchId, searchParam, filterText, start, end, pageableable);
        }
    }

    @Override
    public ElectricityBills getVerifiedElectricityBills(Long electricityBillsId, Long branchId) {
        return this.electricityBillsDao.findVerifiedElectricityBills(electricityBillsId, branchId);
    }

    @Override
    public Integer delete(Long electricityBillsId, Long branchId) {
        return this.electricityBillsDao.delete(electricityBillsId, branchId);
    }

    @Override
    public List<String> getMeterNumbers(Long branchId) {
        return this.electricityBillsDao.findMeterNumbers(branchId);
    }

}
