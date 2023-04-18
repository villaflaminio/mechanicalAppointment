package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicalActionRepository extends JpaRepository<MechanicalAction, Long> {
}