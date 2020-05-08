package com.spring.group.models.rental;

import javax.persistence.*;
import java.util.Date;

/**
 * @author George.Giazitzis
 */
@Entity(name = "PaymentLogs")
public class PaymentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentlog_id")
    private int id;
    private int paidAmount;
    private Date transactionTimestamp;
    @ManyToOne
    private Rental rental;

    public PaymentLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Date getTransactionTime() {
        return transactionTimestamp;
    }

    public void setTransactionTime(Date transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }
}
