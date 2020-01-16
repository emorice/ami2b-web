package org.ami2b.web.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.ami2b.web.models.Sequence;
import org.ami2b.web.models.Feature;

@Entity
@Data
public class Genome  {
	@Id
	@GeneratedValue
	private Long id;

	private String description;

	private int length;

	@OneToOne(cascade=CascadeType.ALL)
	private Sequence sequence;

	@OneToMany(mappedBy="genome", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JsonIgnore
	private List<Feature> features;

	@OneToMany(mappedBy="genome", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Peptide> peptides;

	public void setSequence(Sequence sequence) {
		setLength(sequence.getSequence().length());
		this.sequence = sequence;
	}
}
