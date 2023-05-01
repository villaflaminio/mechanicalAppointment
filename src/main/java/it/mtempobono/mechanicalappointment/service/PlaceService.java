package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.entity.Place;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Place business logics.
*/
public interface PlaceService {
    ResponseEntity<List<Place>> findAll(int page, int size);

    ResponseEntity<Place> findById(Long id);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<List<Place>> findPlaceByMunicipalityStartsWith(String municipality);

    ResponseEntity<List<Place>> findPlaceByProvinceStartsWith(String province);

    ResponseEntity<List<Place>> findPlaceByRegionStartsWith(String region);
}
