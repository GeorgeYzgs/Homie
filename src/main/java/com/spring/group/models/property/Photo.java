package com.spring.group.models.property;

import javax.persistence.*;

/**
 * @author George.Giazitzis
 */
@Entity(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private int id;
    private String imageUrl;
    @ManyToOne()
    private Property property;

    public Photo() {
    }

    public Photo(String imageUrl, Property property) {
        this.imageUrl = imageUrl;
        this.property = property;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Property getProperty() { return property; }

    public void setProperty(Property property) { this.property = property; }
}
