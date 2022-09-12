
package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.HostelInfoDao;
import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.HostelInfo;
import com.idb.hmis.service.HostelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class HostelInfoServiceImpl implements  HostelInfoService {

    @Autowired
    HostelInfoDao hostelInfoDao;    

    @Override
    public HostelInfo getByHostel(Long hostelId) {
        Hostel hostel = new Hostel(hostelId);
        return this.hostelInfoDao.findByHostel(hostel).orElse(new HostelInfo(hostel));
    }

    @Override
    public HostelInfo save(HostelInfo hostelInfo) {
        return this.hostelInfoDao.save(hostelInfo);
    }

}
