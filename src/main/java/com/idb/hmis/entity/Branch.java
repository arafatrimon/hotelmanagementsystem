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
@Table(name = "branch",
        catalog = "hmis"
)
public class Branch implements java.io.Serializable {

    private Long id;
    private Hostel hostel;
    private String name;
    private String location;
    private String phone;
    private Long cash;
    private String photo;
    private Date estDate;

    public Branch() {
    }

    public Branch(Long id) {
        this.id = id;
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
    @JoinColumn(name = "hostel", nullable = false)
    public Hostel getHostel() {
        return this.hostel;
    }

    public void setHostel(Hostel hostel) {
        this.hostel = hostel;
    }

    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "location")
    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "phone")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "cash")
    public Long getCash() {
        return this.cash;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

    @Column(name = "photo")
    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "est_date", length = 10)
    public Date getEstDate() {
        return this.estDate;
    }

    public void setEstDate(Date estDate) {
        this.estDate = estDate;
    }

    @Override
    public String toString() {
        return name;
    }
    
    

}
