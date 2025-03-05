package br.danielkgm.ebingo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // para facilitar testes com HTTPCLIENT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API sem
                                                                                                              // estado
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll() // Acesso sem Autenticação
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Rotas de ADM
                        .anyRequest().authenticated() // Todas as outras rotas exigem autenticação
                );

        return http.build();
    }
}