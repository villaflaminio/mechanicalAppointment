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

            // Find linked Place
            PlaceDto placeDto = garage.getPlace();
            if (placeDto == null) {
                logger.error("Place is null, you have to insert a valid one");
                return ResponseEntity.badRequest().build();
            }

            // PlaceDto has different fields, which are:
            // istat -> the ISTAT code of the place -> UNIQUE KEY
            // municipality -> the municipality name of the place
            // province -> the province initials of the place
            // region -> the region of the place

//            // If the PlaceDto has the istat field, we can find the Place by istat
//            Place place = null;
//            if (placeDto.getIstat() != null) {
//                place = placeRepository.findByIstat(placeDto.getIstat());
//            }
            Place place = null;

            // If the PlaceDto has the municipality, province and region fields, we can find the
            // Place by municipality, province and region
            if (placeDto.getMunicipality() != null &&
                placeDto.getProvince() != null &&
                placeDto.getRegion() != null) {
//                place = placeRepository.findByMunicipalityAndProvinceAndRegion(
//                        placeDto.getMunicipality(),
//                        placeDto.getProvince(),
//                        placeDto.getRegion()
//                );
                place = placeRepository.findPlaceByMunicipalityContainingIgnoreCaseAndRegionContainingIgnoreCaseAndRegionContainingIgnoreCase(
                        placeDto.getMunicipality(),
                        placeDto.getProvince(),
                        placeDto.getRegion()
                );
            }

            // Maps the GarageDto to Garage building a new Garage object
            Garage newGarage = Garage.builder()
                    .cap(garage.getCap())
                    .email(garage.getEmail())
                    .logo(garage.getLogo())
                    .name(garage.getName())
                    .phone(garage.getPhone())
                    .place(place)
                    .address(garage.getAddress())
                    .longitude(garage.getLongitude())
                    .latitude(garage.getLatitude())
                    .linkGoogleMaps(garage.getLinkGoogleMaps())
                    .website(garage.getWebsite())
                    .build();

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
        try {
            logger.info("update() called with garage: {}", garage);

            // Try to find the garage by id
            Optional<Garage> garageToUpdate = garageRepository.findById(id);

            // If the garage doesn't exist, return a 404 error
            if (garageToUpdate.isEmpty()) {
                logger.error("Garage not found");
                return ResponseEntity.notFound().build();
            }

            // Set the id of the garage to update
            garage.setId(garageToUpdate.get().getId());

            return ResponseEntity.ok(garageRepository.save(garage));
        } catch (Exception e) {
            logger.error("Error in update() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from update() method");
        }
        return ResponseEntity.badRequest().build();
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
