package com.idb.hmis.dao;

import com.idb.hmis.entity.Admin;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin, Long> {
    
    Admin findByUsername(String username);

    @Query("SELECT a.photo FROM Admin a WHERE a.username = :username")
    String findPhoto(@Param("username") String username);
    
    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByNationalId(String nationalId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Admin SET photo = ? WHERE username = ?", nativeQuery = true)
    Integer updateProfilePhoto(String photo, String username);

    @Transactional
    @Modifying
    @Query("Delete FROM Admin a WHERE a.username = ?2")
    Integer delete(String username);
}
