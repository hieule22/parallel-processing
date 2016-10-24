package main;

import java.util.concurrent.locks.ReentrantLock;

/**
 * TextLine holds all the information associated with one line of text,
 * including its content and the number of limited gap strings found.
 * @author Hieu Le
 * @version 10/23/16
 */
public class TextLine {
    // The content of this text line.
    private final String content;
    // The number of limited gap strings found on this text line.
    private int matchCount;
    // The mutex lock protecting variable matchCount.
    private ReentrantLock matchCountLock;

    /**
     * Constructs a TextLine object from a given String.
     * @param content the String populating this text line
     */
    public TextLine(String content) {
        this.content = content;
        matchCount = 0;
        matchCountLock = new ReentrantLock();
    }

    /**
     * Returns the content of this text line.
     * @return this text line's String content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the number of limited gap strings found on this text line.
     * @return how many times the limited gap string pattern is matched
     */
    public int getMatchCount() {
        return matchCount;
    }

    /**
     * Increases the number of limited gap strings found on this text line.
     * @param delta the value to increase
     */
    public void increaseMatchCountBy(int delta) {
        matchCountLock.lock();
        this.matchCount += delta;
        matchCountLock.unlock();
    }
}
