package test;

import main.TextLine;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Unit tests for TextLine.
 * @author Hieu Le
 * @version 10/23/16
 */
public class TextLineTest {

    @org.junit.Test
    public void testGetContent() throws Exception {
        final String expected = "Foo";
        TextLine line = new TextLine(expected);
        assertEquals(line.getContent(), expected);
    }

    @org.junit.Test
    public void testGetMatchCount() throws Exception {
        TextLine line = new TextLine("");
        assertEquals(line.getMatchCount(), 0);
        final int expected = 1;
        line.increaseMatchCountBy(expected);
        assertEquals(line.getMatchCount(), expected);
    }

    @org.junit.Test
    public void testIncreaseMatchCountBy() throws Exception {
        TextLine line = new TextLine("");
        final int original = line.getMatchCount();
        final int increase = 1;
        line.increaseMatchCountBy(increase);
        assertEquals(line.getMatchCount(), original + increase);
    }

    @org.junit.Test
    public void testConstructorException() throws Exception {
        try {
            new TextLine(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertThat(ex.getMessage(), containsString("content"));
        }
    }
}