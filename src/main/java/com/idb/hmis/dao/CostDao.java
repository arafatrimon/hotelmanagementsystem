package com.idb.hmis.dao;

import com.idb.hmis.entity.Cost;
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
public interface CostDao extends JpaRepository<Cost, Long> {

    final String SEARCH_CONSTRAINT = "(c.type LIKE %:param% OR c.amount LIKE %:param% OR c.month LIKE %:param% "
            + "OR c.year LIKE %:param% OR c.date LIKE %:param% OR c.description LIKE %:param%)";
    final String FILTER_CONSTRAINT = "c.type = :filter";
    final String DATE_CONSTRAINT = "(c.date BETWEEN :start AND :end)";
    final String FIND_BY_BRANCH_QUERY = "SELECT c FROM Cost c WHERE c.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_DATE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
    final String SEARCH_BY_DATE_QUERY = FIND_BY_DATE_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_FILTER_QUERY = FIND_BY_BRANCH_QUERY + " AND " + FILTER_CONSTRAINT + " AND " + DATE_CONSTRAINT;
    final String SEARCH_IN_FILTER_QUERY = FIND_BY_FILTER_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Cost> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Cost> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_BY_FILTER_QUERY)
    Page<Cost> filter(
            @Param("branch") Long branchId,
            @Param("filter") String filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_IN_FILTER_QUERY)
    Page<Cost> searchInFilter(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("filter") String filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(FIND_BY_DATE_QUERY)
    Page<Cost> findByDate(
            @Param("branch") Long branchId,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_BY_DATE_QUERY)
    Page<Cost> searchByDate(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query("SELECT DISTINCT c.type FROM Cost c WHERE c.branch.id = :id")
    List<String> findCostTypes(@Param("id") Long branchId);

    @Query("SELECT c FROM Cost c WHERE c.id = ?1 and c.branch.id = ?2")
    Cost findVerifiedCost(Long costId, Long branchId);

    @Query("SELECT SUM(amount) AS total FROM Cost b WHERE b.date BETWEEN ?1 and ?2 and b.branch.id = ?3")
    Long findAmount(String startDate, String endDate, Long branchId);
    
    @Transactional
    @Modifying
    @Query("Delete FROM Cost c WHERE c.id = ?1 and c.branch.id = ?2")
    Integer delete(Long costId, Long branchId);
}
