package org.ami2b.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import org.ami2b.web.models.Project;
import org.ami2b.web.models.Feature;

@Entity
@Data
public class User  {
	@Id
	@GeneratedValue
	private Long id;

	@Column(unique=true)
	private String username;

	private String firstName;
	private String lastName;

	@JsonIgnore
	private String password;

	@ManyToMany(fetch=FetchType.LAZY)
	public List<Feature> tasks = new ArrayList();

	@ManyToMany(fetch=FetchType.LAZY)
	@JsonManagedReference
	public Set<Project> projects = new HashSet();

	public JwtUser toJwtUser() {
		// For now everything is builtin, but can be easely changed to
		// persistent attributes later
		JwtUser user = new JwtUser();
		user.setId(this.id);
		user.setUsername(this.username);
		user.setPassword(this.password);
		user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setApiAccessAllowed(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		return user;
	}
}
