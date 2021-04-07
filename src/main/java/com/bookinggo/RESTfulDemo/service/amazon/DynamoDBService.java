package com.bookinggo.RestfulDemo.service.amazon;

import java.time.LocalDateTime;
import java.util.List;

public interface DynamoDBService {
    enum DynamoDBUser {
        CUSTOMER, TOUR, TOURBOOKING
    }

    void addToDynamoDB(String name, LocalDateTime localDateTime, String tableName, DynamoDBUser user);
    List<String> dumpTable(String tableName);
}