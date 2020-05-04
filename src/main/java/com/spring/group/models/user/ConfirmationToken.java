package com.spring.group.models.user;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author George.Giazitzis
 */
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER) //TODO recheck this.
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        this.creationDate = new Date();
        this.confirmationToken = UUID.randomUUID().toString();
    }

    public ConfirmationToken() {
    }

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
