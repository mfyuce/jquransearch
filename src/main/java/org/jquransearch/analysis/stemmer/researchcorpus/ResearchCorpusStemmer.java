package org.jquransearch.analysis.stemmer.researchcorpus;

import org.apache.maven.shared.utils.StringUtils;
import org.jquransearch.analysis.stemmer.arabiccorpus.AttributeTags;
import org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem;
import org.jquransearch.analysis.stemmer.arabiccorpus.PartOfSpeechTag;
import org.jquransearch.core.CorpusLocation;
import org.jqurantree.analysis.AnalysisTable;
import org.jqurantree.arabic.ArabicText;
import org.jqurantree.arabic.encoding.EncodingType;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Location;
import org.jqurantree.orthography.Token;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem.parse;
import static org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem.parseResearchCorpus;
import static org.jquransearch.analysis.stemmer.arabiccorpus.QuranicCorpusStemmer.ROOT;
import static org.jquransearch.core.utils.ResourceUtil.loadResourceFile;
import static org.jquransearch.tools.Tools.splitInput;

public class ResearchCorpusStemmer {
    public static Map<CorpusLocation, CorpusItem> corpus = new LinkedHashMap<>();
    public static Map<String, String> roots = new LinkedHashMap<>();
    public static Map<String, String> partOfSpeechConversion = new LinkedHashMap<String, String>(){
        {
            put("PREP", String.valueOf(PartOfSpeechTag.P));
            put("part_verb", String.valueOf(PartOfSpeechTag.CERT)); // because all part_verbs shows `قد`
            put("pron_dem", String.valueOf(PartOfSpeechTag.DEM));
            put("part_det", String.valueOf(PartOfSpeechTag.DET));
            put("part_focus", String.valueOf(PartOfSpeechTag.EXL));
            put("part_fut", String.valueOf(PartOfSpeechTag.FUT));
            put("part_interrog", String.valueOf(PartOfSpeechTag.INTG));
            put("noun", String.valueOf(PartOfSpeechTag.N));
            put("part_neg", String.valueOf(PartOfSpeechTag.NEG));
            put("noun_prop", String.valueOf(PartOfSpeechTag.PN));
            put("prep", String.valueOf(PartOfSpeechTag.P));
            put("pron_rel", String.valueOf(PartOfSpeechTag.REL));
            put("conj_sub", String.valueOf(PartOfSpeechTag.SUB));
            put("verb", String.valueOf(PartOfSpeechTag.V));
            put("part_voc", String.valueOf(PartOfSpeechTag.VOC));
        }};
    static {
        try {
            loadCorpusFile();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void loadCorpusFile() throws IOException, URISyntaxException {
        String inBetween = null;
        String lastRoot = null;
        Location lastLocation = null;
        for (String line : loadResourceFile("stemming/research-quranic-corpus-morphology.txt", ResearchCorpusStemmer.class, StandardCharsets.UTF_8)) {
            String trim = line.trim();
            if (StringUtils.isNotBlank(trim) && !trim.startsWith("#") && !trim.toLowerCase(Locale.ENGLISH).startsWith("location")) {
                String[] items = line.split("\t", -1);
                CorpusItem parsed = parseResearchCorpus(items);
                CorpusLocation currentLocation = parsed.getLocation();

                if (lastLocation != null && !lastLocation.equals(currentLocation)) {
                    // unrootable word
                    addNewRoot(inBetween, ArabicText.fromUnicode(inBetween).toBuckwalter());

                    inBetween = null;
                    lastRoot = null;
                    lastLocation = null;
                }
                Token token = Document.getToken(currentLocation);
                Map<String, String> features = parsed.getFeatures();
                boolean canCheckForRoot = true;
                String text = ArabicText.fromBuckwalter(parsed.getText()).toUnicode();
                boolean lastRootExists = StringUtils.isNotBlank(lastRoot);
                if (features.containsKey(ROOT) || lastRootExists) {
                    String root = lastRootExists ? lastRoot : features.get(ROOT);
                    String currentToken = token.toUnicode();
                    if (!text.equals(currentToken)) {
                        if (StringUtils.isNotBlank(inBetween)) {
                            inBetween = inBetween + text;
                            if (!currentToken.equals(inBetween)) {
                                canCheckForRoot = false;
                                lastRoot = root;
                                lastLocation = currentLocation;
                            } else {
                                text = inBetween;
                                inBetween = null;
                                lastRoot = null;
                                lastLocation = null;
                            }
                        } else {
                            inBetween = text;
                            lastRoot = root;
                            canCheckForRoot = false;
                            lastLocation = currentLocation;
                        }
                    } else {
                        inBetween = null;
                        lastRoot = null;
                        lastLocation = null;
                    }
                    if (canCheckForRoot) {
                        addNewRoot(text, root);
                    }
                } else {
                    inBetween = String.format("%s%s", StringUtils.isNotBlank(inBetween) ? inBetween : "", text);
                    lastLocation = currentLocation;
                }
                if(corpus.containsKey(currentLocation)){
                    System.out.println();
                }
                corpus.put(currentLocation, parsed);
            }
        }
    }

    private static void addNewRoot(String text, String root) {
        if (!roots.containsKey(text)) {
            roots.put(text, ArabicText.fromBuckwalter(root).removeDiacritics().removeNonLetters().toUnicode());
        }
    }

    public static String stem(ArabicText text) {
        String u = text.toUnicode();
        if (text.toUnicode().equals("ماكول")) {
            System.out.println();
        }
        if (roots.containsKey(u)) {
            return roots.get(u);
        }
        return u;
    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        loadCorpusFile();
    }
}
