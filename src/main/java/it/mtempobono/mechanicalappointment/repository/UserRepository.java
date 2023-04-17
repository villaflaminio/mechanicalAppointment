package it.mtempobono.mechanicalappointment.repository;

import com.flaminiovilla.obd.model.User;
import com.flaminiovilla.obd.repository.projection.UserMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to manage all the operations related to the user.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<UserMe> findProjectedById(long id);
    Optional<User> findByIdAndSubscriptions_active(long id, boolean active);

}
