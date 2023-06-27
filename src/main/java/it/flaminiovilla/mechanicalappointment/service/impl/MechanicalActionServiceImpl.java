package it.flaminiovilla.mechanicalappointment.service.impl;

import it.flaminiovilla.mechanicalappointment.util.PropertyCheckerUtils;
import it.flaminiovilla.mechanicalappointment.model.builder.MechanicalActionBuilder;
import it.flaminiovilla.mechanicalappointment.model.dto.MechanicalActionDto;
import it.flaminiovilla.mechanicalappointment.model.entity.MechanicalAction;
import it.flaminiovilla.mechanicalappointment.repository.MechanicalActionRepository;
import it.flaminiovilla.mechanicalappointment.service.MechanicalActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains MechanicalAction business logics.
 */
@Service
public class MechanicalActionServiceImpl implements MechanicalActionService {
    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(MechanicalActionServiceImpl.class);

    @Autowired
    private MechanicalActionRepository mechanicalActionRepository;

    // endregion Fields

    // region Methods

    /**
     * Get all the mechanicalActions
     *
     * @return the list of mechanicalActions
     */
    @Override
    public ResponseEntity<List<MechanicalAction>> findAll() {
        try {
            logger.debug("Enter into findAll() method");
            return ResponseEntity.ok(mechanicalActionRepository.findAll());
        } catch (Exception e) {
            logger.error("Error in findAll() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from findAll() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the mechanicalAction by id
     *
     * @param id the mechanicalAction id
     * @return the mechanicalAction
     */
    @Override
    public ResponseEntity<MechanicalAction> findById(Long id) {
        try {
            logger.info("findById() called with id: {}", id);
            Optional<MechanicalAction> mechanicalAction = mechanicalActionRepository.findById(id);
            return mechanicalAction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in findById() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from findById() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Create a new mechanicalAction
     *
     * @param mechanicalAction the mechanicalAction to create
     * @return the created mechanicalAction
     */
    @Override
    public ResponseEntity<MechanicalAction> save(MechanicalActionDto mechanicalAction) {
        try {
            logger.info("save() called with mechanicalAction: {}", mechanicalAction);

            // Check if internal duration and external duration are not null
            if (mechanicalAction.getInternalDuration() == null || mechanicalAction.getExternalDuration() == null) {
                logger.error("Internal duration or external duration is null");
                throw new IllegalArgumentException("Internal duration or external duration is null");
            }

            MechanicalAction newMechanicalAction = MechanicalActionBuilder.aMechanicalAction()
                    .name(mechanicalAction.getName())
                    .description(mechanicalAction.getDescription())
                    .internalDuration(mechanicalAction.getInternalDuration().getDuration())
                    .externalDuration(mechanicalAction.getExternalDuration().getDuration())
                    .price(mechanicalAction.getPrice())
                    .isActive(mechanicalAction.getIsActive())
                    .build();

            return ResponseEntity.ok(mechanicalActionRepository.save(newMechanicalAction));

        } catch (Exception e) {
            logger.error("Error in save() method: {}", e.getMessage());
            throw e;
        } finally {
            logger.debug("Exit from save() method");
        }
    }

    /**
     * Update the mechanicalAction
     *
     * @param mechanicalAction the mechanicalAction to update
     * @return the updated mechanicalAction
     */
    @Override
    public ResponseEntity<MechanicalAction> update(MechanicalActionDto mechanicalAction, Long id) {
        try {
            logger.info("update() called with mechanicalAction: {}", mechanicalAction);

            // Try to find the mechanicalAction by id
            Optional<MechanicalAction> mechanicalActionToUpdateOptional = mechanicalActionRepository.findById(id);

            // If the mechanicalAction doesn't exist, return a 404 error
            if (mechanicalActionToUpdateOptional.isEmpty()) {
                logger.error("MechanicalAction not found");

                throw new IllegalArgumentException("MechanicalAction not found");
            }

            PropertyCheckerUtils.copyNonNullProperties(mechanicalAction, mechanicalActionToUpdateOptional.get());
            mechanicalActionToUpdateOptional.get().setId(id);

            // If internal duration is not null, then update it
            if (mechanicalAction.getInternalDuration() != null) {
                mechanicalActionToUpdateOptional.get().setInternalDuration(mechanicalAction.getInternalDuration().getDuration());
            }

            // If external duration is not null, then update it
            if (mechanicalAction.getExternalDuration() != null) {
                mechanicalActionToUpdateOptional.get().setExternalDuration(mechanicalAction.getExternalDuration().getDuration());
            }

            return ResponseEntity.ok(mechanicalActionRepository.save(mechanicalActionToUpdateOptional.get()));
        } catch (Exception e) {
            logger.error("Error in update() method: {}", e.getMessage());
            throw e;

        } finally {
            logger.debug("Exit from update() method");
        }
    }

    /**
     * Delete the mechanicalAction
     *
     * @param id the mechanicalAction id
     * @return the deleted mechanicalAction
     */
    @Override
    public ResponseEntity<Void> delete(Long id) {
        try {
            logger.info("delete() called with id: {}", id);

            // Check if the mechanicalAction exists
            if (!mechanicalActionRepository.existsById(id)) {
                logger.error("MechanicalAction not found");
                throw new IllegalArgumentException("MechanicalAction not found");
            }

            mechanicalActionRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
            throw e;
        } finally {
            logger.debug("Exit from delete() method");
        }
    }

    // endregion Methods
}
