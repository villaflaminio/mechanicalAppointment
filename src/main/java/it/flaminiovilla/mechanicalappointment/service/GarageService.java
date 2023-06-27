package it.flaminiovilla.mechanicalappointment.service;

import it.flaminiovilla.mechanicalappointment.model.dto.GarageDto;
import it.flaminiovilla.mechanicalappointment.model.entity.Garage;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Garage business logics.
*/
public interface GarageService {
    ResponseEntity<List<Garage>> findAll();

    ResponseEntity<Garage> findById(Long id);

    ResponseEntity<Garage> save(GarageDto garage);

    ResponseEntity<Garage> update(GarageDto garage, Long id);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<List<Garage>> findGarageByPlaceMunicipalityStartsWith(String municipality);

    ResponseEntity<List<Garage>> findGarageByPlaceProvinceStartsWith(String province);

    ResponseEntity<List<Garage>> findGarageByPlaceRegionStartsWith(String region);
}
