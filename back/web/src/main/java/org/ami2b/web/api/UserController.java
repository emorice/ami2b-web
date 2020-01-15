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

@RepositoryRestController
public class UserController {

    private final UserRepository users;

    @Autowired
    public UserController(UserRepository users) { 
        this.users = users;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/self") 
    public @ResponseBody ResponseEntity<?> getSelf() {
	    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if(principal instanceof JwtUser)
		    return ResponseEntity
			    .status(HttpStatus.MOVED_PERMANENTLY)
			    .header(HttpHeaders.LOCATION,"/api/users/" + ((JwtUser)principal).getId()).build();
	    return null;
    }

}
