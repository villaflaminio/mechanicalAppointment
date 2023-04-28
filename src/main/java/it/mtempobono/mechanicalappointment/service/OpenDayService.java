package it.mtempobono.mechanicalappointment.service;

import it.mtempobono.mechanicalappointment.model.dto.OpenDayDto;
import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
}
