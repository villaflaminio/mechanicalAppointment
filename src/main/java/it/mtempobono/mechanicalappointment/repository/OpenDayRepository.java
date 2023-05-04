package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.entity.OpenDay;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OpenDayRepository extends JpaRepository<OpenDay, Long> {
    List<OpenDay> findByGarageId(Long id);

    Page<OpenDay> findAll(Specification<OpenDay> specification, Pageable pageable);

    boolean existsByGarageIdAndDate(Long garageId, LocalDate date);

}