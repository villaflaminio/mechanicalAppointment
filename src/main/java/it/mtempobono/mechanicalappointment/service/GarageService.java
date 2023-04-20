package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.GarageDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.repository.GarageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that contains Garage business logics.
 */
@Service
public class GarageService {
    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(GarageService.class);

    @Autowired
    private GarageRepository garageRepository;
    // endregion Fields

    // region Methods
    /**
     * Get all the garages
     * @return the list of garages
     */
    public ResponseEntity<List<GarageDto>> findAll(){
        try {
            logger.debug("Enter into findAll() method");
            return ResponseEntity.ok(garageRepository.findAll().stream().map(GarageDto::new).collect(Collectors.toList()));
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
    public ResponseEntity<GarageDto> findById(@PathVariable("id") Long id){
        try {
            logger.info("findById() called with id: {}", id);
            Optional<Garage> garage = garageRepository.findById(id);
            return garage.map(g -> ResponseEntity.ok(new GarageDto(g))).orElseGet(() -> ResponseEntity.notFound().build());
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
    ResponseEntity<Garage> save(@RequestBody @Validated Garage garage){
        try {
            logger.info("save() called with garage: {}", garage);
            return ResponseEntity.ok(garageRepository.save(garage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update the garage
     * @param garage the garage to update
     * @return the updated garage
     */
    ResponseEntity<Garage> update(@RequestBody Garage garage, @PathVariable("id") Long id){
        logger.info("update() called with garage: {}", garage);
        return ResponseEntity.ok(garageRepository.save(garage));
    }

    /**
     * Delete the garage
     * @param id the garage id
     * @return the deleted garage
     */
    ResponseEntity<Void> delete(@PathVariable("id") Long id){
        logger.info("delete() called with id: {}", id);
        garageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    // endregion Methods
}
