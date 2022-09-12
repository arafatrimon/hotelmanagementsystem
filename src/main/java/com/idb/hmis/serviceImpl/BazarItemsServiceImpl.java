package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.BazarItemsDao;
import com.idb.hmis.entity.BazarItems;
import com.idb.hmis.service.BazarItemsService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class BazarItemsServiceImpl implements BazarItemsService {

    @Autowired
    BazarItemsDao bazarItemsDao;

    @Override
    public BazarItems save(BazarItems bazarItems) {
        return this.bazarItemsDao.save(bazarItems);        
    }

    @Override
    public Integer delete(Long bazarItemsId, Long branchId) {
        return this.bazarItemsDao.delete(bazarItemsId, branchId);
    }

    @Override
    public List<BazarItems> getByBranchId(Long id) {
        return this.bazarItemsDao.findByBranchId(id);
    }

    @Override
    public BazarItems getVerifiedBazarItems(Long bazarItemsId, Long branchId) {
        return this.bazarItemsDao.findVerifiedBazarItems(bazarItemsId, branchId).orElse(null);
    }

    @Override
    public BazarItems alreadyExists(BazarItems bazarItems) {
        String bazarItemsName = bazarItems.getName();
        Long branchId = bazarItems.getBranch().getId();
        return this.bazarItemsDao.alreadyExists(bazarItemsName, branchId);
    }
//
//    @Override
//    public List<BazarItems> getMCList(Long branchId) { 
//        return this.bazarItemsDao.findMCList(branchId);
//    }
}
