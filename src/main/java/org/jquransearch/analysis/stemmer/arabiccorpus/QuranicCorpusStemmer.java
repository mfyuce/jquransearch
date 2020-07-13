package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.maven.shared.utils.StringUtils;
import org.jquransearch.core.CorpusLocation;
import org.jqurantree.analysis.AnalysisTable;
import org.jqurantree.arabic.ArabicText;
import org.jqurantree.arabic.encoding.EncodingType;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Token;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem.parse;
import static org.jquransearch.core.utils.ResourceUtil.loadResourceFile;
import static org.jquransearch.tools.Tools.splitInput;

public class QuranicCorpusStemmer {
    public static final String ROOT = "ROOT";
    public static Map<CorpusLocation, CorpusItem> corpus = new LinkedHashMap<>();
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
        CorpusItem lastLocation = null;
        for (String line : loadResourceFile("stemming/quranic-corpus-morphology-0.4.txt", QuranicCorpusStemmer.class, StandardCharsets.UTF_8)) {
            String trim = line.trim();
            if (StringUtils.isNotBlank(trim) && !trim.startsWith("#") && !trim.startsWith("LOCATION")) {
                String[] items = line.split("\t", -1);
                CorpusItem parsed = parse(items);
                CorpusLocation currentLocation = parsed.getLocation();

                if (lastLocation != null && ! lastLocation.getLocation().equalsNoLetter(currentLocation)) {
                    // unrootable word
                    String rText = ArabicText.fromUnicode(inBetween).toBuckwalter();
                    addNewRoot(inBetween, rText);
                    String r = parsed.getTaggedFeatures().get(AttributeTags.ROOT);
                    if(StringUtils.isNotBlank(r)){
                        parsed.getTaggedFeatures().put(AttributeTags.ROOT, rText);
                    }
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
                                lastLocation = parsed;
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
                            lastLocation = parsed;
                        }
                    } else {
                        inBetween = null;
                        lastRoot = null;
                        lastLocation = null;
                    }
                    if (canCheckForRoot) {
                        addNewRoot(text, root);
                        String r = parsed.getTaggedFeatures().get(AttributeTags.ROOT);
                        if(StringUtils.isNotBlank(r)){
                            parsed.getTaggedFeatures().put(AttributeTags.ROOT, root);
                        }
                    }
                } else {
                    inBetween = String.format("%s%s", StringUtils.isNotBlank(inBetween) ? inBetween : "", text);
                    lastLocation = parsed;
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
                                       EncodingType outputEncodingType,
                                       Map<CorpusLocation, CorpusItem> corpus,
                                       Map<CorpusLocation, CorpusItem> compareCorpus,
                                       boolean onlyProblems) {
        boolean stemIsBlank = stem == null;
        boolean lemmaIsBlank = lemma==null;
        boolean rootIsBlank = root==null;
        String[] rootTexts=rootIsBlank?null:splitInput( EncodingType.Buckwalter,root) ;
        String[] lemmaTexts=lemmaIsBlank?null: splitInput(EncodingType.Buckwalter,lemma) ;
        String[] stemTexts=stemIsBlank?null: splitInput(EncodingType.Buckwalter,stem) ;
        String[] noDiacriticsStems = stemIsBlank?null:splitInput(EncodingType.Buckwalter,stem.removeDiacritics());
        List<CorpusItem> ret = corpus.values().stream()
                .filter(t -> {
                    boolean isMatch = false;
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
                                            isMatch=true;
                                        }
                                    } else {
                                        isMatch=true;
                                    }
                                }
                            }
                        }
                    }
                    return isMatch;
                }).collect(Collectors.toList());
        return toTable(ret, compareCorpus, outputEncodingType,onlyProblems);
    }

    public static AnalysisTable toTable(List<CorpusItem> lst,
                                        Map<CorpusLocation, CorpusItem> compareCorpus,
                                        EncodingType outputEncodingType,
                                        boolean onlyProblems) {
        List<String> columns = new LinkedList<>(Arrays.asList("location", "text", "tag", "features"));
        if (compareCorpus != null) {
            columns.addAll(Arrays.asList("compare_tag", "compare_features"));
        }
        String[] columnsArr = new String[columns.size()];
        columns.toArray(columnsArr);
        AnalysisTable tbl = new AnalysisTable(columnsArr);
        lst.stream().forEach(t -> {
            boolean isMatch = true;
            CorpusLocation location = t.getLocation();
            String text = t.getText();

            String root = t.getRootBW();
            String lemma = t.getStem();
            String stem = t.getStem();

            boolean isBWEncoding = outputEncodingType.equals(EncodingType.Buckwalter);
            ArabicText bwText = ArabicText.fromBuckwalter(text);
            PartOfSpeechTag corpusPOS = t.getPartOfSpeechTag();
            List<Object> values = new LinkedList<>(Arrays.asList(
                    location.getChapterNumber() +
                            ":" + location.getVerseNumber() +
                            ":" + location.getTokenNumber() +
                            ":" + location.getLetter(),
                    isBWEncoding ? text : bwText.toString(outputEncodingType),
                    corpusPOS,
                    getFeaturesText(outputEncodingType, t, isBWEncoding)));
            if (compareCorpus != null) {
                CorpusItem ci = compareCorpus.get(t.getLocation());
                if (ci != null) {
                    PartOfSpeechTag comparePOS = ci.getPartOfSpeechTag();
                    String otherRoot = ci.getRootBW();
                    String otherLemma = ci.getTaggedFeatures().get(AttributeTags.LEM);
                    String otherStem = ci.getStem();

                    if(onlyProblems
                            && corpusPOS.equals(comparePOS)
                            && ((StringUtils.isBlank(otherRoot) && StringUtils.isBlank(root))
                                || (otherRoot !=null && otherRoot.equals(root)))
                            && ((StringUtils.isBlank(lemma) && StringUtils.isBlank(otherLemma))
                                || (otherLemma!=null && otherLemma.equals(lemma)))
                            && ((StringUtils.isBlank(stem) && StringUtils.isBlank(stem))
                                || (stem!=null && otherLemma.equals(stem)))) {
                        isMatch=false;
                    }
                    values.add(comparePOS);
                    values.add(getFeaturesText(outputEncodingType, ci, isBWEncoding));
                }
            }
            if(isMatch) {
                tbl.add(values.toArray());
            }
        });
        return tbl;
    }

    private static String getFeaturesText(EncodingType outputEncodingType, CorpusItem t, boolean isBWEncoding) {
        return StringUtils.join(t.getTaggedFeatures().entrySet()
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
                .distinct().collect(Collectors.toList()).toArray(),"|");
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
//        loadCorpusFile();
    }
}
