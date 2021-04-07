package com.bookinggo.RestfulDemo.service.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private static final String JSON_CONTENT_TYPE = "application/json";

    private final AmazonS3 amazonS3;

    @Override
    public String retrieve(String bucketName, String key) {
        try {
            return amazonS3.getObjectAsString(bucketName, key);
       } catch (AmazonS3Exception e) {
            log.info("Downloading S3 - AmazonS3Exception: {}", e);
            return "";
        }
    }

    @Override
    public void upload(String bucketName, String key, byte[] inputByteArray) {
        try {
            PutObjectRequest request = createPutObjectRequest(bucketName, key, inputByteArray);
            amazonS3.putObject(request);
            log.info("Successfully uploaded new object to {} bucket for {}", bucketName, key);
        } catch (AmazonS3Exception e) {
            log.info("Uploading S3 - AmazonS3Exception: {}", e);
        }
    }

    private PutObjectRequest createPutObjectRequest(String bucketName, String key, byte[] inputByteArray) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(JSON_CONTENT_TYPE);
        metadata.setContentLength(inputByteArray.length);
        return new PutObjectRequest(bucketName, key, new ByteArrayInputStream(inputByteArray), metadata);
    }
}