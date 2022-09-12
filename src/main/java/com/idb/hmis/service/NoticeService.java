/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Notice;
import com.idb.hmis.entity.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface NoticeService {

    String save(Notice notice);

    Page<Notice> getNotice(Long  branchId, String searchParam, Pageable pageable);

    Notice getVerifiedNotice(Long noticeId, Long branchId);

    Integer delete(Long noticeId, Long branchId);
}
