package org.jquransearch.analysis.stemmer.arabiccorpus;

import org.apache.commons.lang.StringUtils;

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
    //Nouns
    N,//	اسم	Noun
    PN,//اسم علم	Proper noun
    PRON_INTERROG, // Interrogative Pronoun => Only Research Corpus
    //Derived nominals
    ADJ,//	صفة	Adjective
    IMPN,//	اسم فعل أمر	Imperative verbal noun
    //Pronouns
    PRON,//	ضمير	Personal pronoun
    DEM,//	اسم اشارة	Demonstrative pronoun
    REL,//	اسم موصول	Relative pronoun
    //Adverbs
    ADV, // Adverb => Only Research Corpus
    INTERROG_ADV, // Interrogation Adverb => Only Research Corpus
    T, //	ظرف زمان	Time adverb
    LOC, //	ظرف مكان	Location adverb
    //Verbs
    V, // فعل	Verb
    VERB_PSEUDO, //  => Only Research Corpus
    //Prepositions
    P, // حرف جر	Preposition
    //Conjunctions
    CONJ, //حرف عطف	Coordinating conjunction
    SUB,  // حرف مصدري	Subordinating conjunction
    //Particles
    PART, // => Only Research Corpus
    ACC	 , //حرف نصب	Accusative particle
    AMD	 , //حرف استدراك	Amendment particle
    ANS	 , //حرف جواب	Answer particle
    AVR	 , //حرف ردع	Aversion particle
    CAUS	 , //حرف سببية	Particle of cause
    CERT	 , //حرف تحقيق	Particle of certainty
    CIRC	 , //حرف حال	Circumstantial particle
    COM	 , //واو , // المعية	Comitative particle
    COND	 , //حرف شرط	Conditional particle
    EQ	 , //حرف تسوية	Equalization particle
    EXH , //حرف تحضيض	Exhortation particle
    EXL	 , //حرف تفصيل	Explanation particle
    EXP	 , //أداة استثناء	Exceptive particle
    FUT	 , //حرف استقبال	Future particle
    INC	 , //حرف ابتداء	Inceptive particle
    INT	 , //حرف تفسير	Particle of interpretation
    INTG	 , //حرف استفهام	Interrogative particle
    NEG	 , //حرف نفي	Negative particle
    PREV	 , //حرف كاف	Preventive particle
    PRO	 , //حرف نهي	Prohibition particle
    REM	 , //حرف استئنافية	Resumption particle
    RES	 , //أداة حصر	Restriction particle
    RET	 , //حرف اضراب	Retraction particle
    RSLT , //	حرف واقع في جواب الشرط	Result particle
    SUP	 , //حرف زائد	Supplemental particle
    SUR	 , //حرف فجاءة	Surprise particle
    VOC	 , //حرف نداء	Vocative particle
    //Disconnected letters
    INL, //حروف مقطعة	Quranic initials
    // lām Prefixes
    EMPH, //	لام التوكيد	Emphatic lām prefix
    IMPV, //	لام الامر	Imperative lām prefix
    PRP, //	لام التعليل	Purpose lām prefix
    // definite article - ال
    DET,
    // Abbreviations
    ABBREV,// => Only Research Corpus

    //interjection
    INTERJ// => Only Research Corpus

    ;
    public static PartOfSpeechTag contains(String test) {
        if(StringUtils.isNotBlank(test)) {
            test = test.toLowerCase(Locale.ENGLISH);

            for (PartOfSpeechTag c : PartOfSpeechTag.values()) {
                if (c.name().toLowerCase(Locale.ENGLISH).equals(test)) {
                    return c;
                }
            }
        }
        return null;
    }
}
