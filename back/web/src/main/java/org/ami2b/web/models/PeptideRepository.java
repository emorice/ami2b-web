package org.ami2b.web.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.ami2b.web.models.Peptide;


public interface PeptideRepository extends PagingAndSortingRepository<Peptide, Long> {
	public Page<Peptide> findByGenomeId(Long id, Pageable p);

	@Query("SELECT p FROM Peptide p, Feature f WHERE f.accession = p.accession AND f.id = ?1")
	public Peptide findMatching(Long id);
}
