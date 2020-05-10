package org.jquransearch.tools;

import org.apache.commons.cli.*;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.ar.ArabicStemmer;
import org.apache.maven.shared.utils.io.FileUtils;
import org.jquransearch.analysis.stemmer.arabiccorpus.AttributeTags;
import org.jquransearch.analysis.stemmer.arabiccorpus.PartOfSpeechTag;
import org.jquransearch.analysis.stemmer.arabiccorpus.QuranicCorpusStemmer;
import org.jqurantree.analysis.AnalysisTable;
import org.jquransearch.analysis.stemmer.StemmerType;
import org.jquransearch.analysis.stemmer.StemmingManager;
import org.jqurantree.arabic.ArabicCharacter;
import org.jqurantree.arabic.ArabicText;
import org.jqurantree.arabic.encoding.EncodingType;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Token;
import org.jquransearch.search.SearchOptions;
import org.jquransearch.search.TokenSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Tools {

    public static final String CORPUS_STEM_SEARCH = "cs";
    public static final String CORPUS_LEMMA_SEARCH = "cl";
    public static final String CORPUS_ROOT_SEARCH = "cr";
    public static final String CORPUS_FORM_SEARCH = "cf";
    public static final String INPUT_FILE = "in";
    public static final String INPUT_FORMAT = "if";
    public static final String OUTPUT_FORMAT = "of";
    public static final String OPERATION = "o";
    public static final String OUTPUT_FILE = "out";
    public static final String CORPUS_PART_OF_SPEECH_SEARCH = "cp";
    public static final String SEPARATOR_CHARS = "\r\n\t, ";

    public static void main(String[] args) throws IOException {

        Options options = new Options();

        Option inputFile = new Option(INPUT_FILE, "input-file", true, "input file path");
        inputFile.setRequired(false);
        options.addOption(inputFile);

        Option inputFormat = new Option(INPUT_FORMAT, "input-format", true, "input text format 1=Buckwalter 2-Arabic [Default =1] ");
        inputFile.setRequired(false);
        options.addOption(inputFormat);

        Option outputFile = new Option(OUTPUT_FILE, "output-file", true, "output file");
        outputFile.setRequired(false);
        options.addOption(outputFile);

        Option outputFormat = new Option(OUTPUT_FORMAT, "output-format", true, "output text format 1=Buckwalter 2-Arabic [Default =1] ");
        inputFile.setRequired(false);
        options.addOption(outputFormat);

        Option output = new Option(OPERATION, "operation", true, "operation 1=Diacritics Free Search 2-Exact Token Search 3-Root Search  4-Corpus Search [Default=1]");
        output.setRequired(false);
        options.addOption(output);

        Option partOfSpeech = new Option(CORPUS_PART_OF_SPEECH_SEARCH, "corpus-part-of-speech", true, "Nouns\n-->N=Noun\n-->PN=Proper noun\n\nDerived nominals\n-->ADJ=Adjective\n-->IMPN=Imperative verbal noun\n\nPronouns\n-->PRON=Personal pronoun\n-->DEM=Demonstrative pronoun\n-->REL=Relative pronoun\n\nAdverbs\n-->T=Time adverb\n-->LOC=Location adverb\n\nVerbs\n-->V=Verb\n\nPrepositions\n-->P=Preposition\n\nConjunctions\n-->CONJ=Coordinating conjunction\n-->SUB=Subordinating conjunction\n\nParticles\n-->ACC=Accusative particle\n-->AMD=Amendment particle\n-->ANS=Answer particle\n-->AVR=Aversion particle\n-->CERT=Particle of certainty\n-->COND=Conditional particle\n-->EXP=Exceptive particle\n-->EXH=Exhortation particle\n-->EXL=Explanation particle\n-->FUT=Future particle\n-->INC=Inceptive particle\n-->INT=Particle of interpretation\n-->INT=GInterogative particle\n-->NEG=Negative particle\n-->PRO=Prohibition particle\n-->RES=Restriction particle\n-->RET=Retraction particle\n-->SUP=Supplemental particle\n-->SUR=Surprise particle\n\nDisconnected letters\n-->INL=Quranic initials\n [Default=all]");
        output.setRequired(false);
        options.addOption(partOfSpeech);

        Option corpusForm = new Option(CORPUS_FORM_SEARCH, "corpus-form", false, "i=I\nii=II\niii=III\niv=IV\nv=V\nvi=VI\nvii=VII\nviii=VIII\nix=IX\nx=X\nxi=XI\nxii=XII\n [Default=all]");
        output.setRequired(false);
        options.addOption(corpusForm);

        Option corpusRoot = new Option(CORPUS_ROOT_SEARCH, "corpus-root", false, "text [Default=all]");
        output.setRequired(false);
        output.setOptionalArg(true);
        options.addOption(corpusRoot);

        Option corpusLemma = new Option(CORPUS_LEMMA_SEARCH, "corpus-lemma", false, "text [Default=all]");
        output.setRequired(false);
        output.setOptionalArg(true);
        options.addOption(corpusLemma);

        Option corpusStem = new Option(CORPUS_STEM_SEARCH, "corpus-stem", false, "text [Default=all]");
        output.setRequired(false);
        output.setOptionalArg(true);
        options.addOption(corpusStem);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        if(args.length==0){
            formatter.printHelp("jquransearch [input-text]", options);

            System.exit(1);
            return;
        }
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("jquransearch [input-text]", options);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue(INPUT_FILE);
        String outputFilePath = cmd.getOptionValue(OUTPUT_FILE);
        String inputFormatText = cmd.getOptionValue(INPUT_FORMAT);
        String outputFormatText = cmd.getOptionValue(OUTPUT_FORMAT);
        String operation = cmd.getOptionValue(OPERATION);
        String inputPartOfSpeech = cmd.getOptionValue(CORPUS_PART_OF_SPEECH_SEARCH);
        String inputCorpusForm = cmd.getOptionValue(CORPUS_FORM_SEARCH);
        String inputCorpusRoot = cmd.getOptionValue(CORPUS_ROOT_SEARCH);
        String inputCorpusLemma = cmd.getOptionValue(CORPUS_LEMMA_SEARCH);
        String inputCorpusStem = cmd.getOptionValue(CORPUS_STEM_SEARCH);

        int operationNum = 1;
        int inputFormatNum = 1;
        int outputFormatNum = 1;
        PartOfSpeechTag partOfSpeechTag = PartOfSpeechTag.None;
        if(StringUtils.isNotBlank(operation)){
            operationNum = Integer.parseInt(operation);
        }
        if(StringUtils.isNotBlank(inputFormatText)){
            inputFormatNum = Integer.parseInt(inputFormatText);
        }
        if(StringUtils.isNotBlank(outputFormatText)){
            outputFormatNum = Integer.parseInt(outputFormatText);
        }
        if(StringUtils.isNotBlank(inputPartOfSpeech)){
            partOfSpeechTag = Enum.valueOf(PartOfSpeechTag.class, inputPartOfSpeech);
        }
        boolean corpusLemmaIsBlank = StringUtils.isBlank(inputCorpusLemma);
        boolean corpusRootIsBlank = StringUtils.isBlank(inputCorpusRoot);
        boolean corpusStemIsBlank = StringUtils.isBlank(inputCorpusStem);

        String inputTextIn = null;
        String[] remaininArgs = cmd.getArgs();
        if(remaininArgs!=null && remaininArgs.length>0){
            inputTextIn = remaininArgs[0];
        }else{
            if(StringUtils.isNotBlank(inputFilePath)){
                inputTextIn = readFile(inputFilePath);
            }
        }

        ArabicText inputCorpusRootText = null;
        ArabicText inputCorpusLemmaText = null;
        ArabicText inputCorpusStemText = null;

        ArabicText inputText = null;
        if(StringUtils.isNotBlank(inputTextIn)) {
            switch (inputFormatNum) {
                case 1: //Buckwalter
                default:
                    inputText = ArabicText.fromBuckwalter(inputTextIn);
                    inputCorpusRootText = corpusRootIsBlank?null: ArabicText.fromBuckwalter(inputCorpusRoot);
                    inputCorpusLemmaText =  corpusLemmaIsBlank?null:ArabicText.fromBuckwalter(inputCorpusLemma);
                    inputCorpusStemText = corpusStemIsBlank?null: ArabicText.fromBuckwalter(inputCorpusStem);
                    break;
                case 2: //Arabic
                    inputText = ArabicText.fromUnicode(inputTextIn);
                    inputCorpusRootText =  corpusRootIsBlank?null:ArabicText.fromUnicode(inputCorpusRoot);
                    inputCorpusLemmaText =  corpusLemmaIsBlank?null:ArabicText.fromUnicode(inputCorpusLemma);
                    inputCorpusStemText =  corpusStemIsBlank?null:ArabicText.fromUnicode(inputCorpusStem);
                    break;
            }
        }
        EncodingType outputEncodingType = EncodingType.Buckwalter;
        switch (outputFormatNum) {
            case 1: //Buckwalter
            default:
                outputEncodingType = EncodingType.Buckwalter;
                break;
            case 2: //Arabic
                outputEncodingType = EncodingType.Unicode;
                break;
        }
        if(corpusLemmaIsBlank && cmd.hasOption(CORPUS_LEMMA_SEARCH)){
            inputCorpusLemmaText = inputText;
        }
        if(corpusRootIsBlank && cmd.hasOption(CORPUS_ROOT_SEARCH)){
            inputCorpusRootText = inputText;
        }
        if(corpusStemIsBlank && cmd.hasOption(CORPUS_STEM_SEARCH)){
            inputCorpusStemText = inputText;
        }

        String outPutText = null;
        boolean general_out = true;
        switch (operationNum){
            case 1:
            default:
                handleSearch(outputFilePath, inputText, SearchOptions.RemoveDiacritics,false,outputEncodingType);
                general_out = false;
                break;
            case 2:
                handleSearch(outputFilePath, inputText, null,false,outputEncodingType);
                general_out = false;
                break;
            case 3:
                handleSearch(outputFilePath, inputText, null,true,outputEncodingType);
                general_out = false;
                break;
            case 4:
                AnalysisTable searchResult = QuranicCorpusStemmer.search(partOfSpeechTag,
                        AttributeTags.parse(inputCorpusForm),
                        inputCorpusRootText ,
                        inputCorpusLemmaText,
                        inputCorpusStemText,
                        outputEncodingType);
                handleResult(outputFilePath,searchResult);
                general_out = false;
                break;
        }

        if(general_out) {
            switch (outputFormatNum) {
                case 1: //Buckwalter
                default:
                    outPutText = inputText.toBuckwalter();
                    break;
                case 2: //Arabic
                    outPutText = inputText.toUnicode();
                    break;
            }
            if (StringUtils.isNotBlank(outputFilePath)) {
                writeFile(outputFilePath, outPutText);
            } else {
                System.out.println(outPutText);
            }
        }
//        System.out.println(inputText);
//        System.out.println(outPut);
    }

    private static void handleSearch(String outputFilePath,
                                     ArabicText inputText,
                                     SearchOptions options,
                                     boolean searchRoot,
                                     EncodingType outputEncodingType) throws UnsupportedEncodingException {
        AnalysisTable table = null;
        if(options != null && options.equals(SearchOptions.RemoveDiacritics)) {
            table = listAllReferences(splitInput(outputEncodingType, inputText.removeDiacritics()),searchRoot,outputEncodingType);
        }else{
            table = listAllReferences(splitInput(outputEncodingType, inputText),searchRoot,outputEncodingType);
        }
        handleResult(outputFilePath, table);
    }

    public static String[] splitInput(EncodingType outputEncodingType, ArabicText arabicCharacters) {
        if(arabicCharacters==null){
            return null;
        }
        return StringUtils.split(arabicCharacters.toString(outputEncodingType), SEPARATOR_CHARS);
    }

    private static void handleResult(String outputFilePath, AnalysisTable table) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(outputFilePath)){
            table.writeFile(outputFilePath);
        }else{
            System.out.println(table);
        }
        System.out.println("Matches: " + table.getRowCount());
    }

    /**
     * @throws IOException
     */
    public static void csvWriter(String fileName, Map<ArabicText, String> map, boolean onlyKeys) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            FileUtils.delete(file);
        }
        // Create a File and append if it already exists.
//        Writer writer = new FileWriter(file, true);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        if(!onlyKeys){
            writer.write("word,root" + System.lineSeparator());
        }
        for (Map.Entry<ArabicText, String> kv:map.entrySet()) {
            if(onlyKeys) {
                writer.write(kv.getKey() + System.lineSeparator());
            }else{
                writer.write(kv.getKey() + "," + kv.getValue() + System.lineSeparator());
            }
        }
        writer.flush();
    }
    public static void csvWriter(String fileName, Set<String> map) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            FileUtils.delete(file);
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        for (String kv:map) {
            writer.write(kv + System.lineSeparator());
        }
        writer.flush();
    }
    public static void csvWriter(String fileName, String[] map) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            FileUtils.delete(file);
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        for (String kv:map) {
            writer.write(kv + System.lineSeparator());
        }
        writer.flush();
    }
    public static void csvWriter(String fileName, List<String[]> map) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            FileUtils.delete(file);
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        for (String[] kv:map) {
            writer.write(StringUtils.join(kv,",") + System.lineSeparator());
        }
        writer.flush();
    }
    public static void csvWriter(String fileName, Map<Object, Integer> map) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            FileUtils.delete(file);
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        for (Map.Entry<Object, Integer> kv:map.entrySet()) {
            writer.write(kv.getKey() + "," + kv.getValue() + System.lineSeparator());
        }
        writer.flush();
    }
    public static String readFile(String fileName) throws IOException {
        File file = new File(fileName);
        if(!file.exists()) {
           return null;
        }
        return FileUtils.fileRead(file,"UTF8");
    }
    public static void  writeFile(String fileName, String value) throws IOException {
        File file = new File(fileName);
        if(file.exists()) {
            FileUtils.delete(file);
        }
        FileUtils.fileWrite(file,"UTF8", value);
    }

    public static String[] getAllDistinctLetters(boolean withHaraka) throws IOException {
        Map<String, String> wordsAndRoots= new LinkedHashMap<String, String>();
        for (Token w: Document.getTokens()) {
            for (ArabicCharacter l: withHaraka? w : w.removeDiacritics().removeNonLetters()) {
                String key = l.toUnicode();
                if (!wordsAndRoots.containsKey(key)) {
                    wordsAndRoots.put(key, key);
                }
                if(withHaraka) {
                    key = ArabicText.fromUnicode(l.toUnicode()).removeDiacritics().removeNonLetters().toUnicode();
                    if (StringUtils.isNotBlank(key) && !wordsAndRoots.containsKey(key)) {
                        wordsAndRoots.put(key, key);
                    }
                }
            }
        }
        String[] arr = new String[wordsAndRoots.size()];
         wordsAndRoots.keySet().toArray(arr);
        return arr;
    }
    public static Map<String, String> getAllDistinctWordsAndRootsLLuceneStemmer() throws IOException {
        Map<String, String> wordsAndRoots = new LinkedHashMap<String, String>();
        ArabicStemmer stem = new ArabicStemmer();

        for (Token w : Document.getTokens()) {
            String key = w.removeDiacritics().removeNonLetters().toUnicode();
            if (!wordsAndRoots.containsKey(key)) {
                char[] toStem = key.toCharArray();
                stem.stem(toStem,toStem.length);
                wordsAndRoots.put(key, new String(toStem));
            }
        }

        return wordsAndRoots;
    }
    public static Map<ArabicText, String> getAllDistinctWordsAndRoots(StemmerType stemmerType, boolean addNoHaraka, boolean removeDiacritics) throws Exception {
        Map<ArabicText, String> wordsAndRoots = new LinkedHashMap<ArabicText, String>();
        for (Token w : Document.getTokens()) {
            ArabicText key = w;
            if (!wordsAndRoots.containsKey(key)) {
                String noHaraka = w.removeDiacritics().removeNonLetters().toUnicode();// remove order is important
                String value = null;
                if(removeDiacritics) {
                    value= StemmingManager.stem(stemmerType, key.removeDiacritics().removeNonLetters());
                }else{
                    value= StemmingManager.stem(stemmerType, key);
                }
                wordsAndRoots.put(key, value + (addNoHaraka ? "," + noHaraka : ""));
            }
        }

        return wordsAndRoots;
    }
    public static AnalysisTable listAllReferences(String[] lst,
                                                  boolean searchRoot,
                                                  EncodingType outputEncodingType){
        TokenSearch search = new TokenSearch(outputEncodingType,
                SearchOptions.RemoveDiacritics);

        for (String item:lst) {
            if(searchRoot){
                search.findRoot(item,SearchOptions.RemoveDiacritics);
            }else {
                search.findSubstring(item);
            }
        }

        return search.getResults();
    }
}
