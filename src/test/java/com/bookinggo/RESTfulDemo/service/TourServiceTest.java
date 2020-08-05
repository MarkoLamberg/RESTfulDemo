package com.bookinggo.RESTfulDemo.service;

import com.bookinggo.RESTfulDemo.entity.Tour;
import com.bookinggo.RESTfulDemo.entity.TourPackage;
import com.bookinggo.RESTfulDemo.repository.TourPackageRepository;
import com.bookinggo.RESTfulDemo.repository.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    private static final int TOUR_ID = 234;

    private static final String TOUR_PACKAGE_CODE = "LS";

    private static final String NON_EXISTING_TOUR_PACKAGE_CODE = "DS";

    private static final String TOUR_TITLE = "London City Sightseeing Tour";

    private static final String TOUR_DURATION = "2 hours";

    private static final int TOUR_PRICE = 150;

    private static final String TOUR_LOCATION = "liverpool";

    @Autowired
    private TourService tourService;

    @MockBean
    private TourRepository tourRepositoryMock;

    @MockBean
    private TourPackageRepository tourPackageRepositoryMock;

    @Test
    public void shouldCallGetTourPackageByCode_whenCreateTour_givenTourPackageDoesntExist() {
        Optional<Tour> tour = tourService.createTour(NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallGetTourPackageByCode_whenCreateTour_givenTourPackageExists() {
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.save(any())).thenReturn(buildTour());
        Optional<Tour> tour = tourService.createTour(NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        verify(tourRepositoryMock, times(1)).save(any());
        assertThat(tour).isNotEmpty();
    }

    @Test
    public void shouldCallFindById_whenUpdateTour_givenTourDoesntExist() {
        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verifyNoInteractions(tourPackageRepositoryMock);
        assertThat(updatedTour).isEmpty();
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTour_givenTourPackageDoesntExist() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildSimpleTour()));

        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        assertThat(updatedTour).isEmpty();
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithFourFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, TOUR_PRICE);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        verify(tour, times(1)).setTourPackage(any());
        verify(tour, times(1)).setTitle(TOUR_TITLE);
        verify(tour, times(1)).setDuration(TOUR_DURATION);
        verify(tour, times(1)).setPrice(TOUR_PRICE);
        assertThat(updatedTour).isPresent();
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithThreeFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, TOUR_DURATION, null);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        verify(tour, times(1)).setTourPackage(any());
        verify(tour, times(1)).setTitle(TOUR_TITLE);
        verify(tour, times(1)).setDuration(TOUR_DURATION);
        verify(tour, times(0)).setPrice(TOUR_PRICE);
        assertThat(updatedTour).isPresent();
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithTwoFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, TOUR_TITLE, null, null);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        verify(tour, times(1)).setTourPackage(any());
        verify(tour, times(1)).setTitle(TOUR_TITLE);
        verify(tour, times(0)).setDuration(TOUR_DURATION);
        verify(tour, times(0)).setPrice(TOUR_PRICE);
        assertThat(updatedTour).isPresent();
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithOneField_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, NON_EXISTING_TOUR_PACKAGE_CODE, null, null, null);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        verify(tour, times(1)).setTourPackage(any());
        verify(tour, times(0)).setTitle(TOUR_TITLE);
        verify(tour, times(0)).setDuration(TOUR_DURATION);
        verify(tour, times(0)).setPrice(TOUR_PRICE);
        assertThat(updatedTour).isPresent();
    }

    @Test
    public void shouldCallFindByIdAndGetTourPackageByCode_whenUpdateTourWithZeroFields_givenTourPackageExists() {
        Tour tour = spy(buildSimpleTour());
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(tour));
        when(tourPackageRepositoryMock.getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE)).thenReturn(Optional.of(buildTourPackage(NON_EXISTING_TOUR_PACKAGE_CODE)));
        when(tourRepositoryMock.saveAndFlush(tour)).thenReturn(buildTour());

        Optional<Tour> updatedTour = tourService.updateTour(TOUR_ID, null, null, null, null);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourPackageRepositoryMock, times(0)).getTourPackageByCode(NON_EXISTING_TOUR_PACKAGE_CODE);
        verifyNoMoreInteractions(tour);
        assertThat(updatedTour).isPresent();
    }

    @Test
    public void shouldCallFindAll_whenGetAllTours_givenNoToursExist() {
        List<Tour> tours = tourService.getAllTours();

        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(0);
    }

    @Test
    public void shouldCallFindAll_whenGetAllTours_givenTourExists() {
        when(tourRepositoryMock.findAll()).thenReturn(of(buildSimpleTour()));

        List<Tour> tours = tourService.getAllTours();

        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(1);
    }

    @Test
    public void shouldCallFindById_whenGetTourById_givenNoTourWithIdExists() {
        Optional<Tour> tour = tourService.getTourById(TOUR_ID);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallFindById_whenGetTourById_givenTourWithIdExists() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildSimpleTour()));
        Optional<Tour> tour = tourService.getTourById(TOUR_ID);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        assertThat(tour).isPresent();
    }

    @Test
    public void shouldCallFindAll_whenGetToursByLocation_givenNoTourWithLocationExists() {
        List<Tour> tours = tourService.getToursByLocation(TOUR_LOCATION);

        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(0);
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

        verify(tourRepositoryMock, times(1)).findAll();
        assertThat(tours.size()).isEqualTo(1);
    }

    @Test
    public void shouldCallGetTourPackageByCode_whenGetTourPackageCodeAndTitle_givenNoTourWithThatPackageCodeAndTitleExists() {
        Optional<Tour> tour = tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(TOUR_PACKAGE_CODE);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallGetTourPackageByCodeAndFindTourByTourPackageAndTitle_whenGetTourPackageCodeAndTitle_givenNoTourWithThatPackageCodeAndTitleExists() {
        TourPackage tourPackage = buildTourPackage(TOUR_PACKAGE_CODE);
        when(tourPackageRepositoryMock.getTourPackageByCode(TOUR_PACKAGE_CODE)).thenReturn(Optional.of(tourPackage));
        Optional<Tour> tour = tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(TOUR_PACKAGE_CODE);
        verify(tourRepositoryMock, times(1)).findTourByTourPackageAndTitle(tourPackage, TOUR_TITLE);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallGetTourPackageByCodeAndFindTourByTourPackageAndTitle_whenGetTourPackageCodeAndTitle_givenATourWithThatPackageCodeAndTitleExists() {
        TourPackage tourPackage = buildTourPackage(TOUR_PACKAGE_CODE);
        when(tourPackageRepositoryMock.getTourPackageByCode(TOUR_PACKAGE_CODE)).thenReturn(Optional.of(tourPackage));
        when(tourRepositoryMock.findTourByTourPackageAndTitle(tourPackage, TOUR_TITLE)).thenReturn(Optional.of(buildSimpleTour()));
        Optional<Tour> tour = tourService.getTourByTourPackageCodeAndTitle(TOUR_PACKAGE_CODE, TOUR_TITLE);

        verify(tourPackageRepositoryMock, times(1)).getTourPackageByCode(TOUR_PACKAGE_CODE);
        verify(tourRepositoryMock, times(1)).findTourByTourPackageAndTitle(tourPackage, TOUR_TITLE);
        assertThat(tour).isNotEmpty();
    }

    @Test
    public void shouldNotCallDeleteById_whenDeleteTourById_givenTourDoesntExist() {
        Optional<Tour> tour = tourService.deleteTourById(TOUR_ID);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourRepositoryMock, times(0)).deleteById(TOUR_ID);
        assertThat(tour).isEmpty();
    }

    @Test
    public void shouldCallDeleteById_whenDeleteTourById_givenTourExists() {
        when(tourRepositoryMock.findById(TOUR_ID)).thenReturn(Optional.of(buildSimpleTour()));
        Optional<Tour> tour = tourService.deleteTourById(TOUR_ID);

        verify(tourRepositoryMock, times(1)).findById(TOUR_ID);
        verify(tourRepositoryMock, times(1)).deleteById(TOUR_ID);
        assertThat(tour).isPresent();
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
