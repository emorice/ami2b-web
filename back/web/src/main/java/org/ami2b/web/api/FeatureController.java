package org.ami2b.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.data.rest.webmvc.support.BaseUriLinkBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.ami2b.web.models.Feature;
import org.ami2b.web.models.FeatureRepository;
import org.ami2b.web.models.PeptideRepository;

@RepositoryRestController
public class FeatureController {

	@Autowired
	private FeatureRepository features;

	@Autowired
	private PeptideRepository peptides;

	@RequestMapping(method = RequestMethod.GET, value = "/features/{id}/peptide") 
	public @ResponseBody PersistentEntityResource getMatchingPeptide(
		@PathVariable Long id,
		@Autowired PersistentEntityResourceAssembler assembler
		) {
			return assembler.toResource(
				peptides.findMatching(id)
				);
	}

	@Autowired
	private RepositoryRestConfiguration config;

	@Bean
	public ResourceProcessor<Resource<Feature>> featureProcessor() {

		return new ResourceProcessor<Resource<Feature>>() {
			/**
			 * bugfix https://github.com/spring-projects/spring-hateoas/issues/434
			 */

			private LinkBuilder linkTo(Object invocationValue) {
				LinkBuilder target = ControllerLinkBuilder.linkTo(invocationValue);
				LinkBuilder context = BaseUriLinkBuilder.create(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri());
				URI basedContext = context.slash(config.getBasePath()).toUri();
				URI suffix = context.toUri().relativize(target.toUri());
				return BaseUriLinkBuilder.create(basedContext).slash(suffix);
			}

			@Override
			public Resource<Feature> process(Resource<Feature> resource) {

				resource.add(
					linkTo(methodOn(FeatureController.class)
						.getMatchingPeptide(resource.getContent().getId(), null)
					      )
					.withRel("peptide")
					);
				return resource;
			}
		};
	}
}
