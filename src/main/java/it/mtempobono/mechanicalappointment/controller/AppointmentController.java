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
import it.mtempobono.mechanicalappointment.model.entity.Appointment;
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
     * @return the list of appointments
     */
    @Operation(
            summary = "Retrieve all Appointments",
            description = "Get all Appointments objects. The response is a list of Appointments objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    ResponseEntity<List<Appointment>> findAll();

    /**
     * Get the appointment by id
     * @param id the appointment id
     * @return the appointment
     */
    @Operation(
            summary = "Retrieve a Appointment by Id",
            description = "Get a Appointment object by specifying its id. The response is Appointment object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    ResponseEntity<Appointment> findById(@PathVariable("id") Long id);

    //getAvailableAppointments
    @Operation(
            summary = "Retrieve all available Appointments",
            description = "Get all available Appointments objects. The response is a list of Appointments objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Appointment.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/availableTimeSlots")
    ResponseEntity<List<TimePeriod>> getAvailableAppointmentsTimeSlots(Long opendayId , Long mechanicalActionId);


    /**
     * Create a new appointment
     * @param appointment the appointment to create
     * @return the created appointment
     */
    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment. The response is the created Appointment object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    ResponseEntity<Appointment> save(@RequestBody @Validated AppointmentDto appointment);

    /**
     * Update the appointment
     * @param appointment the appointment to update
     * @return the updated appointment
     */
    @Operation(
            summary = "Updates an existing appointment",
            description = "Updates an existing appointment. The response is the updated Appointment object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Appointment.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{id}")
    ResponseEntity<Appointment> update(@RequestBody AppointmentDto appointment, @PathVariable("id") Long id);

    /**
     * Delete the appointment
     * @param id the appointment id
     * @return the deleted appointment
     */
    @Operation(
            summary = "Deletes a appointment",
            description = "Deletes a appointment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

}
