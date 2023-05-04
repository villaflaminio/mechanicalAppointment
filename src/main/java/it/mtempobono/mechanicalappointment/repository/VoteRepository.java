package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}