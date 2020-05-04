package org.jquransearch.analysis.stemmer;

import AlKhalil.AlKhalil;
import AlKhalil.analyse.Analyzer;
import AlKhalil.result.Result;
import AlKhalil.token.Tokens;
import AlKhalil.ui.Settings;
import org.jqurantree.arabic.ArabicText;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.jquransearch.core.utils.ResourceUtil.getResourceFileUri;

public class AlkhalilStemmer {
    private static Analyzer analyzer = new Analyzer();
    static{
        try {
            if (Settings.dbchoice) {
                analyzer.VRoots = analyzer.db.LoadRoots(new File(getResourceFileUri("db/AllRoots2.txt", AlKhalil.class)).getAbsolutePath());
                analyzer.NRoots = analyzer.db.LoadRoots(new File(getResourceFileUri("db/AllRoots2.txt", AlKhalil.class)).getAbsolutePath());
            } else {
                analyzer.VRoots = analyzer.db.LoadRoots(new File(getResourceFileUri("db/AllRoots1.txt", AlKhalil.class)).getAbsolutePath());
                analyzer.NRoots = analyzer.db.LoadRoots(new File(getResourceFileUri("db/AllRoots1.txt", AlKhalil.class)).getAbsolutePath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static String stem(String text) throws Exception {
        Tokens tokens = new Tokens(text);

        List unvoweledTokens = tokens.getUnvoweledTokens();

        List normalizedTokens = tokens.getNormalizedTokens();

        List lst= analyzer.Analyze((String)normalizedTokens.get(0), (String)unvoweledTokens.get(0));
        if(lst!=null && lst.size()>0) {
            String wordroot = ((Result) lst.get(0)).getWordroot();
            if(!wordroot.trim().equals("#")) {
                return wordroot;
            }
        }
        return text;
    }
    public static void main(String[] args) throws Exception {
        stem("خَلَاقٍ");
    }
}
