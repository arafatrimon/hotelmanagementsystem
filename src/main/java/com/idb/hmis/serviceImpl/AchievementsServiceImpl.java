package com.idb.hmis.serviceImpl;

import com.idb.hmis.entity.Achievements;
import com.idb.hmis.entity.Hostel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.dao.AchievementsDao;
import com.idb.hmis.service.AchievementsService;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class AchievementsServiceImpl implements AchievementsService {

    @Autowired
    AchievementsDao achievementsDao;

    @Override
    public String save(Achievements achievements) {
        if (achievements.getId() != null) {
            return this.update(achievements);
        }
        this.achievementsDao.save(achievements);
        return "Achievement saved successfully";
    }

    public String update(Achievements achievements) {
        Long achievementsId = achievements.getId();
        Long hostelId = achievements.getHostel().getId();
        String message = "Invalid data found. Failed to update.";
        Achievements verifiedAchievements = this.achievementsDao.findVerifiedAchievements(achievementsId, hostelId);
        if (verifiedAchievements != null) {
            this.achievementsDao.save(achievements);
            message = "Updated successfully";
        }
        return message;
    }

    @Override
    public Page<Achievements> getAchievements(Long hostelId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.achievementsDao.findByHostelId(hostelId, pageable);
        }
        return this.achievementsDao.searchInHostel(hostelId, searchParam, pageable);
    }

    @Override
    public Achievements getVerifiedAchievements(Long achievementsId, Long hostelId) {
        return this.achievementsDao.findVerifiedAchievements(achievementsId, hostelId);
    }

    @Override
    public Integer delete(Long achievementsId, Long hostelId) {
        return this.achievementsDao.delete(achievementsId, hostelId);
    }

}
