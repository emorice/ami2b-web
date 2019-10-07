package org.ami2b.web.auth;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api")
public class AuthController {
	@GetMapping("/hello")
	public String getHello() {
		return "Hello";
	}

	@GetMapping("/vip_hello")
	public String getVipHello() {
		return "Hello, trusted user";
	}
}
