package com.spring.group.models.user;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * @author George.Giazitzis
 */
@Entity
public class ResetPassToken {

    @Transient
    private static final int EXPIRATION_HOURS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String resetPassToken;
    private Instant creationDate;
    private Instant expirationDate;
    private boolean isUsed;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ResetPassToken(User user) {
        this.user = user;
        this.creationDate = Instant.now();
        this.expirationDate = creationDate.plus(EXPIRATION_HOURS, ChronoUnit.HOURS);
        this.resetPassToken = UUID.randomUUID().toString();
    }

    public ResetPassToken() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResetPassToken() {
        return resetPassToken;
    }

    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
