/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Admin;
import com.idb.hmis.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface AdminService {

    String register(Admin admin, User user, MultipartFile photo);

    Admin update(Admin admin, MultipartFile photo);
    
    String updatePhoto(MultipartFile photo, String username);

    Admin getByUsername(String username);

}
