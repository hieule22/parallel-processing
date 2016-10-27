import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * CS 435 - Project 2: Concurrent Pattern Matching.
 * @author Hieu Le
 * @version 10/27/16
 */
public class ConcurrentPatternMatching {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.printf("Usage: java %s <filename> <prefix> <min>..<max> <suffix>\n",
                    ConcurrentPatternMatching.class.getName());
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

        String prefix = args[1];
        String suffix = args[3];
        NumberRange gapLengthRange = new NumberRange(args[2]);

        patternMatch(textLines, prefix, suffix, gapLengthRange);

        for (int i = 0; i < textLines.size(); ++i) {
            TextLine currentLine = textLines.get(i);
            System.out.printf("%d: %d matches %s\n", i, currentLine.getMatchCount(),
                    currentLine.getContent());
        }
    }

    /**
     * Matches a list of text lines against a given limited gap string.
     * @param textLines the list of text lines to examine
     * @param prefix the prefix of the LGS
     * @param suffix the suffix of the LGS
     * @param gapLengthRange gap length range of the LGS
     */
    private static void patternMatch(List<TextLine> textLines, String prefix, String suffix,
                                     NumberRange gapLengthRange) {
        final int THREAD_COUNT = gapLengthRange.getUpperBound() - gapLengthRange.getLowerBound() + 1;
        Thread[] matchers = new Thread[THREAD_COUNT];
        for (int i = 0; i < matchers.length; ++i) {
            int gapLength = gapLengthRange.getLowerBound() + i;
            matchers[i] = new Thread(new MatcherThread(textLines, prefix, suffix, gapLength));
        }

        for (Thread matcher : matchers) {
            matcher.start();
        }

        for (int i = 0; i < matchers.length; ++i) {
            try {
                matchers[i].join();
            } catch (InterruptedException ex) {
                System.err.printf("Error joining thread %d.\n", i);
                ex.printStackTrace(System.err);
                System.exit(-1);
            }
        }
    }

    private static class NumberRange {
        // The regex patterns for range notation.
        private static final String DELIMITER_REGEX = "\\.{2}";
        private static final String RANGE_REGEX = "\\d+" + DELIMITER_REGEX + "\\d+";

        // The lower bound of the range.
        private final int lowerBound;
        // The upper bound of the range.
        private final int upperBound;

        public NumberRange(String range) {
            if (!range.matches(RANGE_REGEX)) {
                throw new IllegalArgumentException(String.format("Invalid number range. " +
                        "Expected format: <min>..<max>. Found: %s", range));
            }

            final String[] tokens = range.split(DELIMITER_REGEX);
            lowerBound = Integer.parseInt(tokens[0]);
            upperBound = Integer.parseInt(tokens[1]);

            if (lowerBound > upperBound) {
                throw new IllegalArgumentException("Lower bound must not exceed upper bound");
            }
        }

        /**
         * Returns the lower bound of this number range.
         * @return the lower bound
         */
        public int getLowerBound() {
            return lowerBound;
        }

        /**
         * Returns the upper bound of this number range.
         * @return the upper bound
         */
        public int getUpperBound() {
            return upperBound;
        }
    }
}
