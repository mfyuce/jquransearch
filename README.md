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

# Research & Design 	
Ghulam Rabani (@grabani) 
Also thanks for his friendship and his extensive search and test.