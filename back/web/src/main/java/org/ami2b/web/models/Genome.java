package org.ami2b.web.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import java.util.Collections;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.ami2b.web.models.Sequence;

@Entity
@Data
public class Genome  {
	@Id
	@GeneratedValue
	private Long id;

	private String description;

	private int length;

	@OneToOne(cascade=CascadeType.ALL)
	@JsonIgnore // Seq can be very long, separate endpoint to retrieve it.
	private Sequence sequence;

	public void setSequence(Sequence sequence) {
		setLength(sequence.getSequence().length());
		this.sequence = sequence;
	}
}
