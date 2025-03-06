package br.danielkgm.ebingo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        // Configura o CORS para permitir todas as origens, métodos e cabeçalhos.
        registry.addMapping("/**") // Permitindo para todas as rotas
                .allowedOrigins("http://localhost:4200") // Permitindo requisições apenas de localhost:4200
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permitindo esses métodos
                .allowedHeaders("*") // Permitindo qualquer cabeçalho
                .allowCredentials(true); // Permite o envio de credenciais, como cookies
    }
}
