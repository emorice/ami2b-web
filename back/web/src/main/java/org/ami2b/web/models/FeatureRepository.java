package org.ami2b.web.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.ami2b.web.models.Feature;


public interface FeatureRepository extends PagingAndSortingRepository<Feature, Long> {
	public Page<Feature> findByGenomeId(Long id, Pageable p);

	public Page<Feature> findByGeneContaining(String keyword, Pageable p);
	public Page<Feature> findByDescriptionContaining(String keyword, Pageable p);
	public Feature findByAccession(String accession);
}
