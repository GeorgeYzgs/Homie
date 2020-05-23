package com.spring.group.models.rental;

import javax.persistence.*;
import java.time.Instant;

/**
 * @author George.Giazitzis
 */
@Entity(name = "PaymentLogs")
public class PaymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentlog_id")
    private int id;
    private Double paidAmount;
    private Instant transactionTime;
    @ManyToOne
    private Rental rental;

    public PaymentLog() {
    }

    public PaymentLog(Double paidAmount, Rental rental) {
        this.paidAmount = paidAmount;
        this.transactionTime = Instant.now();
        this.rental = rental;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Instant getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Instant transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }
}
