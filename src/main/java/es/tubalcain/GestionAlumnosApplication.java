package es.tubalcain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class GestionAlumnosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionAlumnosApplication.class, args);
    }

    @Bean
    public CommandLineRunner mostrarEndpoints(ApplicationContext context) {
        return args -> {
            System.out.println("\n================ ENDPOINTS DISPONIBLES ================\n");

            RequestMappingHandlerMapping mapping =
                    context.getBean(RequestMappingHandlerMapping.class);

            mapping.getHandlerMethods().forEach((info, method) -> {
                System.out.println(
                        info.getMethodsCondition() + " " +
                        info.getPatternValues() + "  --->  " +
                        method.getMethod().getName()
                );
            });
        };
    }
}
