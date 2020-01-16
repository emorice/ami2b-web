package org.ami2b.web.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;
import lombok.Data;

import org.ami2b.web.models.Genome;
import org.ami2b.web.models.User;
import org.ami2b.web.models.Peptide;

@Entity
@Data
public class Feature  {
	@Id
	@GeneratedValue
	private Long id;

	private String accession;

	@ManyToOne()
	@JsonBackReference
	private Genome genome;

	@ManyToMany(mappedBy="tasks")
	@JsonBackReference
	private List<User> annotators = new ArrayList();;

	private Long start;

	private Long stop;

	private Long strand;

	private String gene = "";

	private String geneBiotype = "";

	private String transcriptBiotype = "";

	private String description = "";

	private int length;

	@OneToOne(cascade=CascadeType.ALL)
	private Sequence sequence;

	@OneToOne()
	private Peptide peptide;
}
