package it.mtempobono.mechanicalappointment.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mtempobono.mechanicalappointment.model.DayPlan;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class TimePeriodConverter implements AttributeConverter<TimePeriod, String> {
    private static final Logger logger = LoggerFactory.getLogger(TimePeriodConverter.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TimePeriod dayPlan) {
        logger.info("Convert to TimePeriod -> String (JSON)");
        try {
            return objectMapper.writeValueAsString(dayPlan);
        } catch (JsonProcessingException e) {
            logger.error("Unexpected JsonProcessingException encoding json to database: " + dayPlan, e);
        }
        return null;
    }

    @Override
    public TimePeriod convertToEntityAttribute(String json) {
      logger.info("Convert to String (JSON)-> TimePeriod ");

        try {
            return objectMapper.readValue(json, TimePeriod.class);
        } catch (IOException ex) {
            logger.error("Unexpected IOEx decoding json from database: " + json);
        }
        return null;
    }

}