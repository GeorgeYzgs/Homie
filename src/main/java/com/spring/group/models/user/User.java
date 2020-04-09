package com.spring.group.models.user;

import com.spring.group.models.Address;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;



import javax.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @author George.Giazitzis
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   @Basic
   @Column(nullable = false , length = 25)
    private String username;
    @Basic
    @Column(nullable = false, length = 45)
    private String email;
    @Basic
    @Column(nullable = false, length = 255)
    private String password;
    @Basic
    @Column(nullable = false , length = 25)
    private String firstName;
    @Basic
    @Column(nullable = false , length = 25)
    private String lastName;

    private Date creation;
    private Date updated;
    @Basic
    @Column(nullable = false , length = 27)
    private String Iban;    //intentionally could be null, would be asked and validated upon creating a property.
    private boolean isEnabled;
    private boolean isLocked;
    @OneToOne
    private Address address;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Collection<Rental> rentalCollection;
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    private Collection<Property> propertyCollection;

    public User() {
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

    public Date getCreation() {
        return creation;
    }

    public Date getUpdated() {
        return updated;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
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
                ", creation=" + creation +
                ", address=" + address +
                ", userRole=" + userRole +
                '}';
    }
}
