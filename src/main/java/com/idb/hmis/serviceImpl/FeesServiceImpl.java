package com.idb.hmis.serviceImpl;

import com.idb.hmis.entity.Fees;
import com.idb.hmis.entity.Branch;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.dao.FeesDao;
import com.idb.hmis.service.FeesService;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class FeesServiceImpl implements FeesService {

    @Autowired
    FeesDao feesDao;

    @Override
    public Fees save(Fees fees) {
        return this.feesDao.save(fees);
    }

    @Override
    public Page<Fees> getFees(Long  branchId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.feesDao.findByBranchId(branchId, pageable);
        }
        return this.feesDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Fees getVerifiedFees(Long feesId, Long branchId) {
        return this.feesDao.findVerifiedFees(feesId, branchId);
    }

    @Override
    public Integer delete(Long feesId, Long branchId) {
        return this.feesDao.delete(feesId, branchId);
    }

    @Override
    public List<String> getFeesTypes(Long branchId) {
        return this.feesDao.findFeesTypes(branchId);
    }

}
