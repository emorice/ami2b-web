package org.ami2b.web.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.ami2b.web.models.Genome;


public interface GenomeRepository extends PagingAndSortingRepository<Genome, Long> {
}
