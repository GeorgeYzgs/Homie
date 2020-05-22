package com.spring.group.models.rental;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;


/**
 * @author George.Giazitzis
 */
@Entity(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private int id;
    @Basic
    @Column(nullable = false)
    private int agreedPrice;
    private Instant startDate;
    private Instant endDate;
    @ManyToOne()
    private Property residence;
    @ManyToOne()
    private User tenant;
    private boolean isPending;
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Collection<PaymentLog> paymentLogs;

    public Rental() {
    }

    public Rental(int agreedPrice, Property residence, User tenant) {
        this.agreedPrice = agreedPrice;
        this.residence = residence;
        this.tenant = tenant;
        this.isPending = true;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgreedPrice() {
        return agreedPrice;
    }

    public void setAgreedPrice(int agreedPrice) {
        this.agreedPrice = agreedPrice;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Property getResidence() {
        return residence;
    }

    public void setResidence(Property residence) {
        this.residence = residence;
    }

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }

    public Collection<PaymentLog> getPaymentLogs() {
        return paymentLogs;
    }

    public void setPaymentLogs(Collection<PaymentLog> paymentLogs) {
        this.paymentLogs = paymentLogs;
    }
}
