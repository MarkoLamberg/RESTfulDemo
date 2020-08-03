package com.bookinggo.RESTfulDemo.config;

import com.bookinggo.RESTfulDemo.dto.BookingDto;
import com.bookinggo.RESTfulDemo.dto.ExpandedBookingDto;
import com.bookinggo.RESTfulDemo.entity.TourBooking;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(TourBooking.class, BookingDto.class)
                .addMapping(src -> src.getCustomer().getId(), BookingDto::setCustomerId)
                .addMapping(TourBooking::getTotalPriceString, BookingDto::setTotalPrice);

        modelMapper.createTypeMap(TourBooking.class, ExpandedBookingDto.class)
                .addMapping(src -> src.getCustomer().getId(), ExpandedBookingDto::setCustomerId)
                .addMapping(TourBooking::getTotalPriceString, ExpandedBookingDto::setTotalPrice)
                .addMapping(src -> src.getTour().getId(), ExpandedBookingDto::setTourId);

        return modelMapper;
    }
}
