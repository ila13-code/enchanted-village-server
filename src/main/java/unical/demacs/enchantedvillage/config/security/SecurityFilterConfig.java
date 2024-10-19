package unical.demacs.enchantedvillage.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityFilterConfig {

    private BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                            throw accessDeniedException;
                        })
                )
                .addFilterBefore(bearerTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        //USER
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/getUser").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/user/updateUser").hasAnyRole("USER","ADMIN")

                        //GAME INFORMATION
                        .requestMatchers(HttpMethod.GET, "/api/v1/game-information/getGameInformation").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/game-information/createGameInformation").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/game-information/updateGameInformation").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/game-information/getGameInformation").hasAnyRole("USER", "ADMIN")

                        //BATTLE INFORMATION
                        .requestMatchers(HttpMethod.GET, "/api/v1/battle-information/getBattleInformation").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/battle-information/createBattleInformation").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/battle-information/updateBattleInformation").hasAnyRole("USER", "ADMIN")


                        //SWAGGER
                        .requestMatchers( "/enchanted-village/swagger-ui/**", "/v3/api-docs", "/swagger-ui/**", "/enchanted-village/api-docs", "/v3/api-docs/swagger-config").permitAll()

                        //OTHERS
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }

    public static boolean isPublicEndpoint(String uri) {
        return uri.startsWith("/enchanted-village/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-ui/**")
                || uri.startsWith("/v3/api-docs/swagger-config")
                || uri.startsWith("/enchanted-village/api-docs");
    }
}
