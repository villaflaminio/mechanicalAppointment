package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.model.dto.VehicleDto;
import it.mtempobono.mechanicalappointment.model.entity.User;
import it.mtempobono.mechanicalappointment.model.entity.Vehicle;
import it.mtempobono.mechanicalappointment.repository.UserRepository;
import it.mtempobono.mechanicalappointment.repository.VehicleRepository;
import it.mtempobono.mechanicalappointment.service.VehicleService;
import it.mtempobono.mechanicalappointment.util.PropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Vehicle> save(VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();
        PropertiesHelper.copyNonNullProperties(vehicleDto, vehicle);

        User user = userRepository.findById(vehicleDto.getUserId()).orElseThrow( () -> new RuntimeException("User not found"));
        vehicle.setUser(user);

        return ResponseEntity.ok(vehicleRepository.save(vehicle));
    }
}
