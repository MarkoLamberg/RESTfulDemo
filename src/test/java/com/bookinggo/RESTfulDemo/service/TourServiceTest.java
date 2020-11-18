package com.bookinggo.RestfulDemo.service;

import com.bookinggo.RestfulDemo.ServiceTests;
import com.bookinggo.RestfulDemo.entity.Tour;
import com.bookinggo.RestfulDemo.entity.TourPackage;
import com.bookinggo.RestfulDemo.exception.TourServiceException;
import com.bookinggo.RestfulDemo.repository.TourPackageRepository;
import com.bookinggo.RestfulDemo.repository.TourRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourServiceTest implements ServiceTests {

    private static final int TOUR_ID = 234;
    private static final String TOUR_PACKAGE_CODE = "LS";
    private static final String NON_EXISTING_TOUR_PACKAGE_CODE = "DS";
    private static final String TOUR_TITLE = "London City Sightseeing Tour";
    private static final String TOUR_DURATION = "2 hours";
    private static final int TOUR_PRICE = 150;
    private static final String TOUR_LOCATION = "liverpool";

    @Autowired
    private com.bookinggo.RestfulDemo.service.TourService tourService;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourPackageRepository tourPackageRepositoryMock;

    @Captor
    private ArgumentCaptor<Tour> tourCaptor;

    @Test
    public void shouldCallGetTourPackageByCode_whenCreateTour_givenTourPackageDoesntExist() {
        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.createTour(NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE)),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE));
    }

    @Test
    public void shouldCallGetTourPackageByCode_whenCreateTour_givenTourPackageExists() {
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.save(tourCaptor.capture())).thenReturn(buildTour());

        Tour tour = tourService.createTour(NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        assertAll(
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE),
                () -> verify(tourRepositoryMock).save(any()),
                () -> assertThat(tour).isNotNull(),
                () -> assertThat(tourCaptor.getValue().getTitle()).isEqualTo(TOUR_TITLE),
                () -> assertThat(tourCaptor.getValue().getDuration()).isEqualTo(TOUR_DURATION),
                () -> assertThat(tourCaptor.getValue().getPrice()).isEqualTo(TOUR_PRICE));
    }

    @Test
    public void shouldCallFindById_whenUpdateTour_givenTourDoesntExist() {
        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE)),
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verifyNoInteractions(tourPackageRepositoryMock));
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTour_givenTourPackageDoesntExist() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildSimpleTour()));

        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE)),
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE));
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithFourFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());

        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tourCaptor.capture())).thenReturn(buildTour());

        Tour updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE),
                () -> verify(tour).setTourPackage(any()),
                () -> verify(tour).setTitle(TOUR_TITLE),
                () -> verify(tour).setDuration(TOUR_DURATION),
                () -> verify(tour).setPrice(TOUR_PRICE),
                () -> assertThat(tourCaptor.getValue().getTitle()).isEqualTo(TOUR_TITLE),
                () -> assertThat(tourCaptor.getValue().getDuration()).isEqualTo(TOUR_DURATION),
                () -> assertThat(tourCaptor.getValue().getPrice()).isEqualTo(TOUR_PRICE),
                () -> assertThat(updatedTour).isNotNull());
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithThreeFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());

        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tourCaptor.capture())).thenReturn(buildTour());

        Tour updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, null);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE),
                () -> verify(tour).setTourPackage(any()),
                () -> verify(tour).setTitle(TOUR_TITLE),
                () -> verify(tour).setDuration(TOUR_DURATION),
                () -> verify(tour, times(0)).setPrice(TOUR_PRICE),
                () -> assertThat(tourCaptor.getValue().getTitle()).isEqualTo(TOUR_TITLE),
                () -> assertThat(tourCaptor.getValue().getDuration()).isEqualTo(TOUR_DURATION),
                () -> assertThat(updatedTour).isNotNull());
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithTwoFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());

        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tourCaptor.capture())).thenReturn(buildTour());

        Tour updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, null, null);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE),
                () -> verify(tour).setTourPackage(any()),
                () -> verify(tour).setTitle(TOUR_TITLE),
                () -> verify(tour, times(0)).setDuration(TOUR_DURATION),
                () -> verify(tour, times(0)).setPrice(TOUR_PRICE),
                () -> assertThat(tourCaptor.getValue().getTitle()).isEqualTo(TOUR_TITLE),
                () -> assertThat(updatedTour).isNotNull());
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithOneField_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());

        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Tour updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, null, null, null);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE),
                () -> verify(tour).setTourPackage(any()),
                () -> verify(tour, times(0)).setTitle(TOUR_TITLE),
                () -> verify(tour, times(0)).setDuration(TOUR_DURATION),
                () -> verify(tour, times(0)).setPrice(TOUR_PRICE),
                () -> assertThat(updatedTour).isNotNull());
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithZeroFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());

        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Tour updatedTour = tourService.updateTour(TOUR_ID, null, null, null, null);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourPackageRepositoryMock, times(0)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE),
                () -> verifyNoMoreInteractions(tour),
                () -> assertThat(updatedTour).isNotNull());
    }

    @Test
    public void shouldCallFindAll_whenGetAllTours_givenNoToursExist() {
        List<Tour> tours = tourService.getAllTours();

        assertAll(
                () -> verify(tourRepositoryMock).findAll(),
                () -> assertThat(tours.size()).isEqualTo(0));
    }

    @Test
    public void shouldCallFindAll_whenGetAllTours_givenTourExists() {
        when(tourRepositoryMock.findAll()).thenReturn(of(buildSimpleTour()));
        List<Tour> tours = tourService.getAllTours();

        assertAll(
                () -> verify(tourRepositoryMock).findAll(),
                () -> assertThat(tours.size()).isEqualTo(1));
    }

    @Test
    public void shouldCallFindById_whenGetTourById_givenNoTourWithIdExists() {
        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.getTourById(TOUR_ID)),
                () -> verify(tourRepositoryMock).findById(TOUR_ID));
    }

    @Test
    public void shouldCallFindById_whenGetTourById_givenTourWithIdExists() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildSimpleTour()));
        Tour tour = tourService.getTourById(TOUR_ID);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> assertThat(tour).isNotNull());
    }

    @Test
    public void shouldCallFindAll_whenGetToursByLocation_givenNoTourWithLocationExists() {
        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.getToursByLocation(TOUR_LOCATION)),
                () -> verify(tourRepositoryMock).findAll());
    }

    @Test
    public void shouldCallFindAll_whenGetToursByLocation_givenTourWithLocationExists() {
        when(tourRepositoryMock.findAll()).thenReturn(of(Tour
                .builder()
                .tourPackage(TourPackage
                        .builder()
                        .location(TOUR_LOCATION)
                        .build())
                .build()));
        List<Tour> tours = tourService.getToursByLocation(TOUR_LOCATION);

        assertAll(
                () -> verify(tourRepositoryMock).findAll(),
                () -> assertThat(tours.size()).isEqualTo(1));
    }

    @Test
    public void shouldCallGetTourPackageByCode_whenGetTourPackageCodeAndTitle_givenNoTourWithThatPackageCodeAndTitleExists() {
        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE)),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(TOUR_PACKAGE_CODE));
    }

    @Test
    public void shouldCallGetTourPackageByCodeAndFindTourByTourPackageAndTitle_whenGetTourPackageCodeAndTitle_givenNoTourWithThatPackageCodeAndTitleExists() {
        TourPackage tourPackage = buildTourPackage(TOUR_PACKAGE_CODE);
        when(tourPackageRepositoryMock.getTourPackageByCode(TOUR_PACKAGE_CODE)).thenReturn(Optional.of(tourPackage));

        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE)),
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(TOUR_PACKAGE_CODE),
                () -> verify(tourRepositoryMock).findTourByTourPackageAndTitle(tourPackage, TOUR_TITLE));
    }

    @Test
    public void shouldCallGetTourPackageByCodeAndFindTourByTourPackageAndTitle_whenGetTourPackageCodeAndTitle_givenATourWithThatPackageCodeAndTitleExists() {
        TourPackage tourPackage = buildTourPackage(TOUR_PACKAGE_CODE);

        when(tourPackageRepositoryMock.getTourPackageByCode(TOUR_PACKAGE_CODE)).thenReturn(Optional.of(tourPackage));
        when(tourRepositoryMock.findTourByTourPackageAndTitle(tourPackage, TOUR_TITLE)).thenReturn(Optional.of(buildSimpleTour()));

        Tour tour = tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        assertAll(
                () -> verify(tourPackageRepositoryMock).getTourPackageByCode(TOUR_PACKAGE_CODE),
                () -> verify(tourRepositoryMock).findTourByTourPackageAndTitle(tourPackage, TOUR_TITLE),
                () -> assertThat(tour).isNotNull());
    }

    @Test
    public void shouldNotCallDeleteById_whenDeleteTourById_givenTourDoesntExist() {
        assertAll(
                () -> assertThrows(TourServiceException.class,
                        () -> tourService.deleteTourById(TOUR_ID)),
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourRepositoryMock, times(0)).deleteById(TOUR_ID));
    }

    @Test
    public void shouldCallDeleteById_whenDeleteTourById_givenTourExists() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildSimpleTour()));
        Tour tour = tourService.deleteTourById(TOUR_ID);

        assertAll(
                () -> verify(tourRepositoryMock).findById(TOUR_ID),
                () -> verify(tourRepositoryMock).deleteById(TOUR_ID),
                () -> assertThat(tour).isNotNull());
    }

    private Tour buildSimpleTour() {
        return Tour
                .builder()
                .id(TOUR_ID)
                .build();
    }

    private Tour buildTour() {
        return Tour
                .builder()
                .id(TOUR_ID)
                .title(TOUR_TITLE)
                .duration(TOUR_DURATION)
                .price(TOUR_PRICE)
                .build();
    }

    private TourPackage buildTourPackage(String code) {
        return TourPackage
                .builder()
                .code(code)
                .build();
    }
}
