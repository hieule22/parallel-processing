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
        LimitedGapString gapString = new LimitedGapString(expected, "", 0, 0);
        assertEquals(gapString.getPrefix(), expected);
    }

    @Test
    public void testGetSuffix() throws Exception {
        final String expected = "Foo";
        LimitedGapString gapString = new LimitedGapString("", expected, 0, 0);
        assertEquals(gapString.getSuffix(), expected);
    }

    @Test
    public void testGetMinGapLength() throws Exception {
        final int expected = 1;
        LimitedGapString gapString = new LimitedGapString("", "", expected, expected);
        assertEquals(gapString.getMinGapLength(), expected);
    }

    @Test
    public void testGetMaxGapLength() throws Exception {
        final int expected = 1;
        LimitedGapString gapString = new LimitedGapString("", "", expected, expected);
        assertEquals(gapString. getMaxGapLength(), expected);
    }

    private void testCreationFailure(final String gapLengthRange) throws Exception {
        try {
            LimitedGapString.create("", "", gapLengthRange);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("Invalid gap length range." +
                    " Expected format: <min>..<max>. Found:"));
        }
    }

    @Test
    public void createWithIllegalGapRange() throws Exception {
        testCreationFailure("");
        testCreationFailure("..");
        testCreationFailure("12");
        testCreationFailure("1.2");
        testCreationFailure("2...3");
        testCreationFailure("a..b");
        testCreationFailure("a..1");
        testCreationFailure("1..b");
        testCreationFailure("1..2..3");
    }

    private void testConstructionFailure(final int minGapLength, final int maxGapLength) {
        try {
            new LimitedGapString("", "", minGapLength, maxGapLength);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("Minimum gap length must be" +
                    " non-negative and must not exceed maximum gap length"));
        }
    }

    @Test
    public void constructWithIllegalGapRange() {
        testConstructionFailure(-1, -2);
        testConstructionFailure(-1, 10);
        testConstructionFailure(5, 3);
    }
}