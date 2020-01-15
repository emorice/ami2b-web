package org.ami2b.web;

import eu.fraho.spring.securityJwt.base.config.JwtSecurityConfig;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.*;
import org.springframework.stereotype.Component;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.ami2b.web.models.UserRepository;

@Configuration
@Order(89) // Used to decide which web security Spring uses (generic)
@Slf4j
public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

	// We want to "extend" the JwtConfig, but doing a second publicly visible JwtConfig messes with JWT-boot
	// So we let the default one be, and wire it privately here.
	@Autowired
	private JwtSecurityConfig jwtConfig;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Apply default from jwt
		jwtConfig.configure(http);
		// After this, session and csrf are globally disabled,
		// Authorization by token bearer is globally enabled,
		// Everything is unprotected by default.

		// Secure the rest
		http
			.authorizeRequests()
				.antMatchers("/api/hello").permitAll()
				.antMatchers("/api/**").permitAll() // TODO: remove
				.antMatchers("/auth/login").permitAll()
				.antMatchers("/h2-console/**").permitAll() // TODO: remove
				.anyRequest().authenticated()
				.and()
			.csrf().ignoringAntMatchers("/api/**", "/auth/**", "/h2-console/**")
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // allow sessions for developper tools
			.and()
			.headers().frameOptions().sameOrigin(); // allow frames for dev tools

	}

	@Component
	static public class StupidUserDetailsService implements UserDetailsService {
		@Autowired
		private UserRepository users;

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			return users.findByUsername(username).toJwtUser();
		}
	}
}


