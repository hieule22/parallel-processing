package main;

/**
 * A limited gap string is a gap string where only a specified number of characters may appear
 * between the prefix and suffix.
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapString {
    // The regex patterns for gap length notation.
    private static final String DELIMITER_REGEX = "\\.{2}";
    private static final String RANGE_REGEX = "\\d+" + DELIMITER_REGEX + "\\d+";

    // The prefix of this limited gap string.
    private final String prefix;
    // The suffix of this limited gap string.
    private final String suffix;
    // The minimum number of characters permitted between prefix and suffix.
    private final int minGapLength;
    // The maximum number of characters permitted between prefix and suffix.
    private final int maxGapLength;

    /**
     * Constructs and returns a LimitedGapString from a LGS notation.
     * The notation must be of the form <prefix> <min>..<max> <suffix> where min and max are
     * non-negative integers such that min is less than or equal to max.
     * @param prefix The prefix of this limited gap string
     * @param suffix The suffix of this limited gap string
     * @param gapLengthRange The range for the number of characters permitted between prefix and
     *                       suffix.
     * @return If executed successfully, returns a fully constructed LimitedGapString object
     */
    public static LimitedGapString create(String prefix, String suffix, String gapLengthRange) {
        if (prefix == null || suffix == null || gapLengthRange == null) {
            throw new NullPointerException();
        }
        if (!gapLengthRange.matches(RANGE_REGEX)) {
            throw new IllegalArgumentException(String.format("Invalid gap length range. " +
                    "Expected format: <min>..<max>. Found: %s", gapLengthRange));
        }

        final String[] tokens = gapLengthRange.split(DELIMITER_REGEX);
        int minGapLength = Integer.parseInt(tokens[0]);
        int maxGapLength = Integer.parseInt(tokens[1]);

        if (minGapLength > maxGapLength) {
            throw new IllegalArgumentException(
                    "Minimum gap length must not exceed maximum gap length");
        }

        return new LimitedGapString(prefix, suffix, minGapLength, maxGapLength);
    }

    private LimitedGapString(String prefix, String suffix, int minGapLength, int maxGapLength) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.minGapLength = minGapLength;
        this.maxGapLength = maxGapLength;
    }

    /**
     * Returns the prefix of this limited gap string.
     * @return The prefix string
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns the suffix of this limited gap string.
     * @return The suffix string
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Returns the minimum number of characters permitted between prefix and suffix.
     * @return The minimum gap length
     */
    public int getMinGapLength() {
        return minGapLength;
    }

    /**
     * Returns the maximum number of characters permitted between prefix and suffix.
     * @return The maximum gap length
     */
    public int getMaxGapLength() {
        return maxGapLength;
    }
}
