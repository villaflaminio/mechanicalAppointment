package it.mtempobono.mechanicalappointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSearchDto implements Serializable {
    private Long opendayId;
    private Long mechanicalActionId;
    private boolean externalTimeslot;
}
