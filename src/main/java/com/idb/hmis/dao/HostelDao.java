package com.idb.hmis.dao;

import com.idb.hmis.entity.Hostel;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HostelDao extends JpaRepository<Hostel, Long> {

    @Query("SELECT h FROM Hostel h WHERE h.admin.id = :id")
    List<Hostel> findByAdminId(@Param("id") Long id);

    boolean existsByName(String name);

    boolean existsBySlogan(String slogan);

    boolean existsByWebsite(String website);

    @Query("SELECT h FROM Hostel h WHERE h.id = ?1 and h.admin.id = ?2")
    Hostel findVerifiedHostel(Long hostelId, Long adminId);

    @Query("SELECT h.logo FROM Hostel h WHERE h.id = ?1 and h.admin.id = ?2")
    String findLogo(Long hostelId, Long adminId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Hostel SET logo = ? where id = ? AND admin = ?", nativeQuery = true)
    Integer updateLogo(String logo, Long hostelId, Long adminId);

    @Transactional
    @Modifying
    @Query("Delete FROM Hostel h WHERE h.id = ?1 and h.admin.id = ?2")
    Integer delete(Long hostelId, Long adminId);
}
