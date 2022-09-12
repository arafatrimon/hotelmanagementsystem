package com.idb.hmis.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "student",
        catalog = "hmis",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "phone")
            , @UniqueConstraint(columnNames = "national_id")
            , @UniqueConstraint(columnNames = "email")
            , @UniqueConstraint(columnNames = "passport_no")}
)
public class Student implements java.io.Serializable {

    private Long id;
    private Branch branch;
    private Seat seat;
    private String studentId;
    private String name;
    private String username;
    private String nameBangla;
    private String fatherName;
    private String motherName;
    private String parentOccupation;
    private String presentAddress;
    private String permanentAddress;
    private String localGuardian;
    private String relWithGuardian;
    private String guardianAddress;
    private String localGuardianPhone;
    private String phone;
    private String email;
    private Double sscGpa;
    private String sscGroup;
    private Long sscPassYear;
    private String school;
    private String presentInstitute;
    private String classTeacherName;
    private String classTeacherPhone;
    private String currentSubject;
    private String batchNo;
    private String gender;
    private Date dateOfBirth;
    private String bloodGroup;
    private String nationality;
    private String nationalId;
    private String passportNo;
    private String fatherPhone;
    private String motherPhone;
    private Double monthlyFee;
    private String photo;
    private Date admissionDate;
    private Boolean isActive;

    public Student() {
    }

    public Student(Long id, String studentId, String name) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
    }

    public Student(Long id, String studentId, String name, Double monthlyFee) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.monthlyFee = monthlyFee;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch", nullable = false)
    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat", nullable = false)
    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @Column(name = "student_id", nullable = false)
    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "name_bangla")
    public String getNameBangla() {
        return this.nameBangla;
    }

    public void setNameBangla(String nameBangla) {
        this.nameBangla = nameBangla;
    }

    @Column(name = "father_name", nullable = false)
    public String getFatherName() {
        return this.fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Column(name = "mother_name", nullable = false)
    public String getMotherName() {
        return this.motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    @Column(name = "parent_occupation")
    public String getParentOccupation() {
        return this.parentOccupation;
    }

    public void setParentOccupation(String parentOccupation) {
        this.parentOccupation = parentOccupation;
    }

    @Column(name = "present_address", length = 65535)
    public String getPresentAddress() {
        return this.presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    @Column(name = "permanent_address", length = 65535)
    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    @Column(name = "local_guardian", nullable = false)
    public String getLocalGuardian() {
        return this.localGuardian;
    }

    public void setLocalGuardian(String localGuardian) {
        this.localGuardian = localGuardian;
    }

    @Column(name = "rel_with_guardian")
    public String getRelWithGuardian() {
        return this.relWithGuardian;
    }

    public void setRelWithGuardian(String relWithGuardian) {
        this.relWithGuardian = relWithGuardian;
    }

    @Column(name = "guardian_address", length = 65535)
    public String getGuardianAddress() {
        return this.guardianAddress;
    }

    public void setGuardianAddress(String guardianAddress) {
        this.guardianAddress = guardianAddress;
    }

    @Column(name = "local_guardian_phone", nullable = false)
    public String getLocalGuardianPhone() {
        return this.localGuardianPhone;
    }

    public void setLocalGuardianPhone(String localGuardianPhone) {
        this.localGuardianPhone = localGuardianPhone;
    }

    @Column(name = "phone", unique = true, nullable = false)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "email", unique = true)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "ssc_gpa", precision = 3)
    public Double getSscGpa() {
        return this.sscGpa;
    }

    public void setSscGpa(Double sscGpa) {
        this.sscGpa = sscGpa;
    }

    @Column(name = "ssc_group")
    public String getSscGroup() {
        return this.sscGroup;
    }

    public void setSscGroup(String sscGroup) {
        this.sscGroup = sscGroup;
    }

    @Column(name = "ssc_pass_year")
    public Long getSscPassYear() {
        return this.sscPassYear;
    }

    public void setSscPassYear(Long sscPassYear) {
        this.sscPassYear = sscPassYear;
    }

    @Column(name = "school", nullable = false)
    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Column(name = "present_institute", nullable = false)
    public String getPresentInstitute() {
        return this.presentInstitute;
    }

    public void setPresentInstitute(String presentInstitute) {
        this.presentInstitute = presentInstitute;
    }

    @Column(name = "class_teacher_name")
    public String getClassTeacherName() {
        return this.classTeacherName;
    }

    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
    }

    @Column(name = "class_teacher_phone")
    public String getClassTeacherPhone() {
        return this.classTeacherPhone;
    }

    public void setClassTeacherPhone(String classTeacherPhone) {
        this.classTeacherPhone = classTeacherPhone;
    }

    @Column(name = "current_subject", nullable = false)
    public String getCurrentSubject() {
        return this.currentSubject;
    }

    public void setCurrentSubject(String currentSubject) {
        this.currentSubject = currentSubject;
    }

    @Column(name = "batch_no", nullable = false)
    public String getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Column(name = "gender", nullable = false, length = 8)
    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth", nullable = false, length = 10)
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "blood_group", nullable = false, length = 5)
    public String getBloodGroup() {
        return this.bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Column(name = "nationality", nullable = false)
    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Column(name = "national_id", unique = true, nullable = false)
    public String getNationalId() {
        return this.nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Column(name = "passport_no", unique = true)
    public String getPassportNo() {
        return this.passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    @Column(name = "father_phone", nullable = false)
    public String getFatherPhone() {
        return this.fatherPhone;
    }

    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }

    @Column(name = "mother_phone")
    public String getMotherPhone() {
        return this.motherPhone;
    }

    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }

    @Column(name = "monthly_fee", precision = 12)
    public Double getMonthlyFee() {
        return this.monthlyFee;
    }

    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    @Column(name = "photo")
    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "admission_date", length = 10)
    public Date getAdmissionDate() {
        return this.admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Column(name = "is_active")
    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "(" + studentId + ") " + name;
    }
}
