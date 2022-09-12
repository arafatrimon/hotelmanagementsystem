package com.idb.hmis.service;

import com.idb.hmis.entity.Achievements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Yasir Araphat
 */
public interface AchievementsService {

    String save(Achievements achievements);

    Page<Achievements> getAchievements(Long hostelId, String searchParam, Pageable pageable);

    Achievements getVerifiedAchievements(Long achievementsId, Long hostelId);

    Integer delete(Long achievementsId, Long hostelId);
}
