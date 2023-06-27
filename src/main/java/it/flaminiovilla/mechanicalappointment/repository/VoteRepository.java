package it.flaminiovilla.mechanicalappointment.repository;

import it.flaminiovilla.mechanicalappointment.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}