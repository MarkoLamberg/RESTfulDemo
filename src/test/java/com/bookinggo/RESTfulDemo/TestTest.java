package com.bookinggo.RESTfulDemo;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTest {

    @Test
    void test() throws JsonProcessingException {
/*        ObjectMapper objectMapper = new ObjectMapper();
        //Tour tour = objectMapper.readValue(new File("/ExploreCalifornia.json"), Tour.class);
        //String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        String json = "{ \"packageType\": \"Taste of California\",\n" +
                "\"title\": \"Oranges & Apples Tour\",\n" +
                "\"length\": \"3 days\",\n" +
                "\"price\": \"350\",\n" +
                "\"region\": \"South\"}";

        Tour tour = objectMapper.readValue(json, Tour.class);
        assertNotNull(tour);*/
    }

}
