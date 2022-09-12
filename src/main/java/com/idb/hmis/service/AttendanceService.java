package com.idb.hmis.service;

import com.idb.hmis.entity.Attendance;
import com.idb.hmis.entity.Student;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttendanceService {

    String save(Attendance attendance);

    Page<Attendance> getAttendanceList(Long branchId, String searchParam, Pageable pageable);

//    boolean alreadyExists(Long studentId);

    List<Student> getUnhandledStudents(Long branchId);

    Attendance getVerifiedAttendance(Long attendanceId, Long branchId);

    Integer delete(Long attendanceId, Long branchId);
}
