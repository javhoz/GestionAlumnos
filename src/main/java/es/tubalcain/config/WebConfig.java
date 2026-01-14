package es.tubalcain.config;

import org.springframework.context.annotation.Configuration;

/**
 * Web configuration.
 * Note: Authentication is handled by Spring Security (SecurityConfig),
 * and ownership validation is handled in the service layer (AlumnoService).
 * No interceptors needed - simpler approach!
 */
@Configuration
public class WebConfig {
    // No interceptors needed - Spring Security handles authentication
    // and the service layer handles ownership validation
}
