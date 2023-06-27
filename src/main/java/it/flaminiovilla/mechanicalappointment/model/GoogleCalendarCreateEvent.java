package it.flaminiovilla.mechanicalappointment.model;

import com.google.api.client.util.DateTime;

public class GoogleCalendarCreateEvent {

    //region Fields
    private String summary;
    private String location;
    private String description;
    private DateTime startTime;
    private DateTime endTime;
    //endregion

    //region Constructors
    public GoogleCalendarCreateEvent() {
    }

    public GoogleCalendarCreateEvent(String summary, String location, String description, DateTime startTime, DateTime endTime) {
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    //endregion

    //region Getters and Setters

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }
    //endregion
}
