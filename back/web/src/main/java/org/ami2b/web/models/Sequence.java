package org.ami2b.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import java.util.Collections;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;

import org.ami2b.web.models.Genome;

@Entity
@Data
public class Sequence {
	@Id
	@GeneratedValue
	private Long id;

	@Column(columnDefinition="text")
	@JsonIgnore
	private String sequence;
}
