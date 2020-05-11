package com.spring.group.services.bases;

import com.spring.group.models.property.Photo;

import java.util.Collection;

/**
 * @author George.Giazitzis
 */
public interface PhotoServiceInterface {

    Photo insertPhoto(Photo photo);

    Collection<Photo> insertPhotoAlbum(Collection<Photo> photoAlbum);
}
