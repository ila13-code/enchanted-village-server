package unical.demacs.enchantedvillage.config.security.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketAuthenticationFilter webSocketAuthenticationFilter;

    @Autowired
    public WebSocketConfig(WebSocketAuthenticationFilter webSocketAuthenticationFilter) {
        this.webSocketAuthenticationFilter = webSocketAuthenticationFilter;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ev-socket")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketAuthenticationFilter)
                .withSockJS();
    }


}