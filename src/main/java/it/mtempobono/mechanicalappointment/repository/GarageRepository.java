package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageRepository extends JpaRepository<Garage, Long> {
}