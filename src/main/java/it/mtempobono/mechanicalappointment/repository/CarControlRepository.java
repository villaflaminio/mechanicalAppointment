package it.mtempobono.mechanicalappointment.repository;

import com.flaminiovilla.obd.model.CarControl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarControlRepository extends JpaRepository<CarControl, Long> {
}