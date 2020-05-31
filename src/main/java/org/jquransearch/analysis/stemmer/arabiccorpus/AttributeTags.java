package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Attr;

import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isNumeric;


/**
 * (XII),
 * IMPV,
 * INDEF,
 * PCPL,
 * 3MD,
 * +VOC,
 * ka+,
 * 3MP,
 * MD,
 * 3MS,
 * sa+,
 * (III),
 * SUFFIX,
 * (IX),
 * ya+,
 * MP,
 * +n,
 * (XI),
 * MS,
 * NOM,
 * GEN,
 * 3FD,
 * PASS,
 * PREFIX,
 * POS,
 * IMPF,
 * VN,
 * (V),
 * (VI),
 * 3FP,
 * FD,
 * 1P,
 * 3FS,
 * (VII),
 * Al+,
 * A,
 * 1S,
 * ROOT,
 * PRON,
 * bi+,
 * F,
 * 2MD,
 * FP,
 * PERF,
 * M,
 * FS,
 * P,
 * 2D,
 * MOOD,
 * ha+,
 * 2MP,
 * 2MS,
 * SP,
 * ta+,
 * ACC,
 * f,
 * (IV),
 * l,
 * (VIII),
 * STEM,
 * 2FD,
 * 3D,
 * ACT,
 * (II),
 * LEM,
 * w,
 * (X),
 * 2FP,
 * 2FS
 */
public enum AttributeTags {
    None,
    _XII_,
    IMPV,
    INDEF,
    PCPL,
    _3MD,
    _PLUS_VOC,
    ka_PLUS_,
    _3MP,
    MD,
    _3MS,
    sa_PLUS_,
    _III_,
    SUFFIX,
    _IX_,
    ya_PLUS_,
    MP,
    _PLUS_n,
    _XI_,
    MS,
    NOM,
    GEN,
    _3FD,
    PASS,
    PREFIX,
    POS,
    IMPF,
    VN,
    _V_,
    _VI_,
    _3FP,
    FD,
    _1P,
    _3FS,
    _VII_,
    Al_PLUS_,
    A,
    _1S,
    ROOT,
    PRON,
    bi_PLUS_,
    F,
    _2MD,
    FP,
    PERF,
    M,
    FS,
    P,
    _2D,
    MOOD,
    ha_PLUS_,
    _2MP,
    _2MS,
    SP,
    ta_PLUS_,
    ACC,
    f,
    _IV_,
    l,
    _VIII_,
    STEM,
    _2FD,
    _3D,
    ACT,
    _II_,
    LEM,
    w,
    _X_,
    _2FP,
    _2FS;

    public static final String PLUS = "_PLUS_";

    public static AttributeTags parse(String tag) {
        if(StringUtils.isBlank(tag)){
            return None;
        }
        if (Character.isDigit(tag.charAt(0))) {
            tag = "_" + tag;
        }
        tag = StringUtils.replace(
                StringUtils.replace(
                StringUtils.replace(tag, "(", "_")
                        , ")", "_")
                        , "+", "_");
        AttributeTags contains = contains(tag);
        if(contains!=null) {
            return contains;
        }
        String newTag = "_" + tag;
        contains = contains(newTag);
        if(contains!=null) {
            return contains;
        }
        newTag = tag + "_";
        contains = contains(newTag);
        if(contains!=null) {
            return contains;
        }
        newTag = "_" + tag + "_";
        contains = contains(newTag);
        if(contains!=null) {
            return contains;
        }

        newTag = PLUS + tag ;
        contains = contains(newTag);
        if(contains!=null) {
            return contains;
        }
        newTag =  tag + PLUS;
        contains = contains(newTag);
        if(contains!=null) {
            return contains;
        }
        return null;
    }
    public static AttributeTags contains(String test) {
        test = test.toLowerCase(Locale.ENGLISH);

        for (AttributeTags c : AttributeTags.values()) {
            if (c.name().toLowerCase(Locale.ENGLISH).equals(test)) {
                return c;
            }
        }

        return null;
    }
}
