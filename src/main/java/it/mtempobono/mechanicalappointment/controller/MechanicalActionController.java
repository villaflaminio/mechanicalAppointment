package it.mtempobono.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mtempobono.mechanicalappointment.model.dto.MechanicalActionDto;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for MechanicalAction entity
 */
@RestController
@RequestMapping("/api/mechanicalActions")
@Tag(name = "MechanicalAction", description = "The MechanicalAction APIs")
public interface MechanicalActionController {

    /**
     * Get all the mechanicalActions
     * @return the list of mechanicalActions
     */
    @Operation(
            summary = "Retrieve all MechanicalActions",
            description = "Get all MechanicalActions objects. The response is a list of MechanicalActions objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = MechanicalAction.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    ResponseEntity<List<MechanicalAction>> findAll();

    /**
     * Get the mechanicalAction by id
     * @param id the mechanicalAction id
     * @return the mechanicalAction
     */
    @Operation(
            summary = "Retrieve a MechanicalAction by Id",
            description = "Get a MechanicalAction object by specifying its id. The response is MechanicalAction object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MechanicalAction.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    ResponseEntity<MechanicalAction> findById(@PathVariable("id") Long id);

    /**
     * Create a new mechanicalAction
     * @param mechanicalAction the mechanicalAction to create
     * @return the created mechanicalAction
     */
    @Operation(
            summary = "Create a new mechanicalAction",
            description = "Creates a new mechanicalAction. The response is the created MechanicalAction object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MechanicalAction.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    ResponseEntity<MechanicalAction> save(@RequestBody @Validated MechanicalActionDto mechanicalAction);

    /**
     * Update the mechanicalAction
     * @param mechanicalAction the mechanicalAction to update
     * @return the updated mechanicalAction
     */
    @Operation(
            summary = "Updates an existing mechanicalAction",
            description = "Updates an existing mechanicalAction. The response is the updated MechanicalAction object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MechanicalAction.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping("/{id}")
    ResponseEntity<MechanicalAction> update(@RequestBody MechanicalActionDto mechanicalAction, @PathVariable("id") Long id);

    /**
     * Delete the mechanicalAction
     * @param id the mechanicalAction id
     * @return the deleted mechanicalAction
     */
    @Operation(
            summary = "Deletes a mechanicalAction",
            description = "Deletes a mechanicalAction."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

}
