package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
* Class that contains Garage business logics.
*/
public interface GarageService {
    ResponseEntity<List<Garage>> findAll();

    ResponseEntity<Garage> findById(Long id);

    ResponseEntity<Garage> save(GarageDto garage);

    ResponseEntity<Garage> update(Garage garage, Long id);

    ResponseEntity<Void> delete(Long id);
}
