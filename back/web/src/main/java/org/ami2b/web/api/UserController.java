package org.ami2b.web.api;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.ami2b.web.models.User;
import org.ami2b.web.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.PersistentEntityResource;

@RepositoryRestController
public class UserController {

    private final UserRepository users;

    @Autowired
    public UserController(UserRepository users) { 
        this.users = users;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/self") 
    public @ResponseBody PersistentEntityResource getSelf(@Autowired PersistentEntityResourceAssembler assembler) {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(principal instanceof JwtUser)
		    return assembler.toResource(
			    users.findById(((JwtUser)principal).getId()).get()
			    );
	    return null;
    }

}
