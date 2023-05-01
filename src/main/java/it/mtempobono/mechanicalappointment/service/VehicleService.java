package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Vehicle business logics.
*/
public interface VehicleService {
    ResponseEntity<List<Vehicle>> findAll();

    ResponseEntity<Vehicle> findById(Long id);

    ResponseEntity<Vehicle> save(VehicleDto vehicle);

    ResponseEntity<Vehicle> update(VehicleDto vehicle, Long id);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<List<Vehicle>> findByUserId(Long id);
}
