package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.dto.TourDto;
import com.bookinggo.RESTfulDemo.entity.Tour;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integTest")
public class TourControllerIT {

    private static final int TOUR_ID = 1;

    private static final String LOCAL_HOST = "http://localhost:";

    private static final String TOUR_LOCATION = "barcelona";

    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String TITLE = "London Tower Bridge";

    private static final String DURATION = "2 hours";

    private static final int PRICE = 150;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void shouldReturn201_whenTourCreated_givenValidTour() {
        TourDto tourDto = TourDto.builder()
                .tourPackageCode(TOUR_PACKAGE_CODE)
                .title(TITLE)
                .duration(DURATION)
                .price(PRICE)
                .build();

        ResponseEntity<Tour> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/tours", tourDto, Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }

    @Test
    public void shouldReturnEightTours_whenGetAllTours_givenToursExist() {
        Tour[] tours = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours", Tour[].class)
                .getBody();

        assertThat(tours.length).isEqualTo(8);
    }

    @Test
    public void shouldReturnTour_whenGetTourById_givenTourExists() {
        Tour tour = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID, Tour.class)
                .getBody();

        assertThat(tour.getId().intValue()).isEqualTo(1);
    }

    @Test
    public void shouldReturnTwoTours_whenGetToursByLocation_givenToursExist() {
        Tour[] tours = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/byLocation/" + TOUR_LOCATION, Tour[].class)
                .getBody();

        assertThat(tours.length).isEqualTo(2);
    }
}
