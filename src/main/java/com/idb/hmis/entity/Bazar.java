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
@Table(name = "bazar",
        catalog = "hmis"
)
public class Bazar implements java.io.Serializable {

    private Long id;
    private BazarItems bazarItems;
    private Branch branch;
    private double quantity;
    private double untitPrice;
    private double total;
    private Date date;

    public Bazar() {
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
    @JoinColumn(name = "item_id", nullable = false)
    public BazarItems getBazarItems() {
        return this.bazarItems;
    }

    public void setBazarItems(BazarItems bazarItems) {
        this.bazarItems = bazarItems;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch", nullable = false)
    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Column(name = "quantity", nullable = false, precision = 12, scale = 3)
    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Column(name = "untit_price", nullable = false, precision = 12)
    public double getUntitPrice() {
        return this.untitPrice;
    }

    public void setUntitPrice(double untitPrice) {
        this.untitPrice = untitPrice;
    }

    @Column(name = "total", nullable = false, precision = 12)
    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
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
