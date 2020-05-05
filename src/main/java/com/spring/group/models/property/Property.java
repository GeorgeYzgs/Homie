package com.spring.group.models.property;

import com.spring.group.models.Address;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author George.Giazitzis
 */
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, precision = 2)
    private BigDecimal price;
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @Enumerated(EnumType.STRING)   //Να γίνει boolean isListed σταδιάλα με τα ηναμ
    private ListingStatus listingStatus;

    @Enumerated(EnumType.STRING)  //Θα γίνει λουκ-απ σταδιάλα με τα ηναμ
    private Category category;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private Collection<Photo> photoCollection;
    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL)
    private Collection<Rental> rentalCollection;
    @ManyToOne
    private User owner;

    //TODO ADD ANOTHER ENUMERATION FOR RENT / SALE / AIRBNB ??


    public Property() {
    }

    public Property(String description, BigDecimal price, Address address, ListingStatus listingStatus, Category category, User owner){
      this.description=description;
      this.price=price;
      this.address=address;
      this.listingStatus=listingStatus;
      this.category=category;
      this.owner=owner;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ListingStatus getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(ListingStatus listingStatus) {
        this.listingStatus = listingStatus;
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
