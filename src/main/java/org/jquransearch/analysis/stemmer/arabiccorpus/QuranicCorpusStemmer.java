package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.maven.shared.utils.StringUtils;
import org.jqurantree.analysis.AnalysisTable;
import org.jqurantree.arabic.ArabicText;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Location;
import org.jqurantree.orthography.Token;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jquransearch.analysis.stemmer.arabiccorpus.CorpusItem.parse;
import static org.jquransearch.core.utils.ResourceUtil.loadResourceFile;

public class QuranicCorpusStemmer {
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
        for (String line : loadResourceFile("stemming/quranic-corpus-morphology-0.4.txt", QuranicCorpusStemmer.class, StandardCharsets.UTF_8)) {
            String trim = line.trim();
            if (StringUtils.isNotBlank(trim) && !trim.startsWith("#") && !trim.startsWith("LOCATION")) {
                String[] items = line.split("\t", -1);
                CorpusItem parsed = parse(items);
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
                                       String root,
                                       String lemma,
                                       String stem) {
        List<CorpusItem> ret = corpus.stream()
                .filter(t -> {
                    if (partOfSpeechTag == PartOfSpeechTag.None || t.getPartOfSpeechTag() == partOfSpeechTag) {
                        Map<AttributeTags, String> taggedFeatures = t.getTaggedFeatures();
                        if (form == null || form == AttributeTags.None || taggedFeatures.containsKey(form)) {
                            String currentLemma = taggedFeatures.get(AttributeTags.LEM);
                            if (StringUtils.isBlank(lemma)
                                    || (StringUtils.isNotBlank(currentLemma) && currentLemma.equals(lemma))) {
                                if (StringUtils.isBlank(stem) || (taggedFeatures.containsKey(AttributeTags.STEM)
                                        && t.getText().equals(stem))) {
                                    if (StringUtils.isNotBlank(root)) {
                                        String currentRoot = taggedFeatures.get(AttributeTags.ROOT);
                                        if (StringUtils.isNotBlank(currentRoot) && currentRoot.equals(root)) {
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
        return toTable(ret);
    }

    public static AnalysisTable toTable(List<CorpusItem> lst) {
         AnalysisTable tbl = new AnalysisTable(new String[]{"location","text", "tag", "features"});
         lst.stream().forEach(t->{

             Location location = t.getLocation();
             tbl.add(location.getChapterNumber() +
                     ":" + location.getVerseNumber() +
                     ":" + location.getTokenNumber() +
                     ":" + t.getLetter(),
                     t.getText(),
                     t.getTagText(),
                     StringUtils.join(t.getFeatures().entrySet()
                             .stream().map(u->u.getKey()
                                     + (StringUtils.isBlank(u.getValue())?"":":" + u.getValue()))
                             .collect(Collectors.toList()).toArray(),"|"));
         });
        return tbl;
    }
}
