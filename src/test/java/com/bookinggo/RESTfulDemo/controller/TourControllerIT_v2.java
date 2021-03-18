package com.bookinggo.RestfulDemo.controller;

import com.bookinggo.RestfulDemo.ControllerTests;
import com.bookinggo.RestfulDemo.dto.TourDto;
import com.bookinggo.RestfulDemo.dto.TourPatchDto;
import com.bookinggo.RestfulDemo.service.AbstractRestfulDemoIT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integratedTest")
public class TourControllerIT_v2 extends AbstractRestfulDemoIT implements ControllerTests {

    private static final int TOUR_ID = 1;
    private static final int NON_EXISTING_TOUR_ID = 10;
    private static final String TOUR_LOCATION = "paris";
    private static final String NON_EXISTING_TOUR_LOCATION = "barcelona";
    private static final String TOUR_PACKAGE_CODE = "LS";
    private static final String TOUR_TITLE = "London Tower Bridge";
    private static final String NON_EXISTING_TOUR_TITLE = "London City Sightseeing Tour";
    private static final String TOUR_DURATION = "2 hours";
    private static final int TOUR_PRICE = 150;

    @Autowired
    TourController tourController;

    private MockMvc mockMvc;

    private ObjectWriter objectWriter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tourController).build();
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Test
    public void shouldReturn201_whenCreateTour_givenValidTour() throws Exception {
        mockMvc.perform(post("/tours")
                .content(objectWriter.writeValueAsString(buildTourDto(TOUR_TITLE)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenCreateTour_givenTourWithThatTourPackageCodeAndNameAlreadyExists.sql")
    @Test
    public void shouldReturn400_whenCreateTour_givenTourWithThatTourPackageCodeAndNameAlreadyExists() throws Exception {
        mockMvc.perform(post("/tours")
                .content(objectWriter.writeValueAsString(buildTourDto(NON_EXISTING_TOUR_TITLE)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn200_whenUpdateTour_givenValidTour.sql")
    @Test
    public void shouldReturn200_whenUpdateTour_givenValidTour() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID)
                .content(objectWriter.writeValueAsString(buildTourPatchDto(TOUR_TITLE)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value(TOUR_TITLE))
                .andExpect(jsonPath("$.duration").value(TOUR_DURATION))
                .andExpect(jsonPath("$.price").value(TOUR_PRICE));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn200_whenUpdateTourSome_givenValidTour.sql")
    @Test
    public void shouldReturn200_whenUpdateTourSome_givenValidTour() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID)
                .content(objectWriter.writeValueAsString(TourPatchDto.builder()
                        .title(NON_EXISTING_TOUR_TITLE)
                        .price(TOUR_PRICE)
                        .build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title").value(NON_EXISTING_TOUR_TITLE))
                .andExpect(jsonPath("$.price").value(TOUR_PRICE));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenUpdateTour_givenTourWithIdDoesntExist.sql")
    @Test
    public void shouldReturn400_whenUpdateTour_givenTourWithIdDoesntExist() throws Exception {
        mockMvc.perform(put("/tours/" + NON_EXISTING_TOUR_ID)
                .content(objectWriter.writeValueAsString(buildTourPatchDto(NON_EXISTING_TOUR_TITLE)))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenUpdateTour_givenAnotherTourWithNewNameExists.sql")
    @Test
    public void shouldReturn400_whenUpdateTour_givenAnotherTourWithNewNameExists() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID)
                .content(objectWriter.writeValueAsString(TourPatchDto.builder()
                        .title(TOUR_TITLE)
                        .build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenUpdateTour_givenTourWithNameExistsWithinNewTourPackage.sql")
    @Test
    public void shouldReturn400_whenUpdateTour_givenTourWithNameExistsWithinNewTourPackage() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID)
                .content(objectWriter.writeValueAsString(TourPatchDto.builder()
                        .tourPackageCode(TOUR_PACKAGE_CODE)
                        .build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn200_whenUpdateTour_givenTourNameAndTourPackageHasNotChanged.sql")
    @Test
    public void shouldReturn200_whenUpdateTour_givenTourNameAndTourPackageHasNotChanged() throws Exception {
        mockMvc.perform(put("/tours/" + TOUR_ID)
                .content(objectWriter.writeValueAsString(TourPatchDto.builder()
                        .tourPackageCode(TOUR_PACKAGE_CODE)
                        .price(TOUR_PRICE)
                        .build()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.price").value(TOUR_PRICE));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturnFourTours_whenGetAllTours_givenToursExist.sql")
    @Test
    public void shouldReturnFourTours_whenGetAllTours_givenToursExist() throws Exception {
        mockMvc.perform(get("/tours"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturnTour_whenGetTourById_givenTourExists.sql")
    @Test
    public void shouldReturnTour_whenGetTourById_givenTourExists() throws Exception {
        mockMvc.perform(get("/tours/" + TOUR_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(TOUR_ID));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenGetTourById_givenTourDoesntExist.sql")
    @Test
    public void shouldReturn400_whenGetTourById_givenTourDoesntExist() throws Exception {
        mockMvc.perform(get("/tours/" + NON_EXISTING_TOUR_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturnTwoTours_whenGetToursByLocation_givenToursExist.sql")
    @Test
    public void shouldReturnTwoTours_whenGetToursByLocation_givenToursExist() throws Exception {
        mockMvc.perform(get("/tours/byLocation/" + TOUR_LOCATION))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenGetToursByLocation_givenLocationDoesntExist.sql")
    @Test
    public void shouldReturn400_whenGetToursByLocation_givenLocationDoesntExist() throws Exception {
        mockMvc.perform(get("/tours/byLocation/" + NON_EXISTING_TOUR_LOCATION))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldDeleteTour_whenDeleteTour_givenTourExists.sql")
    @Test
    public void shouldDeleteTour_whenDeleteTour_givenTourExists() throws Exception {
        mockMvc.perform(get("/tours"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));

        mockMvc.perform(delete("/tours/" + TOUR_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(get("/tours"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }


    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenDeleteTour_givenTourExistsAndTourHasBookings.sql")
    @Test
    public void shouldReturn400_whenDeleteTour_givenTourExistsAndTourHasBookings() throws Exception {
        mockMvc.perform(delete("/tours/" + TOUR_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Sql("classpath:com/bookinggo/RESTfulDemo/controller/TourControllerIT.shouldReturn400_whenDeleteTour_givenTourDoesntExist.sql")
    @Test
    public void shouldReturn400_whenDeleteTour_givenTourDoesntExist() throws Exception {
        mockMvc.perform(delete("/tours/" + NON_EXISTING_TOUR_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
