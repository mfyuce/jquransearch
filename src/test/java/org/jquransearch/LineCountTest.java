package org.jquransearch;

import org.apache.maven.shared.utils.StringUtils;
import org.jquransearch.analysis.stemmer.StemmerType;
import org.jquransearch.core.error.JQuranSearchException;
import org.jqurantree.arabic.ArabicText;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Token;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.jquransearch.analysis.stemmer.StemmerType.AlKhalil1_1;
import static org.jquransearch.tools.Tools.*;

public class LineCountTest {

	public static final int MAX_COL = 18;

	@Test
	@Ignore
	public void extractRootsAndWordsAlKhalil1_1() throws Exception {
		csvWriter("src/main/resources/stemming/roots_alkhalil1.1_auto.csv",getAllDistinctWordsAndRoots(AlKhalil1_1,true, false), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsNoDiacriticsAlKhalil1_1() throws Exception {
		csvWriter("src/main/resources/stemming/roots_alkhalil1.1_no_haraka_auto.csv",getAllDistinctWordsAndRoots(AlKhalil1_1,true, true), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsAlKhalil2_1() throws Exception {
		csvWriter("src/main/resources/stemming/roots_alkhalil2.1_auto.csv",getAllDistinctWordsAndRoots(AlKhalil1_1,true, false), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsNoDiacriticsAlKhalil2_1() throws Exception {
		csvWriter("src/main/resources/stemming/roots_alkhalil2.1_no_haraka_auto.csv",getAllDistinctWordsAndRoots(AlKhalil1_1,true, true), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsISRI() throws Exception {
		csvWriter("src/main/resources/stemming/roots_isri_auto.csv",getAllDistinctWordsAndRoots(StemmerType.ISRI,true, false), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsNoDiacriticsISRI() throws Exception {
		csvWriter("src/main/resources/stemming/roots_isri_no_haraka_auto.csv",getAllDistinctWordsAndRoots(StemmerType.ISRI,true,  true), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsKhodja() throws Exception {
		csvWriter("src/main/resources/stemming/roots_khodja_auto.csv",getAllDistinctWordsAndRoots(StemmerType.KODJA,true, false), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsNoDiacriticsKhodja() throws Exception {
		csvWriter("src/main/resources/stemming/roots_khodja_no_haraka_auto.csv",getAllDistinctWordsAndRoots(StemmerType.KODJA,true, true), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsLucene() throws Exception {
		csvWriter("src/main/resources/stemming/roots_lucene_auto.csv",getAllDistinctWordsAndRoots(StemmerType.Lucene,true, false), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsNoDiacriticsLucene() throws Exception {
		csvWriter("src/main/resources/stemming/roots_lucene_no_haraka_auto.csv",getAllDistinctWordsAndRoots(StemmerType.Lucene,true,true), false);
	}
	@Test
	@Ignore
	public void extractRootsAndWordsQuranicCorpus() throws Exception {
		csvWriter("src/main/resources/stemming/roots_quraniccorpus_auto.csv",getAllDistinctWordsAndRoots(StemmerType.QuranicCorpus,true,false), false);
	}
	@Test
	@Ignore
	public void extractRootsAndLetters() throws Exception {
		Map<String, Integer> lettersAndNumbers = getCharacterIntegerMap();
		csvWriter("letter_and_numbers.csv", Collections.unmodifiableMap(lettersAndNumbers));
		Map<ArabicText, String> wordsAndRoots= getAllDistinctWordsAndRoots(StemmerType.QuranicCorpus,false,false);
		List<String[]> ret = new ArrayList<>();
		String[] n = new String[MAX_COL];
		for (int i=0;i<MAX_COL-3;i++){
			n[i] = "l" + i;
		}
		n[MAX_COL-3] = "r1";
		n[MAX_COL-2] = "r2";
		n[MAX_COL-1] = "r3";
		ret.add(n);
		for (Map.Entry<ArabicText, String> v: wordsAndRoots.entrySet()) {
			n = new String[MAX_COL];
			String key = v.getKey().removeDiacritics().removeNonLetters().toUnicode();
			String value = v.getValue();
			if(value.length()>2) {
				for (int iW = 0; iW < MAX_COL - 3 ; iW++) {
					if(iW < key.length()) {
						n[iW] = String.valueOf(lettersAndNumbers.get(String.valueOf(key.charAt(iW))));
					}else{
						n[iW] = "0";
					}
				}
				ArabicText t = ArabicText.fromUnicode(value);
				n[MAX_COL - 3] = "h"+ lettersAndNumbers.get(t.getCharacter(0).toUnicode());
				n[MAX_COL - 2] = "h"+ lettersAndNumbers.get(t.getCharacter(1).toUnicode());
				n[MAX_COL - 1] = "h"+ lettersAndNumbers.get(t.getCharacter(2).toUnicode());
				ret.add(n);
			}
		}
		csvWriter("roots_and_letters.csv",ret);
	}

	private Map<String, Integer> getCharacterIntegerMap() throws IOException {
		String[] letters = getAllDistinctLetters(true);
		Map<String, Integer> lettersAndNumbers = new LinkedHashMap<>();
		int currentNumber=1;
		for (String l:letters) {
			if(StringUtils.isNotBlank(l)) {
				lettersAndNumbers.put(l, currentNumber++);
			}
		}
		return lettersAndNumbers;
	}

	@Test
	@Ignore
	public void extractWords() throws IOException {
		Map<ArabicText, String> wordsAndRoots= new LinkedHashMap<ArabicText, String>();
		for (Token v: Document.getTokens()) {
			if(!wordsAndRoots.containsKey(v)) {
				wordsAndRoots.put(v, v.removeDiacritics().toUnicode());
			}
		}
		csvWriter("words.csv",wordsAndRoots, true);
	}
	@Test
	@Ignore
	public void extractLetters() throws IOException {
		csvWriter("letters.csv",getAllDistinctLetters(true));
	}
	@Test
	@Ignore
	public void testLineCount() {

		// Count lines.
		int mainCount = countDirectory(new File("src/main"));
		int testCount = countDirectory(new File("src/test"));

		// Display counts.
		System.out.println("Line count (main): " + mainCount);
		System.out.println("Line count (test): " + testCount);
		System.out.println("Total: " + (mainCount + testCount));
	}

	private int countDirectory(File file) {

		// Count files.
		int count = 0;
		if (file.getName().endsWith(".java")) {
			count += countFile(file);
		}

		// Recurse through subdirectories.
		String[] directories = file.list();
		if (directories != null) {
			for (String directory : directories) {
				count += countDirectory(new File(file, directory));
			}
		}

		// Return count.
		return count;
	}

	private int countFile(File file) {
		int count = 0;
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			while (reader.readLine() != null) {
				count++;
			}
			reader.close();
		} catch (IOException exception) {
			throw new JQuranSearchException("Failed to read: "
					+ file.getAbsolutePath(), exception);
		}
		return count;
	}
}
