package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.GarageController;
import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.service.GarageService;
import it.mtempobono.mechanicalappointment.service.impl.GarageServiceImpl;
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
    @Override
    public ResponseEntity<List<Garage>> findAll() {
        return garageService.findAll();
    }

    @Override
    public ResponseEntity<Garage> findById(Long id) {
        return garageService.findById(id);
    }

    @Override
    public ResponseEntity<Garage> save(GarageDto garage) {
        return garageService.save(garage);
    }

    @Override
    public ResponseEntity<Garage> update(GarageDto garage, Long id) {
        logger.info("update() called with garage: {} and id: {}", garage, id);
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Long id) { return garageService.delete(id);}
    //endregion CRUD Methods
}
