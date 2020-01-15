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
import java.util.HashSet;
import java.util.Arrays;

import org.ami2b.web.models.*;

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
    private GenomeRepository genomes;

    @Autowired
    private ProjectRepository projects;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addFirstUser() {
	    log.info("Seeding first user");
	    User user1 = new User();
	    user1.setUsername("user@domain");
	    user1.setPassword(passwordEncoder.encode("password"));
	    addSample(user1);
	    users.save(user1);
    }

    public void addSample(User user) {
	    log.info("Seeding sample data");
	    Sequence seq1 = new Sequence();
	    seq1.setSequence("AATTCGGCTGACAGATTCGGTGCTGGATAGGCTGAGTCGGTAGGCTGAGTCGGATG");
	    Genome g1 = new Genome();
	    g1.setSequence(seq1);
	    genomes.save(g1);
	    Project project1 = new Project();
	    Project project2 = new Project();
	    project1.setName("prj1");
	    project1.getGenomes().add(g1);
	    project2.setName("prj2");
	    projects.save(project1);
	    projects.save(project2);
	    user.setProjects(new HashSet<Project>(Arrays.asList(project1, project2)));
    }
}
