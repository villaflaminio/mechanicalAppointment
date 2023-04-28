package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.MechanicalActionDto;
import it.mtempobono.mechanicalappointment.model.entity.MechanicalAction;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains MechanicalAction business logics.
*/
public interface MechanicalActionService {
    ResponseEntity<List<MechanicalAction>> findAll();

    ResponseEntity<MechanicalAction> findById(Long id);

    ResponseEntity<MechanicalAction> save(MechanicalActionDto mechanicalAction);

    ResponseEntity<MechanicalAction> update(MechanicalActionDto mechanicalAction, Long id);

    ResponseEntity<Void> delete(Long id);
}
