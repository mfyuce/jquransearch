package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.commons.lang.StringUtils;
import org.jquransearch.core.CorpusLocation;

import java.util.*;

import static org.jquransearch.analysis.stemmer.researchcorpus.ResearchCorpusStemmer.partOfSpeechConversion;

public class CorpusItem {
    private CorpusLocation location;
    private String text;
    private String tagStr;
    private PartOfSpeechTag partOfSpeechTag;
    private Map<String,String> features;
    private Map<AttributeTags,String> taggedFeatures;
    public static Set<String> distinctPOS = new TreeSet<>();
    public static Set<String> distinctAttributes = new TreeSet<>();

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
            AttributeTags parsedTag = AttributeTags.parse(t);
            if(parsedTag!=null && parsedTag!=AttributeTags.None) {
                taggedFeatures.put(parsedTag,  tv );
            }
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

    public static CorpusItem parseResearchCorpus(String[] items) {
        String[] location = StringUtils.split(items[0], ":");
        Map<String, String> features  = new LinkedHashMap<>();
        Map<AttributeTags, String> taggedFeatures  = new LinkedHashMap<>();
        for (int i = 5; i < items.length-1; i++) {
            String item = items[i];
            if(StringUtils.isNotBlank(item)) {
                String[] ts = StringUtils.split(item, ":");
                String t = ts[0];
                String tv = ts.length > 1 ? ts[1]: "";
                features.put(t, tv );
                distinctAttributes.add(t+":"+tv);
                AttributeTags parsedTag = AttributeTags.parse(t);
                if (parsedTag != null && parsedTag != AttributeTags.None) {
                    taggedFeatures.put(parsedTag, tv);
                }
            }
        }
        String root = items[28];
        if(StringUtils.isNotBlank(root)){
            features.put("ROOT", root);
            taggedFeatures.put(AttributeTags.ROOT, root);
        }
        String pos = features.get("pos");
        if(StringUtils.isNotBlank(pos)){
            distinctPOS.add(pos);
        }
        return new CorpusItem(Integer.parseInt( StringUtils.strip(location[0], "()")),
                Integer.parseInt(StringUtils.strip(location[1], "()")),
                Integer.parseInt(StringUtils.strip(location[2], "()")),
                Integer.parseInt(StringUtils.strip(location[3], "()")),
                items[2],
                partOfSpeechConversion.get(pos),
                features,
                taggedFeatures);
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

    @Override
    public String toString() {
        return "location=" + location +
                ", text='" + text + '\'' +
                ", tagStr='" + tagStr + '\'' +
                ", partOfSpeechTag=" + partOfSpeechTag +
                ", features=" + features +
                ", taggedFeatures=" + taggedFeatures;
    }
}
