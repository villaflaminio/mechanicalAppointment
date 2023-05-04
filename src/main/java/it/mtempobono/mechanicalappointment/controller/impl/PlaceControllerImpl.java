package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.controller.PlaceController;
import it.mtempobono.mechanicalappointment.model.entity.Place;
import it.mtempobono.mechanicalappointment.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which implements all
 * the operations for Place entity
 */
@RestController
public class PlaceControllerImpl implements PlaceController {

    // region Fields

    @Autowired
    private PlaceService placeService;

    // endregion Fields

    //region CRUD Methods
    @Override
    public ResponseEntity<List<Place>> findAll(int page, int size) {
        return placeService.findAll(page, size);
    }

    @Override
    public ResponseEntity<Place> findById(Long id) {
        return placeService.findById(id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) throws Exception { return placeService.delete(id);}
    //endregion CRUD Methods

    // region Autocomplete methods
    @Override
    public ResponseEntity<List<Place>> findPlaceByMunicipalityStartsWith(String municipality) throws Exception {
        return placeService.findPlaceByMunicipalityStartsWith(municipality);
    }

    @Override
    public ResponseEntity<List<Place>> findPlaceByProvinceStartsWith(String province) throws Exception {
        return placeService.findPlaceByProvinceStartsWith(province);
    }

    @Override
    public ResponseEntity<List<Place>> findPlaceByRegionStartsWith(String region) throws Exception {
        return placeService.findPlaceByRegionStartsWith(region);
    }
    // endregion
}
