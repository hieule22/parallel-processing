package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class GapStringMatcher {
    public static final String REGEX_FORMAT = "%s.{%d}%s";

    private final Pattern pattern;

    public GapStringMatcher(String prefix, String suffix, int gapLength) {
        String regex = String.format(REGEX_FORMAT, prefix, gapLength, suffix);
        pattern = Pattern.compile(regex);
    }

    public int countMatch(String s) {
        Matcher matcher = pattern.matcher(s);
        int result = 0;
        while (matcher.find()) {
            result++;
        }
        return result;
    }
}
