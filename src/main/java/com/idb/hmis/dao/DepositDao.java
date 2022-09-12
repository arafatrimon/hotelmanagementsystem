package com.idb.hmis.dao;

import com.idb.hmis.entity.Deposit;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositDao extends JpaRepository<Deposit, Long> {

    final String SEARCH_CONSTRAINT = "(d.month LIKE %:param% OR d.year LIKE %:param% OR d.amount LIKE %:param% OR d.date LIKE %:param%)";
    final String DATE_CONSTRAINT = "(d.date BETWEEN :start AND :end)";
    final String FIND_BY_BRANCH_QUERY = "SELECT d FROM Deposit d WHERE d.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_DATE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
    final String SEARCH_BY_DATE_QUERY = FIND_BY_DATE_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Deposit> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Deposit> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_BY_DATE_QUERY)
    Page<Deposit> findByDate(
            @Param("branch") Long branchId,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_BY_DATE_QUERY)
    Page<Deposit> searchByDate(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query("SELECT d FROM Deposit d WHERE d.id = ?1 and d.branch.id = ?2")
    Deposit findVerifiedDeposit(Long depositId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Deposit d WHERE d.id = ?1 and d.branch.id = ?2")
    Integer delete(Long depositId, Long branchId);
}
