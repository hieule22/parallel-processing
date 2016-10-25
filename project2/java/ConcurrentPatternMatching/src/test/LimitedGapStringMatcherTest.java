package test;

import main.LimitedGapStringMatcher;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapStringMatcherTest {

    @Test
    public void testConstructException() throws Exception {
        try {
            new LimitedGapStringMatcher("", "", -1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("Gap length must be non-negative"));
        }

        try {
            new LimitedGapStringMatcher(null, "", 0);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertThat(ex.getMessage(), containsString("Prefix and suffix cannot be null"));
        }
    }

    @Test
    public void testCountMatch() throws Exception {
        LimitedGapStringMatcher matcher = new LimitedGapStringMatcher("Foo", "Bar", 1);
        assertEquals(matcher.countMatch("Foo1Bar"), 1);
        assertEquals(matcher.countMatch("Foo Bar"), 1);
        assertEquals(matcher.countMatch("Foo1BarFoo2Bar"), 2);
        assertEquals(matcher.countMatch("fooFoo Barbar"), 1);
        assertEquals(matcher.countMatch("FooBar"), 0);
        assertEquals(matcher.countMatch("FooQuozBar"), 0);
        assertEquals(matcher.countMatch("foo bar"), 0);

        matcher = new LimitedGapStringMatcher("Foo", "Bar", 3);
        assertEquals(matcher.countMatch("Foo123Bar"), 1);
        assertEquals(matcher.countMatch("Foo   Bar"), 1);
        assertEquals(matcher.countMatch("FooBar"), 0);
        assertEquals(matcher.countMatch("Foo$Bar"), 0);
        assertEquals(matcher.countMatch("Foo()Bar"), 0);

        matcher = new LimitedGapStringMatcher("Foo", "Foo", 2);
        assertEquals(matcher.countMatch("Foo12Foo12Foo"), 2);
        assertEquals(matcher.countMatch("Foo1Foo12Foo"), 1);
    }

    @Test
    public void testCountMatchException() throws Exception {
        try {
            new LimitedGapStringMatcher("", "", 0).countMatch(null);
            fail("Expected a NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertThat(ex.getMessage(), containsString("s"));
        }
    }
}