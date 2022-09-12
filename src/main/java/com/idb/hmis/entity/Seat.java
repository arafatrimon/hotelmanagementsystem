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
@Table(name = "seat",
        catalog = "hmis"
)
public class Seat implements java.io.Serializable {

    private Long id;
    private Branch branch;
    private Room room;
    private String seatNo;
    private double monthlyRent;
    private String photo;
    private String studentId;

    public Seat() {
    }

    public Seat(Room room) {
        this.room = room;
    }

    public Seat(Long id, String roomNo, String seatNo, double monthlyRent) {
        this.id = id;
        this.room = new Room(roomNo);
        this.seatNo = seatNo;
        this.monthlyRent = monthlyRent;
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
    @JoinColumn(name = "room")
    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Column(name = "seat_no", nullable = false)
    public String getSeatNo() {
        return this.seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @Column(name = "monthly_rent", nullable = false, precision = 12)
    public double getMonthlyRent() {
        return this.monthlyRent;
    }

    public void setMonthlyRent(double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    @Column(name = "photo")
    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Column(name = "student_id")
    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return room.getRoomNo() + "-" + seatNo;
    }
}
