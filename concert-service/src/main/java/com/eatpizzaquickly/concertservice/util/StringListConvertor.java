package com.eatpizzaquickly.concertservice.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConvertor implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return attribute != null ? String.join(SPLIT_CHAR, attribute) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return s != null && !s.isEmpty() ? Arrays.stream(s.split(SPLIT_CHAR)).toList() : List.of();
    }
}
