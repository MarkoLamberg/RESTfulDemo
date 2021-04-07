package com.bookinggo.RestfulDemo.service.amazon;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamoDBServiceImpl implements DynamoDBService {

    private final AmazonDynamoDB dynamoDBClient;

    @Override
    public void addToDynamoDB(String name, LocalDateTime localDateTime, String tableName, DynamoDBUser user) {
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
        createDynamoDBTable(dynamoDB, tableName, user);

        if (!checkIfNameExistsInDynamoDB(name, tableName, user)) {
            String created = localDateTime.toString();
            try {
                Table table = dynamoDB.getTable(tableName);
                table.putItem(new Item().withPrimaryKey("created", created, user.toString(), name));
                log.info("PutItem succeeded: {} {}", created, name);

            } catch (Exception e) {
                log.error("Unable to add customer: {} {}", created, name);
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public List<String> dumpTable(String tableName) {
        List<String> items = new LinkedList<>();

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName);

        ScanResult scanResult = dynamoDBClient.scan(scanRequest);

        for (Map<String, AttributeValue> item : scanResult.getItems()) {
            items.add(item.toString());
        }

        return items;
    }

    private void createDynamoDBTable(DynamoDB dynamoDB, String tableName, DynamoDBUser user) {
        try {
            log.debug("Attempting to create table; please wait...");

            Table table = dynamoDB.createTable(tableName,
                    Arrays.asList(new KeySchemaElement("created", KeyType.HASH), // Partition
                            // key
                            new KeySchemaElement(user.toString(), KeyType.RANGE)), // Sort key
                    Arrays.asList(new AttributeDefinition("created", ScalarAttributeType.S),
                            new AttributeDefinition(user.toString(), ScalarAttributeType.S)),
                    new ProvisionedThroughput(10L, 10L));
            table.waitForActive();

            log.debug("Success.  Table status: " + table.getDescription().getTableStatus());

        }
        catch (Exception e) {
            log.debug("Unable to create table - {} table already exists?", tableName);
        }
    }

    private boolean checkIfNameExistsInDynamoDB(String name, String tableName, DynamoDBUser user) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":cName", new AttributeValue().withS(name));
        String expression =  user.toString() + " = :cName";
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName)
                .withFilterExpression(expression)
                .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult scanResult = dynamoDBClient.scan(scanRequest);

        for (Map<String, AttributeValue> item : scanResult.getItems()) {
            log.info("{}", item.toString());
        }

        return (scanResult.getItems().size() > 0);
        /* Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":id_1", new AttributeValue().withS("airport_100"));
        expressionAttributeValues.put(":id_2", new AttributeValue().withS("airport_1361"));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(TABLE_NAME)
                .withProjectionExpression("uniqueBookingIdentifier, city, countryCode")
                .withFilterExpression("uniqueBookingIdentifier = :id_1 or uniqueBookingIdentifier = :id_2")
                .withExpressionAttributeValues(expressionAttributeValues);


        ScanResult result = client.scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()) {
            log.info("{}", item.toString());
        }*/
    }
}