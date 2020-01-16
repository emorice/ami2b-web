package org.ami2b.web.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ami2b.web.models.Peptide;


public interface PeptideRepository extends PagingAndSortingRepository<Peptide, Long> {
	public Page<Peptide> findByGenomeId(Long id, Pageable p);
}
