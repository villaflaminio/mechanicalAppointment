package it.flaminiovilla.mechanicalappointment.service;

import it.flaminiovilla.mechanicalappointment.model.dto.MechanicalActionDto;
import it.flaminiovilla.mechanicalappointment.model.entity.MechanicalAction;
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
