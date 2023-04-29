package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Appointment business logics.
*/
public interface AppointmentService {
    ResponseEntity<List<Appointment>> findAll();

    ResponseEntity<Appointment> findById(Long id);

    ResponseEntity<Appointment> save(AppointmentDto appointment);

    ResponseEntity<Appointment> update(AppointmentDto appointment, Long id);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(Long opendayId, Long mechanicalActionId);

    ResponseEntity<List<Appointment>> findByUserPrincipal(UserPrincipal userPrincipal);
}
