package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.VehicleController;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import it.mtempobono.mechanicalappointment.service.VehicleService;
import it.mtempobono.mechanicalappointment.service.impl.VehicleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which implements all
 * the operations for Vehicle entity
 */
@RestController
public class VehicleControllerImpl implements VehicleController {

    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;

    // endregion Fields

    //region CRUD Methods
    @Override
    public ResponseEntity<List<Vehicle>> findAll() {
        return vehicleService.findAll();
    }

    @Override
    public ResponseEntity<Vehicle> findById(Long id) {
        return vehicleService.findById(id);
    }

    @Override
    public ResponseEntity<Vehicle> save(VehicleDto vehicle) {
        return vehicleService.save(vehicle);
    }

    @Override
    public ResponseEntity<Vehicle> update(VehicleDto vehicle, Long id) {
        return vehicleService.update(vehicle, id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) { return vehicleService.delete(id);}
    //endregion CRUD Methods


    //region Public Methods
    @Override
    public ResponseEntity<List<Vehicle>> findByUserId(Long id) {
        return vehicleService.findByUserId(id);
    }
    //endregion Public Methods
}
