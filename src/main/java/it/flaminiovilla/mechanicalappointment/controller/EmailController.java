package it.flaminiovilla.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/emails")
@Tag(name = "Emails", description = "The emails APIs")
public interface EmailController {

    /**
     * Email the customer with the appointment details for the appointment confirmation.
     * @param appointmentId the appointment details
     * @return 200 OK if the email has been sent
     */
    @Operation(summary = "Email the customer with the appointment details for the appointment confirmation.")
    @GetMapping("/sendCustomAppointmentApprovedMail/{appointmentId}")
    ResponseEntity<Void> sendAppointmentApprovedMail(@PathVariable("appointmentId") Long appointmentId);

    /**
     * Email the customer with the appointment details for the appointment rejection.
     * @param appointmentId the appointment details
     * @return 200 OK if the email has been sent
     */
    @Operation(summary = "Email the customer with the appointment details for the appointment rejection.")
    @GetMapping("/sendCustomAppointmentRejectedMail/{appointmentId}")
    ResponseEntity<Void> sendAppointmentRejectedMail(@PathVariable("appointmentId") Long appointmentId);

    /**
     * Email the customer with the finished appointment details .
     * @param appointmentId the appointment details
     * @return 200 OK if the email has been sent
     */
    @Operation(summary = "Email the customer with the finished appointment details")
    @GetMapping("/sendFinishedAppointmentData/{appointmentId}")
    ResponseEntity<Void> sendFinishedAppointmentData(@PathVariable("appointmentId") Long appointmentId);

    /**
     * Email the customer with the just created appointment details .
     * @param appointmentId the appointment details
     * @return 200 OK if the email has been sent
     */
    @Operation(summary = "Email the customer with the just created appointment details .")
    @GetMapping("/sendNewAppointmentMail/{appointmentId}")
    ResponseEntity<Void> sendNewAppointmentMail(@PathVariable("appointmentId") Long appointmentId);

    /**
     * Email the customer with the deleted appointment details .
     * @param appointmentId the appointment details
     * @return 200 OK if the email has been sent
     */
    @Operation(summary = "Email the customer with the deleted appointment details")
    @GetMapping("/sendDeletedAppointmentData/{appointmentId}")
    ResponseEntity<Void> sendDeletedAppointmentData(@PathVariable("appointmentId") Long appointmentId);

}
