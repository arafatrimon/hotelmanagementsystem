package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.BranchDao;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.service.BranchService;
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
public class BranchServiceImpl implements BranchService {

    @Autowired
    BranchDao branchDao;
    @Autowired
    FileService fileService;

    @Override
    public String save(Branch branch, MultipartFile photo) {
        if (branch.getId() != null) {
            return this.update(branch, photo);
        }
        boolean alreadyExists = this.branchDao.alreadyExists(branch.getName(), branch.getHostel().getId());
        if (!alreadyExists) {
            String photoTitle = this.fileService.store(photo);
            branch.setPhoto(photoTitle);
            this.branchDao.save(branch);
            return "Branch data saved successfully";
        }
        return "Failed to save branch. Branch name already exists in hostel.";
    }

    @Override
    public int delete(Long branchId, Long hostelId) {
        Branch branch = this.branchDao.findVerifiedBranch(branchId, hostelId);
        boolean photoDeleted = this.fileService.delete(branch.getPhoto());
        int count = this.branchDao.delete(branchId, hostelId);
        return (count == 0) ? 0 : (photoDeleted) ? 11 : 1;
    }

    @Override
    public String updatePhoto(MultipartFile photo, Long branchId, Long hostelId) {
        String photoTitle = this.fileService.store(photo);
        if (photoTitle == null) {
            return "Something went wrong. Update failed";
        }
        String existingPhoto = this.branchDao.findPhoto(branchId, hostelId);
        Integer count = this.branchDao.updatePhoto(photoTitle, branchId, hostelId);
        if (count == 1) {
            this.fileService.delete(existingPhoto);
        }
        return photoTitle;
    }

    private String update(Branch branch, MultipartFile photo) {
        Long branchId = branch.getId();
        Long hostelId = branch.getHostel().getId();
        Branch verifiedBranch = this.branchDao.findVerifiedBranch(branchId, hostelId);
        if (verifiedBranch == null) {
            return "Invalid data found. Failed to update.";
        }
        String previousPhoto = verifiedBranch.getPhoto();
        String newPhoto = fileService.store(photo);
        if (newPhoto == null) {
            branch.setPhoto(previousPhoto);
        } else {
            branch.setPhoto(newPhoto);
            this.fileService.delete(previousPhoto);
        }
        this.branchDao.save(branch);
        return "Branch data updated successfully";
    }

    @Override
    public List<Branch> getByHostelId(Long id) {
        return this.branchDao.findByHostelId(id);
    }

    @Override
    public Branch getVerifiedBranch(Long branchId, Long hostelId) {
        return this.branchDao.findVerifiedBranch(branchId, hostelId);
    }

}
