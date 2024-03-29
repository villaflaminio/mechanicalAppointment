package it.flaminiovilla.mechanicalappointment.util.converters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Converter
public class DurationConverter implements AttributeConverter<Duration, Long> {
     
    Logger log = LoggerFactory.getLogger(DurationConverter.class);
 
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        log.info("Convert to Long");
        return attribute.toNanos();
    }
 
    @Override
    public Duration convertToEntityAttribute(Long duration) {
        log.info("Convert to Duration");
        return Duration.of(duration, ChronoUnit.NANOS);
    }
}