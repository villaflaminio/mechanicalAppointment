package it.flaminiovilla.mechanicalappointment.security;

import it.flaminiovilla.mechanicalappointment.config.AppProperties;
import it.flaminiovilla.mechanicalappointment.model.builder.AuthResponseDtoBuilder;
import it.flaminiovilla.mechanicalappointment.model.dto.AuthResponseDto;
import it.flaminiovilla.mechanicalappointment.repository.UserRepository;
import it.flaminiovilla.mechanicalappointment.model.entity.RefreshToken;
import it.flaminiovilla.mechanicalappointment.model.entity.Role;
import it.flaminiovilla.mechanicalappointment.model.entity.User;
import it.flaminiovilla.mechanicalappointment.model.entity.UserPrincipal;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to handle the JWT token creation and validation.
 */
@Service
public class TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private AppProperties appProperties;
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Token provider constructor.
     */
    public TokenProvider(AppProperties appProperties, ApplicationContext context) {
        this.appProperties = appProperties;
        this.refreshTokenService = context.getBean(RefreshTokenService.class);
    }

    /**
     * Create a JWT token.
     * @param authentication The authentication object.
     * @return The JWT token.
     */
    public String createToken(Authentication authentication) {
        // Get the user principal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Get the user roles
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        // Get today.
        Date now = new Date();

        // Get the expiration date.
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        String signingKey = appProperties.getAuth().getTokenSecret();

        // Create the token.
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setAudience("secure-app")
                .setSubject(Long.toString(userPrincipal.getId()))
                .setExpiration(expiryDate)
                .claim("rol", roles)
                .compact();

    }

    /**
     * Create authentication response.
     * @param authentication The authentication object.
     * @param response The HTTP response.
     * @return The authentication response.
     */
    public HttpServletResponse createAuthResponse(Authentication authentication, HttpServletResponse response) {
        // Get the user principal
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Create the token and refresh token.
        String token = createToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        response.addHeader("Authorization", "Bearer " + token);

        try{
            // Prepare repsonse to send to FE with username, authorities and duration of the token.
            response.setHeader("Access-Control-Expose-Headers", "*");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Create the response object.
            AuthResponseDto authResponseDto = AuthResponseDtoBuilder.anAuthResponseDto()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRoles())
                    .token(token)
                    .refreshToken(refreshToken.getToken())
                    .duration(Long.toString(appProperties.getAuth().getTokenExpirationMsec()))
                    .build();
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(authResponseDto));

        }catch ( Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Generate token from the given user.
     * @param user The user.
     * @return the token.
     */
    public String generateTokenFromUser(User user) {
        // Get the user roles
        List<String> roles = new ArrayList<>();
        for(Role r : user.getRoles()){
            roles.add(r.getName());
        }
        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        String signingKey = appProperties.getAuth().getTokenSecret();

        // Create the token.
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setAudience("secure-app")
                .setSubject(Long.toString(user.getId()))
                .setExpiration(expiryDate)
                .claim("rol", roles)
                .compact();
    }

    /**
     * Generate authentication from the user.
     * @param user The user.
     * @return The authentication.
     */
    public AuthResponseDto generateAuthFromUser(User user) {
        // Get token and refresh token.
        String token = generateTokenFromUser(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        try{
            // Prepare repsonse to send to FE with username, authorities and duration of the token.
            return AuthResponseDtoBuilder.anAuthResponseDto()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRoles())
                    .token(token)
                    .refreshToken(refreshToken.getToken())
                    .duration(Long.toString(appProperties.getAuth().getTokenExpirationMsec()))
                    .build();

        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the user id from the token.
     * @param token The token.
     * @return The user id.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validate the token.
     * @param authToken The token to be validated.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
