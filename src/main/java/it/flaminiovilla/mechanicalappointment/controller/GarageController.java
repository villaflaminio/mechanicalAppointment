package it.flaminiovilla.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.flaminiovilla.mechanicalappointment.model.dto.GarageDto;
import it.flaminiovilla.mechanicalappointment.model.entity.Garage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for Garage entity
 */
@RestController
@RequestMapping("/api/garages")
@Tag(name = "Garage", description = "The Garage APIs")
public interface GarageController {

    //region CRUD Methods

    /**
     * Get all the garages
     *
     * @return the list of garages
     */
    @Operation(
            summary = "Retrieve all Garages",
            description = "Get all Garages objects. The response is a list of Garages objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(
                    array = @ArraySchema(schema = @Schema(implementation = Garage.class)),
                    mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping
    ResponseEntity<List<Garage>> findAll();

    /**
     * Get the garage by id
     *
     * @param id the garage id
     * @return the garage
     */
    @Operation(
            summary = "Retrieve a Garage by Id",
            description = "Get a Garage object by specifying its id. The response is Garage object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Garage.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    ResponseEntity<Garage> findById(@PathVariable("id") Long id);

    /**
     * Create a new garage
     *
     * @param garage the garage to create
     * @return the created garage
     */
    @Operation(
            summary = "Create a new garage",
            description = "Creates a new garage. The response is the created Garage object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Garage.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping
    ResponseEntity<Garage> save(@RequestBody @Validated GarageDto garage);

    /**
     * Update the garage
     *
     * @param garage the garage to update
     * @return the updated garage
     */
    @Operation(
            summary = "Updates an existing garage",
            description = "Updates an existing garage. The response is the updated Garage object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Garage.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PutMapping("/{id}")
    ResponseEntity<Garage> update(@RequestBody GarageDto garage, @PathVariable("id") Long id);

    /**
     * Delete the garage
     *
     * @param id the garage id
     * @return the deleted garage
     */
    @Operation(
            summary = "Deletes a garage",
            description = "Deletes a garage."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);
    //endregion CRUD Methods

    //region Custom Methods

    /**
     * Get the garages by place municipality starts with
     *
     * @param municipality the municipality starts with string
     * @return the list of garages by municipality starts with
     */
    @Operation(
            summary = "Retrieve Garages by municipality starts with",
            description = "Get the garages by place municipality starts with. The response is a list of garages by municipality starts with."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Garage.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/findGarageByPlaceMunicipalityStartsWith")
    ResponseEntity<List<Garage>> findGarageByPlaceMunicipalityStartsWith(
            @RequestParam("municipality") String municipality
    );

    /**
     * Get the garages by place province starts with
     *
     * @param province the province starts with string
     * @return the list of garages by province starts with
     */
    @Operation(
            summary = "Retrieve Garages by province starts with",
            description = "Get the garages by place province starts with. The response is a list of garages by province starts with."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Garage.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/findGarageByPlaceProvinceStartsWith")
    ResponseEntity<List<Garage>> findGarageByPlaceProvinceStartsWith(
            @RequestParam("province") String province
    );

    /**
     * Get the garages by place region starts with
     *
     * @param region the region starts with string
     * @return the list of garages by region starts with
     */
    @Operation(
            summary = "Retrieve Garages by region starts with",
            description = "Get the garages by place region starts with. The response is a list of garages by region starts with."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Garage.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @GetMapping("/findGarageByPlaceRegionStartsWith")
    ResponseEntity<List<Garage>> findGarageByPlaceRegionStartsWith(
            @RequestParam("region") String region
    );

    //endregion Custom Methods

}
