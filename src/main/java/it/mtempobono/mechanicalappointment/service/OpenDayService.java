package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.controller.FilterDay;
import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
* Class that contains OpenDay business logics.
*/
public interface OpenDayService {
    ResponseEntity<List<OpenDay>> findAll();

    ResponseEntity<OpenDay> findById(Long id);

    ResponseEntity<OpenDay> save(OpenDayDto garage);

    ResponseEntity<OpenDay> update(OpenDayDto garage, Long id);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<List<OpenDay>> findByGarageId(Long id);

    ResponseEntity<Page<OpenDay>> findFilterByDays(FilterDay filterDay, Integer page, Integer size, String sortField, String sortDirection);
}
