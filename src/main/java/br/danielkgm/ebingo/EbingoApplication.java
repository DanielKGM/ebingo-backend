package br.danielkgm.ebingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableMethodSecurity
public class EbingoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbingoApplication.class, args);
	}

}
