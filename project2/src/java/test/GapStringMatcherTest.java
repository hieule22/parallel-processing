package test;

import main.GapStringMatcher;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class GapStringMatcherTest {

    @Test
    public void testCountMatch() throws Exception {
        {
            GapStringMatcher matcher = new GapStringMatcher("Foo", "Bar", 1);
            assertEquals(matcher.countMatch("Foo1Bar"), 1);
            assertEquals(matcher.countMatch("Foo Bar"), 1);
            assertEquals(matcher.countMatch("FooBar"), 0);
            assertEquals(matcher.countMatch("FooQuozBar"), 0);
        }
        {
            GapStringMatcher matcher = new GapStringMatcher("Foo", "Bar", 3);
            assertEquals(matcher.countMatch("Foo123Bar"), 1);
            assertEquals(matcher.countMatch("Foo   Bar"), 1);
            assertEquals(matcher.countMatch("FooBar"), 0);
            assertEquals(matcher.countMatch("Foo$Bar"), 0);
            assertEquals(matcher.countMatch("Foo()Bar"), 0);
        }
    }
}