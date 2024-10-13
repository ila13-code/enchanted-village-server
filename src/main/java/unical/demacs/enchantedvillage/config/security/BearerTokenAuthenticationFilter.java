package unical.demacs.enchantedvillage.config.security;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import unical.demacs.enchantedvillage.config.security.tokenvalidator.TokenValidatorService;
import unical.demacs.enchantedvillage.persistence.entities.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Optional;

@Component
public class BearerTokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenValidatorService tokenValidatorService;

    @Autowired
    public BearerTokenAuthenticationFilter(TokenValidatorService tokenValidatorService) {
        this.tokenValidatorService = tokenValidatorService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        /*if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }*/

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            try {
                Optional<?> user = tokenValidatorService.getUser(accessToken, true);
                if (user.isPresent() && user.get() instanceof User) {
                    CustomUserDetails userDetails = new CustomUserDetails((User) user.get());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            } catch (JOSEException | ParseException | InvalidKeySpecException | NoSuchAlgorithmException e) {
                logger.error("Authentication error: ", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            }
        }
        else
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing - The endpoint requires a valid token.");

        filterChain.doFilter(request, response);
    }



}

