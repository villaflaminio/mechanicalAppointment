package it.mtempobono.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for Vehicle entity
 */
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicle", description = "The Vehicle APIs")
public interface VehicleController {

    /**
     * Get all the vehicles
     * @return the list of vehicles
     */
    @Operation(
            summary = "Retrieve all Vehicles",
            description = "Get all Vehicles objects. The response is a list of Vehicles objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Vehicle.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    ResponseEntity<List<Vehicle>> findAll();

    /**
     * Get the vehicle by id
     * @param id the vehicle id
     * @return the vehicle
     */
    @Operation(
            summary = "Retrieve a Vehicle by Id",
            description = "Get a Vehicle object by specifying its id. The response is Vehicle object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Vehicle.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    ResponseEntity<Vehicle> findById(@PathVariable("id") Long id);

    /**
     * Create a new vehicle
     * @param vehicle the vehicle to create
     * @return the created vehicle
     */
    @Operation(
            summary = "Create a new vehicle",
            description = "Creates a new vehicle. The response is the created Vehicle object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Vehicle.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    ResponseEntity<Vehicle> save(@RequestBody @Validated VehicleDto vehicle);

    /**
     * Update the vehicle
     * @param vehicle the vehicle to update
     * @return the updated vehicle
     */
    @Operation(
            summary = "Updates an existing vehicle",
            description = "Updates an existing vehicle. The response is the updated Vehicle object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Vehicle.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{id}")
    ResponseEntity<Vehicle> update(@RequestBody VehicleDto vehicle, @PathVariable("id") Long id);

    /**
     * Delete the vehicle
     * @param id the vehicle id
     * @return the deleted vehicle
     */
    @Operation(
            summary = "Deletes a vehicle",
            description = "Deletes a vehicle."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);


    /**
     * Get the vehicle list by user id
     * @param id the user id
     * @return the vehicle list linked to the user id
     */
    @Operation(
            summary = "Retrieve a list of Vehicles by User Id",
            description = "Get a list of Vehicles objects by specifying its user id. The response is a list of Vehicle objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Vehicle.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/user/{id}")
    ResponseEntity<List<Vehicle>> findByUserId(@PathVariable("id") Long id);


}
