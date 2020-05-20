package com.spring.group.pojo;

import com.spring.group.models.Address;
import com.spring.group.models.property.Photo;
import com.spring.group.models.property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyResponse {

    @Autowired
    MessageSource messageSource;
    private int id;
    private String description;
    private int price;
    private Address address;
    private String category;
    private List<String> photosUrlList;
    private int numberOfRooms;
    private int area;
    private int floor;

    public PropertyResponse(int id, String description, int price, Address address, String category, List<String> photosUrlList, int numberOfRooms, int area, int floor) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.address = address;
        this.category = category;
        this.photosUrlList = photosUrlList;
        this.numberOfRooms = numberOfRooms;
        this.area = area;
        this.floor = floor;
    }

    public PropertyResponse(Property property) {
        this.id = property.getId();
        this.description = property.getDescription();
        this.price = property.getPrice();
        this.address = property.getAddress();
        System.out.println("Category." + property.getCategory().toString());
        this.category = "Category." + property.getCategory().toString();
        if (property.getPhotoCollection() != null) {
            this.photosUrlList = property.getPhotoCollection().stream()
                    .map(Photo::getImageUrl)
                    .collect(Collectors.toList());
        }
        this.numberOfRooms = property.getNumberOfRooms();
        this.area = property.getArea();
        this.floor = property.getFloor();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getPhotosUrlList() {
        return photosUrlList;
    }

    public void setPhotosUrlList(List<String> photosUrlList) {
        this.photosUrlList = photosUrlList;
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

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
