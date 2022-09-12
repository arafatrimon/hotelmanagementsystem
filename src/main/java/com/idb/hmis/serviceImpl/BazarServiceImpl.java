package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.BazarDao;
import com.idb.hmis.dao.BazarItemsDao;
import com.idb.hmis.entity.Bazar;
import com.idb.hmis.entity.BazarItems;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.service.BazarService;
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
public class BazarServiceImpl implements BazarService {

    @Autowired
    BazarDao bazarDao;
    @Autowired
    BazarItemsDao bazarItemsDao;

    @Override
    public Bazar save(Bazar bazar) {
        return this.bazarDao.save(bazar);
    }

    @Override
    public Page<Bazar> getByBranch(Long branchId, Pageable pageable) {
        return this.bazarDao.findByBranchId(branchId, pageable);
    }

    @Override
    public Page<Bazar> searchInBranch(Long branchId, String searchParam, Pageable pageable) {
        return this.bazarDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Page<Bazar> filter(Long branchId, String searchParam, String filterText, String startDate, String endDate, Pageable pageableable) {
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
                return this.bazarDao.findByDate(branchId, start, end, pageableable);
            }
            return this.bazarDao.searchByDate(branchId, searchParam, start, end, pageableable);
        } else {
            Long bazarItemsId = Long.parseLong(filterText);
            if (noSearchParam) {
                return this.bazarDao.filter(branchId, bazarItemsId, start, end, pageableable);
            }
            return this.bazarDao.searchInFilter(branchId, searchParam, bazarItemsId, start, end, pageableable);
        }
    }

    @Override
    public Bazar getVerifiedBazar(Long bazarId, Long branchId) {
        return this.bazarDao.findVerifiedBazar(bazarId, branchId);
    }

    @Override
    public Integer delete(Long bazarId, Long branchId) {
        return this.bazarDao.delete(bazarId, branchId);
    }

    @Override
    public List<BazarItems> getBazarItemNames(Long branchId) {
        return this.bazarItemsDao.findBazarItemNames(branchId);
    }

}
