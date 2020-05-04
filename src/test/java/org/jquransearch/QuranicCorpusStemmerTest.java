package org.jquransearch;

import org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem;
import org.jquransearch.analysis.stemmer.arabiccorpus.QuranicCorpusStemmer;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class QuranicCorpusStemmerTest {
	@Test
	@Ignore
	public void distinctTags() {
		HashMap<String, Boolean> distinctTags = new HashMap<String,Boolean>();
		for (CorpusItem i: QuranicCorpusStemmer.corpus) {
			if(!distinctTags.containsKey(i.getPartOfSpeechTag())){
				distinctTags.put(i.getTagText(),true);
			}
		}
		System.out.println();
	}
    @Test
//    @Ignore
    public void distinctFeatureNames()  {
        HashMap<String, Boolean> distinctFeatureNames = new HashMap<String,Boolean>();
        for (CorpusItem i: QuranicCorpusStemmer.corpus) {
            for (String f: i.getFeatures().keySet()) {
                if (!distinctFeatureNames.containsKey(f)) {
                    distinctFeatureNames.put(f, true);
                }
            }
        }
        System.out.println();
    }
}
