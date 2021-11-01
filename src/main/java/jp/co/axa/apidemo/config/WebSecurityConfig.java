package jp.co.axa.apidemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//Spring boot security configuration class.
@Configuration
@EnableWebSecurity    // Enables security for our application.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	// Securing the urls and allowing role-based access to these urls.
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers("/api/v1/**").permitAll()
		.antMatchers("/api/test/**").permitAll()
		.anyRequest().authenticated();

	
    }

	// In-memory authentication to authenticate the user i.e. the user credentials are stored in the memory.
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("username")
				.password("password").roles("USER");
	}
}
