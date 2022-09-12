/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.Branch;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface BranchService {

    String save(Branch branch, MultipartFile photo);

    String updatePhoto(MultipartFile photo, Long branchId, Long hostelId);

    List<Branch> getByHostelId(Long id);

    Branch getVerifiedBranch(Long branchId, Long hostelId);

    int delete(Long branchId, Long hostelId);
}
