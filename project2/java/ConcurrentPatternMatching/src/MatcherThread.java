import java.util.List;

/**
 * A MatcherThread searches for limited gap strings with a fixed gap length from a list of text
 * lines.
 * @author Hieu Le
 * @version 10/27/16
 */
public class MatcherThread implements Runnable {
    // The list of text lines to search from.
    private final List<TextLine> textLines;
    // The prefix of the limited gap string.
    private final String prefix;
    // The suffix of the limited gap string.
    private final String suffix;
    // The number of characters permitted between prefix and suffix.
    private final int gapLength;

    /**
     * Constructs a matcher thread from a list of text lines and a limited gap string.
     * @param textLines a list of text lines to examine
     * @param prefix the prefix of the LGS
     * @param suffix the suffix of the LGS
     * @param gapLength the number of characters permitted between prefix and suffix
     */
    public MatcherThread(List<TextLine> textLines, String prefix, String suffix, int gapLength) {
        this.textLines = textLines;
        this.prefix = prefix;
        this.suffix = suffix;
        this.gapLength = gapLength;
    }

    @Override
    public void run() {
        for (TextLine textLine : textLines) {
            int occurrences = countOccurrences(textLine.getContent(), prefix, suffix, gapLength);
            textLine.increaseMatchCountBy(occurrences);
        }
    }

    /**
     * Returns the number of occurrences of a limited gap string on a specified target string.
     * @param target the target string to examine
     * @param prefix the prefix of the LGS
     * @param suffix the suffix of the LGS
     * @param gapLength the number of characters permitted between prefix
     *                  and suffix
     * @return the number of occurrences of this LGS in target
     */
    private static int countOccurrences(String target, String prefix,
                                        String suffix, int gapLength) {
        final int gapStringLength = prefix.length() + suffix.length() + gapLength;
        int result = 0;
        for (int startIndex = 0; startIndex <= target.length() - gapStringLength; ++startIndex) {
            final int endIndex = startIndex + gapStringLength;
            if (areEqual(target, startIndex, prefix, 0, prefix.length()) &&
                    areEqual(target, endIndex - suffix.length(), suffix, 0, suffix.length())) {
                result++;
            }
        }
        return result;
    }

    /**
     * Checks if the substring of given length starting at index sStart of s is equal to the
     * substring starting at index tStart of t.
     * @param s the string containing the first substring
     * @param sStart the starting index in s of the first substring
     * @param t the string containing the second substring
     * @param tStart the starting index in t of the second substring
     * @param length the length of both substrings
     * @return true if the two substrings are the same; false otherwise
     */
    private static boolean areEqual(String s, int sStart, String t, int tStart, int length) {
        if (sStart + length > s.length() || tStart + length > t.length()) {
            return false;
        }

        for (int offset = 0; offset < length; ++offset) {
            if (s.charAt(sStart + offset) != t.charAt(tStart + offset)) {
                return false;
            }
        }
        return true;
    }
}