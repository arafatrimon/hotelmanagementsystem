package com.idb.hmis.dao;

import com.idb.hmis.entity.Fees;
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
public interface FeesDao extends JpaRepository<Fees, Long> {

    final String SEARCH_CONSTRAINT = "(f.type LIKE %:param% OR f.amount LIKE %:param% OR f.description LIKE %:param%)";
    final String FIND_BY_BRANCH_QUERY = "SELECT f FROM Fees f WHERE f.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Fees> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Fees> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT DISTINCT f.type FROM Fees f WHERE f.branch.id = :branch")
    List<String> findFeesTypes(@Param("branch") Long branchId);

    @Query("SELECT f FROM Fees f WHERE f.id = ?1 and f.branch.id = ?2")
    Fees findVerifiedFees(Long feesId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Fees f WHERE f.id = ?1 and f.branch.id = ?2")
    Integer delete(Long feesId, Long branchId);
}
