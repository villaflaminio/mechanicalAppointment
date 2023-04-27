package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository which contains all the queries related to the Place entity.
 */
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByIstat(Long istat);

    @Query("SELECT p FROM Place p WHERE " +
            "p.municipality LIKE :municipality AND " +
            "p.province LIKE :province AND " +
            "p.region LIKE :region")
    Place findByMunicipalityAndProvinceAndRegion(
            @Param("municipality") String municipality,
            @Param("province") String province,
            @Param("region") String region);

    Place findPlaceByMunicipalityContainingIgnoreCaseAndRegionContainingIgnoreCaseAndRegionContainingIgnoreCase
            (String municipality, String province, String region);
}