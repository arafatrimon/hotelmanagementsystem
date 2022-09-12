package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.DepositDao;
import com.idb.hmis.entity.Deposit;
import com.idb.hmis.service.DepositService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class DepositServiceImpl implements DepositService {

    @Autowired
    DepositDao depositDao;

    @Override
    public String save(Deposit deposit) {
        deposit.setUsername(getUsername());
        if (deposit.getId() != null) {
            return this.update(deposit);
        }
        this.depositDao.save(deposit);
        return "Deposit saved successfully";
    }

    public String update(Deposit deposit) {
        Long depositId = deposit.getId();
        Long branchId = deposit.getBranch().getId();
        String message = "Invalid data found. Failed to update.";
        Deposit verifiedDeposit = this.depositDao.findVerifiedDeposit(depositId, branchId);
        if (verifiedDeposit != null) {
            this.depositDao.save(deposit);
            message = "Updated successfully";
        }
        return message;
    }

    @Override
    public Page<Deposit> getByBranch(Long branchId, Pageable pageable) {
        return this.depositDao.findByBranchId(branchId, pageable);
    }

    @Override
    public Page<Deposit> searchInBranch(Long branchId, String searchParam, Pageable pageable) {
        return this.depositDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Page<Deposit> filter(Long branchId, String searchParam, String startDate, String endDate, Pageable pageableable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
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

        if (searchParam == null || searchParam.isEmpty()) {
            return this.depositDao.findByDate(branchId, start, end, pageableable);
        }
        return this.depositDao.searchByDate(branchId, searchParam, start, end, pageableable);

    }

    @Override
    public Deposit getVerifiedDeposit(Long depositId, Long branchId) {
        return this.depositDao.findVerifiedDeposit(depositId, branchId);
    }

    @Override
    public Integer delete(Long depositId, Long branchId) {
        return this.depositDao.delete(depositId, branchId);
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
