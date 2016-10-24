package test;

import main.LimitedGapString;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapStringTest {

    @Test
    public void testCreate() throws Exception {
        final String expectedPrefix = "Foo";
        final String expectedSuffix = "Bar";
        final int expectedMinLength = 1;
        final int expectedMaxLength = 2;
        LimitedGapString gapString = LimitedGapString.create(expectedPrefix, expectedSuffix,
                expectedMinLength + ".." + expectedMaxLength);
        assertEquals(gapString.getPrefix(), expectedPrefix);
        assertEquals(gapString.getSuffix(), expectedSuffix);
        assertEquals(gapString.getMinGapLength(), expectedMinLength);
        assertEquals(gapString.getMaxGapLength(), expectedMaxLength);
    }

    @Test
    public void testGetPrefix() throws Exception {
        final String expected = "Foo";
        LimitedGapString gapString = LimitedGapString.create(expected, "", "0..0");
        assertEquals(gapString.getPrefix(), expected);
    }

    @Test
    public void testGetSuffix() throws Exception {
        final String expected = "Foo";
        LimitedGapString gapString = LimitedGapString.create("", expected, "0..0");
        assertEquals(gapString.getSuffix(), expected);
    }

    @Test
    public void testGetMinGapLength() throws Exception {
        final int expected = 1;
        LimitedGapString gapString = LimitedGapString.create("", "", expected + ".." + expected);
        assertEquals(gapString.getMinGapLength(), expected);
    }

    @Test
    public void testGetMaxGapLength() throws Exception {
        final int minGapLength = 1;
        final int maxGapLength = 2;
        LimitedGapString gapString = LimitedGapString.create("", "",
                minGapLength + ".." + maxGapLength);
        assertEquals(gapString. getMaxGapLength(), maxGapLength - minGapLength + 1);
    }

    private void testCreationFailure(final String gapLengthRange,
                                     final String exceptionMessage) throws Exception {
        try {
            LimitedGapString.create("", "", gapLengthRange);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString(exceptionMessage));
        }
    }

    @Test
    public void createWithIllegalGapRange() throws Exception {
        {
            final String exceptionMessage = "Invalid gap length range." +
                    " Expected format: <min>..<max>. Found:";
            testCreationFailure("", exceptionMessage);
            testCreationFailure("..", exceptionMessage);
            testCreationFailure("12", exceptionMessage);
            testCreationFailure("1.2", exceptionMessage);
            testCreationFailure("2...3", exceptionMessage);
            testCreationFailure("a..b", exceptionMessage);
            testCreationFailure("a..1", exceptionMessage);
            testCreationFailure("1..b", exceptionMessage);
            testCreationFailure("1..2..3", exceptionMessage);
            testCreationFailure("-1..-2", exceptionMessage);
            testCreationFailure("-1..10", exceptionMessage);
            testCreationFailure("2 .. 3", exceptionMessage);
        }
        {
            final String exceptionMessage = "Minimum gap length must not exceed maximum gap length";
            testCreationFailure("5..3", exceptionMessage);
            testCreationFailure("1..0", exceptionMessage);
        }
    }
}