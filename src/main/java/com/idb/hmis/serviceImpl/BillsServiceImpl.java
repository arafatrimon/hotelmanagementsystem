package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.BillsDao;
import com.idb.hmis.entity.Bills;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.service.BillsService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class BillsServiceImpl implements BillsService {

    @Autowired
    BillsDao billsDao;

    @Override
    public Bills save(Bills bills) {
        return this.billsDao.save(bills);
    }

    @Override
    public Page<Bills> getByBranch(Long  branchId, Pageable pageable) {
        return this.billsDao.findByBranchId(branchId, pageable);
    }

    @Override
    public Page<Bills> searchInBranch(Long  branchId, String searchParam, Pageable pageable) {
        return this.billsDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Page<Bills> filter(Long  branchId, String searchParam, String filterText, String startDate, String endDate, Pageable pageableable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        boolean noFilter = filterText == null || filterText.isEmpty();
        boolean noSearchParam = searchParam == null || searchParam.isEmpty();
        boolean noStartDate = startDate == null || startDate.isEmpty();
        boolean noEndDate = endDate == null || endDate.isEmpty();
        try {
            if (noStartDate) {
                start = sdf.parse("1971-01-01");
            } else {
                start = sdf.parse(startDate);
            }
            if (noEndDate) {
                end = new Date();
            } else {
                end = sdf.parse(endDate);
            }
        } catch (ParseException e) {
        }
        if (noFilter) {
            if (noSearchParam) {
                return this.billsDao.findByDate(branchId, start, end, pageableable);
            }
            return this.billsDao.searchByDate(branchId, searchParam, start, end, pageableable);
        } else {
            if (noSearchParam) {
                return this.billsDao.filter(branchId, filterText, start, end, pageableable);
            }
            return this.billsDao.searchInFilter(branchId, searchParam, filterText, start, end, pageableable);
        }
    }

    @Override
    public Bills getVerifiedBills(Long billsId, Long branchId) {
        return this.billsDao.findVerifiedBills(billsId, branchId);
    }

    @Override
    public Integer delete(Long billsId, Long branchId) {
        return this.billsDao.delete(billsId, branchId);
    }

    @Override
    public List getBillTypes(Long branchId) {
        return this.billsDao.findBillTypes(branchId);
    }

}
