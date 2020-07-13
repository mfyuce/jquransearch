# JQuranSearch

JQuranSearch is a set of Java APIs for accessing and analyzing the Quran, in its authentic Arabic form. The Java library is released as an open source project. The Uthmani distribution of the Tanzil project is used (http://tanzil.info) and is left unmodified.

# The Library:
The library contains;
* a Java API which wraps the XML Uthmani Script of the Tanzil project
* an object model for the Quran’s orthography
* encoders and decoders for converting Arabic text
* Java classes for searching the text of the Quran
* The library includes a user guide and API documentation, with examples on using the library to perform basic analysis of Quranic text.
* Basic stemming algorithms


# Command line

```!bash
usage: jquransearch [input-text]
 -cf,--corpus-form                   i=I
                                     ii=II
                                     iii=III
                                     iv=IV
                                     v=V
                                     vi=VI
                                     vii=VII
                                     viii=VIII
                                     ix=IX
                                     x=X
                                     xi=XI
                                     xii=XII
                                     [Default=all]
 -cl,--corpus-lemma                  text [Default=all]
 -cp,--corpus-part-of-speech <arg>   Nouns
                                     -->N=Noun
                                     -->PN=Proper noun
                                     Derived nominals
                                     -->ADJ=Adjective
                                     -->IMPN=Imperative verbal noun
                                     Pronouns
                                     -->PRON=Personal pronoun
                                     -->DEM=Demonstrative pronoun
                                     -->REL=Relative pronoun
                                     Adverbs
                                     -->T=Time adverb
                                     -->LOC=Location adverb
                                     Verbs
                                     -->V=Verb
                                     Prepositions
                                     -->P=Preposition
                                     Conjunctions
                                     -->CONJ=Coordinating conjunction
                                     -->SUB=Subordinating conjunction
                                     Particles
                                     -->ACC=Accusative particle
                                     -->AMD=Amendment particle
                                     -->ANS=Answer particle
                                     -->AVR=Aversion particle
                                     -->CERT=Particle of certainty
                                     -->COND=Conditional particle
                                     -->EXP=Exceptive particle
                                     -->EXH=Exhortation particle
                                     -->EXL=Explanation particle
                                     -->FUT=Future particle
                                     -->INC=Inceptive particle
                                     -->INT=Particle of interpretation
                                     -->INT=GInterogative particle
                                     -->NEG=Negative particle
                                     -->PRO=Prohibition particle
                                     -->RES=Restriction particle
                                     -->RET=Retraction particle
                                     -->SUP=Supplemental particle
                                     -->SUR=Surprise particle
                                     Disconnected letters
                                     -->INL=Quranic initials
                                     [Default=all]
 -cr,--corpus-root                   text [Default=all]
 -cs,--corpus-stem                   text [Default=all]
 -if,--input-format <arg>            input text format 1=Buckwalter
                                     2-Arabic [Default =1]
 -in,--input-file <arg>              input file path
 -o,--operation <arg>                operation 1=Diacritics Free Search
                                     2-Exact Token Search 3-Root Search
                                     4-Corpus Search [Default=1]
 -of,--output-format <arg>           output text format 1=Buckwalter
                                     2-Arabic [Default =1]
 -out,--output-file <arg>            output file

Sample;

java -cp jquransearch-1.0.0.jar  org.jquransearch.tools.Tools qamar

java -cp jquransearch-1.0.0.jar  org.jquransearch.tools.Tools -o 2 -in <path>\roots.csv -out <path>\roots_bw.csv

java -cp jquransearch-1.0.0-jar-with-dependencies.jar org.jquransearch.tools.Tools -o 4 -if 1 -of 2 -out try_file_output.csv -cr xlq 

java -cp jquransearch-1.0.0-jar-with-dependencies.jar org.jquransearch.tools.Tools -o 4 -if 1 -of 2 -in try_file_input.csv -out try_file_output.csv -cr 

java -cp jquransearch-1.0.0-jar-with-dependencies.jar org.jquransearch.tools.Tools xlq -o 4 -if 1 -of 2 -out try_file_output.csv -cr 

```

# Research Corpus Descriptions

| Feature  	| MADAMIRA 2.1 	| Feature Value Definition  	|                       	|   	|
|:--------:	|:------------:	|:-------------------------:	|:---------------------:	|---	|
|  Aspect  	|      asp     	|                           	|                       	|   	|
|  Aspect  	|      asp     	|             c             	|        Command        	|   	|
|  Aspect  	|      asp     	|             i             	|     Imperfective      	|   	|
|  Aspect  	|      asp     	|             p             	|      Perfective       	|   	|
|  Aspect  	|      asp     	|             na            	|    Not applicable     	|   	|
|   Case   	|      cas     	|                           	|                       	|   	|
|   Case   	|      cas     	|             n             	|      Nominative       	|   	|
|   Case   	|      cas     	|             a             	|      Accusative       	|   	|
|   Case   	|      cas     	|             g             	|       Genitive        	|   	|
|   Case   	|      cas     	|             na            	|    Not applicable     	|   	|
|   Case   	|      cas     	|             u             	|       Undefined       	|   	|
|  Gender  	|      gen     	|                           	|                       	|   	|
|  Gender  	|      gen     	|             f             	|       Feminine        	|   	|
|  Gender  	|      gen     	|             m             	|       Masculine       	|   	|
|  Gender  	|      gen     	|             na            	|    Not applicable     	|   	|
|   Mood   	|      mod     	|                           	|                       	|   	|
|   Mood   	|      mod     	|             i             	|      Indicative       	|   	|
|   Mood   	|      mod     	|             j             	|        Jussive        	|   	|
|   Mood   	|      mod     	|             s             	|      Subjunctive      	|   	|
|   Mood   	|      mod     	|             na            	|    Not applicable     	|   	|
|   Mood   	|      mod     	|             u             	|       Undefined       	|   	|
|  Number  	|      num     	|                           	|                       	|   	|
|  Number  	|      num     	|             s             	|       Singular        	|   	|
|  Number  	|      num     	|             p             	|        Plural         	|   	|
|  Number  	|      num     	|             d             	|         Dual          	|   	|
|  Number  	|      num     	|             na            	|    Not applicable     	|   	|
|  Number  	|      num     	|             u             	|       Undefined       	|   	|
|  Person  	|      per     	|                           	|                       	|   	|
|  Person  	|      per     	|             1             	|          1st          	|   	|
|  Person  	|      per     	|             2             	|          2nd          	|   	|
|  Person  	|      per     	|             3             	|          3rd          	|   	|
|  Person  	|      per     	|             na            	|    Not applicable     	|   	|
|  State   	|      stt     	|                           	|                       	|   	|
|  State   	|      stt     	|             i             	|      Indefinite       	|   	|
|  State   	|      stt     	|             d             	|       Definitie       	|   	|
|  State   	|      stt     	|             c             	| Construct/Poss/Idafa  	|   	|
|  State   	|      stt     	|             na            	|    Not applicable     	|   	|
|  State   	|      stt     	|             u             	|       Undefined       	|   	|
|  Voice   	|      vox     	|                           	|                       	|   	|
|  Voice   	|      vox     	|             a             	|        Active         	|   	|
|  Voice   	|      vox     	|             p             	|        Passive        	|   	|
|  Voice   	|      vox     	|             na            	|    Not applicable     	|   	|
|  Voice   	|      vox     	|             u             	|       Undefined       	|   	|



| POS   Definition 	|  MADAMIRA 2.1 	|                                          Penn ATB                                          	|
|:----------------:	|:-------------:	|:------------------------------------------------------------------------------------------:	|
|       LABEL      	|      pos      	|                                              —                                             	|
|       Nouns      	|      noun     	|                                  FOREIGN, FUNC_WORD, NOUN                                  	|
|   Number Words   	|    noun_num   	|                                          NOUN_NUM                                          	|
|                  	|   noun_quant  	|                                         NOUN_QUANT                                         	|
|   Proper Nouns   	|   noun_prop   	|                                OREIGN, FUNC_WORD, NOUN_PROP                                	|
|    Adjectives    	|      adj      	|                                             ADJ                                            	|
|                  	|    adj_comp   	|                                          ADJ_COMP                                          	|
|                  	|    adj_num    	|                                           ADJ_NUM                                          	|
|      Adverbs     	|      adv      	|                                             ADV                                            	|
|                  	|  adv_interrog 	|                                        INTERROG_ADV                                        	|
|                  	|    adv_rel    	|                                           REL_ADV                                          	|
|     Pronouns     	|      pron     	| PRON_[1P \| 1S \| 2D \| 2FP \| 2FS \|   2MP \|      2MS \| 3D \| 3FP \| 3FS \| 3MP \| 3MS] 	|
|                  	|    pron_dem   	|              DEM_PRON_[D \| F \| FD \|      FP \| FS \| MD \| MP \| MS \| P ]              	|
|                  	|  pron_exclam  	|                                         EXCLAM_PRON                                        	|
|                  	| pron_interrog 	|                                        INTERROG_PRON                                       	|
|                  	|    pron_rel   	|                                          REL_PRON                                          	|
|       Verbs      	|      verb     	|                      VERB \| CV \| IV \|      IV_PASS \| PV \| PV_PASS                     	|
|                  	|  verb_pseudo  	|                                         PSEUDO_VERB                                        	|
|     Particles    	|      part     	|                                            PART                                            	|
|                  	|    part_dem   	|                                              —                                             	|
|                  	|    part_det   	|                                             DET                                            	|
|                  	|   part_focus  	|                                         FOCUS_PART                                         	|
|                  	|    part_fut   	|                                          FUT_PART                                          	|
|                  	| part_interrog 	|                                        INTERROG_PART                                       	|
|                  	|    part_neg   	|                                          NEG_PART                                          	|
|                  	| part_restrict 	|                                        RESTRIC_PART                                        	|
|                  	|   part_verb   	|                                          VERB_PART                                         	|
|                  	|    part_voc   	|                                          VOC_PART                                          	|
|   Prepositions   	|      prep     	|                                            PREP                                            	|
|   Abbreviations  	|     abbrev    	|                                           ABBREV                                           	|
|    Punctuation   	|      punc     	|                                        NUMERIC_COMMA                                       	|
|   Conjunctions   	|      conj     	|                                            CONJ                                            	|
|                  	|    conj_sub   	|                                          SUB_CONJ                                          	|
|   Interjections  	|     interj    	|                                           INTERJ                                           	|
|  Digital Numbers 	|     digit     	|                                              —                                             	|
|   Foreign/Latin  	|     latin     	|                                              —                                             	|




# Research & Design 	
Ghulam Rabani (@grabani) 
Also thanks for his friendship and his extensive search and test.