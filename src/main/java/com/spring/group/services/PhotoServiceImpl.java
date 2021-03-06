package com.spring.group.services;

import com.spring.group.models.property.Photo;
import com.spring.group.models.property.Property;
import com.spring.group.repos.PhotoRepository;
import com.spring.group.services.bases.PhotoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author George.Giazitzis
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoServiceInterface {

    @Autowired
    private AmazonWebService amazonWebService;

    private static final List<String> ACCEPTED_IMAGE_EXTENSIONS = Collections.unmodifiableList(Arrays.asList(".jpg", ".png"));

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public Photo insertPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public Collection<Photo> insertPhotoAlbum(Collection<Photo> photoAlbum) {
        return photoRepository.saveAll(photoAlbum);
    }

    @Override
    public void removePhotoById(Integer id) {
        photoRepository.deleteById(id);
    }

    @Override
    public void removePhotosById(List<Integer> ids) {
        ids.forEach(this::removePhotoById);
    }

    /**
     * Filters the uploaded files the user has provided to only include certain extensions, uploads them to AWS
     * and then stores the collection of uploaded files to our database
     *
     * @param fileList a collection of multipart files the user has uploaded
     * @param property the property these files are linked to
     * @throws IOException
     */
    public void uploadPhotos(Collection<MultipartFile> fileList, Property property) throws IOException {
        List<Photo> photoAlbum = new ArrayList<>();
        for (MultipartFile file : fileList) {
            if (isValidPhoto(file)) {
                photoAlbum.add(new Photo(amazonWebService.uploadFile(file, property.getId()), property));
            }
        }
        insertPhotoAlbum(photoAlbum);
    }

    /**
     * Filters the uploaded files to make sure they are within our list of accepted extensions.
     *
     * @param file the file to be filtered
     * @return true if the file has an accepted extension, otherwise false.
     */
    private boolean isValidPhoto(MultipartFile file) {
        if ("".equals(file.getOriginalFilename())) return false;
        return ACCEPTED_IMAGE_EXTENSIONS.contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
    }
}
