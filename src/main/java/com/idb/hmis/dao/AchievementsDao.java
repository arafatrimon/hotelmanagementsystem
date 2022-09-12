package com.idb.hmis.dao;

import com.idb.hmis.entity.Achievements;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementsDao extends JpaRepository<Achievements, Long> {

    final String SEARCH_CONSTRAINT = "(a.title LIKE %:param% OR a.description LIKE %:param% OR a.year LIKE %:param%)";
    final String FIND_BY_BRANCH_QUERY = "SELECT a FROM Achievements a WHERE a.hostel.id = :hostel";
    final String SEARCH_IN_BRANCH_QUERY = FIND_BY_BRANCH_QUERY + " AND " + SEARCH_CONSTRAINT;

    @Query(FIND_BY_BRANCH_QUERY)
    Page<Achievements> findByHostelId(@Param("hostel") Long id, Pageable page);

    @Query(SEARCH_IN_BRANCH_QUERY)
    Page<Achievements> searchInHostel(@Param("hostel") Long hostelId, @Param("param") String searchParam, Pageable page);

    @Query("SELECT a FROM Achievements a WHERE a.id = ?1 and a.hostel.id = ?2")
    Achievements findVerifiedAchievements(Long achievementsId, Long hostelId);

    @Transactional
    @Modifying
    @Query("Delete FROM Achievements a WHERE a.id = ?1 and a.hostel.id = ?2")
    Integer delete(Long achievementsId, Long hostelId);
}
