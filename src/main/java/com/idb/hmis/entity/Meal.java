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
@Table(name = "meal",
        catalog = "hmis"
)
public class Meal implements java.io.Serializable {

    private Long id;
    private Branch branch;
    private Student student;
    private Long breakfast;
    private Long lunch;
    private Long supper;
    private Double rate;
    private Date date;

    public Meal() {
        breakfast = 1l;
        lunch = 1l;
        supper = 1l;
        date = new Date();
    }

    public Meal(Long id, Branch branch) {
        this.id = id;
        this.branch = branch;
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

    @Column(name = "breakfast")
    public Long getBreakfast() {
        return this.breakfast;
    }

    public void setBreakfast(Long breakfast) {
        this.breakfast = breakfast;
    }

    @Column(name = "lunch")
    public Long getLunch() {
        return this.lunch;
    }

    public void setLunch(Long lunch) {
        this.lunch = lunch;
    }

    @Column(name = "supper")
    public Long getSupper() {
        return this.supper;
    }

    public void setSupper(Long supper) {
        this.supper = supper;
    }

    @Column(name = "rate", precision = 7)
    public Double getRate() {
        return this.rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false, length = 10)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
