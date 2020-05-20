package com.spring.group.pojo;

import com.spring.group.dto.property.specifications.SearchCriteria;
import com.spring.group.models.property.Category;
import com.spring.group.models.property.HeatingType;
import com.spring.group.models.property.Property_;

import java.util.ArrayList;
import java.util.List;

public class SearchParamsPojo {

    private String category;
    private String heating;
    private String sortBy;
    private Integer startPrice;
    private Integer endPrice;
    private Integer startArea;
    private Integer endArea;
    private Integer startRooms;
    private Integer endRooms;
    private Integer startFloors;
    private Integer endFloors;

    public SearchParamsPojo() {
        this.category = "ALL";
        this.heating = "ALL";
    }

    public SearchParamsPojo(String category, String heating, String sortBy, Integer startPrice, Integer endPrice, Integer startArea, Integer endArea, Integer startRooms, Integer endRooms, Integer startFloors, Integer endFloors) {
        this.category = category;
        this.heating = heating;
        this.sortBy = sortBy;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.startArea = startArea;
        this.endArea = endArea;
        this.startRooms = startRooms;
        this.endRooms = endRooms;
        this.startFloors = startFloors;
        this.endFloors = endFloors;
    }

    public List<SearchCriteria> toSearchCriteria() {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (this.category != null && !this.category.equals("ALL"))
            searchCriteriaList.add(new SearchCriteria(Property_.category, ":", Category.valueOf(this.category)));
        if (this.heating != null && !this.heating.equals("ALL"))
            searchCriteriaList.add(new SearchCriteria(Property_.heatingType, ":", HeatingType.valueOf(this.heating)));
        if (this.startPrice > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.price, ">", startPrice));
        if (this.endPrice > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.price, "<", endPrice));
        if (this.startArea > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.area, ">", startArea));
        if (this.endArea > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.area, "<", endArea));
        if (this.startRooms > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.numberOfRooms, ">", startRooms));
        if (this.endRooms > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.numberOfRooms, "<", endRooms));
        if (this.startFloors > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.floor, ">", startFloors));
        if (this.endFloors > 0)
            searchCriteriaList.add(new SearchCriteria(Property_.floor, "<", endFloors));

        return searchCriteriaList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHeating() {
        return heating;
    }

    public void setHeating(String heating) {
        this.heating = heating;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Integer startPrice) {
        this.startPrice = startPrice;
    }

    public Integer getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Integer endPrice) {
        this.endPrice = endPrice;
    }

    public Integer getStartArea() {
        return startArea;
    }

    public void setStartArea(Integer startArea) {
        this.startArea = startArea;
    }

    public Integer getEndArea() {
        return endArea;
    }

    public void setEndArea(Integer endArea) {
        this.endArea = endArea;
    }

    public Integer getStartRooms() {
        return startRooms;
    }

    public void setStartRooms(Integer startRooms) {
        this.startRooms = startRooms;
    }

    public Integer getEndRooms() {
        return endRooms;
    }

    public void setEndRooms(Integer endRooms) {
        this.endRooms = endRooms;
    }

    public Integer getStartFloors() {
        return startFloors;
    }

    public void setStartFloors(Integer startFloors) {
        this.startFloors = startFloors;
    }

    public Integer getEndFloors() {
        return endFloors;
    }

    public void setEndFloors(Integer endFloors) {
        this.endFloors = endFloors;
    }
}
