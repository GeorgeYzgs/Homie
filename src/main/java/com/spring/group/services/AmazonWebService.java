package com.spring.group.services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author George.Giazitzis
 * Utilizing AWS s3 buckets to avoid saving images on our database.
 * The sensitive information needed to establish a connection is hidden in the application.yml file
 * It is accessed with the @Value annotations upon initializition.
 */
@Service
public class AmazonWebService {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    /**
     * The s3client is initialized immediately after construction of the bean with the above given values,
     * We are forced to use a deprecated method as our chosen s3 endpoint of eu south 1 is not listed in Amazon's
     * enumeration of Regions.
     *
     * @see com.amazonaws.regions.Regions
     */
    @PostConstruct
    private void initializeAmazon() {
        this.s3client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    }

    /**
     * A method that uploads the file to AWS, and will return the url link to be stored in our database
     *
     * @param multipartFile the file that will be uploaded
     * @param propertyID    the affiliated property this file will be linked to
     * @return a string url of the uploaded file
     * @throws IOException
     */
    public String uploadFile(MultipartFile multipartFile, Integer propertyID) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile, propertyID);
        String fileUrl = "https://" + bucketName + endpointUrl + "/" + fileName;
        uploadFileToAWS(fileName, file);
        file.delete();
        return fileUrl;
    }

    /**
     * Converts the MultiPartFile to a File, the accepted format for AWS bucket uploading
     *
     * @param file the multipart file to be converted
     * @return a file conversion of the given initial file
     * @throws IOException
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Craetes a unique name for every file and its affiliated property to avoid conflicts with common file names
     *
     * @param multiPart  the file provided
     * @param propertyID the property the file is linked to
     * @return a string of the combined name of the two parameters
     */
    private String generateFileName(MultipartFile multiPart, Integer propertyID) {
        return propertyID + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    /**
     * The method that actually uploads the file to AWS
     *
     * @param fileName the name of the file to be uploaded
     * @param file     the actual file
     */
    private void uploadFileToAWS(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
