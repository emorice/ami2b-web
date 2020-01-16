package org.ami2b.web.models;

import org.springframework.data.repository.CrudRepository;
import org.ami2b.web.models.Feature;


public interface FeatureRepository extends CrudRepository<Feature, Long> {
}
