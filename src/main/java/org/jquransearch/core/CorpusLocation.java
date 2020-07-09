package org.jquransearch.core;

import org.jqurantree.orthography.Location;

import java.util.Objects;

public class CorpusLocation extends Location {
    private int letter;
    public CorpusLocation(int chapterNumber) {
        super(chapterNumber);
    }

    public CorpusLocation(int chapterNumber, int verseNumber) {
        super(chapterNumber, verseNumber);
    }

    public CorpusLocation(int chapterNumber, int verseNumber, int tokenNumber) {
        super(chapterNumber, verseNumber, tokenNumber);
    }

    public CorpusLocation(int chapterNumber, int verseNumber, int tokenNumber, int letterNumber) {
        super(chapterNumber, verseNumber, tokenNumber);
        this.letter = letterNumber;
    }

    public int getLetter() {
        return letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CorpusLocation that = (CorpusLocation) o;
        return letter == that.letter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), letter);
    }

    @Override
    public String toString() {
        return "CorpusLocation{" +
                super.toString() +
                "letter=" + letter +
                '}';
    }
}
