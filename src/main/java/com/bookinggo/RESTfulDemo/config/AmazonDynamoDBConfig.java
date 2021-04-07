package com.bookinggo.RestfulDemo.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class AmazonDynamoDBConfig {
    @Bean
    public AmazonDynamoDB dynamoDBClient(@Value("${cloud.aws.region.static}") String awsRegion) {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", awsRegion))
                .build();
    }
}
