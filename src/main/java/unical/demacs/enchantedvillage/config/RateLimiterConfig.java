package unical.demacs.enchantedvillage.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {
    @Bean
    // FIXME - da sistemare
    public RateLimiter rateLimiter(){
        return RateLimiter.create(500000d, Duration.ofSeconds(15));
    }
}
