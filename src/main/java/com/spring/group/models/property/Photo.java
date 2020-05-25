package com.spring.group.models.property;

import org.springframework.lang.Nullable;

import javax.persistence.*;

/**
 * @author George.Giazitzis
 */
@Entity(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer id;
    private String imageUrl;
    @ManyToOne()
    @Nullable
    private Property property;

    public Photo() {
    }

    public Photo(String imageUrl, Property property) {
        this.imageUrl = imageUrl;
        this.property = property;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Property getProperty() { return property; }

    public void setProperty(Property property) { this.property = property; }
}
