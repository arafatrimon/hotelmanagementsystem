/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Hostel;
import com.idb.hmis.entity.HostelInfo;

/**
 *
 * @author Yasir Araphat
 */
public interface HostelInfoService {

    HostelInfo save(HostelInfo hostelInfo);

    HostelInfo getByHostel(Long hostelId);

}
