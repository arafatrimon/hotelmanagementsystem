package com.idb.hmis.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "food_menu",
         catalog = "hmis"
)
public class FoodMenu implements java.io.Serializable {

    private Long id;
    private Branch branch;
    private String days;
    private String breakfast;
    private String lunch;
    private String supper;

    public FoodMenu() {
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

    @Column(name = "days", nullable = false)
    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Column(name = "breakfast")
    public String getBreakfast() {
        return this.breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    @Column(name = "lunch")
    public String getLunch() {
        return this.lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    @Column(name = "supper")
    public String getSupper() {
        return this.supper;
    }

    public void setSupper(String supper) {
        this.supper = supper;
    }

}
