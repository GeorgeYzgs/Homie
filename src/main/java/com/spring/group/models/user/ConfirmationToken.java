package com.spring.group.models.user;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * @author George.Giazitzis
 * A confirmation token class that is created upon every successful registration attempt,
 * sent as an email to the user in order to fully activate a user's account, for a set limited time.
 */
@Entity
public class ConfirmationToken {

    @Transient
    private static final Integer EXPIRATION_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String confirmationToken;
    private Instant creationDate;
    private Instant expirationDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        this.creationDate = Instant.now();
        this.expirationDate = creationDate.plus(EXPIRATION_HOURS, ChronoUnit.HOURS);
        this.confirmationToken = UUID.randomUUID().toString();
    }

    public ConfirmationToken() {
    }

    public static Integer getExpirationHours() { return EXPIRATION_HOURS; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
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
}
