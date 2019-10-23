package org.ami2b.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.Data;

import org.ami2b.web.models.Sequence;

@Entity
@Data
public class Genome  {
	@Id
	@GeneratedValue
	private Long id;

	@OneToOne(cascade=CascadeType.ALL)
	private Sequence sequence;
}
