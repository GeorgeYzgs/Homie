package com.spring.group.models.rental;

import com.spring.group.models.property.Property;
import com.spring.group.models.user.User;


import javax.persistence.*;
import java.util.Collection;
import java.util.Date;


/**
 * @author George.Giazitzis
 */
@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(nullable = false )
    private int agreedPrice;

    private Date startDate;
    private Date endDate;
    @ManyToOne()
    private Property residence;
    @ManyToOne()
    private User user;
    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private Collection<PaymentLog> paymentLogs;

    public Rental() {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Property getResidence() {
        return residence;
    }

    public void setResidence(Property residence) {
        this.residence = residence;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<PaymentLog> getPaymentLogs() {
        return paymentLogs;
    }

    public void setPaymentLogs(Collection<PaymentLog> paymentLogs) {
        this.paymentLogs = paymentLogs;
    }
}
