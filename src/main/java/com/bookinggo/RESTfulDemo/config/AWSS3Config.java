package com.bookinggo.RestfulDemo.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class AWSS3Config {
    @Bean
    public AmazonS3 amazonS3(@Value("${cloud.aws.region.static}") String awsRegion,
                             @Value("${aws.s3.connection-timeout-ms:10000}") int connectionTimeoutMillis,
                             @Value("${aws.s3.client-execution-timeout-ms:50000}") int clientExecutionTimeoutMillis,
                             @Value("${aws.s3.request-timeout-ms:20000}") int requestTimeoutMillis,
                             @Value("${aws.s3.max-error-retry:3}") int maxErrorRetry,
                             AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(awsCredentialsProvider)
                .withClientConfiguration(new ClientConfiguration()
                        .withConnectionTimeout(connectionTimeoutMillis)
                        .withClientExecutionTimeout(clientExecutionTimeoutMillis)
                        .withRequestTimeout(requestTimeoutMillis)
                        .withMaxErrorRetry(maxErrorRetry)
                )
                .build();
    }

    @Bean
    @Primary
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSCredentialsProviderChain(
                new ProfileCredentialsProvider(),
                new EC2ContainerCredentialsProviderWrapper(),
                new EnvironmentVariableCredentialsProvider()
        );
    }
}