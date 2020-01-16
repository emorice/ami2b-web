package org.ami2b.web.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.ami2b.web.models.Sequence;

public interface SequenceRepository extends CrudRepository<Sequence, Long> {
	@Query("SELECT SUBSTRING(sequence, ?2, ?3 - ?2 + 1) FROM Sequence WHERE id=?1")
	public String getSegment(Long id, Long start, Long stop);
	@Query("SELECT sequence FROM Sequence WHERE id=?1")
	public String getFullSequence(Long id);
}
