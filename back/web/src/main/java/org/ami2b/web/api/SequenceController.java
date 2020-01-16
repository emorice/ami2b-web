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

import org.ami2b.web.models.SequenceRepository;

@RepositoryRestController
public class SequenceController {

	@Autowired
	private SequenceRepository sequences;

	@RequestMapping(method = RequestMethod.GET, value = "/sequences/{id}/fullSequence") 
	public @ResponseBody String getSelf(@PathVariable Long id) {
		// this probably could be done with a projection or excerpt
		return sequences.getFullSequence(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/sequences/{id}/partialSequence") 
	public @ResponseBody String getSelf(@PathVariable Long id, @RequestParam Long start, @RequestParam Long stop) {
		// this probably could be done with a projection or excerpt
		return sequences.getSegment(id, start, stop);
	}

}
