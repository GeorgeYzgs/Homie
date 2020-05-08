package com.spring.group.dto;

import com.spring.group.models.Address;
import com.spring.group.models.property.Category;
import com.spring.group.models.property.Photo;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;

import java.math.BigDecimal;
import java.util.Collection;

public class PropertyDTO {


    //    private int id;
    private String description;
    private BigDecimal price;

    private String address_street;
    private int address_number;
    private String address_city;
    private String address_state;
    private int address_zipCode;

    private boolean isListed;
    private Category category;
    private Collection<Photo> photoCollection;
    private Collection<Rental> rentalCollection;
    private User owner;

    public PropertyDTO() {
    }

    public Property unWrapProperty() {
        Address tempAddress = new Address(address_street, address_number, address_city, address_state, address_zipCode);
        return new Property(description, price, tempAddress, isListed, category, owner);
    }

    public Address unWrapAddress() {
        return new Address(address_street, address_number, address_city, address_state, address_zipCode);
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

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public int getAddress_number() {
        return address_number;
    }

    public void setAddress_number(int address_number) {
        this.address_number = address_number;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public int getAddress_zipCode() {
        return address_zipCode;
    }

    public void setAddress_zipCode(int address_zipCode) {
        this.address_zipCode = address_zipCode;
    }

    public boolean isListed() {
        return isListed;
    }

    public void setListed(boolean listed) {
        isListed = listed;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
