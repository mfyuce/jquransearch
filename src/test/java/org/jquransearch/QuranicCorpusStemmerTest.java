package org.jquransearch;

import org.apache.commons.lang3.StringUtils;
import org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem;
import org.jquransearch.analysis.stemmer.arabiccorpus.PartOfSpeechTag;
import org.jquransearch.analysis.stemmer.arabiccorpus.QuranicCorpusStemmer;
import org.jquransearch.analysis.stemmer.researchcorpus.ResearchCorpusStemmer;
import org.jquransearch.core.CorpusLocation;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;

public class QuranicCorpusStemmerTest {
    @Test
    @Ignore
    public void distinctTags() {
        HashMap<String, Boolean> distinctTags = new HashMap<String, Boolean>();
        for (CorpusItem i : QuranicCorpusStemmer.corpus.values()) {
            if (!distinctTags.containsKey(i.getPartOfSpeechTag())) {
                distinctTags.put(i.getTagText(), true);
            }
        }
    }

    @Test
    @Ignore
    public void distinctFeatureNames() {
        HashMap<String, Boolean> distinctFeatureNames = new HashMap<String, Boolean>();
        for (CorpusItem i : QuranicCorpusStemmer.corpus.values()) {
            for (String f : i.getFeatures().keySet()) {
                if (!distinctFeatureNames.containsKey(f)) {
                    distinctFeatureNames.put(f, true);
                }
            }
        }
    }

    @Test
    @Ignore
    public void corpusCompare() {
        HashMap<String, Boolean> distinctFeatureNames = new HashMap<String, Boolean>();
        for (CorpusItem i : QuranicCorpusStemmer.corpus.values()) {
            for (String f : i.getFeatures().keySet()) {
                if (!distinctFeatureNames.containsKey(f)) {
                    distinctFeatureNames.put(f, true);
                }
            }
        }
    }

    @Test
    @Ignore
    public void corpusCompareTestPositions() {
        int cntNotMatch = 0;
        for (CorpusLocation i : ResearchCorpusStemmer.corpus.keySet()) {
            if (!QuranicCorpusStemmer.corpus.containsKey(i)) {
                System.out.println("Not contained in Corpus:" + ResearchCorpusStemmer.corpus.get(i));
                cntNotMatch++;
            }
        }
        System.out.println("Number of Location mismatch (not contained in Corpus): " + cntNotMatch);
        cntNotMatch = 0;
        for (CorpusLocation i : QuranicCorpusStemmer.corpus.keySet()) {
            if (!ResearchCorpusStemmer.corpus.containsKey(i)) {
                System.out.println("Not contained in ResearchCorpus:" + QuranicCorpusStemmer.corpus.get(i));
                cntNotMatch++;
            }
        }
        System.out.println("Number of Location mismatch (not contained in ResearchCorpus): " + cntNotMatch);
    }

    @Test
    @Ignore
    public void corpusCompareTestPartOfSpeech() {
        int cntNotMatch = 0;
        Map<String, Integer> mismatches = new LinkedHashMap<>();
        for (CorpusItem corpus : QuranicCorpusStemmer.corpus.values()) {
            CorpusItem research = ResearchCorpusStemmer.corpus.get(corpus.getLocation());
            if (research != null) {
                PartOfSpeechTag researchPOS = research.getPartOfSpeechTag();
                PartOfSpeechTag corpusPOS = corpus.getPartOfSpeechTag();
                if (researchPOS != corpusPOS) {
                    String key = corpusPOS + "->" + researchPOS;
                    cntNotMatch++;
                    if (!mismatches.containsKey(key)) {
                        mismatches.put(key, 0);
                    }
                    mismatches.replace(key, mismatches.get(key) + 1);
                }
            }
        }
        System.out.println("Number of direct POS mismatch: " + cntNotMatch);
        System.out.println("Lists: " + join(mismatches.keySet().stream().map(t -> t + ": " + mismatches.get(t)).collect(Collectors.toList()), System.lineSeparator()));
    }

    @Test
    @Ignore
    public void researchCorpusPseudoVerbListTest() {
        for (CorpusItem i : ResearchCorpusStemmer.corpus.values()) {
            if (i.getPartOfSpeechTag().equals(PartOfSpeechTag.VERB_PSEUDO)) {
                System.out.println(i.getLocation() + " " + i.getPartOfSpeechTag());
            }
        }
    }

    public static boolean isPureAscii(String v) {
        return Charset.forName("US-ASCII").newEncoder().canEncode(v);
        // or "ISO-8859-1" for ISO Latin 1
        // or StandardCharsets.US_ASCII with JDK1.7+
    }

    @Test
    @Ignore
    public void distinctStemcatTest() {
        Map<String, Set<String>> distinctFeaturesAndValues = new HashMap<>();
        for (CorpusItem i : ResearchCorpusStemmer.corpus.values()) {
            Map<String, String> features = i.getFeatures();
            for (String feature : features.keySet()) {
                if (!distinctFeaturesAndValues.containsKey(feature)) {
                    distinctFeaturesAndValues.put(feature, new TreeSet<>());
                }
                if (!feature.equals("stem")
                        && !feature.equals("lex")
                        && !feature.equals("bw")
                        && !feature.equals("gloss")
                        && !feature.equals("ROOT")
                        && !feature.equals("LEM")
                        && !feature.equals("diac")) {

                    if (!isPureAscii(feature.charAt(0) + "")) {
                        System.out.println();
                    }
                    distinctFeaturesAndValues.get(feature).add(features.get(feature));
                }
            }
        }
        for (String feature : distinctFeaturesAndValues.keySet()) {
            System.out.println(System.lineSeparator() + System.lineSeparator() + System.lineSeparator()
                    + feature + ":");
            System.out.println(StringUtils.join(distinctFeaturesAndValues.get(feature).toArray(), System.lineSeparator()));
        }
    }

    @Test
//    @Ignore
    public void rootProblemsTest() {

        int cntNotMatch = 0;
        Map<String, Integer> mismatches = new LinkedHashMap<>();
        for (CorpusItem corpus : QuranicCorpusStemmer.corpus.values()) {
            CorpusItem research = ResearchCorpusStemmer.corpus.get(corpus.getLocation());
            if (research != null) {
                String researchRoot = research.getRootBW();
                String corpusRoot = corpus.getRootBW();
                if ((StringUtils.isNotBlank(researchRoot) && StringUtils.isNotBlank(corpusRoot)) && (
                        StringUtils.isBlank(researchRoot) && StringUtils.isNotBlank(corpusRoot)
                        || StringUtils.isNotBlank(researchRoot) && StringUtils.isBlank(corpusRoot)
                        || !researchRoot.equals(corpusRoot))) {
                    String key = corpusRoot + "->" + researchRoot;
                    cntNotMatch++;
                    if (!mismatches.containsKey(key)) {
                        mismatches.put(key, 0);
                    }
                    mismatches.replace(key, mismatches.get(key) + 1);
                }
            }
        }
        System.out.println("Number of direct mismatch: " + cntNotMatch);
        System.out.println("Lists: " + join(mismatches.keySet().stream().map(t -> t + ": " + mismatches.get(t)).collect(Collectors.toList()), System.lineSeparator()));
    }
}
