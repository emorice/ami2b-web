package org.ami2b.web.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import lombok.Data;

import org.ami2b.web.models.Genome;

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

	private Long start;

	private Long stop;

	private Long strand;

	private String gene;

	private String geneBiotype;

	private String transcriptBiotype;

	private String description;

	private int length;
}
