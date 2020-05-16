package com.spring.group.services.bases;

import com.spring.group.models.property.Photo;

import java.util.Collection;

/**
 * @author George.Giazitzis
 */
public interface PhotoServiceInterface {

    /**
     * Persists a given photo to our database and returns the persisted object
     *
     * @param photo the target photo to be saved
     * @return a photo object that was saved
     */
    Photo insertPhoto(Photo photo);

    /**
     * Persists a collection of photos to our databbase and returns the persisted colleciton
     * Leverages JPA transactions to persist all the photos at once.
     *
     * @param photoAlbum the target collection to be saved
     * @return a collection of photos that was saved.
     */
    Collection<Photo> insertPhotoAlbum(Collection<Photo> photoAlbum);
}
