package com.spring.group.pojo;

import java.util.List;

public class PropertyCollectionResponse {

    List<PropertyResponse> properties;
    private int currentPage;
    private int totalPages;

    public PropertyCollectionResponse() {
    }

    public PropertyCollectionResponse(List<PropertyResponse> properties) {
        this.properties = properties;
    }

    public PropertyCollectionResponse(int currentPage, int totalPages, List<PropertyResponse> properties) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.properties = properties;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<PropertyResponse> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyResponse> properties) {
        this.properties = properties;
    }
}
