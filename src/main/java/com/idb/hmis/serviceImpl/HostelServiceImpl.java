package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.HostelDao;
import com.idb.hmis.entity.Hostel;
import com.idb.hmis.service.HostelService;
import com.idb.hmis.utils.FileService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class HostelServiceImpl implements HostelService {

    @Autowired
    HostelDao hostelDao;
    @Autowired
    FileService fileService;

    @Override
    public String save(Hostel hostel, MultipartFile logo) {
        if (hostel.getId() != null) {
            return this.update(hostel, logo);
        }
        String message = this.areadyExists(hostel);
        if (message == null) {
            String logoTitle = this.fileService.store(logo);
            hostel.setLogo(logoTitle);
            this.hostelDao.save(hostel);
            return "Hostel data saved successfully";
        }
        return message;
    }

    @Override
    public Integer delete(Long hostelId, Long adminId) {
        Hostel hostel = this.hostelDao.findVerifiedHostel(hostelId, adminId);
        boolean logoDeleted = this.fileService.delete(hostel.getLogo());
        Integer count = this.hostelDao.delete(hostelId, adminId);
        return (count == 0) ? 0 : (logoDeleted) ? 11 : 1;
    }

    @Override
    public String updateLogo(MultipartFile logo, Long hostelId, Long adminId) {
        String logoTitle = this.fileService.store(logo);
        if (logoTitle == null) {
            return "Something went wrong. Update failed.";
        }
        String existingLogo = this.hostelDao.findLogo(hostelId, adminId);
        Integer count = this.hostelDao.updateLogo(logoTitle, hostelId, adminId);
        if (count == 1) {
            this.fileService.delete(existingLogo);
        }
        return logoTitle;
    }

    private String update(Hostel hostel, MultipartFile logo) {
        Long hostelId = hostel.getId();
        Long adminId = hostel.getAdmin().getId();
        Hostel verifiedHostel = this.hostelDao.findVerifiedHostel(hostelId, adminId);
        if (verifiedHostel == null) {
            return "Invalid data found. Failed to update.";
        }
        String previousLogo = verifiedHostel.getLogo();
        String newLogo = fileService.store(logo);
        if (newLogo == null) {
            hostel.setLogo(previousLogo);
        } else {
            hostel.setLogo(newLogo);
            this.fileService.delete(previousLogo);
        }
        this.hostelDao.save(hostel);
        return "Hostel data updated successfully";
    }

    @Override
    public List<Hostel> getByAdminId(Long id) {
        return this.hostelDao.findByAdminId(id);
    }

    @Override
    public Hostel getVerifiedHostel(Long hostelId, Long adminId) {
        return this.hostelDao.findVerifiedHostel(hostelId, adminId);
    }

    private String areadyExists(Hostel hostel) {

        boolean existsByName = this.hostelDao.existsByName(hostel.getName());
        if (existsByName) {
            return "Failed to register. Name already exists";
        }
        boolean existsBySlogan = this.hostelDao.existsBySlogan(hostel.getSlogan());
        if (existsBySlogan) {
            return "Failed to register. Slogan already exists";
        }
        boolean existsByWebsite = this.hostelDao.existsByWebsite(hostel.getWebsite());
        if (existsByWebsite) {
            return "Failed to register. NID number already used";
        }
        return null;
    }

}
