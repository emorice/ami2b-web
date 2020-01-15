package org.ami2b.web.api;

import java.util.LinkedHashMap;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.ami2b.web.models.Sequence;
import org.ami2b.web.models.Genome;
import org.ami2b.web.models.GenomeRepository;

@RestController
@RequestMapping("/api/genome")
@Slf4j
public class GenomeController {
	@Autowired
	private GenomeRepository genomeRepository;

	@PostMapping("/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public void upload(@RequestParam MultipartFile fastaFile) throws IOException {
		log.info("Received file of length " + fastaFile.getSize());
		LinkedHashMap<String,DNASequence> sequences =
			FastaReaderHelper.readFastaDNASequence(fastaFile.getInputStream());
		log.info("Parsed " + sequences.size() + " sequences");
		// Handle multifasta
		if(sequences.size() > 1) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"More than one sequence in genome FASTA");
		}
		if(sequences.size() < 1) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"No valid sequence in genome FASTA");
		}
		Sequence sequence = new Sequence();
		String header = sequences.keySet().iterator().next();
		sequence.setSequence(sequences.get(header).getSequenceAsString());
		Genome genome = new Genome();
		genome.setSequence(sequence);
		genomeRepository.save(genome);
	}	

}