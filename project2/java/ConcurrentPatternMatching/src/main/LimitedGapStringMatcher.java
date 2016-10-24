package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LimitedGapStringMatcher determines how many times a limited gap string of
 * a given gap length is matched on an input String.
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapStringMatcher {
    // The format for the regular expression to be matched.
    private static final String REGEX_FORMAT = "%s.{%d}%s";

    // The pattern associated with this LimitedGapStringMatcher.
    private final Pattern pattern;

    /**
     * Constructs a LimitedGapStringMatcher from a limited gap string of
     * given length.
     * @param prefix The prefix of the LGS
     * @param suffix The suffix of the LGS
     * @param gapLength The number of characters permitted between prefix
     *                  and suffix.
     */
    public LimitedGapStringMatcher(String prefix, String suffix, int gapLength) {
        String regex = String.format(REGEX_FORMAT, prefix, gapLength, suffix);
        pattern = Pattern.compile(regex);
    }

    /**
     * Returns the number of times this limited gap string is matched on an
     * input String.
     * @param s The input String to match.
     * @return The number of matches on s.
     */
    public int countMatch(String s) {
        Matcher matcher = pattern.matcher(s);
        int result = 0;
        while (matcher.find()) {
            result++;
        }
        return result;
    }
}
