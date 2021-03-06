package org.ami2b.web.auth;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api")
public class AuthController {
	@GetMapping("/hello")
	public String getHello() {
		return "Hello";
	}


	@Autowired
        private AuthenticationManagerBuilder builder;

	static class Credentials {
		public String login;
		public String password;
	}


	@GetMapping("/vip_hello")
	public String getVipHello() {
		return "Hello, trusted user";
	}
}
