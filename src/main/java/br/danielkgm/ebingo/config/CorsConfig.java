package br.danielkgm.ebingo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${api.frontend.origin}")
    private String frontendOrigin;

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        // Configura o CORS para permitir todas as origens, métodos e cabeçalhos.
        registry.addMapping("/**")
                .allowedOrigins(frontendOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
