package it.mtempobono.mechanicalappointment.security;

import it.mtempobono.mechanicalappointment.exception.TokenRefreshException;
import it.mtempobono.mechanicalappointment.model.entity.RefreshToken;
import it.mtempobono.mechanicalappointment.model.entity.User;
import it.mtempobono.mechanicalappointment.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing refresh tokens.
 */
@Service
@AllArgsConstructor
public class RefreshTokenService {
  private RefreshTokenRepository refreshTokenRepository;
  private Environment env;

  /**
   * Get refresh token from the given one
   * @param token the token used to retrieve the refresh token.
   * @return the refresh token.
   */
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  /**
   * Create a new refresh token for the given user.
   * @param user the user for which the refresh token will be created.
   * @return the created refresh token.
   */
  public RefreshToken createRefreshToken(User user) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(user);
    refreshToken.setExpiryDate(Instant.now().plusMillis(Long.parseLong(Objects.requireNonNull(env.getProperty("app.auth.refreshTokenExpiration")))));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken = refreshTokenRepository.save(refreshToken);

    return refreshToken;
  }

  /**
   * Check if the given refresh token is valid or expired.
   * @param token the refresh token to be checked.
   * @return true if the token is valid, false otherwise.
   */
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }


}
