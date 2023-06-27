package it.flaminiovilla.mechanicalappointment.repository;

import it.flaminiovilla.mechanicalappointment.model.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GarageRepository extends JpaRepository<Garage, Long> {
    @Query("SELECT g FROM Garage g WHERE g.place.municipality LIKE CONCAT(:municipality, '%')")
    List<Garage> findGarageByPlaceMunicipalityStartsWith(@Param("municipality") String municipality);

    @Query("SELECT g FROM Garage g WHERE g.place.province LIKE CONCAT(:province, '%')")
    List<Garage> findGarageByPlaceProvinceStartsWith(@Param("province") String province);

    @Query("SELECT g FROM Garage g WHERE g.place.region LIKE CONCAT(:region, '%')")
    List<Garage> findGarageByPlaceRegionStartsWith(@Param("region") String region);
}