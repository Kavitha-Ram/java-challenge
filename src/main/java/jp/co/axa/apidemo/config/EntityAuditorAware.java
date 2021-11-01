package jp.co.axa.apidemo.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
/**
 * @author rkavitha
 *
 */
public class EntityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 if (authentication == null || !authentication.isAuthenticated()) {
			 return Optional.of("admin");
		 	}
		 System.out.println(authentication.getName());
		return Optional.of(authentication.getName());
	}
	
}
