package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}