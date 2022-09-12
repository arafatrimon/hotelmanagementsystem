package com.idb.hmis.dao;

import com.idb.hmis.entity.Notice;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeDao extends JpaRepository<Notice, Long> {

    final String SEARCH_CONSTRAINT = "(n.title LIKE %:param% OR n.description LIKE %:param% OR n.date LIKE %:param%)";
    final String FIND_BY_BRANCH_QUERY = "SELECT n FROM Notice n WHERE n.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Notice> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Notice> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT n FROM Notice n WHERE n.id = ?1 and n.branch.id = ?2")
    Notice findVerifiedNotice(Long noticeId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Notice n WHERE n.id = ?1 and n.branch.id = ?2")
    Integer delete(Long noticeId, Long branchId);
}
