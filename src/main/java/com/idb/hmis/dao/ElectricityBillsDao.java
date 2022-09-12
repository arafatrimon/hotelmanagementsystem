package com.idb.hmis.dao;

import com.idb.hmis.entity.ElectricityBills;
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
public interface ElectricityBillsDao extends JpaRepository<ElectricityBills, Long> {

    final String SEARCH_CONSTRAINT = "(e.meterNo LIKE %:param% OR e.units LIKE %:param% OR e.month LIKE %:param% "
            + "OR e.year LIKE %:param% OR e.total LIKE %:param% OR e.date LIKE %:param%)";
    final String FILTER_CONSTRAINT = "e.meterNo = :filter";
    final String DATE_CONSTRAINT = "(e.date BETWEEN :start AND :end)";
    final String FIND_BY_BRANCH_QUERY = "SELECT e FROM ElectricityBills e WHERE e.branch.id = :branch";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_DATE_QUERY = FIND_BY_BRANCH_QUERY + " AND " + DATE_CONSTRAINT;
    final String SEARCH_BY_DATE_QUERY = FIND_BY_DATE_QUERY + " AND " + SEARCH_CONSTRAINT;
    final String FIND_BY_FILTER_QUERY = FIND_BY_BRANCH_QUERY + " AND " + FILTER_CONSTRAINT + " AND " + DATE_CONSTRAINT;
    final String SEARCH_IN_FILTER_QUERY = FIND_BY_FILTER_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<ElectricityBills> findByBranchId(@Param("branch") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<ElectricityBills> searchInBranch(@Param("branch") Long branchId, @Param("param") String searchParam, Pageable page);

    @Query(FIND_BY_FILTER_QUERY)
    Page<ElectricityBills> filter(
            @Param("branch") Long branchId,
            @Param("filter") String filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_IN_FILTER_QUERY)
    Page<ElectricityBills> searchInFilter(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("filter") String filterText,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(FIND_BY_DATE_QUERY)
    Page<ElectricityBills> findByDate(
            @Param("branch") Long branchId,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query(SEARCH_BY_DATE_QUERY)
    Page<ElectricityBills> searchByDate(
            @Param("branch") Long branchId,
            @Param("param") String searchParam,
            @Param("start") Date startDate,
            @Param("end") Date endDate,
            Pageable page);

    @Query("SELECT DISTINCT e.meterNo FROM ElectricityBills e WHERE e.branch.id = :id")
    List<String> findMeterNumbers(@Param("id") Long branchId);

    @Query("SELECT e FROM ElectricityBills e WHERE e.id = ?1 and e.branch.id = ?2")
    ElectricityBills findVerifiedElectricityBills(Long electricitybillsId, Long branchId);

    @Query("SELECT SUM(total) AS total FROM ElectricityBills b WHERE b.date BETWEEN ?1 and ?2 and b.branch.id = ?3")
    Long findAmount(String startDate, String endDate, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM ElectricityBills e WHERE e.id = ?1 and e.branch.id = ?2")
    Integer delete(Long electricitybillsId, Long branchId);
}
