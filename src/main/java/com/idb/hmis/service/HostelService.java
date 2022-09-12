/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Hostel;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface HostelService {

    String save(Hostel hostel, MultipartFile photo);
    
    String updateLogo(MultipartFile photo, Long hostelId, Long adminId);

    List<Hostel> getByAdminId(Long id);

    Hostel getVerifiedHostel(Long hostelId, Long adminId);

    Integer delete(Long hostelId, Long adminId);
}
