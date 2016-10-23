package main;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapString {

    private final String prefix;
    private final String suffix;
    private final int minGapLength;
    private final int maxGapLength;

    private static final String DELIMITER_REGEX = "\\.{2}";

    public static LimitedGapString create(String prefix, String suffix, String gapLengthRange) {
        String[] tokens = gapLengthRange.split(DELIMITER_REGEX);
        if (tokens.length != 2) {
            throw new IllegalArgumentException(String.format("Invalid gap length range. " +
                    "Expected format: <min>..<max>. Found: %s", gapLengthRange));
        }

        int minGapLength;
        int maxGapLength;
        try {
            minGapLength = Integer.parseInt(tokens[0]);
            maxGapLength = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Invalid gap length range. " +
                    "Expected format: <min>..<max>. Found: %s", gapLengthRange));
        }

        return new LimitedGapString(prefix, suffix, minGapLength, maxGapLength);
    }

    public LimitedGapString(String prefix, String suffix, int minGapLength, int maxGapLength) {
        if (minGapLength < 0 || minGapLength > maxGapLength) {
            throw new IllegalArgumentException("Minimum gap length must be" +
                    " non-negative and must not exceed maximum gap length");
        }
        this.prefix = prefix;
        this.suffix = suffix;
        this.minGapLength = minGapLength;
        this.maxGapLength = maxGapLength;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getMinGapLength() {
        return minGapLength;
    }

    public int getMaxGapLength() {
        return maxGapLength;
    }
}
