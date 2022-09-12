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
@Table(name = "hostel",
        catalog = "hmis",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "name")
            , @UniqueConstraint(columnNames = "slogan")
            , @UniqueConstraint(columnNames = "website")}
)
public class Hostel implements java.io.Serializable {

    private Long id;
    private Admin admin;
    private String name;
    private String slogan;
    private String website;
    private String phone;
    private Long cash;
    private String logo;
    private Date estDate;

    public Hostel() {
    }

    public Hostel(Long id) {
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
    @JoinColumn(name = "admin", nullable = false)
    public Admin getAdmin() {
        return this.admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Column(name = "name", unique = true, nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "slogan", unique = true)
    public String getSlogan() {
        return this.slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    @Column(name = "website", unique = true)
    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    @Column(name = "logo")
    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
