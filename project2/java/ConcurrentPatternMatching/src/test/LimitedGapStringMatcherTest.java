package test;

import main.LimitedGapStringMatcher;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Hieu Le
 * @version 10/23/16
 */
public class LimitedGapStringMatcherTest {

    @Test
    public void testCountMatch() throws Exception {
        {
            LimitedGapStringMatcher matcher = new LimitedGapStringMatcher("Foo", "Bar", 1);
            assertEquals(matcher.countMatch("Foo1Bar"), 1);
            assertEquals(matcher.countMatch("Foo Bar"), 1);
            assertEquals(matcher.countMatch("FooBar"), 0);
            assertEquals(matcher.countMatch("FooQuozBar"), 0);
        }
        {
            LimitedGapStringMatcher matcher = new LimitedGapStringMatcher("Foo", "Bar", 3);
            assertEquals(matcher.countMatch("Foo123Bar"), 1);
            assertEquals(matcher.countMatch("Foo   Bar"), 1);
            assertEquals(matcher.countMatch("FooBar"), 0);
            assertEquals(matcher.countMatch("Foo$Bar"), 0);
            assertEquals(matcher.countMatch("Foo()Bar"), 0);
        }
        {
            // TODO(hieule): Fix failing test case.
//            LimitedGapStringMatcher matcher = new LimitedGapStringMatcher("Foo", "Foo", 2);
//            assertEquals(matcher.countMatch("Foo12Foo12Foo"), 2);
        }
    }
}