/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.BranchGalary;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface BranchGalaryService {

    String save(List<MultipartFile> files, Long branchId);

    String update(BranchGalary galary, MultipartFile file);

    BranchGalary getVerifiedBranchGalary(Long photoId, Long branchId);

    Page<BranchGalary> getBranchGalary(Long  branchId, String searchParam, Pageable pageable);

    Integer delete(Long branchGalaryId, Long branchId);
}
