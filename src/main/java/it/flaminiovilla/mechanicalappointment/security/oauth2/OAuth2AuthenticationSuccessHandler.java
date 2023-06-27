package it.flaminiovilla.mechanicalappointment.security.oauth2;

import it.flaminiovilla.mechanicalappointment.config.AppProperties;
import it.flaminiovilla.mechanicalappointment.exception.BadRequestException;
import it.flaminiovilla.mechanicalappointment.util.CookieUtils;
import it.flaminiovilla.mechanicalappointment.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * A class to manage the OAuth2 authentication success.
 */
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private TokenProvider tokenProvider;
    private AppProperties appProperties;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * Instantiates a new OAuth 2 authentication success handler.
     * @param tokenProvider the token provider
     * @param appProperties the app properties
     * @param httpCookieOAuth2AuthorizationRequestRepository the http cookie o auth 2 authorization request repository
     */
    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }


    /**
     * On authentication success method
     * @param request the request
     * @param response the response
     * @param authentication the authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Get the redirect URI from the session
        String targetUrl = determineTargetUrl(request, response, authentication);

        // Check if the response has already been committed
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        // Clear authentication attributes.
        clearAuthenticationAttributes(request, response);
        try{
            // Create auth response.
           // response  = tokenProvider.createAuthResponse(authentication , response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl); // effettua il redirect aggiungendo il token di autenticazione

        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Compute target URL
     * @param request the request
     * @param response the response
     * @param authentication the authentication
     * @return the target URL
     */
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Get the redirect URI from the cookie.
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // Check if the redirect URI is present and valid
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        // Create token.
        String token = tokenProvider.createToken(authentication);

        // Add token to the cookies.
        CookieUtils.addCookie(response, "token", token, 10);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    /**
     * Clear authentication attributes.
     * @param request the request
     * @param response the response
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * Check if the redirect URI is valid.
     * @param uri the URI to check
     * @return true if the URI is valid, false otherwise
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    if(authorizedRedirectUri.contains("*")) {
                        return true;
                    }
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort() ) {
                        return true;
                    }
                    return false;
                });
    }
}
