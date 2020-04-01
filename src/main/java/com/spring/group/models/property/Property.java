package com.spring.group.models.property;

import com.spring.group.models.Address;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author George.Giazitzis
 */
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String description;
    @NotNull
    private int price;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private ListingStatus listingStatus;
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToMany(mappedBy = "property")
    private Collection<Photo> photoCollection;
    @OneToMany(mappedBy = "residence")
    private Collection<Rental> rentalCollection;
    @ManyToOne
    private User owner;

    //TODO ADD ANOTHER ENUMERATION FOR RENT / SALE / AIRBNB ??

    public Property() {
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
