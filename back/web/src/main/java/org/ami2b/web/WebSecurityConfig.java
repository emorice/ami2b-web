package org.ami2b.web;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/api/hello").permitAll()
				.antMatchers("/api/login").permitAll()
				.anyRequest().authenticated()
				.and()
			.csrf().ignoringAntMatchers("/api/**");

	}
}

