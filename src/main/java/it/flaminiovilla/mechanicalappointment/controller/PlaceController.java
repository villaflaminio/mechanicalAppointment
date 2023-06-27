package it.flaminiovilla.mechanicalappointment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.flaminiovilla.mechanicalappointment.model.entity.Place;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller interface which maps all
 * the operations for Place entity
 */
@RestController
@RequestMapping("/api/places")
@Tag(name = "Place", description = "The Place APIs")
public interface PlaceController {

    //region CRUD Methods
    /**
     * Get all places
     * @param page the page number
     *             (default value = 0)
     *             (min = 0)
     * @param size the page size
     *
     * @return the list of places
     */
    @Operation(
            summary = "Retrieve all Places",
            description = "Get all Places objects. The response is a list of Places objects."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Place.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    ResponseEntity<List<Place>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size",  defaultValue = "10") int size
    );

    /**
     * Get the place by id
     * @param id the place id
     * @return the place
     */
    @Operation(
            summary = "Retrieve a Place by Id",
            description = "Get a Place object by specifying its id. The response is Place object."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Place.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    ResponseEntity<Place> findById(@PathVariable("id") Long id);

    /**
     * Delete the place
     * @param id the place id
     * @return the deleted place
     */
    @Operation(
            summary = "Deletes a place",
            description = "Deletes a place."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id) throws Exception;
    //endregion CRUD Methods

    // region Autocomplete Methods

    /**
     * Get the place by municipality starts with the specified string
     * @return the list of places that matches the specified string in the municipality field
     */
    @Operation(
            summary = "Get the place by municipality starts with the specified string",
            description = "Get the place by municipality starts with the specified string"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Place.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findPlaceByMunicipalityStartsWith")
    ResponseEntity<List<Place>> findPlaceByMunicipalityStartsWith(
            @RequestParam(value = "municipality") String municipality) throws Exception;

    /**
     * Get the place by province starts with the specified string
     * @return the list of places that matches the specified string in the province field
     */
    @Operation(
            summary = "Get the place by province starts with the specified string",
            description = "Get the place by province starts with the specified string"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Place.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findPlaceByProvinceStartsWith")
    ResponseEntity<List<Place>> findPlaceByProvinceStartsWith(
            @RequestParam(value = "province") String province) throws Exception;

    /**
     * Get the place by region starts with the specified string
     * @return the list of places that matches the specified string in the region field
     */
    @Operation(
            summary = "Get the place by region starts with the specified string",
            description = "Get the place by region starts with the specified string"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content (
                    array = @ArraySchema(schema = @Schema(implementation = Place.class)),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/findPlaceByRegionStartsWith")
    ResponseEntity<List<Place>> findPlaceByRegionStartsWith(
            @RequestParam(value = "region") String region) throws Exception;

    // endregion Autocomplete Methods
}
