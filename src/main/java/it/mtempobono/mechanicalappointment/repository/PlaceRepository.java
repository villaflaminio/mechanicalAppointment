package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository which contains all the queries related to the Place entity.
 */
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByIstat(Integer istat);
}