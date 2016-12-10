package main;

/**
 * LimitedGapStringMatcher determines how many times a limited gap string of a given gap length is
 * matched on an input string.
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapStringMatcher {
    // The prefix of the limited gap string.
    private final String prefix;
    // The suffix of the limited gap string.
    private final String suffix;
    // The number of characters permitted between prefix and suffix.
    private final int gapLength;

    /**
     * Constructs a LimitedGapStringMatcher from a limited gap string of given length.
     * @param prefix The prefix of the LGS
     * @param suffix The suffix of the LGS
     * @param gapLength The number of characters permitted between prefix and suffix
     */
    public LimitedGapStringMatcher(String prefix, String suffix, int gapLength) {
        if (prefix == null || suffix == null) {
            throw new NullPointerException("Prefix and suffix cannot be null");
        }
        if (gapLength < 0) {
            throw new IllegalArgumentException("Gap length must be non-negative: " + gapLength);
        }
        this.prefix = prefix;
        this.suffix = suffix;
        this.gapLength = gapLength;
    }

    /**
     * Returns the number of times this limited gap string is matched on an input string.
     * @param s The input string to match
     * @return The number of matches on s
     */
    public int countMatch(String s) {
        if (s == null) {
            throw new NullPointerException("s");
        }
        // TODO(hieule): Optimize this naive algorithm.
        final int gapStringLength = prefix.length() + gapLength + suffix.length();
        int result = 0;
        for (int startIndex = 0; startIndex <= s.length() - gapStringLength; ++startIndex) {
            final int endIndex = startIndex + gapStringLength;
            if (areEqual(s, startIndex, prefix, 0, prefix.length()) &&
                    areEqual(s, endIndex - suffix.length(), suffix, 0, suffix.length())) {
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
