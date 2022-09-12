package com.idb.hmis.dao;

import com.idb.hmis.entity.FoodMenu;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodMenuDao extends JpaRepository<FoodMenu, Long> {

    @Query("SELECT f FROM FoodMenu f WHERE f.branch.id = :id")
    List<FoodMenu> findByBranchId(@Param("id") Long id);

    @Query("SELECT f FROM FoodMenu f WHERE f.days = :days and f.branch.id = :id")
    FoodMenu alreadyExists(@Param("days") String weekDay, @Param("id") Long branchId);

    @Query("SELECT f FROM FoodMenu f WHERE f.id = ?1 and f.branch.id = ?2")
    FoodMenu findVerifiedFoodMenu(Long foodMenuId, Long branchId);

    @Transactional
    @Modifying
    @Query("Delete FROM FoodMenu f WHERE f.id = ?1 and f.branch.id = ?2")
    Integer delete(Long foodMenuId, Long branchId);
}
