package it.mtempobono.mechanicalappointment.service.impl;

import it.mtempobono.mechanicalappointment.controller.FilterDay;
import it.mtempobono.mechanicalappointment.model.builders.OpenDayBuilder;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.entity.Garage;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.repository.GarageRepository;
import it.mtempobono.mechanicalappointment.repository.OpenDayRepository;
import it.mtempobono.mechanicalappointment.service.OpenDayService;
import it.mtempobono.mechanicalappointment.util.PropertiesHelper;
import it.mtempobono.mechanicalappointment.util.PropertyCheckerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class that contains OpenDay business logics.
 */
@Service
public class OpenDayServiceImpl implements OpenDayService {
    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(OpenDayServiceImpl.class);

    @Autowired
    private OpenDayRepository openDayRepository;

    @Autowired
    private GarageRepository garageRepository;

    // endregion Fields

    // region Methods

    /**
     * Get all the openDays
     *
     * @return the list of openDays
     */
    @Override
    public ResponseEntity<List<OpenDay>> findAll() {
        try {
            logger.debug("Enter into findAll() method");
            return ResponseEntity.ok(openDayRepository.findAll());
        } catch (Exception e) {
            logger.error("Error in findAll() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from findAll() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get the openDay by id
     *
     * @param id the openDay id
     * @return the openDay
     */
    @Override
    public ResponseEntity<OpenDay> findById(Long id) {
        try {
            logger.info("findById() called with id: {}", id);
            Optional<OpenDay> openDay = openDayRepository.findById(id);
            return openDay.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error in findById() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from findById() method");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Create a new openDay
     *
     * @param openDay the openDay to create
     * @return the created openDay
     */
    @Override
    public ResponseEntity<OpenDay> save(OpenDayDto openDay) {
        try {
            logger.info("save() called with openDay: {}", openDay);

            // Retrieve the linked garage for the openDay
            Long garageId = openDay.getGarageId();
            Garage garage = garageRepository.findById(garageId).orElse(null);
            if (garage == null) {
                logger.error("Garage is null, you have to insert a valid one");
                return ResponseEntity.badRequest().build();
            }

            OpenDay newOpenDay = OpenDayBuilder.anOpenDay()
                    .garage(garage)
                    .maxParallelAppointments(openDay.getMaxParallelAppointments())
                    .date(openDay.getDate())
                    .workPlan(openDay.getWorkPlan())
                    .build();

            return ResponseEntity.ok(openDayRepository.save(newOpenDay));

        } catch (Exception e) {
            logger.error("Error in save() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from save() method");
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Update the openDay
     *
     * @param openDay the openDay to update
     * @return the updated openDay
     */
    @Override
    public ResponseEntity<OpenDay> update(OpenDayDto openDay, Long id) {
        try {
            logger.info("update() called with openDay: {}", openDay);

            // Try to find the openDay by id
            Optional<OpenDay> openDayToUpdateOptional = openDayRepository.findById(id);

            // If the openDay doesn't exist, return a 404 error
            if (openDayToUpdateOptional.isEmpty()) {
                logger.error("OpenDay not found");
                return ResponseEntity.notFound().build();
            }

            // Retrieve the linked garage for the openDay
            Long garageId = openDay.getGarageId();
            Garage garage = garageRepository.findById(garageId).orElse(null);
            if (garage == null) {
                logger.error("Garage is null, you have to insert a valid one");
                return ResponseEntity.badRequest().build();
            }

            PropertyCheckerUtils.copyNonNullProperties(openDay, openDayToUpdateOptional.get());

            openDayToUpdateOptional.get().setGarage(garage);
            openDayToUpdateOptional.get().setAppointments(null);

            return ResponseEntity.ok(openDayRepository.save(openDayToUpdateOptional.get()));
        } catch (Exception e) {
            logger.error("Error in update() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from update() method");
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Delete the openDay
     *
     * @param id the openDay id
     * @return the deleted openDay
     */
    @Override
    public ResponseEntity<Void> delete(Long id) {
        try {
            logger.info("delete() called with id: {}", id);

            // Check if the openDay exists
            if (!openDayRepository.existsById(id)) {
                logger.error("OpenDay not found");
                return ResponseEntity.notFound().build();
            }

            openDayRepository.deleteById(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error in delete() method: {}", e.getMessage());
        } finally {
            logger.debug("Exit from delete() method");
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<List<OpenDay>> findByGarageId(Long id) {

        List<OpenDay> openDay = openDayRepository.findByGarageId(id);

        return ResponseEntity.ok(openDay);
    }

    @Override
    public ResponseEntity<Page<OpenDay>> findFilterByDays(FilterDay filterDay, Integer page, Integer size, String sortField, String sortDirection) {
        OpenDay probe = new OpenDay();
        Pageable pageable;
        if (StringUtils.isEmpty(sortField)) {
            pageable = PageRequest.of(page, size);
        } else {
            Sort.Direction dir = StringUtils.isEmpty(sortDirection) ? Sort.Direction.ASC : Sort.Direction.valueOf(sortDirection.trim().toUpperCase());
            pageable = PageRequest.of(page, size, dir, sortField);
        }

        if (filterDay.getGarageId() != null) {
            Garage garage = garageRepository.findById(filterDay.getGarageId()).orElseThrow(() -> new ResourceNotFoundException("Garage not found"));
            probe.setGarage(garage);
        }
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<OpenDay> example = Example.of(probe, matcher);

        Specification<OpenDay> specification = getSpecFromRangesAndExample(example, filterDay);

        Page<OpenDay> result = openDayRepository.findAll(specification, pageable);

        return ResponseEntity.ok(result);
    }

    public Specification<OpenDay> getSpecFromRangesAndExample(Example<OpenDay> example, FilterDay day) {
        return (Specification<OpenDay>) (root, query, builder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Retrieve predicates for effort range.
            if (day.getDateFrom() != null) {
                // Second case:
                Predicate arrivoCase = builder.greaterThanOrEqualTo(root.get("date").as(LocalDate.class), day.getDateFrom());

                predicates.add(arrivoCase);
            }
            if (day.getDateTo() != null) {

                Predicate partenzaCase = builder.lessThanOrEqualTo(root.get("date").as(LocalDate.class), day.getDateTo());

                predicates.add(partenzaCase);
            }

            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
    // endregion Methods
}
