package it.mtempobono.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for OpenDay entity
 */
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "vehicles", description = "The vehicles APIs")
public interface VehiclesController {


    /**
     * Create a new vehicle
     * @param vehicleDto the open day to create
     * @return the created open day
     */
    @Operation(
            summary = "Create a new vehicle",
            description = "Creates a vehicle. The response is the created vehicle object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = OpenDay.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    ResponseEntity<Vehicle> save(@RequestBody @Validated VehicleDto vehicleDto);


}
