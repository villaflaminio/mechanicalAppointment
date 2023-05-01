package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.model.builders.VehicleBuilder;
import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import it.mtempobono.mechanicalappointment.repository.VehicleRepository;
import it.mtempobono.mechanicalappointment.repository.PlaceRepository;
import it.mtempobono.mechanicalappointment.service.VehicleService;
import it.mtempobono.mechanicalappointment.util.PropertyCheckerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Class that contains Vehicle business logics.
 */
@Service
public class VehicleServiceImpl implements VehicleService {
    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    @Autowired
    private VehicleRepository vehicleRepository;

    // endregion Fields

    // region Methods
    /**
     * Get all the vehicles
     * @return the list of vehicles
     */
    @Override
    public ResponseEntity<List<Vehicle>> findAll(){
        try {
            logger.debug("Enter into findAll() method");
            return ResponseEntity.ok(vehicleRepository.findAll());
        } catch (Exception e) {
            logger.error("Error in findAll() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findAll() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the vehicle by id
     * @param id the vehicle id
     * @return the vehicle
     */
    @Override
    public ResponseEntity<Vehicle> findById(Long id){
        try {
            logger.info("findById() called with id: {}", id);
            Optional<Vehicle> vehicle = vehicleRepository.findById(id);
            return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in findById() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findById() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Create a new vehicle
     * @param vehicleDto the vehicle to create
     * @return the created vehicle
     */
    @Override
    public ResponseEntity<Vehicle> save(VehicleDto vehicleDto){
        try {
            logger.info("save() called with vehicle: {}", vehicleDto);

            // Create a new vehicle using fields from vehicleDto
            Vehicle newVehicle = VehicleBuilder.aVehicle()
                    .plate(vehicleDto.getPlate())
                    .model(vehicleDto.getModel())
                    .brand(vehicleDto.getBrand())
                    .year(vehicleDto.getYear())
                    .fuel(vehicleDto.getFuel())
                    .isActive(vehicleDto.getIsActive())
                    .build();

            return ResponseEntity.ok(vehicleRepository.save(newVehicle));

        } catch (Exception e) {
            logger.error("Error in save() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from save() method");
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Update the vehicle
     * @param vehicleDto the vehicle to update
     * @return the updated vehicle
     */
    @Override
    public ResponseEntity<Vehicle> update(VehicleDto vehicleDto, Long id){
        try {
            logger.info("update() called with vehicle: {}", vehicleDto);

            // Try to find the vehicle by id
            Optional<Vehicle> vehicleToUpdateOptional = vehicleRepository.findById(id);

            // If the vehicle doesn't exist, return a 404 error
            if (vehicleToUpdateOptional.isEmpty()) {
                logger.error("Vehicle not found");
                return ResponseEntity.notFound().build();
            }

            PropertyCheckerUtils.copyNonNullProperties(vehicleDto, vehicleToUpdateOptional.get());
            vehicleToUpdateOptional.get().setId(id);

            return ResponseEntity.ok(vehicleRepository.save(vehicleToUpdateOptional.get()));
        } catch (Exception e) {
            logger.error("Error in update() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from update() method");
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Delete the vehicle
     * @param id the vehicle id
     * @return the deleted vehicle
     */
    @Override
    public ResponseEntity<Void> delete(Long id){
        try {
            logger.info("delete() called with id: {}", id);

            // Check if the vehicle exists
            if (!vehicleRepository.existsById(id)) {
                logger.error("Vehicle not found");
                return ResponseEntity.notFound().build();
            }

            vehicleRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from delete() method");
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Get the vehicles by user id
     * @param id the user id
     * @return the list of vehicles
     */
    @Override
    public ResponseEntity<List<Vehicle>> findByUserId(Long id){
        try {
            logger.info("findByUserId() called with id: {}", id);
            return ResponseEntity.ok(vehicleRepository.findByUserId(id));
        } catch (Exception e) {
            logger.error("Error in findByUserId() method: {}", e.getMessage());
        }finally {
            logger.debug("Exit from findByUserId() method");
        }
        return ResponseEntity.notFound().build();
    }

    // endregion Methods


}
