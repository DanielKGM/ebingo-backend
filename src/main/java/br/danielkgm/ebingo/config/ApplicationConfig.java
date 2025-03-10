package br.danielkgm.ebingo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import br.danielkgm.ebingo.audit.ApplicationAuditAware;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditAware();
    }

}
