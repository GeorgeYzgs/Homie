package com.spring.group.dto.property;

import com.spring.group.models.Address;
import com.spring.group.models.property.Category;
import com.spring.group.models.property.HeatingFuel;
import com.spring.group.models.property.HeatingType;
import com.spring.group.models.property.Property;
import com.spring.group.models.rental.Rental;
import com.spring.group.models.user.User;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * The main property data transfer object that unwraps into a property entity
 * its linked address, along with a collection of photos provided for the property.
 */
@ValidHeating
public class PropertyDTO {

    private int propertyID;
    @NotNull(message = "{Not.blank}")
    @Size(min = 4, max = 25, message = "{Size}")
    private String description;

    @NotNull(message = "{Not.blank}")
    @Range(min = 0, max = 9999, message = "{Range}")
    private int price;

    @NotBlank(message = "{Not.blank}")
    private String address_street;

    @NotNull(message = "{Not.blank}")
    @Range(min = 0, max = 999, message = "{Range}")
    private int address_number;

    @NotBlank(message = "{Not.blank}")
    private String address_city;

    @NotBlank(message = "{Not.blank}")
    private String address_state;

    @NotNull(message = "{Not.blank}")
    @Range(min = 0, max = 99999, message = "{Range}")
    private int address_zipCode;
    @NotNull(message = "{Not.blank}")
    @Range(min = 0, max = 999, message = "{Range}")
    private int numberOfRooms;

    @NotNull(message = "{Not.blank}")
    @Range(min = 0, max = 99999, message = "{Range}")
    private int area;
    @NotNull(message = "{Not.blank}")
    @Range(min = 0, max = 99999, message = "{Range}")
    private int floor;

    private HeatingType heatingType;
    private HeatingFuel heatingFuel;
    private Category category;
    private Collection<MultipartFile> photoCollection;


    private Collection<Rental> rentalCollection;


    public PropertyDTO() {
    }

    //TODO to beatify these shit with the reflection BEAN methods
    public PropertyDTO(Property property) {
        this.propertyID = property.getId();
        this.description = property.getDescription();
        this.price = property.getPrice();
        this.address_street = property.getAddress().getStreet();
        this.address_city = property.getAddress().getCity();
        this.address_number = property.getAddress().getNumber();
        this.address_zipCode = property.getAddress().getZipCode();
        this.address_state = property.getAddress().getState();
        this.area = property.getArea();
        this.category = property.getCategory();
        this.floor = property.getFloor();
        this.heatingFuel = property.getHeatingFuel();
        this.heatingType = property.getHeatingType();
        this.numberOfRooms = property.getNumberOfRooms();
//        this.photoCollection=property.getPhotoCollection();
    }

    //TODO to beatify these shit with the reflection BEAN methods
    public Property unWrapProperty(User owner) {
        Address tempAddress = new Address(address_street, address_number, address_city, address_state, address_zipCode);
        return new Property(description, price, tempAddress, category, numberOfRooms, area, floor, heatingType, heatingFuel, owner);
    }

    public int getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
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

    public HeatingType getHeatingType() {
        return heatingType;
    }

    public void setHeatingType(HeatingType heatingType) {
        this.heatingType = heatingType;
    }

    public HeatingFuel getHeatingFuel() {
        return heatingFuel;
    }

    public void setHeatingFuel(HeatingFuel heatingFuel) {
        this.heatingFuel = heatingFuel;
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
