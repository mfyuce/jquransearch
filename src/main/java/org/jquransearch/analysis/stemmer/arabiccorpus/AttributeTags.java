package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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
 *
 * http://corpus.quran.com/documentation/morphologicalfeatures.jsp
 */
public enum AttributeTags {
    None,
    /***
     * Verb Forms
     * http://corpus.quran.com/documentation/verbforms.jsp
     * `I` implied/default
     */
    _I_, //فَعَلَ
    _II_, // فَعَّلَ
    _III_, //فَاعَلَ
    _IV_, // أَفْعَلَ
    _V_, // تَفَعَّلَ
    _VI_, // تَفَاعَلَ
    _VII_, // إِنْفَعَلَ
    _VIII_, // إِفْتَعَلَ
    _IX_, // إِفْعَلَّ
    _X_, // إِسْتَفْعَلَ
    _XI_, // مُدْهَآمَّتَانِ
    _XII_, // يَطْمَئِنَّ
    /**
     * Prefixes
     * Al+	determiner (al)	DET – determiner prefix ("the")
     * bi+	preposition (bi)	P – preposition prefix ("by", "with", "in")
     * ka+	preposition (ka)	P – preposition prefix ("like" or "thus")
     * ta+	preposition (ta)	P – particle of oath prefix used as a preposition ("by Allah")
     * sa+	future particle (sa)	P – prefixed particle indicating the future ("they will")
     * ya+	vocative particle (yā)	VOC – a vocative prefix usually translated as "O"
     * ha+	vocative particle (hā)	VOC – a vocative prefix usually translated as "Lo!"
     */
    Al_PLUS_, // DET – determiner prefix ("the")
    bi_PLUS_, // P – preposition prefix ("by", "with", "in")
    ha_PLUS_, // P – preposition prefix ("like" or "thus")
    ta_PLUS_, // P – particle of oath prefix used as a preposition ("by Allah")
    ka_PLUS_, // P – prefixed particle indicating the future ("they will")
    sa_PLUS_, // VOC – a vocative prefix usually translated as "O"
    ya_PLUS_, // VOC – a vocative prefix usually translated as "Lo!"
    /**
     * Alif
     * A:INTG+	interrogative particle (alif)	INTG – prefixed interrogative particle ("is?", "did?", "do?")
     * A:EQ+	equalization particle (alif)	EQ – prefixed equalization particle ("whether")
     */
    A,
    INTG_PLUS_, // interrogative particle (alif)	INTG – prefixed interrogative particle ("is?", "did?", "do?")
    EQ_PLUS_, // equalization particle (alif)	EQ – prefixed equalization particle ("whether")
    /**
     * wāw
     * w:CONJ+	conjunction (wa)	CONJ – conjunction prefix ("and")
     * w:REM+	resumption (wa)	REM – resumption prefix ("then" or "so")
     * w:CIRC+	circumstantial (wa)	CIRC – circumstantial prefix ("while")
     * w:SUP+	supplemental (wa)	SUP – supplemental prefix ("then" or "so")
     * w:P+	preposition (wa)	P – particle of oath prefix used as a preposition ("by the pen")
     * w:COM+	comitative (wa)	COM – comitative prefix ("with")
     */
    w,
    CONJ_PLUS_, //conjunction (wa)	CONJ – conjunction prefix ("and")
    REM_PLUS_, //resumption (wa)	REM – resumption prefix ("then" or "so")
    CIRC_PLUS_, //circumstantial (wa)	CIRC – circumstantial prefix ("while")
    SUP_PLUS_, //supplemental (wa)	SUP – supplemental prefix ("then" or "so")
    P_PLUS_, //preposition (wa)	P – particle of oath prefix used as a preposition ("by the pen")
    COM_PLUS_, //comitative (wa)	COM – comitative prefix ("with")
    /**
     *  fa
     * f:REM+	resumption (fa)	REM – resumption prefix ("then" or "so")
     * f:CONJ+	conjunction (fa)	CONJ – conjunction prefix ("and")
     * f:RSLT+	result (fa)	RSLT – result prefix ("then")
     * f:SUP+	supplemental (fa)	SUP – supplemental prefix ("then" or "so")
     * f:CAUS+	cause (fa)	CAUS – cause prefix ("then" or "so")
     */
    F,
//    REM_PLUS_, //resumption (fa)	REM – resumption prefix ("then" or "so")
//    CONJ_PLUS_, //conjunction (fa)	CONJ – conjunction prefix ("and")
    RSLT_PLUS_, //result (fa)	RSLT – result prefix ("then")
//    SUP_PLUS_, //supplemental (fa)	SUP – supplemental prefix ("then" or "so")
    CAUS_PLUS_, //cause (fa)	CAUS – cause prefix ("then" or "so")
    /**
     *  lām
     *  l:P+	preposition (lām)	P – the letter lām as a prefixed preposition
     * l:EMPH+	emphasis (lām)	P – the letter lām as a prefixed particle used to give emphasis
     * l:PRP+	purpose (lām)	P – the letter lām as a prefixed particle used to indicate purpose
     * l:IMPV+	imperative (lām)	P – the letter lām as a prefixed particle used to form an imperative
     */
    l,
//    P_PLUS_, //preposition (lām)	P – the letter lām as a prefixed preposition
    EMPH_PLUS_, //emphasis (lām)	P – the letter lām as a prefixed particle used to give emphasis
    PRP_PLUS_, //purpose (lām)	P – the letter lām as a prefixed particle used to indicate purpose
    IMPV_PLUS_, //imperative (lām)	P – the letter lām as a prefixed particle used to form an imperative
    /**
     * root lemma
     * ROOT:	root	Indicates the (usually triliteral) root of a word, for example ROOT:ktb
     * LEM:	lemma	Specifies the common lemma for a group of words, for example LEM:kitaAb
     * SP:	special	Indicates that the word belongs to a special group, for example SP:<in~
     */
     ROOT, //root	Indicates the (usually triliteral) root of a word, for example ROOT:ktb
     LEM, //lemma	Specifies the common lemma for a group of words, for example LEM:kitaAb
     SP, //special	Indicates that the word belongs to a special group, for example SP:<in~
    /**
     * Person, Gender and Number
     * person	الاسناد	1, 2, 3	first person, second person, third person
     * gender	الجنس	M, F	masculine, feminine
     * number	العدد	S, D, P	singular, dual, plural
     */
    FD,
    FP,
    FS,
    M,
    MD,
    MP,
    MS,
    P,
    _1P,
    _1S,
    _2FD,
    _2FP,
    _2FS,
    _2MD,
    _2D,
    _2MP,
    _2MS,
    _3D,
    _3FD,
    _3FP,
    _3MD,
    _3MP,
    _3MS,
    _3FS,
    /**
     * Verb Features
     *
     * Aspect features.
     * PERF	فعل ماض	Perfect verb
     * IMPF	فعل مضارع	Imperfect verb
     * IMPV	فعل أمر	Imperative verb
     *
     * Mood features
     * IND	مرفوع	Indicative mood (default)
     * SUBJ	منصوب	Subjunctive mood
     * JUS	مجزوم	Jussive mood
     *
     * Voice features.
     * ACT	مبني للمعلوم	Active voice (default)
     * PASS	مبني للمجهول	Passive voice
     *
     */

     // Aspect features.
     PERF, //	فعل ماض	Perfect verb
     IMPF, //	فعل مضارع	Imperfect verb
     IMPV, //	فعل أمر	Imperative verb
     // Mood features
     IND, //	مرفوع	Indicative mood (default)
     SUBJ, //	منصوب	Subjunctive mood
     JUS, //	مجزوم	Jussive mood
     // Voice features.
     ACT, //	مبني للمعلوم	Active voice (default)
     PASS, //	مبني للمجهول	Passive voice
    /**
     * Derived Nouns
     * ACT PCPL	اسم فاعل	Active participle
     * PASS PCPL	اسم مفعول	Passive participle
     * VN	مصدر	Verbal noun
     *
     */
    PCPL, // participle
    VN, //	مصدر	Verbal noun
    /**
     * Nominal Features
     *
     * State features
     * DEF	معرفة	Definite state
     * INDEF	نكرة	Indefinite state
     *
     * Case features
     * NOM	مرفوع	Nominative case
     * ACC	منصوب	Accusative case
     * GEN	مجرور	Genitive case
     *
     */
     DEF, //	معرفة	Definite state
     INDEF, //	نكرة	Indefinite state
     NOM, //	مرفوع	Nominative case
     ACC, //	منصوب	Accusative case
     GEN, //	مجرور	Genitive case

    /**
     * Suffixes
     *
     */
    _PLUS_n,
    _PLUS_VOC,
    EMPH,

    SUFFIX,
    PREFIX,
    POS,
    PRON,
    MOOD,
    f,
    STEM,


    /**
     * Values
     */
    INNA, //<in~,
    ADJ,
    AMD,
    ANS,
    AVR,
    CERT,
    COND,
    CONJ,
    DEM,
    EXH,
    EXL,
    EXP,
    FUT,
    IMPN,
    INC,
    INL,
    INT,
    INTG,
    LOC,
    N,
    NEG,
    PN,
    PREV,
    PRO,
    REL,
    RES,
    RET,
    SUB,
    SUP,
    SUR,
    T,
    V,
    kaAd,
    kaAn ,
    //whole attributes
    _PLUS_n_SC_EMPH,
    A_SC_EQ_PLUS_,
    A_SC_INTG_PLUS_,
    MOOD_SC_JUS,
    MOOD_SC_SUBJ,
    POS_SC_ACC,
    POS_SC_ADJ,
    POS_SC_AMD,
    POS_SC_ANS,
    POS_SC_AVR,
    POS_SC_CERT,
    POS_SC_COND,
    POS_SC_CONJ,
    POS_SC_DEM,
    POS_SC_EXH,
    POS_SC_EXL,
    POS_SC_EXP,
    POS_SC_FUT,
    POS_SC_IMPN,
    POS_SC_INC,
    POS_SC_INL,
    POS_SC_INT,
    POS_SC_INTG,
    POS_SC_LOC,
    POS_SC_N,
    POS_SC_NEG,
    POS_SC_P,
    POS_SC_PN,
    POS_SC_PREV,
    POS_SC_PRO,
    POS_SC_PRON,
    POS_SC_REL,
    POS_SC_RES,
    POS_SC_RET,
    POS_SC_SUB,
    POS_SC_SUP,
    POS_SC_SUR,
    POS_SC_T,
    POS_SC_V,
    PRON_SC_1P,
    PRON_SC_1S,
    PRON_SC_2D,
    PRON_SC_2FD,
    PRON_SC_2FP,
    PRON_SC_2FS,
    PRON_SC_2MD,
    PRON_SC_2MP,
    PRON_SC_2MS,
    PRON_SC_3D,
    PRON_SC_3FD,
    PRON_SC_3FP,
    PRON_SC_3FS,
    PRON_SC_3MD,
    PRON_SC_3MP,
    PRON_SC_3MS,
    SP_SC_INNA,
    SP_SC_kaAd,
    SP_SC_kaAn,
    f_SC_CAUS_PLUS_,
    f_SC_CONJ_PLUS_,
    f_SC_REM_PLUS_,
    f_SC_RSLT_PLUS_,
    f_SC_SUP_PLUS_,
    l_SC_EMPH_PLUS_,
    l_SC_IMPV_PLUS_,
    l_SC_P_PLUS_,
    l_SC_PRP_PLUS_,
    w_SC_CIRC_PLUS_,
    w_SC_COM_PLUS_,
    w_SC_CONJ_PLUS_,
    w_SC_P_PLUS_,
    w_SC_REM_PLUS_,
    w_SC_SUP_PLUS_ ;

    public static final String PLUS = "_PLUS_";
    public static final String SEMICOLON = "_SC_";
    public static final String TEXT_INNA = "<in~";
    public static Map<String,AttributeTags> tagMaps = null;
    public static AttributeTags parse(String tag) {
        if(StringUtils.isBlank(tag)){
            return None;
        }
        if (Character.isDigit(tag.charAt(0))) {
            tag = "_" + tag;
        }
        tag = StringUtils.replace(
                StringUtils.replace(
                StringUtils.replace(
                StringUtils.replace(
                StringUtils.replace(tag, "(", "_")
                        , ")", "_")
                         , "+", PLUS)
                        , TEXT_INNA, INNA.name())
                        , ":", SEMICOLON);
        AttributeTags contains = contains(tag);
        if(contains!=null) {
            return contains;
        }
//        String newTag = "_" + tag;
//        contains = contains(newTag);
//        if(contains!=null) {
//            return contains;
//        }
//        newTag = tag + "_";
//        contains = contains(newTag);
//        if(contains!=null) {
//            return contains;
//        }
//        newTag = "_" + tag + "_";
//        contains = contains(newTag);
//        if(contains!=null) {
//            return contains;
//        }
//
//        newTag = PLUS + tag ;
//        contains = contains(newTag);
//        if(contains!=null) {
//            return contains;
//        }
//        newTag =  tag + PLUS;
//        contains = contains(newTag);
//        if(contains!=null) {
//            return contains;
//        }
        return null;
    }
    public static AttributeTags contains(String test) {
        test = test.toLowerCase(Locale.ENGLISH);
        if (tagMaps == null) {
            tagMaps = new LinkedHashMap<>();
            for (AttributeTags c : AttributeTags.values()) {
                tagMaps.put(c.name().toLowerCase(Locale.ENGLISH), c);
            }
        }
        return tagMaps.get(test);
    }

    @Override
    public String toString() {
        String ret = super.toString();
        ret = StringUtils.replace(ret, PLUS, "+" );
        ret = StringUtils.replace(ret, INNA.name(), TEXT_INNA );
        ret = StringUtils.replace(ret, SEMICOLON,":" );
        if(ret.charAt(0) == '_'){
             if(ret.length()>1 && Character.isDigit(ret.charAt(1))){
                ret = ret.substring(1);
            }else {
                 ret ="(" + StringUtils.strip(ret, "_") + ")";
             }
        }

        return ret;
    }
}
