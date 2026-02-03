package es.tubalcain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "es.tubalcain.repository")
public class RedisConfig {
    // minimal: enabling Redis repositories. Spring Boot will auto-configure connection factory from application.properties.
}
