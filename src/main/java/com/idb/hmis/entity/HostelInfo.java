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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "hostel_info",
        catalog = "hmis",
        uniqueConstraints = @UniqueConstraint(columnNames = "hostel")
)
public class HostelInfo implements java.io.Serializable {

    private Long id;
    private Hostel hostel;
    private String history;
    private String eduEnvironment;
    private String administration;
    private String security;
    private String entertainment;
    private String foodInfo;
    private String specialFacilities;
    private String instructions;
    private String facilities;
    private String galary;
    private String photo;

    public HostelInfo() {
    }

    public HostelInfo(Hostel hostel) {
        this.hostel = hostel;
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
    @JoinColumn(name = "hostel", unique = true, nullable = false)
    public Hostel getHostel() {
        return this.hostel;
    }

    public void setHostel(Hostel hostel) {
        this.hostel = hostel;
    }

    @Column(name = "history", length = 65535)
    public String getHistory() {
        return this.history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @Column(name = "edu_environment", length = 65535)
    public String getEduEnvironment() {
        return this.eduEnvironment;
    }

    public void setEduEnvironment(String eduEnvironment) {
        this.eduEnvironment = eduEnvironment;
    }

    @Column(name = "administration", length = 65535)
    public String getAdministration() {
        return this.administration;
    }

    public void setAdministration(String administration) {
        this.administration = administration;
    }

    @Column(name = "security", length = 65535)
    public String getSecurity() {
        return this.security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    @Column(name = "entertainment", length = 65535)
    public String getEntertainment() {
        return this.entertainment;
    }

    public void setEntertainment(String entertainment) {
        this.entertainment = entertainment;
    }

    @Column(name = "food_info", length = 65535)
    public String getFoodInfo() {
        return this.foodInfo;
    }

    public void setFoodInfo(String foodInfo) {
        this.foodInfo = foodInfo;
    }

    @Column(name = "special_facilities", length = 65535)
    public String getSpecialFacilities() {
        return this.specialFacilities;
    }

    public void setSpecialFacilities(String specialFacilities) {
        this.specialFacilities = specialFacilities;
    }

    @Column(name = "instructions", length = 65535)
    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Column(name = "facilities", length = 65535)
    public String getFacilities() {
        return this.facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Column(name = "galary", length = 65535)
    public String getGalary() {
        return this.galary;
    }

    public void setGalary(String galary) {
        this.galary = galary;
    }

    @Column(name = "photo")
    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
