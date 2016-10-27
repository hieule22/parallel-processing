import main.LimitedGapString;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for ConcurrentPatternMatching
 * @author Hieu Le
 * @version 10/27/16
 */
public class ConcurrentPatternMatchingTest {

    private static List<TextLine> generateTextLines(String[] lines) {
        List<TextLine> textLines = new ArrayList<>(lines.length);
        for (String line : lines) {
            textLines.add(new TextLine(line));
        }
        return textLines;
    }

    private void validate(String[] lines, LimitedGapString gapString,
                          int[] expected) throws Exception {
        List<TextLine> textLines = generateTextLines(lines);

        ConcurrentPatternMatching.patternMatch(textLines, gapString.getPrefix(),
                gapString.getSuffix(), new ConcurrentPatternMatching.NumberRange(
                        gapString.getMinGapLength() + ".." + gapString.getMaxGapLength()));
        for (int i = 0; i < textLines.size(); ++i) {
            assertEquals(textLines.get(i).getMatchCount(), expected[i]);
        }
    }

    @Test
    public void testPatternMatchBasic() throws Exception {
        validate(new String[] {"fooabcbar"}, LimitedGapString.create("foo", "bar", "0..3"),
                new int[] {1});
        validate(new String[] {"foo foo bar bar"}, LimitedGapString.create("foo", "bar", "0..20"),
                new int[] {4});
        validate(new String[] {"a b a b a b"}, LimitedGapString.create("a", "b", "1..1"),
                new int[] {3});
        validate(new String[] {"aaabbb"}, LimitedGapString.create("a", "b", "2..2"), new int[] {3});
        validate(new String[] {"foo bar"}, LimitedGapString.create("foo", "bar", "0..0"),
                new int[] {0});
        validate(new String[] {"foobarbaz"}, LimitedGapString.create("foo", "ba", "0..100"),
                new int[] {2});
    }

    @Test
    public void testPatternMatchStress() throws Exception {
        final int MAX_LINE_LENGTH = 1024;
        final int MAX_LINE_COUNT = 2048;
        String[] lines = new String[MAX_LINE_COUNT];
        StringBuilder builder = new StringBuilder(MAX_LINE_LENGTH);
        for (int i = 0; i < MAX_LINE_LENGTH; ++i) {
            builder.append('a');
        }
        final String line = builder.toString();

        for (int i = 0; i < lines.length; ++i) {
            lines[i] = line;
        }

        int[] expected = new int[lines.length];
        for (int i = 0; i < expected.length; ++i) {
            expected[i] = 0;
        }
        validate(lines, LimitedGapString.create("c", "d", "1..1"), expected);

        for (int i = 0; i < expected.length; ++i) {
            expected[i] = MAX_LINE_LENGTH - 1;
        }
        validate(lines, LimitedGapString.create("a", "a", "0..0"), expected);
    }
}