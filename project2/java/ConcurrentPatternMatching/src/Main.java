import main.LimitedGapString;
import main.MatcherThread;
import main.TextLine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * CS 435 - Project 2: Concurrent Pattern Matching.
 * @author Hieu Le
 * @version 10/23/16
 */
public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.printf("Usage: java %s <filename> <prefix> <min>..<max> <suffix>\n",
                    Main.class.getName());
            System.exit(-1);
        }

        FileReader inputFile = null;
        final String fileName = args[0];
        try {
            inputFile = new FileReader(fileName);
        } catch (FileNotFoundException ex) {
            System.err.printf("Error opening file: %s\n", fileName);
            System.exit(-1);
        }

        Scanner fileScanner = new Scanner(inputFile);
        List<TextLine> textLines = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            textLines.add(new TextLine(fileScanner.nextLine()));
        }
        try {
            inputFile.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        final String prefix = args[1];
        final String suffix = args[3];
        final String gapLengthRange = args[2];
        LimitedGapString gapString = LimitedGapString.create(prefix, suffix, gapLengthRange);

        patternMatch(textLines, gapString);

        for (int i = 0; i < textLines.size(); ++i) {
            TextLine currentLine = textLines.get(i);
            System.out.printf("%d: %d matches %s\n", i, currentLine.getMatchCount(),
                    currentLine.getContent());
        }
    }

    /**
     * Match a list of text lines against a given limited gap string.
     * @param textLines the list of lines to match
     * @param gapString the limited gap string to match
     */
    private static void patternMatch(List<TextLine> textLines, LimitedGapString gapString) {
        final int THREAD_COUNT = gapString.getMaxGapLength() - gapString.getMinGapLength() + 1;
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; ++i) {
            int gapLength = gapString.getMinGapLength() + i;
            threads[i] = new Thread(new MatcherThread(textLines, gapString.getPrefix(),
                    gapString.getSuffix(), gapLength));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                System.err.printf("Error joining thread %d.\n", i);
                ex.printStackTrace(System.err);
                System.exit(-1);
            }
        }
    }
}
