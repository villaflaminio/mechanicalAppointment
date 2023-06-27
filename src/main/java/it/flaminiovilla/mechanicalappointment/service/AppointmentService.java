package it.flaminiovilla.mechanicalappointment.service;

import it.flaminiovilla.mechanicalappointment.model.entity.Appointment;
import it.flaminiovilla.mechanicalappointment.model.dto.AppointmentSearchDto;
import it.flaminiovilla.mechanicalappointment.model.dto.AppointmentVote;
import it.flaminiovilla.mechanicalappointment.model.TimePeriod;
import it.flaminiovilla.mechanicalappointment.model.dto.AppointmentDto;
import it.flaminiovilla.mechanicalappointment.model.dto.CustomAppointmentEvaluation;
import it.flaminiovilla.mechanicalappointment.model.entity.UserPrincipal;
import it.flaminiovilla.mechanicalappointment.model.entity.Vote;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Appointment business logics.
*/
public interface AppointmentService {
    ResponseEntity<List<Appointment>> findAll();

    ResponseEntity<Appointment> findById(Long id);

    ResponseEntity<Appointment> save(AppointmentDto appointment) throws Exception;

    ResponseEntity<Appointment> update(AppointmentDto appointment, Long id);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(AppointmentSearchDto appointmentSearchDto);

    ResponseEntity<List<Appointment>> findAllByUserPrincipal(UserPrincipal userPrincipal);

    ResponseEntity<List<Appointment>> findAllByUserId(Long id);

    ResponseEntity<Appointment> handleCustomAppointment(CustomAppointmentEvaluation customAppointmentEvaluation);

    ResponseEntity<Appointment> appointmentStateUpdate(String status, Long appointmentId);

    ResponseEntity<Vote> voteAppointment(AppointmentVote appointmentVote);

    ResponseEntity<Vote> modifyVote(AppointmentVote appointmentVote, Long id);

    ResponseEntity<Boolean> deleteVote(Long id);
}
