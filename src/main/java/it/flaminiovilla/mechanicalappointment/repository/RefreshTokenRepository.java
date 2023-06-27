package it.flaminiovilla.mechanicalappointment.repository;

import it.flaminiovilla.mechanicalappointment.model.entity.RefreshToken;
import it.flaminiovilla.mechanicalappointment.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

/**
 * Repository to manage all the operations related to the refresh token.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Transactional
  void deleteAllByExpiryDateIsLessThan(Instant expiryDate);

  @Modifying
  @Transactional
  void deleteByExpiryDateIsLessThan(Instant expiryDate);

  @Modifying
  int deleteByUser(User user);
}
