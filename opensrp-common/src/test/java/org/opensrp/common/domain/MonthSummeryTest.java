package org.opensrp.common.domain;


import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MonthSummeryTest {

    @Test
    public void testEqualMethodWorksForSameType() {
        MonthSummary m1 = new MonthSummary("e", "3", "ee", "ff", Arrays.asList("sup1", "sup2", "sup3"));
        MonthSummary m2 = new MonthSummary("e", "3", "ee", "ff", Arrays.asList("sup1", "sup2", "sup3"));
        MonthSummary m3 = new MonthSummary(null,null,null,null,null);

        assertTrue(m1.equals(m1));
        assertTrue(m1.equals(m2));
        assertTrue(m2.equals(m1));

        assertFalse(m1.equals(m3));
        assertFalse(m3.equals(m1));
    }

    @Test
    public void testEqualMethodForInvalidTypeAndMethod() {
        MonthSummary m1 = new MonthSummary("e", "3", "ee", "ff", Arrays.asList("sup1", "sup2", "sup3"));
        MonthSummary m2 = null;
        String m3 = "";

        assertFalse(m1.equals(m2));
        assertFalse(m1.equals(m3));
    }
}
