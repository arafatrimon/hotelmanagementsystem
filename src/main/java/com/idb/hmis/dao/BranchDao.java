package com.idb.hmis.dao;

import com.idb.hmis.entity.Branch;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchDao extends JpaRepository<Branch, Long> {

    @Query("SELECT b FROM Branch b WHERE b.hostel.id = :id")
    List<Branch> findByHostelId(@Param("id") Long id);

    @Query("SELECT case when count(b.name)> 0 then true else false end FROM Branch b WHERE b.name = :name and b.hostel.id = :id")
    Boolean alreadyExists(@Param("name") String branchName, @Param("id") Long hostelId);

    @Query("SELECT b FROM Branch b WHERE b.id = ?1 and b.hostel.id = ?2")
    Branch findVerifiedBranch(Long branchId, Long hostelId);

    @Query("SELECT b.photo FROM Branch b WHERE b.id = ?1 and b.hostel.id = ?2")
     String findPhoto(Long branchId, Long hostelId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Branch SET photo = ? where id = ? AND hostel = ?", nativeQuery = true)
    Integer updatePhoto(String photo, Long branchId, Long hostelId);

    @Transactional
    @Modifying
    @Query("Delete FROM Branch b WHERE b.id = ?1 and b.hostel.id = ?2")
    int delete(Long branchId, Long hostelId);
}
