package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.AttendanceDao;
import com.idb.hmis.entity.Attendance;
import com.idb.hmis.entity.Branch;
import com.idb.hmis.entity.Student;
import com.idb.hmis.service.AttendanceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceDao attendanceDao;

    @Override
    public String save(Attendance attendance) {
        Long branchId = attendance.getBranch().getId();
        Long studentId = attendance.getStudent().getId();
        boolean studentExists = this.attendanceDao.studentExists(studentId, branchId);
        if (!studentExists) {
            return null;
        }
        if (attendance.getId() != null) {
            return this.update(attendance);
        }
        Boolean exists = this.attendanceDao.alreadyExists(studentId, branchId);
        if (exists) {
            return "Attendance already given for today";
        }
        this.attendanceDao.save(attendance);
        return "Attendance entry successful";
    }

    public String update(Attendance attendance) {
        Attendance verifiedAttendance = this.attendanceDao.findVerifiedAttendance(attendance.getId(), attendance.getBranch().getId());
        if (verifiedAttendance != null) {
            this.attendanceDao.save(attendance);
            return "Update successful";
        }
        return null;
    }

    @Override
    public Page<Attendance> getAttendanceList(Long branchId, String searchParam, Pageable pageable) {
        if (searchParam == null || searchParam.isEmpty()) {
            return this.attendanceDao.findByBranchId(branchId, pageable);
        }
        return this.attendanceDao.searchInBranch(branchId, searchParam, pageable);
    }

    @Override
    public Attendance getVerifiedAttendance(Long attendanceId, Long branchId) {
        return this.attendanceDao.findVerifiedAttendance(attendanceId, branchId);
    }

    @Override
    public Integer delete(Long attendanceId, Long branchId) {
        return this.attendanceDao.delete(attendanceId, branchId);
    }

    @Override
    public List<Student> getUnhandledStudents(Long branchId) {
        return this.attendanceDao.findUnhandledStudents(branchId);
    }
//
//    @Override
//    public boolean alreadyExists(Long studentId) {
//        return this.attendanceDao.alreadyExists(studentId);
//    }

}
