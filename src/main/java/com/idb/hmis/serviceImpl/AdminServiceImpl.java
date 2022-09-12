package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.AdminDao;
import com.idb.hmis.entity.Admin;
import com.idb.hmis.entity.User;
import com.idb.hmis.service.AdminService;
import com.idb.hmis.service.UserService;
import com.idb.hmis.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Override
    public String register(Admin admin, User user, MultipartFile photo) {
        String status = this.alreadyExists(admin);
        if (status != null) {
            return status;
        }
        status = this.userService.register(user, "ADMIN");
        if (!status.equals("1")) return status;
        String photoTitle = this.fileService.store(photo);
        admin.setPhoto(photoTitle);
        admin.setId(null);
        admin.setIsActive(true);
        this.adminDao.save(admin);
        return (photoTitle != null) ? "1" : "0";
    }

    @Override
    public Admin getByUsername(String username) {
        return this.adminDao.findByUsername(username);
    }

    @Override
    public String updatePhoto(MultipartFile photo, String username) {
        String photoTitle = this.fileService.store(photo);
        if (photoTitle == null) {
            return "Something went wrong. Update failed.";
        }
        String existingPhoto = this.adminDao.findPhoto(username);
        Integer count = this.adminDao.updateProfilePhoto(photoTitle, username);
        if (count == 1) {
            this.fileService.delete(existingPhoto);
        }
        return photoTitle;
    }

    @Override
    public Admin update(Admin admin, MultipartFile photo) {
        Admin currentAdmin = this.adminDao.findByUsername(admin.getUsername());
        admin.setId(currentAdmin.getId());
        admin.setIsActive(currentAdmin.getIsActive());
        String previousPhoto = currentAdmin.getPhoto();
        String newPhoto = fileService.store(photo);
        if (newPhoto == null) {
            admin.setPhoto(previousPhoto);
        } else {
            admin.setPhoto(newPhoto);
            this.fileService.delete(previousPhoto);
        }
        return this.adminDao.save(admin);
    }

    private String alreadyExists(Admin admin) {
        if (this.adminDao.existsByUsername(admin.getUsername())) {
            return "Failed to register. User already exists";
        }
        if (this.adminDao.existsByPhone(admin.getPhone())) {
            return "Failed to register. Phone number already used";
        }
        if (this.adminDao.existsByNationalId(admin.getNationalId())) {
            return "Failed to register. NID number already used";
        }
        return null;
    }

}
