package it.mtempobono.mechanicalappointment.util.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mtempobono.mechanicalappointment.model.DayPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public class DayPlanConverter implements AttributeConverter<DayPlan, String> {
    private static final Logger logger = LoggerFactory.getLogger(DayPlanConverter.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DayPlan dayPlan) {
        logger.info("Convert to DayPlan -> String (JSON)");
        try {
            return objectMapper.writeValueAsString(dayPlan);
        } catch (JsonProcessingException e) {
            logger.error("Unexpected JsonProcessingException encoding json to database: " + dayPlan, e);
        }
        return null;
    }

    @Override
    public DayPlan convertToEntityAttribute(String json) {
      logger.info("Convert to String (JSON)-> DayPlan ");

        try {
            return objectMapper.readValue(json, DayPlan.class);
        } catch (IOException ex) {
            logger.error("Unexpected IOEx decoding json from database: " + json);
        }
        return null;
    }

}