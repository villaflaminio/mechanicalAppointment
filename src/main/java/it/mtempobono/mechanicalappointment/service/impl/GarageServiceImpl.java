package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.dto.PlaceDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.model.entity.Place;
import it.mtempobono.mechanicalappointment.repository.GarageRepository;
import it.mtempobono.mechanicalappointment.repository.PlaceRepository;
import it.mtempobono.mechanicalappointment.service.GarageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains Garage business logics.
 */
@Service
public class GarageServiceImpl implements GarageService {
    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(GarageServiceImpl.class);

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private PlaceRepository placeRepository;
    // endregion Fields

    // region Methods
    /**
     * Get all the garages
     * @return the list of garages
     */
    @Override
    public ResponseEntity<List<Garage>> findAll(){
        try {
            logger.debug("Enter into findAll() method");
            return ResponseEntity.ok(garageRepository.findAll());
        } catch (Exception e) {
            logger.error("Error in findAll() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findAll() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the garage by id
     * @param id the garage id
     * @return the garage
     */
    @Override
    public ResponseEntity<Garage> findById(Long id){
        try {
            logger.info("findById() called with id: {}", id);
            Optional<Garage> garage = garageRepository.findById(id);
            return garage.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in findById() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findById() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Create a new garage
     * @param garage the garage to create
     * @return the created garage
     */
    @Override
    public ResponseEntity<Garage> save(GarageDto garage){
        try {
            logger.info("save() called with garage: {}", garage);
            Garage newGarage = new Garage();

            // Find linked Place by id
            Place place = placeRepository.findById(garage.getPlaceId()).orElse(null);
            if (place == null) {
                logger.error("Place not found");
                return ResponseEntity.badRequest().build();
            }

            // Maps the GarageDto to Garage
            BeanUtils.copyProperties(garage, newGarage);
            newGarage.setPlace(place);

            return ResponseEntity.ok(garageRepository.save(newGarage));

        } catch (Exception e) {
            logger.error("Error in save() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from save() method");
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Update the garage
     * @param garage the garage to update
     * @return the updated garage
     */
    @Override
    public ResponseEntity<Garage> update(Garage garage, Long id){
        logger.info("update() called with garage: {}", garage);
        return ResponseEntity.ok(garageRepository.save(garage));
    }

    /**
     * Delete the garage
     * @param id the garage id
     * @return the deleted garage
     */
    @Override
    public ResponseEntity<Void> delete(Long id){
        try {
            logger.info("delete() called with id: {}", id);

            // Check if the garage exists
            if (!garageRepository.existsById(id)) {
                logger.error("Garage not found");
                return ResponseEntity.notFound().build();
            }

            garageRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from delete() method");
        }
        return ResponseEntity.badRequest().build();
    }

    // endregion Methods
}
