package com.bookinggo.RestfulDemo.service.amazon;

public interface AmazonS3Service {
    String retrieve(String bucketName, String id);
    void upload(String bucketName, String id, byte[] input);
}