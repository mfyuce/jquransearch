package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.commons.lang.StringUtils;
import org.jqurantree.orthography.Location;

import java.util.LinkedHashMap;
import java.util.Map;

public class CorpusItem {
    private Location location;
    private int letter;
    private String text;
    private String tagStr;
    private PartOfSpeechTag partOfSpeechTag;
    private Map<String,String> features;
    private Map<AttributeTags,String> taggedFeatures;

    public CorpusItem(int surah, int ayah, int word, int letter, String text, String tag, Map<String, String> features, Map<AttributeTags, String> taggedFeatures) {
        this.location = new Location(surah,ayah,word);
        this.letter = letter;
        this.text = text;
        this.tagStr = tag;
        this.partOfSpeechTag = PartOfSpeechTag.contains(tag);
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
            features.put(ts[0],ts.length>1?ts[1]:"");
            AttributeTags parsedTag = AttributeTags.parse(ts[0]);
            if(parsedTag!=null && parsedTag!=AttributeTags.None) {
                taggedFeatures.put(parsedTag, ts.length > 1 ? ts[1] : "");
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
        String[] featuresArray = StringUtils.split(items[6], "|");
        Map<String, String> features  = new LinkedHashMap<>();
        Map<AttributeTags, String> taggedFeatures  = new LinkedHashMap<>();
        for (String item:featuresArray) {
            String[] ts = StringUtils.split(item, ":");
            features.put(ts[0],ts.length>1?ts[1]:"");
            AttributeTags parsedTag = AttributeTags.parse(ts[0]);
            if(parsedTag!=null && parsedTag!=AttributeTags.None) {
                taggedFeatures.put(parsedTag, ts.length > 1 ? ts[1] : "");
            }
        }
        return new CorpusItem(1,
                Integer.parseInt( StringUtils.strip(location[0], "()")),
                Integer.parseInt(StringUtils.strip(location[1], "()")),
                Integer.parseInt(StringUtils.strip(location[2], "()")),
                items[1],
                items[2],
                features,
                taggedFeatures);
    }


    public int getLetter() {
        return letter;
    }

    public String getText() {
        return text;
    }
    public Location getLocation() {
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
                ", letter=" + letter +
                ", text='" + text + '\'' +
                ", tagStr='" + tagStr + '\'' +
                ", partOfSpeechTag=" + partOfSpeechTag +
                ", features=" + features +
                ", taggedFeatures=" + taggedFeatures;
    }
}
