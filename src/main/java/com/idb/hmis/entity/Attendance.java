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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "attendance",
        catalog = "hmis"
)
public class Attendance implements java.io.Serializable {

    private Long id;
    private Branch branch;
    private Student student;
    private Date date;
    private Boolean isPresent;
    private String causeOfAbsence;

    public Attendance() {
        isPresent = true;
        date = new Date();
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
    @JoinColumn(name = "student", nullable = false)
    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false, length = 10)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "is_present")
    public Boolean getIsPresent() {
        return this.isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    @Column(name = "cause_of_absence", length = 65535)
    public String getCauseOfAbsence() {
        return this.causeOfAbsence;
    }

    public void setCauseOfAbsence(String causeOfAbsence) {
        this.causeOfAbsence = causeOfAbsence;
    }

}
