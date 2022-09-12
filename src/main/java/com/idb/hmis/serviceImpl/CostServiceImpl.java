package com.idb.hmis.serviceImpl;

import com.idb.hmis.entity.Cost;
import com.idb.hmis.service.CostService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.dao.CostDao;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class CostServiceImpl implements CostService {

    @Autowired
    CostDao costDao;

    @Override
    public Cost save(Cost cost) {
        return this.costDao.save(cost);
    }

    @Override
    public Page<Cost> getByBranch(Long  branchId, Pageable pageable) {
        return this.costDao.findByBranchId(branchId, pageable);
    }

    @Override
    public Page<Cost> searchInBranch(Long  branchId, String searchParam, Pageable pageable) {
        return this.costDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Page<Cost> filter(Long  branchId, String searchParam, String filterText, String startDate, String endDate, Pageable pageableable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        boolean noFilter = filterText == null || filterText.isEmpty();
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
        if (noFilter) {
            if (noSearchParam) {
                return this.costDao.findByDate(branchId, start, end, pageableable);
            }
            return this.costDao.searchByDate(branchId, searchParam, start, end, pageableable);
        } else {
            if (noSearchParam) {
                return this.costDao.filter(branchId, filterText, start, end, pageableable);
            }
            return this.costDao.searchInFilter(branchId, searchParam, filterText, start, end, pageableable);
        }
    }

    @Override
    public Cost getVerifiedCost(Long costId, Long branchId) {
        return this.costDao.findVerifiedCost(costId, branchId);
    }

    @Override
    public Integer delete(Long costId, Long branchId) {
        return this.costDao.delete(costId, branchId);
    }

    @Override
    public List<String> getCostTypes(Long branchId) {
        return this.costDao.findCostTypes(branchId);
    }

}
