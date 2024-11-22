package unical.demacs.enchantedvillage.config.security.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import unical.demacs.enchantedvillage.config.security.CustomUserDetails;
import unical.demacs.enchantedvillage.config.security.tokenvalidator.TokenValidatorService;
import unical.demacs.enchantedvillage.persistence.entities.User;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
public class WebSocketAuthenticationFilter implements HandshakeInterceptor {

    private final TokenValidatorService tokenValidatorService;

    @Autowired
    public WebSocketAuthenticationFilter(TokenValidatorService tokenValidatorService) {
        this.tokenValidatorService = tokenValidatorService;
    }


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        String token = extractQueryParam(uri, "token");
        String roomId = extractQueryParam(uri, "userEmail");

        if (token != null) {
            Optional<?> user = tokenValidatorService.getUser(token, true);
            if (user.isPresent() && user.get() instanceof User) {
                CustomUserDetails userDetails = new CustomUserDetails((User) user.get());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(request);
                attributes.put("user", user.get());
                attributes.put("userEmail", ((User) user.get()).getEmail());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return true;
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private String extractQueryParam(URI uri, String param) {
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0 && pair.substring(0, idx).equals(param)) {
                    return URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }
}