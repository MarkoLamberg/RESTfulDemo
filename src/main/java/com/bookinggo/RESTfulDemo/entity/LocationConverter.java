package com.bookinggo.RESTfulDemo.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    public String convertToDatabaseColumn(Location location) {
        return location.getLabel();
    }

    @Override
    public Location convertToEntityAttribute(String s) {
        return Location.findByLabel(s);
    }
}
