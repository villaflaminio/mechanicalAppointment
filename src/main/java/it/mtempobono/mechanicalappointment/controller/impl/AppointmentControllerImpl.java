package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.AppointmentController;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.UserPrincipal;
import it.mtempobono.mechanicalappointment.service.AppointmentService;
import it.mtempobono.mechanicalappointment.service.impl.AppointmentServiceImpl;
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
    public ResponseEntity<Appointment> save(AppointmentDto appointment) {
        return appointmentService.save(appointment);
    }

    @Override
    public ResponseEntity<Appointment> update(AppointmentDto appointment, Long id) {
        return appointmentService.update(appointment, id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) { return appointmentService.delete(id);}
    //endregion CRUD Methods

    @Override
    public ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(Long opendayId, Long mechanicalActionId) {
        return appointmentService.getAvailableAppointmentsTimeSlots(opendayId, mechanicalActionId);
    }

    @Override
    public ResponseEntity<List<Appointment>> findByUserPrincipal(UserPrincipal userPrincipal) {
        return appointmentService.findByUserPrincipal(userPrincipal);
    }
}
