package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.OpenDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenDayRepository extends JpaRepository<OpenDay, Long> {
}