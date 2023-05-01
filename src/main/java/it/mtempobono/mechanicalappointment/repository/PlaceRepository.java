package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository which contains all the queries related to the Place entity.
 */
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findAll(Pageable pageable);

    @Query("SELECT p FROM Place p WHERE p.municipality LIKE CONCAT(:municipality, '%')")
    List<Place> findPlaceByMunicipalityStartsWith(@Param("municipality") String municipality);

    @Query("SELECT p FROM Place p WHERE p.province LIKE CONCAT(:province, '%')")
    List<Place> findPlaceByProvinceStartsWith(@Param("province") String province);

    @Query("SELECT p FROM Place p WHERE p.region LIKE CONCAT(:region, '%')")
    List<Place> findPlaceByRegionStartsWith(@Param("region") String region);
}