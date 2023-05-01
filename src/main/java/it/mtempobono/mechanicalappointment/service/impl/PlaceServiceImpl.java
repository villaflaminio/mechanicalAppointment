package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.model.entity.Place;
import it.mtempobono.mechanicalappointment.repository.PlaceRepository;
import it.mtempobono.mechanicalappointment.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains Place business logics.
 */
@Service
public class PlaceServiceImpl implements PlaceService {
    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(PlaceServiceImpl.class);

    @Autowired
    private PlaceRepository placeRepository;

    // endregion Fields

    // region Methods
    /**
     * Get all the places
     * @return the list of places
     */
    @Override
    public ResponseEntity<List<Place>> findAll(int page, int size){
        try {
            logger.debug("Enter into findAll() method");

            Pageable pageable = PageRequest.of(page, size);
            Page<Place> allPlaces = placeRepository.findAll(pageable);

            return ResponseEntity.ok(allPlaces.getContent());
        } catch (Exception e) {
            logger.error("Error in findAll() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findAll() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the place by id
     * @param id the place id
     * @return the place
     */
    @Override
    public ResponseEntity<Place> findById(Long id){
        try {
            logger.info("findById() called with id: {}", id);
            Optional<Place> place = placeRepository.findById(id);
            return place.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in findById() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findById() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete the place
     * @param id the place id
     * @return the deleted place
     */
    @Override
    public ResponseEntity<Void> delete(Long id){
        try {
            logger.info("delete() called with id: {}", id);

            // Check if the place exists
            if (!placeRepository.existsById(id)) {
                logger.error("Place not found");
                return ResponseEntity.notFound().build();
            }

            placeRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from delete() method");
        }
        return ResponseEntity.badRequest().build();
    }

    // endregion Methods

    // region Autocomplete methods

    /**
     * Get the places by municipality starts with
     * @param municipality the place municipality
     * @return the list of places that starts with the municipality param
     */
    @Override
    public ResponseEntity<List<Place>> findPlaceByMunicipalityStartsWith(String municipality){
        try {
            logger.info("findPlaceByMunicipalityStartsWith() called with municipality starts with: {}", municipality);
            List<Place> places = placeRepository.findPlaceByMunicipalityStartsWith(municipality);

            // If no places found, return not found
            if (places.isEmpty())
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(places);
        } catch (Exception e) {
            logger.error("Error in findPlaceByMunicipalityStartsWith() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findPlaceByMunicipalityStartsWith() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the places by municipality starts with
     * @param province the place municipality
     * @return the list of places that starts with the municipality param
     */
    @Override
    public ResponseEntity<List<Place>> findPlaceByProvinceStartsWith(String province){
        try {
            logger.info("findPlaceByProvinceStartsWith() called with municipality starts with: {}", province);
            List<Place> places = placeRepository.findPlaceByProvinceStartsWith(province);

            // If no places found, return not found
            if (places.isEmpty())
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(places);
        } catch (Exception e) {
            logger.error("Error in findPlaceByProvinceStartsWith() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findPlaceByProvinceStartsWith() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the places by municipality starts with
     * @param region the place municipality
     * @return the list of places that starts with the municipality param
     */
    @Override
    public ResponseEntity<List<Place>> findPlaceByRegionStartsWith(String region){
        try {
            logger.info("findPlaceByRegionStartsWith() called with municipality starts with: {}", region);
            List<Place> places = placeRepository.findPlaceByRegionStartsWith(region);

            // If no places found, return not found
            if (places.isEmpty())
                return ResponseEntity.notFound().build();

            return ResponseEntity.ok(places);
        } catch (Exception e) {
            logger.error("Error in findPlaceByRegionStartsWith() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findPlaceByRegionStartsWith() method");
        }
        return ResponseEntity.notFound().build();
    }
}
