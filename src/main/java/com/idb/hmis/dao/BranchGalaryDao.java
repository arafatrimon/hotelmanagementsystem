package com.idb.hmis.dao;

import com.idb.hmis.entity.BranchGalary;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchGalaryDao extends JpaRepository<BranchGalary, Long> {

    final String SEARCH_CONSTRAINT = "b.title LIKE %:param%";
    final String FIND_BY_BRANCH_QUERY = "SELECT b FROM BranchGalary b WHERE b.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<BranchGalary> findByBranchId(@Param("branch") Long branchId, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<BranchGalary> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT b FROM BranchGalary b WHERE b.id = ?1 and b.branch.id = ?2")
    BranchGalary findVerifiedBranchGalary(Long branchGalaryId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM BranchGalary b WHERE b.id = ?1 and b.branch.id = ?2")
    Integer delete(Long branchGalaryId, Long branchId);
}
