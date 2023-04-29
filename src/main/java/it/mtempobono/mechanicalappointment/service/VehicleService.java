package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import org.springframework.http.ResponseEntity;

public interface VehicleService {
    ResponseEntity<Vehicle> save(VehicleDto vehicleDto);
}
