package org.jquransearch.analysis.stemmer.researchcorpus;

import org.apache.maven.shared.utils.StringUtils;
import org.jquransearch.analysis.stemmer.arabiccorpus.AttributeTags;
import org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem;
import org.jquransearch.analysis.stemmer.arabiccorpus.PartOfSpeechTag;
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
import static org.jquransearch.core.utils.ResourceUtil.loadResourceFile;
import static org.jquransearch.tools.Tools.splitInput;

public class ResearchCorpusStemmer {
    public static final String ROOT = "ROOT";
    public static List<CorpusItem> corpus = new ArrayList<>();
    public static Map<String, String> roots = new LinkedHashMap<>();

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
                Location currentLocation = parsed.getLocation();

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
                corpus.add(parsed);
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

    public static AnalysisTable search(PartOfSpeechTag partOfSpeechTag,
                                       AttributeTags form,
                                       ArabicText root,
                                       ArabicText lemma,
                                       ArabicText stem,
                                       EncodingType outputEncodingType) {
        boolean stemIsBlank = stem == null;
        boolean lemmaIsBlank = lemma==null;
        boolean rootIsBlank = root==null;
        String[] rootTexts=rootIsBlank?null:splitInput( EncodingType.Buckwalter,root) ;
        String[] lemmaTexts=lemmaIsBlank?null: splitInput(EncodingType.Buckwalter,lemma) ;
        String[] stemTexts=stemIsBlank?null: splitInput(EncodingType.Buckwalter,stem) ;
        String[] noDiacriticsStems = stemIsBlank?null:splitInput(EncodingType.Buckwalter,stem.removeDiacritics());
        List<CorpusItem> ret = corpus.stream()
                .filter(t -> {
                    if (partOfSpeechTag == PartOfSpeechTag.None || t.getPartOfSpeechTag() == partOfSpeechTag) {
                        Map<AttributeTags, String> taggedFeatures = t.getTaggedFeatures();
                        if (form == null || form == AttributeTags.None || taggedFeatures.containsKey(form)) {
                            String currentLemma = taggedFeatures.get(AttributeTags.LEM);
                            if (lemmaIsBlank
                                    || (StringUtils.isNotBlank(currentLemma)
                                        && Arrays.stream(lemmaTexts).anyMatch(lemmaText->currentLemma.equals(lemmaText)) )) {
                                String text = t.getText();
                                String noDiacriticsText = ArabicText.fromBuckwalter(text).removeDiacritics().toBuckwalter();
                                if (stemIsBlank || (taggedFeatures.containsKey(AttributeTags.STEM)
                                        && Arrays.stream(noDiacriticsStems).anyMatch(noDiacriticsStem->noDiacriticsText.contains(noDiacriticsStem) ))) {
                                    if (!rootIsBlank) {
                                        String currentRoot = taggedFeatures.get(AttributeTags.ROOT);
                                        if (StringUtils.isNotBlank(currentRoot) && Arrays.stream(rootTexts).anyMatch(rootText->currentRoot.equals(rootText))) {
                                            return true;
                                        }
                                    } else {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
        return toTable(ret,outputEncodingType);
    }

    public static AnalysisTable toTable(List<CorpusItem> lst,EncodingType outputEncodingType) {
         AnalysisTable tbl = new AnalysisTable(new String[]{"location","text", "tag", "features"});
         lst.stream().forEach(t->{

             Location location = t.getLocation();
             String text = t.getText();

             boolean isBWEncoding = outputEncodingType.equals(EncodingType.Buckwalter);
             ArabicText bwText = ArabicText.fromBuckwalter(text);
             tbl.add(location.getChapterNumber() +
                     ":" + location.getVerseNumber() +
                     ":" + location.getTokenNumber() +
                     ":" + t.getLetter(),
                     isBWEncoding ?text: bwText.toString(outputEncodingType),
                     t.getTagText(),
                     StringUtils.join(t.getTaggedFeatures().entrySet()
                             .stream().map(u->{
                                 AttributeTags key = u.getKey();
                                 String value = u.getValue();
                                 boolean valueIsBlank = StringUtils.isBlank(value);

                                 if(!isBWEncoding
                                         && !valueIsBlank
                                         && (key.equals(AttributeTags.LEM)
                                                || key.equals(AttributeTags.ROOT)))
                                 {
                                     value = ArabicText.fromBuckwalter(value).toString(outputEncodingType);
                                 }
                                 return key
                                         + (valueIsBlank?"":":" + value);
                             })
                             .collect(Collectors.toList()).toArray(),"|"));
         });
        return tbl;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadCorpusFile();
    }
}
