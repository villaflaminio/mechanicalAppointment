package it.flaminiovilla.mechanicalappointment.controller.impl;

import it.flaminiovilla.mechanicalappointment.controller.MechanicalActionController;
import it.flaminiovilla.mechanicalappointment.model.dto.MechanicalActionDto;
import it.flaminiovilla.mechanicalappointment.service.MechanicalActionService;
import it.flaminiovilla.mechanicalappointment.model.entity.MechanicalAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which implements all
 * the operations for MechanicalAction entity
 */
@RestController
public class MechanicalActionControllerImpl implements MechanicalActionController {

    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(MechanicalActionController.class);

    @Autowired
    private MechanicalActionService mechanicalActionService;

    // endregion Fields

    //region CRUD Methods
    @Override
    public ResponseEntity<List<MechanicalAction>> findAll() {
        return mechanicalActionService.findAll();
    }

    @Override
    public ResponseEntity<MechanicalAction> findById(Long id) {
        return mechanicalActionService.findById(id);
    }

    @Override
    public ResponseEntity<MechanicalAction> save(MechanicalActionDto mechanicalAction) {
        return mechanicalActionService.save(mechanicalAction);
    }

    @Override
    public ResponseEntity<MechanicalAction> update(MechanicalActionDto mechanicalAction, Long id) {
        return mechanicalActionService.update(mechanicalAction, id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) { return mechanicalActionService.delete(id);}
    //endregion CRUD Methods
}
