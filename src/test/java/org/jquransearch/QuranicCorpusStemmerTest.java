package org.jquransearch;

import org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem;
import org.jquransearch.analysis.stemmer.arabiccorpus.QuranicCorpusStemmer;
import org.jquransearch.analysis.stemmer.researchcorpus.ResearchCorpusStemmer;
import org.jquransearch.core.CorpusLocation;
import org.jqurantree.orthography.Location;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class QuranicCorpusStemmerTest {
	@Test
	@Ignore
	public void distinctTags() {
		HashMap<String, Boolean> distinctTags = new HashMap<String,Boolean>();
		for (CorpusItem i: QuranicCorpusStemmer.corpus.values()) {
			if(!distinctTags.containsKey(i.getPartOfSpeechTag())){
				distinctTags.put(i.getTagText(),true);
			}
		}
		System.out.println();
	}
    @Test
    @Ignore
    public void distinctFeatureNames()  {
        HashMap<String, Boolean> distinctFeatureNames = new HashMap<String,Boolean>();
        for (CorpusItem i: QuranicCorpusStemmer.corpus.values()) {
            for (String f: i.getFeatures().keySet()) {
                if (!distinctFeatureNames.containsKey(f)) {
                    distinctFeatureNames.put(f, true);
                }
            }
        }
        System.out.println();
    }

    @Test
    @Ignore
    public void corpusCompare()  {
        HashMap<String, Boolean> distinctFeatureNames = new HashMap<String,Boolean>();
        for (CorpusItem i: QuranicCorpusStemmer.corpus.values()) {
            for (String f: i.getFeatures().keySet()) {
                if (!distinctFeatureNames.containsKey(f)) {
                    distinctFeatureNames.put(f, true);
                }
            }
        }
        System.out.println();
    }
    @Test
//    @Ignore
    public void corpusCompareTestPositions()  {
        for (CorpusLocation i: ResearchCorpusStemmer.corpus.keySet()) {
            if(!QuranicCorpusStemmer.corpus.containsKey(i)){
                System.out.println(ResearchCorpusStemmer.corpus.get(i));
            }
        }

        for (CorpusLocation i: QuranicCorpusStemmer.corpus.keySet()) {
            if(!ResearchCorpusStemmer.corpus.containsKey(i)){
                System.out.println(QuranicCorpusStemmer.corpus.get(i));
            }
        }
        System.out.println();
    }
}
