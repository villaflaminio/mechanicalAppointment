package it.mtempobono.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for OpenDay entity
 */
@RestController
@RequestMapping("/api/opendays")
@Tag(name = "OpenDay", description = "The OpenDay APIs")
public interface OpenDayController {

    /**
     * Get all the open days
     * @return the list of open days
     */
    @Operation(
            summary = "Retrieve all OpenDays",
            description = "Get all OpenDays objects. The response is a list of OpenDays objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = OpenDay.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    ResponseEntity<List<OpenDay>> findAll();

    /**
     * Get the open day by id
     * @param id the open day id
     * @return the open day
     */
    @Operation(
            summary = "Retrieve a OpenDay by Id",
            description = "Get a OpenDay object by specifying its id. The response is OpenDay object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = OpenDay.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    ResponseEntity<OpenDay> findById(@PathVariable("id") Long id);

    /**
     * Create a new open day
     * @param openDay the open day to create
     * @return the created open day
     */
    @Operation(
            summary = "Create a new open day",
            description = "Creates a new open day. The response is the created OpenDay object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = OpenDay.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    ResponseEntity<OpenDay> save(@RequestBody @Validated OpenDayDto openDay);

    /**
     * Update the open day
     * @param openDay the open day to update
     * @return the updated open day
     */
    @Operation(
            summary = "Updates an existing open day",
            description = "Updates an existing open day. The response is the updated OpenDay object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = OpenDay.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{id}")
    ResponseEntity<OpenDay> update(@RequestBody OpenDayDto openDay, @PathVariable("id") Long id);

    /**
     * Delete the open day
     * @param id the open day id
     * @return the deleted open day
     */
    @Operation(
            summary = "Deletes a open day",
            description = "Deletes a open day."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

}
