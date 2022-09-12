package com.idb.hmis.dao;

import com.idb.hmis.entity.HostelGalary;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelGalaryDao extends JpaRepository<HostelGalary, Long> {

    final String SEARCH_CONSTRAINT = "h.title LIKE %:param%";
    final String FIND_BY_BRANCH_QUERY = "SELECT h FROM HostelGalary h WHERE h.hostel.id = :hostel";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<HostelGalary> findByHostelId(@Param("hostel") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<HostelGalary> searchInHostel(@Param("hostel") Long hostelId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT h FROM HostelGalary h WHERE h.id = ?1 and h.hostel.id = ?2")
    HostelGalary findVerifiedHostelGalary(Long hostelGalaryId, Long hostelId);

    @Transactional
    @Modifying
    @Query("Delete FROM HostelGalary h WHERE h.id = ?1 and h.hostel.id = ?2")
    Integer delete(Long hostelGalaryId, Long hostelId);
}
