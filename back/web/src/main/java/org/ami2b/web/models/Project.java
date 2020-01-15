package org.ami2b.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import java.util.Collections;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

import org.ami2b.web.models.Genome;

@Entity
@Data
public class Project  {
	@Id
	@GeneratedValue
	private Long id;

	@Column(unique=true)
	private String name;


	@OneToMany
	private Set<Genome> genomes = new HashSet();

}
