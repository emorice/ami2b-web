package org.ami2b.web.models;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.ami2b.web.models.UserRepository;
import org.ami2b.web.models.User;

@Component
@Slf4j
public class UserSeeder {
    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent applicationEvent) {
	    addFirstUser();
    }

    @Autowired
    private UserRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addFirstUser() {
	    log.info("Seeding first user");
	    User user1 = new User();
	    user1.setUsername("user@domain");
	    user1.setPassword(passwordEncoder.encode("password"));
	    users.save(user1);
    }

}
