package com.spring.group.models.property;

import com.spring.group.models.Address;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author George.Giazitzis
 */
@Entity(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id")
    private int id;
    @Basic
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, precision = 2)
    private int price;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
    private boolean isAvailable;
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private Collection<Photo> photoCollection;
    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL)
    private Collection<Rental> rentalCollection;
    @ManyToOne
    private User owner;

    public Property() {
    }

    public Property(String description, int price, Address address, Category category, User owner) {
        this.description = description;
        this.price = price;
        this.isAvailable = true;
        this.address = address;
        this.category = category;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Collection<Photo> getPhotoCollection() {
        return photoCollection;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPhotoCollection(Collection<Photo> photoCollection) {
        this.photoCollection = photoCollection;
    }

    public Collection<Rental> getRentalCollection() {
        return rentalCollection;
    }

    public void setRentalCollection(Collection<Rental> rentalCollection) {
        this.rentalCollection = rentalCollection;
    }
}
