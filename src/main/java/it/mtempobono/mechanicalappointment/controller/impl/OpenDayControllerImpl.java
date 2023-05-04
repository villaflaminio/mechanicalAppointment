package it.mtempobono.mechanicalappointment.controller.impl;

import it.mtempobono.mechanicalappointment.model.dto.FilterDay;
import it.mtempobono.mechanicalappointment.controller.OpenDayController;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import it.mtempobono.mechanicalappointment.service.OpenDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which implements all
 * the operations for OpenDay entity
 */
@RestController
public class OpenDayControllerImpl implements OpenDayController {

    // region Fields
    private static final Logger logger = LoggerFactory.getLogger(OpenDayController.class);

    @Autowired
    private OpenDayService openDayService;

    // endregion Fields

    //region CRUD Methods
    @Override
    public ResponseEntity<List<OpenDay>> findAll() {
        return openDayService.findAll();
    }

    @Override
    public ResponseEntity<OpenDay> findById(Long id) {
        return openDayService.findById(id);
    }

    @Override
    public ResponseEntity<List<OpenDay>> findByGarageId(Long id) {
        return openDayService.findByGarageId(id);
    }

    @Override
    public ResponseEntity<Page<OpenDay>> findFilterByDays(FilterDay filterDay, Integer page, Integer size, String sortField, String sortDirection) {
        return  openDayService.findFilterByDays(filterDay, page, size, sortField, sortDirection);
    }

    @Override
    public ResponseEntity<OpenDay> save(OpenDayDto openDay) {
        return openDayService.save(openDay);
    }

    @Override
    public ResponseEntity<OpenDay> update(OpenDayDto openDay, Long id) {
        return openDayService.update(openDay, id);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) { return openDayService.delete(id);}
    //endregion CRUD Methods
}
