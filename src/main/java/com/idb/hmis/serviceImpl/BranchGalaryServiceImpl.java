package com.idb.hmis.serviceImpl;

import com.idb.hmis.entity.BranchGalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.idb.hmis.dao.BranchGalaryDao;
import com.idb.hmis.service.BranchGalaryService;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class BranchGalaryServiceImpl implements BranchGalaryService {

    @Autowired
    BranchGalaryDao branchGalaryDao;

    @Override
    public String save(List<MultipartFile> files, Long branchId) {
        BranchGalary branchGalary = null;
        byte[] bytes = null;
        String image = null;
        int length = files.size();
        try {
            while (length > 0) {
                MultipartFile photo = files.remove(0);
                String title = photo.getOriginalFilename();
                bytes = photo.getBytes();
                image = Base64.getEncoder().encodeToString(bytes);
                branchGalary = new BranchGalary(branchId, title, image);
                this.branchGalaryDao.save(branchGalary);
                length--;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return "Branch photo(s) saved successfully";
    }

    @Override
    public String update(BranchGalary galary, MultipartFile file) {
        BranchGalary branchGalary = this.branchGalaryDao.findVerifiedBranchGalary(galary.getId(), galary.getBranch().getId());
        if (branchGalary != null) {
            byte[] bytes = null;
            String image = null;
            try {
                bytes = file.getBytes();
                image = Base64.getEncoder().encodeToString(bytes);
                branchGalary.setPhoto(image);
                this.branchGalaryDao.save(branchGalary);
            } catch (IOException e) {
                System.out.println(e);
            }
            return "Updated successfully";
        }
        return "Invalid data found. Failed to update.";
    }

    @Override
    public Page<BranchGalary> getBranchGalary(Long  branchId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.branchGalaryDao.findByBranchId(branchId, pageable);
        }
        return this.branchGalaryDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Integer delete(Long branchGalaryId, Long branchId) {
        return this.branchGalaryDao.delete(branchGalaryId, branchId);
    }

    @Override
    public BranchGalary getVerifiedBranchGalary(Long photoId, Long branchId) {
        return this.branchGalaryDao.findVerifiedBranchGalary(photoId, branchId);
    }

}
