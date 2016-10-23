package main;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class TextLine {
    private final String content;
    private int matchCount;

    public TextLine(String content) {
        this.content = content;
        matchCount = 0;
    }

    public String getContent() {
        return content;
    }

    synchronized public int getMatchCount() {
        return matchCount;
    }

    synchronized public void incrementMatchCountBy(int delta) {
        this.matchCount += delta;
    }
}
