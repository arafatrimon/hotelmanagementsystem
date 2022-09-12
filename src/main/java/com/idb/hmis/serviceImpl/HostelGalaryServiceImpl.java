package com.idb.hmis.serviceImpl;

import com.idb.hmis.entity.HostelGalary;
import com.idb.hmis.entity.Hostel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.dao.HostelGalaryDao;
import com.idb.hmis.service.HostelGalaryService;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class HostelGalaryServiceImpl implements HostelGalaryService {

    @Autowired
    HostelGalaryDao hostelGalaryDao;

    @Override
    public String save(List<MultipartFile> files, Long hostelId) {
        HostelGalary hostelGalary = null;
        byte[] bytes = null;
        String image = null;
        int length = files.size();
        try {
            while (length > 0) {
                MultipartFile photo = files.remove(0);
                String title = photo.getOriginalFilename();
                bytes = photo.getBytes();
                image = Base64.getEncoder().encodeToString(bytes);
                hostelGalary = new HostelGalary(hostelId, title, image);
                this.hostelGalaryDao.save(hostelGalary);
                length--;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return "Hostel photo(s) saved successfully";
    }

    @Override
    public String update(HostelGalary galary, MultipartFile file) {
        HostelGalary hostelGalary = this.hostelGalaryDao.findVerifiedHostelGalary(galary.getId(), galary.getHostel().getId());
        if (hostelGalary != null) {
            byte[] bytes = null;
            String image = null;
            try {
                bytes = file.getBytes();
                image = Base64.getEncoder().encodeToString(bytes);
                hostelGalary.setPhoto(image);
                this.hostelGalaryDao.save(hostelGalary);
            } catch (IOException e) {
                System.out.println(e);
            }
            return "Updated successfully";
        }
        return "Invalid data found. Failed to update.";
    }

    @Override
    public Page<HostelGalary> getHostelGalary(Long hostelId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.hostelGalaryDao.findByHostelId(hostelId, pageable);
        }
        return this.hostelGalaryDao.searchInHostel(hostelId, searchParam, pageable);
    }

    @Override
    public Integer delete(Long hostelGalaryId, Long hostelId) {
        return this.hostelGalaryDao.delete(hostelGalaryId, hostelId);
    }

    @Override
    public HostelGalary getVerifiedHostelGalary(Long photoId, Long hostelId) {
        return this.hostelGalaryDao.findVerifiedHostelGalary(photoId, hostelId);
    }

}
