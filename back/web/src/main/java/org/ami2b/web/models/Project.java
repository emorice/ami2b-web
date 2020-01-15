package org.ami2b.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import java.util.Collections;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.ami2b.web.models.Genome;
import org.ami2b.web.models.User;

@Entity
@Data
public class Project  {
	@Id
	@GeneratedValue
	private Long id;

	@Column(unique=true)
	private String name;

	@OneToMany(fetch=FetchType.LAZY)
	@JsonManagedReference
	private Set<Genome> genomes = new HashSet();

	@ManyToMany(mappedBy="projects", fetch=FetchType.LAZY)
	@JsonBackReference
	private List<User> members;

}
