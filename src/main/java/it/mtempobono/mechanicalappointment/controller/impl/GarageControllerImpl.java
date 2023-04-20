package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.GarageController;
import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which implements all
 * the operations for Garage entity
 */
@RestController
public class GarageControllerImpl implements GarageController {

    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(GarageController.class);

    @Autowired
    private GarageService garageService;

    // endregion Fields

    //region CRUD Methods
    /**
     * Get all the garages
     *
     * @return the list of garages
     */
    @Override
    public ResponseEntity<List<GarageDto>> findAll() {
        return garageService.findAll();
    }

    /**
     * Get the garage by id
     *
     * @param id the garage id
     * @return the garage
     */
    @Override
    public ResponseEntity<GarageDto> findById(Long id) {
        return garageService.findById(id);
    }

    /**
     * Create a new garage
     *
     * @param garage the garage to create
     * @return the created garage
     */
    @Override
    public ResponseEntity<GarageDto> save(GarageDto garage) {
        logger.info("save() called with garage: {}", garage);
        return null;
    }

    /**
     * Update the garage
     *
     * @param garage the garage to update
     * @param id
     * @return the updated garage
     */
    @Override
    public ResponseEntity<GarageDto> update(GarageDto garage, Long id) {
        logger.info("update() called with garage: {} and id: {}", garage, id);
        return null;
    }

    /**
     * Delete the garage
     *
     * @param id the garage id
     * @return the deleted garage
     */
    @Override
    public ResponseEntity<Void> delete(Long id) {
        logger.info("delete() called with id: {}", id);
        return null;
    }
    //endregion CRUD Methods
}
