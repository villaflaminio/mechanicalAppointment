package it.flaminiovilla.mechanicalappointment.controller.impl;

import it.flaminiovilla.mechanicalappointment.controller.AppointmentController;
import it.flaminiovilla.mechanicalappointment.model.TimePeriod;
import it.flaminiovilla.mechanicalappointment.model.dto.AppointmentDto;
import it.flaminiovilla.mechanicalappointment.model.dto.AppointmentSearchDto;
import it.flaminiovilla.mechanicalappointment.model.dto.AppointmentVote;
import it.flaminiovilla.mechanicalappointment.model.dto.CustomAppointmentEvaluation;
import it.flaminiovilla.mechanicalappointment.model.entity.Appointment;
import it.flaminiovilla.mechanicalappointment.service.AppointmentService;
import it.flaminiovilla.mechanicalappointment.model.entity.UserPrincipal;
import it.flaminiovilla.mechanicalappointment.model.entity.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which implements all
 * the operations for Appointment entity
 */
@RestController
public class AppointmentControllerImpl implements AppointmentController {

    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    // endregion Fields

    //region CRUD Methods
    @Override
    public ResponseEntity<List<Appointment>> findAll() {
        return appointmentService.findAll();
    }

    @Override
    public ResponseEntity<Appointment> findById(Long id) {
        return appointmentService.findById(id);
    }

    @Override
    public ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(AppointmentSearchDto appointmentSearchDto) {
       return appointmentService.getAvailableAppointmentsTimeSlots(appointmentSearchDto);
    }

    @Override
    public ResponseEntity<List<Appointment>> findAllByUserPrincipal(UserPrincipal userPrincipal) {
        return appointmentService.findAllByUserPrincipal(userPrincipal);
    }

    @Override
    public ResponseEntity<List<Appointment>> findAllByUserId(Long id) {
        return  appointmentService.findAllByUserId(id);
    }

    @Override
    public ResponseEntity<Appointment> handleCustomAppointment(CustomAppointmentEvaluation customAppointmentEvaluation) {
        return appointmentService.handleCustomAppointment(customAppointmentEvaluation);
    }

    @Override
    public ResponseEntity<Appointment> appointmentStateUpdate(String status, Long appointmentId) {
        return  appointmentService.appointmentStateUpdate(status, appointmentId);
    }

    @Override
    public ResponseEntity<Appointment> save(AppointmentDto appointment) throws Exception {
        return appointmentService.save(appointment);
    }

    @Override
    public ResponseEntity<Appointment> update(AppointmentDto appointment, Long id) {
        return appointmentService.update(appointment, id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) { return appointmentService.delete(id);}

    @Override
    public ResponseEntity<Vote> voteAppointment(AppointmentVote appointmentVote) {
        return  appointmentService.voteAppointment(appointmentVote);
    }

    @Override
    public ResponseEntity<Vote> modifyVote(AppointmentVote appointmentVote, Long id) {
        return appointmentService.modifyVote(appointmentVote, id);
    }

    @Override
    public ResponseEntity<Boolean> deleteVote(Long id) {
        return  appointmentService.deleteVote(id);
    }
    //endregion CRUD Methods
}
