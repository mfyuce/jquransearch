package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.commons.lang.StringUtils;
import org.jquransearch.core.CorpusLocation;
import org.jqurantree.arabic.ArabicText;

import java.util.*;

import static org.jquransearch.analysis.stemmer.researchcorpus.ResearchCorpusStemmer.partOfSpeechConversion;

public class CorpusItem {
    private CorpusLocation location;
    private String text;
    private String tagStr;
    private PartOfSpeechTag partOfSpeechTag;
    private String rootBW;
    private ArabicText root;
    private Map<String,String> features;
    private Map<AttributeTags,String> taggedFeatures;
    public static Set<String> distinctPOS = new TreeSet<>();
    public static Set<String> distinctAttributes = new TreeSet<>();
    public static Set<String> distinctAttributeValues = new TreeSet<>();
    public static Set<String> distinctWholeAttributes = new TreeSet<>();

    public CorpusItem(int surah, int ayah, int word, int letter, String text, String tag, Map<String, String> features, Map<AttributeTags, String> taggedFeatures) {
        this.location = new CorpusLocation(surah,ayah,word,letter);
        this.text = text;
        this.tagStr = tag;
        this.partOfSpeechTag = PartOfSpeechTag.contains(tag);
        if(StringUtils.isNotBlank(tag)){
            distinctPOS.add(tag);
        }
        this.features = features;
        this.taggedFeatures = taggedFeatures;
        setRootBW(taggedFeatures.get(AttributeTags.ROOT));
        if(StringUtils.isNotBlank(getRootBW())){
            setRoot(ArabicText.fromBuckwalter(getRootBW()));
        }
    }

    public static CorpusItem parse(String[] items) {
        String[] location = StringUtils.split(items[0], ":");
        String[] featuresArray = StringUtils.split(items[3], "|");
        Map<String, String> features  = new LinkedHashMap<>();
        Map<AttributeTags, String> taggedFeatures  = new LinkedHashMap<>();
        for (String item:featuresArray) {
            String[] ts = StringUtils.split(item, ":");
            String t = ts[0];
            String tv = ts.length>1?ts[1]:"";
            features.put(t, tv );
            distinctAttributes.add(t+":"+tv);
            if(!t.equals("ROOT") && !t.equals("LEM")) {
                distinctAttributeValues.add(tv);
                distinctWholeAttributes.add(item);
            }

            newAttributeTag(taggedFeatures, t, tv);
            newAttributeTag(taggedFeatures, item, "");
        }
        return new CorpusItem(Integer.parseInt(StringUtils.strip(location[0], "()")),
                Integer.parseInt( StringUtils.strip(location[1], "()")),
                Integer.parseInt(StringUtils.strip(location[2], "()")),
                Integer.parseInt(StringUtils.strip(location[3], "()")),
                items[1],
                items[2],
                features,
                taggedFeatures);
    }

    private static void newAttributeTag(Map<AttributeTags, String> taggedFeatures, String t, String tv) {
        AttributeTags parsedTag = AttributeTags.parse(t);
        if (parsedTag != null && parsedTag != AttributeTags.None) {
            taggedFeatures.put(parsedTag, tv);
        }
    }

    public static CorpusItem parseResearchCorpus(String[] items) {
        String[] location = StringUtils.split(items[0], ":");
        Map<String, String> features  = new LinkedHashMap<>();
        Map<AttributeTags, String> taggedFeatures  = new LinkedHashMap<>();
        for (int i = 5; i < items.length-3; i++) {
            String item = items[i];
            if(StringUtils.isNotBlank(item)) {
                String[] ts = StringUtils.split(item, ":");
                String t = ts[0];
                String tv = ts.length > 1 ? ts[1]: "";
                features.put(t, tv );
                distinctAttributes.add(t+":"+tv);
                newAttributeTag(taggedFeatures, t, tv);
                newAttributeTag(taggedFeatures, item, "");
            }
        }
        String stem1 = items[28];
//        if(StringUtils.isNotBlank(stem1)){
//            features.put("STEM", stem1);
//            taggedFeatures.put(AttributeTags.STEM, stem1);
//        }
        String lemma = items[29];
        if(StringUtils.isNotBlank(lemma)){
            lemma = ArabicText.fromUnicode(lemma).toBuckwalter();
            features.put(AttributeTags.LEM.name(), lemma);
            taggedFeatures.put(AttributeTags.LEM, lemma);
        }
        if(taggedFeatures.containsKey(AttributeTags.STEM)){
            String stem = ArabicText.fromUnicode(taggedFeatures.get(AttributeTags.STEM)).toBuckwalter();
            taggedFeatures.replace(AttributeTags.STEM, stem);
            features.replace(AttributeTags.STEM.name(), stem);
        }
        String root = items[30];
        if(StringUtils.isNotBlank(root)){
            addNewAttributeRoot(taggedFeatures, features, root);
        }
//        else{
//            if(StringUtils.isNotBlank(lemma)) {
//                addNewAttributeRoot(taggedFeatures,features, lemma);
//            }else{
//                if(StringUtils.isNotBlank(stem1)) {
//                    addNewAttributeRoot(taggedFeatures,features, stem1);
//                }else{
//                    String stem = taggedFeatures.get(AttributeTags.STEM);
//                    if(StringUtils.isNotBlank(stem)) {
//                        addNewAttributeRoot(taggedFeatures,features, stem);
//                    }
//                }
//            }
//        }
        String pos = features.get("pos");
        if(StringUtils.isNotBlank(pos)){
            distinctPOS.add(pos);
        }
        String posConversion = partOfSpeechConversion.get(pos);
        return new CorpusItem(Integer.parseInt( StringUtils.strip(location[0], "()")),
                Integer.parseInt(StringUtils.strip(location[1], "()")),
                Integer.parseInt(StringUtils.strip(location[2], "()")),
                Integer.parseInt(StringUtils.strip(location[3], "()")),
                items[2],
                StringUtils.isBlank(posConversion)?pos:posConversion,
                features,
                taggedFeatures);
    }

    private static void addNewAttributeRoot(Map<AttributeTags, String> taggedFeatures,Map<String, String> features, String root) {
        String value = ArabicText.fromUnicode(root).removeDiacritics().removeNonLetters().toBuckwalter();
        features.put(AttributeTags.ROOT.name(), value);
        taggedFeatures.put(AttributeTags.ROOT,value);
    }


    public String getText() {
        return text;
    }
    public CorpusLocation getLocation() {
        return location;
    }
    public PartOfSpeechTag getPartOfSpeechTag() {
        return partOfSpeechTag;
    }
    public String getTagText() {
        return tagStr;
    }
    public Map<String, String> getFeatures() {
        return features;
    }
    public Map<AttributeTags, String> getTaggedFeatures() {
        return taggedFeatures;
    }
    public String getStem(){
        String stem = taggedFeatures.get(AttributeTags.STEM);
        if(StringUtils.isBlank(stem) && taggedFeatures.containsKey(AttributeTags.STEM)){
            return getText();
        }
        return stem;
    }
    @Override
    public String toString() {
        return "location=" + location +
                ", text='" + text + '\'' +
                ", tagStr='" + tagStr + '\'' +
                ", partOfSpeechTag=" + partOfSpeechTag +
                ", features=" + features +
                ", taggedFeatures=" + taggedFeatures;
    }

    public String getRootBW() {
        return rootBW;
    }

    public void setRootBW(String rootBW) {
        this.rootBW = rootBW;
    }

    public ArabicText getRoot() {
        return root;
    }

    public void setRoot(ArabicText root) {
        this.root = root;
    }
}
