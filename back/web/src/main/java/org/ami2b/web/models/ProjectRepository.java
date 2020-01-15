package org.ami2b.web.models;

import org.springframework.data.repository.CrudRepository;
import org.ami2b.web.models.Project;


public interface ProjectRepository extends CrudRepository<Project, Long> {
}
