package com.spring_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Configuracion de OpenAPI para documentar la API con Swagger.
 */

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Seguridad")
                .version("1.0.0")
                .description("Documentación de la API de Seguridad")
            );
    }
}