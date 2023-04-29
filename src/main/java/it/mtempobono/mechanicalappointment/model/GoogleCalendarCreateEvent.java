package it.mtempobono.mechanicalappointment.model;

import com.google.api.client.util.DateTime;
import lombok.Data;

@Data
public class GoogleCalendarCreateEvent {

    private String summary;
    private String location;
    private String description;
    private DateTime startTime;
    private DateTime endTime;

}
