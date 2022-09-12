/*
 
 
 
 */
package com.idb.hmis.service;

import com.idb.hmis.entity.HostelGalary;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yasir Araphat
 */
public interface HostelGalaryService {

    String save(List<MultipartFile> files, Long hostelId);

    String update(HostelGalary galary, MultipartFile file);

    HostelGalary getVerifiedHostelGalary(Long photoId, Long hostelId);

    Page<HostelGalary> getHostelGalary(Long hostelId, String searchParam, Pageable pageable);

    Integer delete(Long hostelGalaryId, Long hostelId);
}
