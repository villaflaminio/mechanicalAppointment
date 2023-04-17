package it.mtempobono.mechanicalappointment.repository;

import com.flaminiovilla.obd.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByModelAndYearToAndProgramNumberAndDateFrom(String model, String yearTo, String programNumber, String dateFrom);
}