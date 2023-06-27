package it.flaminiovilla.mechanicalappointment.service;

import it.flaminiovilla.mechanicalappointment.model.entity.Place;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains Place business logics.
*/
public interface PlaceService {
    ResponseEntity<List<Place>> findAll(int page, int size);

    ResponseEntity<Place> findById(Long id);

    ResponseEntity<Void> delete(Long id) throws Exception;

    ResponseEntity<List<Place>> findPlaceByMunicipalityStartsWith(String municipality) throws Exception;

    ResponseEntity<List<Place>> findPlaceByProvinceStartsWith(String province) throws Exception;

    ResponseEntity<List<Place>> findPlaceByRegionStartsWith(String region) throws Exception;
}
