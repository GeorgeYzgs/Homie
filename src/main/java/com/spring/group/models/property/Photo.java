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
    @Lob
    private byte[] image;
    @ManyToOne()
    private Property property;

    public Photo() {
    }

    public Photo(byte[] image, Property property) {
        this.image = image;
        this.property = property;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public byte[] getImage() { return image; }

    public void setImage(byte[] image) { this.image = image; }

    public Property getProperty() { return property; }

    public void setProperty(Property property) { this.property = property; }
}
