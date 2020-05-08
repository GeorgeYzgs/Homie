package com.spring.group.models.property;

import javax.persistence.*;

/**
 * @author George.Giazitzis
 */
@Entity(name="photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="photo_id")
    private int id;
    private Byte[] image;  //μήπως θέλει BLOB;;; java.sql.blob;
    @ManyToOne()
    private Property property;

    public Photo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
