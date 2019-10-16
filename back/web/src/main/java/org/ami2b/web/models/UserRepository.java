package org.ami2b.web.models;

import org.springframework.data.repository.CrudRepository;
import org.ami2b.web.models.User;


public interface UserRepository extends CrudRepository<User, Long> {
	public User findByUsername(String username);
}
