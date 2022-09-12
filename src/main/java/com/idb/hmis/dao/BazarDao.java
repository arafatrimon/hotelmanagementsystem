package com.idb.hmis.dao;

import com.idb.hmis.entity.Bazar;
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
public interface BazarDao extends JpaRepository<Bazar, Long> {

    final String SEARCH_CONSTRAINT = "(b.bazarItems.name LIKE %:param% OR b.quantity LIKE %:param% OR b.untitPrice LIKE %:param% "
            + "OR b.total LIKE %:param% OR b.date LIKE %:param%)";
    final String FILTER_CONSTRAINT = "b.bazarItems.id = :filter";
    final String DATE_CONSTRAINT = "(b.date BETWEEN :start AND :end)";
    final String FIND_BY_BRANCH_QUERY = "SELECT b FROM Bazar b WHERE b.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_DATE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
    final String SEARCH_BY_DATE_QUERY = FIND_BY_DATE_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_FILTER_QUERY = FIND_BY_BRANCH_QUERY + " AND " + FILTER_CONSTRAINT + " AND " + DATE_CONSTRAINT;
    final String SEARCH_IN_FILTER_QUERY = FIND_BY_FILTER_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Bazar> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Bazar> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_BY_FILTER_QUERY)
    Page<Bazar> filter(
            @Param("branch") Long branchId,
            @Param("filter") Long filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_IN_FILTER_QUERY)
    Page<Bazar> searchInFilter(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("filter") Long filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(FIND_BY_DATE_QUERY)
    Page<Bazar> findByDate(
            @Param("branch") Long branchId,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_BY_DATE_QUERY)
    Page<Bazar> searchByDate(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query("SELECT b FROM Bazar b WHERE b.id = ?1 and b.branch.id = ?2")
    Bazar findVerifiedBazar(Long bazarId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM Bazar b WHERE b.id = ?1 and b.branch.id = ?2")
    Integer delete(Long bazarId, Long branchId);
}
