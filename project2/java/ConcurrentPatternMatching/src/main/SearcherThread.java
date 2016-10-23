package main;

import java.util.List;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class SearcherThread implements Runnable {

    private final List<TextLine> textLines;
    private final GapStringMatcher matcher;

    public SearcherThread(List<TextLine> textLines, String prefix, String suffix, int gapLength) {
        this.textLines = textLines;
        matcher = new GapStringMatcher(prefix, suffix, gapLength);
    }

    @Override
    public void run() {
        for (TextLine line : textLines) {
            int matchCount = matcher.countMatch(line.getContent());
            line.incrementMatchCountBy(matchCount);
        }
    }
}
