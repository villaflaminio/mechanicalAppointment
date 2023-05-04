package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.EmailController;
import it.mtempobono.mechanicalappointment.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for the emails.
 */
@RestController
public class EmailControllerImpl implements EmailController {

    @Autowired
    private EmailService emailService;

    @Override
    public ResponseEntity<Void> sendAppointmentApprovedMail(Long appointmentId) {
        return emailService.sendAppointmentApprovedMail(appointmentId);
    }

    @Override
    public ResponseEntity<Void> sendAppointmentRejectedMail(Long appointmentId) {
        return emailService.sendAppointmentRejectedMail(appointmentId);
    }

//    @Override
//    public ResponseEntity<Void> sendStockAppointmentApprove(Long appointmentId) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<Void> sendStockAppointmentReject(Long appointmentId) {
//        return null;
//    }

    @Override
    public ResponseEntity<Void> sendFinishedAppointmentData(Long appointmentId) {
        return emailService.sendFinishedAppointmentData(appointmentId);
    }
}
