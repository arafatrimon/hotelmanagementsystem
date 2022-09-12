package com.idb.hmis.dao;

import com.idb.hmis.entity.Bills;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillsDao extends JpaRepository<Bills, Long> {

    final String SEARCH_CONSTRAINT = "(b.type LIKE %:param% OR b.slipNo LIKE %:param% OR b.billTo LIKE %:param% "
            + "OR b.payMethod LIKE %:param% OR b.month LIKE %:param% OR b.year LIKE %:param% OR b.amount LIKE %:param% "
            + "OR b.payment LIKE %:param% OR b.due LIKE %:param% OR b.date LIKE %:param%)";
    final String FILTER_CONSTRAINT = "b.type = :filter";
    final String DATE_CONSTRAINT = "(b.date BETWEEN :start AND :end)";
    final String FIND_BY_BRANCH_QUERY = "SELECT b FROM Bills b WHERE b.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_DATE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
    final String SEARCH_BY_DATE_QUERY = FIND_BY_DATE_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_FILTER_QUERY = FIND_BY_BRANCH_QUERY + " AND " + FILTER_CONSTRAINT + " AND " + DATE_CONSTRAINT;
    final String SEARCH_IN_FILTER_QUERY = FIND_BY_FILTER_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Bills> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Bills> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_BY_FILTER_QUERY)
    Page<Bills> filter(
            @Param("branch") Long branchId,
            @Param("filter") String filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_IN_FILTER_QUERY)
    Page<Bills> searchInFilter(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("filter") String filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(FIND_BY_DATE_QUERY)
    Page<Bills> findByDate(
            @Param("branch") Long branchId,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_BY_DATE_QUERY)
    Page<Bills> searchByDate(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query("SELECT DISTINCT b.type FROM Bills b WHERE b.branch.id = :id")
    List<String> findBillTypes(@Param("id") Long branchId);

    @Query("SELECT b FROM Bills b WHERE b.id = ?1 and b.branch.id = ?2")
    Bills findVerifiedBills(Long billsId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Bills b WHERE b.id = ?1 and b.branch.id = ?2")
    Integer delete(Long billsId, Long branchId);
}
