package com.idb.hmis.dao;

import com.idb.hmis.entity.BazarItems;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BazarItemsDao extends JpaRepository<BazarItems, Long> {

    @Query("SELECT b FROM BazarItems b WHERE b.branch.id = :id")
    List<BazarItems> findByBranchId(@Param("id") Long id);
    
    @Query("SELECT NEW BazarItems(b.id, b.name) FROM BazarItems b WHERE b.branch.id = :id")
    List<BazarItems> findBazarItemNames(@Param("id") Long id);
    
    @Query("SELECT b FROM BazarItems b WHERE b.name = :name and b.branch.id = :id")
    BazarItems alreadyExists(@Param("name") String bazarItemsName, @Param("id") Long branchId);

    @Query("SELECT b FROM BazarItems b WHERE b.id = ?1 and b.branch.id = ?2")
    Optional<BazarItems> findVerifiedBazarItems(Long bazarItemsId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM BazarItems b WHERE b.id = ?1 and b.branch.id = ?2")
    Integer delete(Long bazarItemsId, Long branchId);
}
