package it.mtempobono.mechanicalappointment.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateAppointmentRequest {

    public Long openDayId;
    public Long mechanicalActionId;
    public Long vehicleId;
    public String comment;

    public LocalDate startTime;

}
