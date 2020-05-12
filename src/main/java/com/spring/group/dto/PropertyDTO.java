package com.spring.group.dto;

import com.spring.group.models.Address;
import com.spring.group.models.property.Category;
import com.spring.group.models.property.HeatingType;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public class PropertyDTO {


    //    private int id;
    private String description;
    private int price;

    private String address_street;
    private int address_number;
    private String address_city;
    private String address_state;
    private int address_zipCode;
    private int numberOfRooms;

    private int area;
    private int floor;
    private HeatingType heating;
    private Category category;
    private Collection<MultipartFile> photoCollection;


    private Collection<Rental> rentalCollection;


    public PropertyDTO() {
    }

    public Property unWrapProperty(User owner) {
        Address tempAddress = new Address(address_street, address_number, address_city, address_state, address_zipCode);
        return new Property(description, price, tempAddress, category, numberOfRooms, area, floor, heating, owner);
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public HeatingType getHeating() {
        return heating;
    }

    public void setHeating(HeatingType heating) {
        this.heating = heating;
    }

    public Collection<MultipartFile> getPhotoCollection() {
        return photoCollection;
    }

    public void setPhotoCollection(Collection<MultipartFile> photoCollection) {
        this.photoCollection = photoCollection;
    }

    public Collection<Rental> getRentalCollection() {
        return rentalCollection;
    }

    public void setRentalCollection(Collection<Rental> rentalCollection) {
        this.rentalCollection = rentalCollection;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
