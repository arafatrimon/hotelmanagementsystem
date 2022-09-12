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
@Table(name = "hostel_galary",
        catalog = "hmis"
)
public class HostelGalary implements java.io.Serializable {

    private Long id;
    private Hostel hostel;
    private String title;
    private String photo;

    public HostelGalary() {

    }

    public HostelGalary(Long hostelId, String title, String photo) {
        this.hostel = new Hostel(hostelId);
        this.title = title;
        this.photo = photo;
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

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "photo")
    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
