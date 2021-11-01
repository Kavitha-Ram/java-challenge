package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author rkavitha
 *
 */
@Configuration
@EnableJpaAuditing
public class SpringSecurityAuditorAwareConfig {
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new EntityAuditorAware();
	}
}
