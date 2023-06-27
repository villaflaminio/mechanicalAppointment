package it.flaminiovilla.mechanicalappointment.service;

import it.flaminiovilla.mechanicalappointment.model.dto.VehicleDto;
import it.flaminiovilla.mechanicalappointment.model.entity.Vehicle;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Vehicle business logics.
*/
public interface VehicleService {
    ResponseEntity<List<Vehicle>> findAll();

    ResponseEntity<Vehicle> findById(Long id);

    ResponseEntity<Vehicle> save(VehicleDto vehicle) throws Exception;

    ResponseEntity<Vehicle> update(VehicleDto vehicle, Long id) throws Exception;

    ResponseEntity<Void> delete(Long id) throws Exception;

    ResponseEntity<List<Vehicle>> findByUserId(Long id);
}
