package org.ami2b.web.models;

import org.springframework.data.repository.CrudRepository;
import org.ami2b.web.models.Peptide;


public interface PeptideRepository extends CrudRepository<Peptide, Long> {
}
