package org.jquransearch.analysis.stemmer.arabiccorpus;

import java.util.Locale;

/**
 * http://corpus.quran.com/documentation/tagset.jsp
 *
 * 	Tag	Arabic Name	Description
 * Prepositions	P	حرف جر	Preposition
 * lām Prefixes	EMPH	لام التوكيد	Emphatic lām prefix
 * 	IMPV	لام الامر	Imperative lām prefix
 * 	PRP	لام التعليل	Purpose lām prefix
 * Conjunctions	CONJ	حرف عطف	Coordinating conjunction
 * 	SUB	حرف مصدري	Subordinating conjunction
 * Particles	ACC	حرف نصب	Accusative particle
 * 	AMD	حرف استدراك	Amendment particle
 * 	ANS	حرف جواب	Answer particle
 * 	AVR	حرف ردع	Aversion particle
 * 	CAUS	حرف سببية	Particle of cause
 * 	CERT	حرف تحقيق	Particle of certainty
 * 	CIRC	حرف حال	Circumstantial particle
 * 	COM	واو المعية	Comitative particle
 * 	COND	حرف شرط	Conditional particle
 * 	EQ	حرف تسوية	Equalization particle
 * 	EXH	حرف تحضيض	Exhortation particle
 * 	EXL	حرف تفصيل	Explanation particle
 * 	EXP	أداة استثناء	Exceptive particle
 * 	FUT	حرف استقبال	Future particle
 * 	INC	حرف ابتداء	Inceptive particle
 * 	INT	حرف تفسير	Particle of interpretation
 * 	INTG	حرف استفهام	Interogative particle
 * 	NEG	حرف نفي	Negative particle
 * 	PREV	حرف كاف	Preventive particle
 * 	PRO	حرف نهي	Prohibition particle
 * 	REM	حرف استئنافية	Resumption particle
 * 	RES	أداة حصر	Restriction particle
 * 	RET	حرف اضراب	Retraction particle
 * 	RSLT	حرف واقع في جواب الشرط	Result particle
 * 	SUP	حرف زائد	Supplemental particle
 * 	SUR	حرف فجاءة	Surprise particle
 * 	VOC	حرف نداء	Vocative particle
 * Disconnected Letters	INL	حروف مقطعة	Quranic initials
 *
 * Nouns
 * 	N=Noun
 * 	PN=Proper noun
 *
 * Derived nominals
 * 	ADJ=Adjective
 * 	IMPN=Imperative verbal noun
 *
 * Pronouns
 * 	PRON=Personal pronoun
 * 	DEM=Demonstrative pronoun
 * 	REL=Relative pronoun
 *
 * Adverbs
 * 	T=Time adverb
 * 	LOC=Location adverb
 *
 * Verbs
 * 	V=Verb
 *
 * Prepositions
 * 	P=Preposition
 *
 * Conjunctions
 * 	CONJ=Coordinating conjunction
 * 	SUB=Subordinating conjunction
 *
 * Particles
 * 	ACC=Accusative particle
 * 	AMD=Amendment particle
 * 	ANS=Answer particle
 * 	AVR=Aversion particle
 * 	CERT=Particle of certainty
 * 	COND=Conditional particle
 * 	EXP=Exceptive particle
 * 	EXH=Exhortation particle
 * 	EXL=Explanation particle
 * 	FUT=Future particle
 * 	INC=Inceptive particle
 * 	INT=Particle of interpretation
 * 	INT=GInterogative particle
 * 	NEG=Negative particle
 * 	PRO=Prohibition particle
 * 	RES=Restriction particle
 * 	RET=Retraction particle
 * 	SUP=Supplemental particle
 * 	SUR=Surprise particle
 *
 * Disconnected letters
 * 	INL=Quranic initials
 */
public enum PartOfSpeechTag {
    None,
    EMPH,
    RET,
    IMPN,
    RES,
    PRON,
    INL,
    ANS,
    ADJ,
    CIRC,
    IMPV,
    PRO,
    N,
    INT,
    PRP,
    P,
    T,
    V,
    PREV,
    AMD,
    COM,
    ACC,
    SUB,
    LOC,
    CAUS,
    FUT,
    VOC,
    EQ,
    DEM,
    SUP,
    RSLT,
    NEG,
    SUR,
    DET,
    EXH,
    CONJ,
    REL,
    EXL,
    INTG,
    REM,
    CERT,
    EXP,
    COND,
    AVR,
    PN,
    INC;
    public static PartOfSpeechTag contains(String test) {
        test = test.toLowerCase(Locale.ENGLISH);

        for (PartOfSpeechTag c : PartOfSpeechTag.values()) {
            if (c.name().toLowerCase(Locale.ENGLISH).equals(test)) {
                return c;
            }
        }

        return null;
    }
}
