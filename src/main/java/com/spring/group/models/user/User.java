package com.spring.group.models.user;

import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

/**
 * @author George.Giazitzis
 */
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Basic
    @Column(length = 25)
    private String username;
    @Basic
    @Column(length = 45)
    private String email;
    @Basic
    @Column(length = 60)
    private String password;
    @Basic
    @Column(length = 25)
    private String firstName;
    @Basic
    @Column(length = 25)
    private String lastName;
    private Instant creationDate;
    private Instant updatedDate;
    @Basic
    @Column(length = 27)
    private String Iban;    //intentionally could be null, would be asked and validated upon creating a property.
    private boolean isEnabled;
    private boolean isNonLocked;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Rental> rentalCollection;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Collection<Property> propertyCollection;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username.toLowerCase().trim();
        this.password = password;
        this.email = email.toLowerCase().trim();
        this.userRole = UserRole.USER;
        this.creationDate = Instant.now();
        this.isNonLocked = true;
        this.authProvider = AuthProvider.local;
    }

    public User(String username, String email, AuthProvider authProvider) {
        this.username = username;
        this.email = email;
        this.userRole = UserRole.USER;
        this.creationDate = Instant.now();
        this.isNonLocked = true;
        this.isEnabled = true;
        this.authProvider = authProvider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getIban() {
        return Iban;
    }

    public void setIban(String iban) {
        Iban = iban;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isNonLocked() {
        return isNonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        isNonLocked = nonLocked;
    }

    public Collection<Rental> getRentalCollection() {
        return rentalCollection;
    }

    public void setRentalCollection(Collection<Rental> rentalCollection) {
        this.rentalCollection = rentalCollection;
    }

    public Collection<Property> getPropertyCollection() {
        return propertyCollection;
    }

    public void setPropertyCollection(Collection<Property> propertyCollection) {
        this.propertyCollection = propertyCollection;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", creationDate=" + creationDate +
                ", updatedDate=" + updatedDate +
                ", Iban='" + Iban + '\'' +
                ", isEnabled=" + isEnabled +
                ", isNonLocked=" + isNonLocked +
                ", userRole=" + userRole +
                '}';
    }
}
