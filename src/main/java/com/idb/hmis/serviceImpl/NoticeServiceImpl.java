package com.idb.hmis.serviceImpl;

import com.idb.hmis.entity.Notice;
import com.idb.hmis.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.dao.NoticeDao;
import com.idb.hmis.service.NoticeService;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeDao noticeDao;

    @Override
    public String save(Notice notice) {
        if (notice.getId() != null) {
            return this.update(notice);
        }
        this.noticeDao.save(notice);
        return "Notice saved successfully";
    }

    public String update(Notice notice) {
        Long noticeId = notice.getId();
        Long branchId = notice.getBranch().getId();
        String message = "Invalid data found. Failed to update.";
        Notice verifiedNotice = this.noticeDao.findVerifiedNotice(noticeId, branchId);
        if (verifiedNotice != null) {
            this.noticeDao.save(notice);
            message = "Updated successfully";
        }
        return message;
    }

    @Override
    public Page<Notice> getNotice(Long  branchId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.noticeDao.findByBranchId(branchId, pageable);
        }
        return this.noticeDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Notice getVerifiedNotice(Long noticeId, Long branchId) {
        return this.noticeDao.findVerifiedNotice(noticeId, branchId);
    }

    @Override
    public Integer delete(Long noticeId, Long branchId) {
        return this.noticeDao.delete(noticeId, branchId);
    }

}
