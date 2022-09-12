package com.idb.hmis.service;

import com.idb.hmis.entity.Seat;
import com.idb.hmis.entity.Student;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    String save(Student student, MultipartFile photo);

    String updatePhoto(MultipartFile photo, Long studentId, Long branchId);

    Page<Student> getStudents(Long branchId, boolean isActive, Pageable pageable);

    Page<Student> searchStudents(Long branchId, String searchParam, boolean isActive, Pageable pageable);

    List<Seat> getEmptySeats(Long branchId);

    Student getVerifiedStudent(Long studentId, Long branchId);

    Student getByUsername(String username);

    Integer delete(Long studentId, Long branchId);

    String deactivate(Long studentId, Long branchId);
    
}
