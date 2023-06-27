package it.flaminiovilla.mechanicalappointment.repository;

import it.flaminiovilla.mechanicalappointment.model.entity.MechanicalAction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for MechanicalAction entity.
 */
public interface MechanicalActionRepository extends JpaRepository<MechanicalAction, Long> {
    //find by name
    MechanicalAction findByName(String name);
}