package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.VehiclesController;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import it.mtempobono.mechanicalappointment.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehiclesControllerImpl implements VehiclesController {

    @Autowired
    private VehicleService vehicleService;

    @Override
    public ResponseEntity<Vehicle> save(VehicleDto vehicleDto) {
        return vehicleService.save(vehicleDto);
    }
}
