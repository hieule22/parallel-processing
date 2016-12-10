package main;

import java.util.List;

/**
 * A MatcherThread searches for limited gap strings with a fixed gap length from a list of
 * text lines.
 * @author Hieu Le
 * @version 10/23/16
 */
public class MatcherThread implements Runnable {
    // The list of text lines to search from.
    private final List<TextLine> textLines;
    // The limited gap string matcher.
    private final LimitedGapStringMatcher lgsMatcher;

    /**
     * Constructs a MatcherThread from a list of text lines and a limited gap string.
     * @param textLines A list of text lines to search from
     * @param prefix The prefix of the limited gap string
     * @param suffix The suffix of the limited gap string
     * @param gapLength The number of characters permitted between prefix and suffix
     */
    public MatcherThread(List<TextLine> textLines, String prefix, String suffix, int gapLength) {
        if (textLines == null) {
            throw new NullPointerException("textLines");
        }
        this.textLines = textLines;
        lgsMatcher = new LimitedGapStringMatcher(prefix, suffix, gapLength);
    }

    @Override
    public void run() {
        for (TextLine line : textLines) {
            int matchCount = lgsMatcher.countMatch(line.getContent());
            line.increaseMatchCountBy(matchCount);
        }
    }
}
