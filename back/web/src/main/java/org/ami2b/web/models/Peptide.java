package org.ami2b.web.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import lombok.Data;

import org.ami2b.web.models.Genome;
import org.ami2b.web.models.Sequence;

@Entity
@Data
public class Peptide  {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne()
	private Genome genome;

	@OneToOne(cascade=CascadeType.ALL)
	private Sequence sequence;
}
