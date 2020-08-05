package com.bookinggo.RESTfulDemo.controller;

import com.bookinggo.RESTfulDemo.RestfulDemoApplication;
import com.bookinggo.RESTfulDemo.dto.TourDto;
import com.bookinggo.RESTfulDemo.dto.TourPatchDto;
import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.service.AbstractRESTfulDemoIT;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = RestfulDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integTest")
public class TourControllerIT extends AbstractRESTfulDemoIT {

    private static final int TOUR_ID = 1;

    private static final int NON_EXISTING_TOUR_ID = 10;

    private static final String LOCAL_HOST = "http://localhost:";

    private static final String TOUR_LOCATION = "paris";

    private static final String NON_EXISTING_TOUR_LOCATION = "barcelona";

    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String TOUR_TITLE = "London Tower Bridge";

    private static final String NON_EXISTING_TOUR_TITLE = "London City Sightseeing Tour";

    private static final String TOUR_DURATION = "2 hours";

    private static final int TOUR_PRICE = 150;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void shouldReturn201_whenCreateTour_givenValidTour() {
        ResponseEntity<Tour> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/tours",
                        buildTourDto(TOUR_TITLE),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(CREATED.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenCreateTour_givenTourWithThatTourPackageCodeAndNameAlreadyExists() {
        ResponseEntity<Tour> response = restTemplate
                .postForEntity(LOCAL_HOST + port + "/tours",
                        buildTourDto(NON_EXISTING_TOUR_TITLE),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn200_whenUpdateTour_givenValidTour() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(buildTourPatchDto(TOUR_TITLE)),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(OK.value());
        assertThat(response.getBody().getTitle()).isEqualTo(TOUR_TITLE);
        assertThat(response.getBody().getDuration()).isEqualTo(TOUR_DURATION);
        assertThat(response.getBody().getPrice()).isEqualTo(TOUR_PRICE);
    }

    @Sql
    @Test
    public void shouldReturn200_whenUpdateTourSome_givenValidTour() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(TourPatchDto.builder()
                                .title(NON_EXISTING_TOUR_TITLE)
                                .price(TOUR_PRICE)
                                .build()),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(OK.value());
        assertThat(response.getBody().getTitle()).isEqualTo(NON_EXISTING_TOUR_TITLE);
        assertThat(response.getBody().getPrice()).isEqualTo(TOUR_PRICE);
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateTour_givenTourWithIdDoesntExist() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(buildTourPatchDto(NON_EXISTING_TOUR_TITLE)),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateTour_givenAnotherTourWithNewNameExists() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(TourPatchDto.builder()
                                .title(TOUR_TITLE)
                                .build()),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturn400_whenUpdateTour_givenTourWithNameExistsWithinNewTourPackage() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(TourPatchDto.builder()
                                .tourPackageCode(TOUR_PACKAGE_CODE)
                                .build()),
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnFourTours_whenGetAllTours_givenToursExist() {
        Tour[] tours = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours", Tour[].class)
                .getBody();

        assertThat(tours.length).isEqualTo(4);
    }

    @Sql
    @Test
    public void shouldReturnTour_whenGetTourById_givenTourExists() {
        Tour tour = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/" + TOUR_ID, Tour.class)
                .getBody();

        assertThat(tour.getId().intValue()).isEqualTo(1);
    }

    @Sql
    @Test
    public void shouldReturn400_whenGetTourById_givenTourDoesntExist() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + NON_EXISTING_TOUR_ID,
                        HttpMethod.GET,
                        null,
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldReturnTwoTours_whenGetToursByLocation_givenToursExist() {
        Tour[] tours = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours/byLocation/" + TOUR_LOCATION, Tour[].class)
                .getBody();

        assertThat(tours.length).isEqualTo(2);
    }

    @Sql
    @Test
    public void shouldReturn400_whenGetToursByLocation_givenLocationDoesntExist() {
        ResponseEntity<String> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/byLocation/" + NON_EXISTING_TOUR_LOCATION,
                        HttpMethod.GET,
                        null,
                        String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    @Sql
    @Test
    public void shouldDeleteTour_whenDeleteTour_givenTourExists() {
        Tour[] toursBefore = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours", Tour[].class)
                .getBody();

        assertThat(toursBefore.length).isEqualTo(4);

        restTemplate.delete(LOCAL_HOST + port + "/tours/" + TOUR_ID);

        Tour[] toursAfter = restTemplate
                .getForEntity(LOCAL_HOST + port + "/tours", Tour[].class)
                .getBody();

        assertThat(toursAfter.length).isEqualTo(3);
    }

    @Sql
    @Test
    public void shouldReturn400_whenDeleteTour_givenTourExistsAndTourHasBookings() {
        ResponseEntity<Tour> response = restTemplate
                .exchange(LOCAL_HOST + port + "/tours/" + TOUR_ID,
                        HttpMethod.DELETE,
                        null,
                        Tour.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(BAD_REQUEST.value());
    }

    private TourDto buildTourDto(String tourTitle) {
        return TourDto.builder()
                .tourPackageCode(TOUR_PACKAGE_CODE)
                .title(tourTitle)
                .duration(TOUR_DURATION)
                .price(TOUR_PRICE)
                .build();
    }

    private TourPatchDto buildTourPatchDto(String tourTitle) {
        return TourPatchDto.builder()
                .tourPackageCode(TOUR_PACKAGE_CODE)
                .title(tourTitle)
                .duration(TOUR_DURATION)
                .price(TOUR_PRICE)
                .build();
    }
}
