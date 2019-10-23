package org.ami2b.web.models;

import org.springframework.data.repository.CrudRepository;
import org.ami2b.web.models.Genome;


public interface GenomeRepository extends CrudRepository<Genome, Long> {
}
