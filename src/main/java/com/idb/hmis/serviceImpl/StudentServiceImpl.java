package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.SeatDao;
import com.idb.hmis.dao.StudentDao;
import com.idb.hmis.entity.Student;
import com.idb.hmis.entity.Seat;
import com.idb.hmis.service.StudentService;
import com.idb.hmis.utils.FileService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDao studentDao;
    @Autowired
    SeatDao seatDao;
    @Autowired
    FileService fileService;

    @Override
    public String save(Student student, MultipartFile photo) {
        if (student.getId() != null) {
            return this.update(student, photo);
        }
        String message = this.alreadyExists(student);
        if (message == null) {
            String photoTitle = this.fileService.store(photo);
            student.setPhoto(photoTitle);
            Integer result = this.seatDao.bookSeat(student.getStudentId(), student.getSeat().getId(), student.getBranch().getId());
            if (result == 0) {
                return "Something went wrong while booking seat. Failed to admit student";
            }
            this.studentDao.save(student);
            return "Student data saved successfully";
        }
        return message;
    }

    @Override
    public Integer delete(Long studentId, Long branchId) {
        Student student = this.studentDao.findVerifiedStudent(studentId, branchId);
        int count = 0;
        if (student == null) return 0;
        if (student.getIsActive()) {
            Long seatId = student.getSeat().getId();
            count = this.seatDao.releaseSeat(seatId, branchId);
            if (count == 0)  return 100;
        }
        count = this.studentDao.delete(studentId, branchId);
        if (count == 1) {
            boolean photoDeleted = this.fileService.delete(student.getPhoto());
            return (photoDeleted) ? 11 : 1;
        }
        return 0;
    }

    @Override
    public String updatePhoto(MultipartFile photo, Long studentId, Long branchId) {
        String photoTitle = this.fileService.store(photo);
        if (photoTitle == null) {
            return "Something went wrong. Update failed";
        }
        String existingPhoto = this.studentDao.findPhoto(studentId, branchId);
        Integer count = this.studentDao.updatePhoto(photoTitle, studentId, branchId);
        if (count == 1) {
            this.fileService.delete(existingPhoto);
        }
        return photoTitle;
    }

    private String update(Student student, MultipartFile photo) {
        Long studentId = student.getId();
        Long branchId = student.getBranch().getId();
        Student verifiedStudent = this.studentDao.findVerifiedStudent(studentId, branchId);
        if (verifiedStudent == null) {
            return "Invalid data found. Failed to update.";
        }
        String previousPhoto = verifiedStudent.getPhoto();
        String newPhoto = fileService.store(photo);
        if (newPhoto == null) {
            student.setPhoto(previousPhoto);
        } else {
            student.setPhoto(newPhoto);
            this.fileService.delete(previousPhoto);
        }
        Long previousSeat = verifiedStudent.getSeat().getId();
        Long newSeat = student.getSeat().getId();
        Integer count;
        if (!previousSeat.equals(newSeat)) {
            count = this.seatDao.releaseSeat(previousSeat, branchId);
            if (count == 1) {
                count = this.seatDao.bookSeat(verifiedStudent.getStudentId(), newSeat, branchId);
                if (count == 0) {
                    this.seatDao.bookSeat(verifiedStudent.getStudentId(), previousSeat, branchId);
                    return "Something went wrong while updating seat information. Failed to update student";
                }
            }
        }
        this.studentDao.save(student);
        return "Student data updated successfully";
    }

    @Override
    public Page<Student> getStudents(Long branchId, boolean isActive, Pageable pageable) {
        return this.studentDao.findStudents(branchId, isActive, pageable);
    }

    @Override
    public Page<Student> searchStudents(Long branchId, String searchParam, boolean isActive, Pageable pageable) {
        return this.studentDao.searchStudents(branchId, searchParam, isActive, pageable);
    }

    @Override
    public Student getVerifiedStudent(Long studentId, Long branchId) {
        return this.studentDao.findVerifiedStudent(studentId, branchId);
    }

    @Override
    public Student getByUsername(String username) {
        return this.studentDao.findByUsername(username);
    }

    @Override
    public String deactivate(Long studentId, Long branchId) {
        Long seatId = this.studentDao.findSeat(studentId, branchId);
        Integer seatCount = (seatId != null) ? this.seatDao.releaseSeat(seatId, branchId) : 0;
        Integer studentCount = (seatCount > 0) ? this.studentDao.deactivate(studentId, branchId) : 0;
        return (studentCount > 0) ? "Successfully diactivated student" : "Something went wrong. Please try again or contact with admin.";
    }

    @Override
    public List<Seat> getEmptySeats(Long branchId) {
        return this.seatDao.findEmptySeats(branchId);
    }

    private String alreadyExists(Student student) {
        if (this.studentDao.existsByPhone(student.getPhone(), student.getBranch().getId())) {
            return "Failed to register. Phone number already used by a current student";
        }
        if (this.studentDao.existsByNationalId(student.getNationalId())) {
            return "Failed to register. NID number already used";
        }
        if (this.studentDao.existsByEmail(student.getEmail())) {
            return "Failed to register. Email address already used";
        }
        if (student.getPassportNo() != null && this.studentDao.existsByPassportNo(student.getPassportNo())) {
            return "Failed to register. Passport no. already exists";
        }
        if (this.studentDao.existsByStudentId(student.getStudentId(), student.getBranch().getId())) {
            return "Failed to register. Student Id already exists";
        }
        return null;
    }

}
