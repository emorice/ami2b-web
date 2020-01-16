package org.ami2b.web.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.InputStream;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.PagedResources;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.ami2b.web.models.Sequence;
import org.ami2b.web.models.Feature;
import org.ami2b.web.models.FeatureRepository;
import org.ami2b.web.models.Peptide;
import org.ami2b.web.models.PeptideRepository;
import org.ami2b.web.models.Genome;
import org.ami2b.web.models.GenomeRepository;

@RepositoryRestController
@Slf4j
public class GenomeController {
	@Autowired
	private GenomeRepository genomes;

	@Autowired
	private FeatureRepository features;

	@Autowired
	private PeptideRepository peptides;

	private class IUPACDNACompoundSet extends DNACompoundSet {
		public IUPACDNACompoundSet() {
			addNucleotideCompound("M", "K");
			addNucleotideCompound("K", "M");
			addNucleotideCompound("R", "Y");
			addNucleotideCompound("Y", "R");
			addNucleotideCompound("S", "S");
			addNucleotideCompound("W", "W");
			addNucleotideCompound("B", "V");
			addNucleotideCompound("V", "B");
			addNucleotideCompound("D", "H");
			addNucleotideCompound("H", "D");
		}
	}
	private LinkedHashMap<String, DNASequence> readFasta(InputStream stream)
		throws IOException {


		FastaReader<DNASequence, NucleotideCompound> fastaReader = new FastaReader<DNASequence, NucleotideCompound>(
				stream,
				new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
				new DNASequenceCreator(new IUPACDNACompoundSet()));
		return fastaReader.process();
	}
	private LinkedHashMap<String, ProteinSequence> readProteinFasta(InputStream stream)
		throws IOException {

		FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<ProteinSequence, AminoAcidCompound>(
				stream,
				new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
				new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
		return fastaReader.process();
	}

	@PostMapping("/genomes/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody PersistentEntityResource upload(@RequestParam MultipartFile fastaFile, @Autowired PersistentEntityResourceAssembler assembler) throws IOException {
		log.info("Received file of length " + fastaFile.getSize());
		LinkedHashMap<String,DNASequence> sequences =
			readFasta(fastaFile.getInputStream());
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
		genome.setDescription(header);
		genome.setSequence(sequence);
		genome = genomes.save(genome);

		return assembler.toResource(genome);
	}	

	@PostMapping("/genomes/{id}/uploadFeatures")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public ResponseEntity<PersistentEntityResource> uploadFeatures(
			@PathVariable Long id,
			@RequestParam MultipartFile fastaFile,
			@Autowired PersistentEntityResourceAssembler assembler)
			throws IOException {
		log.info("Received file of length " + fastaFile.getSize());

		Genome genome;
		try {
			genome = genomes.findById(id).get();
		} catch(NoSuchElementException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		List<Feature> features = genome.getFeatures();

		LinkedHashMap<String,DNASequence> sequences =
			readFasta(fastaFile.getInputStream());
		log.info("Parsed " + sequences.size() + " sequences");

		for(Map.Entry<String, DNASequence> record: sequences.entrySet() ) {
			Sequence sequence = new Sequence();
			Feature feature = new Feature();
			String header = record.getKey();
			boolean first = true;
			String lastAttr = "";
			for(String part : header.split(" ")) {
				if(first) {
					feature.setAccession(part);
					first = false;
					continue;
				}
				String[] subparts = part.split(":");
				if(subparts[0].equals("chromosome") && (
							subparts.length == 6
							|| subparts.length == 5)) {
					feature.setStart(Long.parseLong(subparts[3]));
					feature.setStop(Long.parseLong(subparts[4]));
					if(subparts.length == 6)
						feature.setStrand(Long.parseLong(subparts[5]));
					continue;
				}
				String left;
				if(subparts.length > 1) {
					lastAttr = subparts[0];
					left = String.join(":", Arrays.copyOfRange(subparts, 1, subparts.length));
				} else {
					left = part;
				}
				switch(lastAttr) {
					case "gene":
						feature.setGene(left);
						break;
					case "gene_biotype":
						feature.setGeneBiotype(left);
						break;
					case "transcript_biotype":
						feature.setTranscriptBiotype(left);
						break;
					case "description":
						feature.setDescription(
							feature.getDescription() == "" ? 
							left :
							(feature.getDescription() + " " + left));
						break;
				}
			}
			sequence.setSequence(record.getValue().getSequenceAsString());
			feature.setSequence(sequence);
			feature.setGenome(genome);
			features.add(feature);
		}

		log.info("Persisting genome with " + genome.getFeatures().size() + " features.");
		genome = genomes.save(genome);

		return ResponseEntity.ok(assembler.toResource(genome));
	}	

	@Autowired
	private PagedResourcesAssembler pagedResourcesAssembler;

	@GetMapping("/genomes/{id}/features")
	public @ResponseBody PagedResources<PersistentEntityResource> getFeatures(
			@PathVariable Long id,
			Pageable pageable,
			@Autowired PersistentEntityResourceAssembler assembler)
			{
				Page<Feature> page = features.findByGenomeId(id, pageable);
				return pagedResourcesAssembler.toResource(page, assembler);
			}

	@PostMapping("/genomes/{id}/uploadPeptides")
	@ResponseStatus(HttpStatus.CREATED)
	@Transactional
	public ResponseEntity<PersistentEntityResource> uploadPeptides(
			@PathVariable Long id,
			@RequestParam MultipartFile fastaFile,
			@Autowired PersistentEntityResourceAssembler assembler)
			throws IOException {
		log.info("Received file of length " + fastaFile.getSize());

		Genome genome;
		try {
			genome = genomes.findById(id).get();
		} catch(NoSuchElementException e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		List<Peptide> peptides = genome.getPeptides();

		LinkedHashMap<String,ProteinSequence> sequences =
			readProteinFasta(fastaFile.getInputStream());
		log.info("Parsed " + sequences.size() + " sequences");

		for(Map.Entry<String, ProteinSequence> record: sequences.entrySet() ) {
			Sequence sequence = new Sequence();
			Peptide peptide = new Peptide();
			String header = record.getKey();
			peptide.setAccession(header.split(" ")[0]);
			sequence.setSequence(record.getValue().getSequenceAsString());
			peptide.setSequence(sequence);
			peptide.setGenome(genome);
			peptides.add(peptide);
		}

		log.info("Persisting genome with " + genome.getPeptides().size() + " peptides.");
		genome = genomes.save(genome);

		return ResponseEntity.ok(assembler.toResource(genome));
	}	

	@GetMapping("/genomes/{id}/peptides")
	public @ResponseBody PagedResources<PersistentEntityResource> getPeptides(
			@PathVariable Long id,
			Pageable pageable,
			@Autowired PersistentEntityResourceAssembler assembler)
			{
				Page<Peptide> page = peptides.findByGenomeId(id, pageable);
				return pagedResourcesAssembler.toResource(page, assembler);
			}
}
