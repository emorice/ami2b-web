package org.ami2b.web;

import eu.fraho.spring.securityJwt.base.config.JwtSecurityConfig;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@Order(89) // Used to decide which web security Spring uses (generic)
public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

	// We want to "extend" the JwtConfig, but doing a second publicly visible JwtConfig messes with JWT-boot
	@Autowired
	private JwtSecurityConfig jwtConfig;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Apply default from jwt
		jwtConfig.configure(http);

		// Secure the rest
		http
			.authorizeRequests()
				.antMatchers("/api/hello").permitAll()
				.antMatchers("/auth/login").permitAll()
				.anyRequest().authenticated()
				.and()
			.csrf().ignoringAntMatchers("/api/**", "/auth/**");

	}

	/*
	// Every instance of JwtSecurityConfig yield a manager, so we need an
	// override in one of them with higher priority
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Primary
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	*/


	@Component
	static public class StupidUserDetailsService implements UserDetailsService {
		@Autowired
		private PasswordEncoder passwordEncoder;

		private static final Logger logger = LoggerFactory.getLogger(StupidUserDetailsService.class);

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			logger.warn("Loading");
			logger.warn(username);
			if (username.equals("user")) {
				logger.warn("Returning");
				logger.warn(username);

				JwtUser user = new JwtUser();
				user.setUsername("user");
				user.setPassword(passwordEncoder.encode("password"));
				user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
				user.setAccountNonExpired(true);
				user.setAccountNonLocked(true);
				user.setApiAccessAllowed(true);
				user.setCredentialsNonExpired(true);
				user.setEnabled(true);
				return user;
			} else {
				throw new UsernameNotFoundException("No such user");
			}
		}
	}

}


