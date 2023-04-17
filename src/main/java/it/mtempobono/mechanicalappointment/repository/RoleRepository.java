package it.mtempobono.mechanicalappointment.repository;

import it.mtempobono.mechanicalappointment.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository to manage all the operations related to the role.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

}