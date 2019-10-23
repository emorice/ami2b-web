package org.ami2b.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import java.util.Collections;
import lombok.Data;

@Entity
@Data
public class Sequence  {
	@Id
	@GeneratedValue
	private Long id;

	@Column(columnDefinition="text")
	private String sequence;
}
