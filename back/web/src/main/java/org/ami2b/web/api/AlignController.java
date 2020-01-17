package org.ami2b.web.api;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.core.alignment.template.Profile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class AlignController {
	@PostMapping("/align")
	public @ResponseBody Map<String, String> align(@RequestBody  Map<String, String> inSeqs) 
		throws CompoundNotFoundException {
		log.info("Aligner");
		List<ProteinSequence> seqList = new ArrayList<ProteinSequence>();
		List<String> keys = new ArrayList<String>();
		for(String k: inSeqs.keySet()) {
			log.info(inSeqs.get(k));
			seqList.add(new ProteinSequence(inSeqs.get(k)));
			keys.add(k);
		}
		Profile<ProteinSequence, AminoAcidCompound> profile = Alignments.getMultipleSequenceAlignment(seqList);
		Map<String, String> result = new HashMap<>();
		int i = 0;
		for(Sequence seq : profile) {
			result.put(keys.get(i), seq.getSequenceAsString());
			i++;
		}
		return result;
	}
}

