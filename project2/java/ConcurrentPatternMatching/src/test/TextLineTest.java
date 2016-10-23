package test;

import main.TextLine;

import static junit.framework.TestCase.assertEquals;

/**
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
        line.incrementMatchCountBy(expected);
        assertEquals(line.getMatchCount(), expected);
    }

    @org.junit.Test
    public void testSetMatchCount() throws Exception {
        TextLine line = new TextLine("");
        final int expected = 1;
        line.incrementMatchCountBy(expected);
        assertEquals(line.getMatchCount(), expected);
    }
}