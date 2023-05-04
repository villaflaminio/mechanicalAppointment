package it.mtempobono.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mtempobono.mechanicalappointment.model.TimePeriod;
import it.mtempobono.mechanicalappointment.model.dto.AppointmentDto;
import it.mtempobono.mechanicalappointment.model.dto.CustomAppointmentEvaluation;
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
import it.mtempobono.mechanicalappointment.model.entity.UserPrincipal;
import it.mtempobono.mechanicalappointment.model.entity.Vote;
import it.mtempobono.mechanicalappointment.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for Appointment entity
 */
@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment", description = "The Appointment APIs")
public interface AppointmentController {

    /**
     * Get all the appointments
     *
     * @return the list of appointments
     */
    @Operation(
            summary = "Retrieve all Appointments",
            description = "Get all Appointments objects. The response is a list of Appointments objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping
    ResponseEntity<List<Appointment>> findAll();

    /**
     * Get the appointment by id
     *
     * @param id the appointment id
     * @return the appointment
     */
    @Operation(
            summary = "Retrieve a Appointment by Id",
            description = "Get a Appointment object by specifying its id. The response is Appointment object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    ResponseEntity<Appointment> findById(@PathVariable("id") Long id);

    //getAvailableAppointments
    @Operation(
            summary = "Retrieve all available Appointments",
            description = "Get all available Appointments objects. The response is a list of Appointments objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/availableTimeSlots")
    ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(Long opendayId, Long mechanicalActionId, boolean externalTimeslot);

    //get all appointments by user principal
    @Operation(
            summary = "Retrieve all Appointments by user principal",
            description = "Get all Appointments objects by user principal. The response is a list of Appointments objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/myappointments")
    ResponseEntity<List<Appointment>> findAllByUserPrincipal(@CurrentUser UserPrincipal userPrincipal);

    //find all appointments by user id
    @Operation(
            summary = "Retrieve all Appointments by user id",
            description = "Get all Appointments objects by user id. The response is a list of Appointments objects."
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/appointmentsUser/{id}")
    ResponseEntity<List<Appointment>> findAllByUserId(@PathVariable("id") Long id);


    @Operation(
            summary = "Handle custom technical evaluation",
            description = " Handle custom technical evaluation. The response is a list of Appointments objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/customAppointment")
    ResponseEntity<Appointment> handleCustomAppointment(CustomAppointmentEvaluation customAppointmentEvaluation);


    /**
     * Update the appointment state
     *
     * @param status        the appointment status
     * @param appointmentId the appointment id
     * @return the updated appointment
     */
    @Operation(
            summary = "Update the appointment state",
            description = "Update the appointment state. The response is the updated Appointment object. The status can be CONFIRMED,  FINISHED, REJECTED")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/{appointmentId}/state/{status}")
    ResponseEntity<Appointment> appointmentStateUpdate(@PathVariable("status") @Schema(example = "CONFIRMED") String status, @PathVariable("appointmentId") @Schema(example = "1") Long appointmentId);

    /**
     * Create a new appointment
     *
     * @param appointment the appointment to create
     * @return the created appointment
     */
    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment. The response is the created Appointment object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping
    ResponseEntity<Appointment> save(@RequestBody @Validated AppointmentDto appointment) throws Exception;

    /**
     * Update the appointment
     *
     * @param appointment the appointment to update
     * @return the updated appointment
     */
    @Operation(
            summary = "Updates an existing appointment",
            description = "Updates an existing appointment. The response is the updated Appointment object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PutMapping("/{id}")
    ResponseEntity<Appointment> update(@RequestBody AppointmentDto appointment, @PathVariable("id") Long id);

    /**
     * Delete the appointment
     *
     * @param id the appointment id
     * @return the deleted appointment
     */
    @Operation(
            summary = "Deletes a appointment",
            description = "Deletes a appointment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);


    @Operation(
            summary = "Vote appointment",
            description = "Vote appointment"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/vote")
    ResponseEntity<Vote> voteAppointment(@RequestBody AppointmentVote appointmentVote);

    @Operation(
            summary = "Modify vote",
            description = "Modify vote"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PutMapping("/vote/{id}")
    ResponseEntity<Vote> modifyVote(@RequestBody AppointmentVote appointmentVote , @PathVariable("id") Long id);

    //delete vote
    @Operation(
            summary = "Delete vote",
            description = "Delete vote"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @DeleteMapping("/vote/{id}")
    ResponseEntity<Boolean> deleteVote(@PathVariable("id") Long id);
}
