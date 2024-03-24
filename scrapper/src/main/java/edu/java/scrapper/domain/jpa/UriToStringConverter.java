package edu.java.scrapper.domain.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;
import java.net.URISyntaxException;

@Converter
public class UriToStringConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public URI convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : new URI(dbData);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI: " + dbData, e);
        }
    }
}
