package org.opensrp.common.util;


import static java.lang.Integer.signum;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a utility class to easily test compare method.
 *
 * @see <a href="https://codereview.stackexchange.com/questions/129358/unit-testing-equals-hashcode-and-comparator-asserting-contracts">Source</a>
 */
public class ComparableTester {

    /**
     * ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y.
     * (This implies that x.compareTo(y) must throw an exception iff y.compareTo(x) throws an exception.) </li>
     *
     * @param <T>
     * @param o1
     * @param o2
     */
    public static <T> void assertComparisonReversal(Comparable<T> o1, Comparable<T> o2) {
        assertTrue("Comparison reversal should apply: sgn(o1.compareTo(o2)) == -sgn(o2.compareTo(o1)). ", signum(o1.compareTo((T) o2)) == -signum(o2.compareTo((T) o1)));
    }

    /**
     * comparator should be consistent with equals if and only if e1.compareTo(e2) == 0 has the same boolean value as
     * e1.equals(e2) for every e1 and e2 of class C
     *
     * @param o1
     * @param o2
     */
    public static <T> void assertConsistencyWithEqual(Comparable<T> o1, Comparable<T> o2) {
        assertEquals("o1 and o2 should be equal. Before testing comparison. ", o1, o2);

        assertTrue("since o1 and o2 are equals, o1.compareTo(o2) should return zero!", o1.compareTo((T) o2) == 0);
    }

    /**
     * e.compareTo(null) should throw a NullPointerException
     *
     * @param o1
     */

    public static <T> void assertNullPointerException(Comparable<T> o1) {
        o1.compareTo(null);
    }

    /**
     * (o3.compareTo(o2)>0 && o2.compareTo(o1)>0) implies o3.compareTo(o1)>0.
     *
     * @param o1
     * @param o2
     * @param o3
     */
    public static <T> void assertTransitivity(Comparable<T> o1, Comparable<T> o2, Comparable<T> o3) {
        assertTrue("(" + o3 + ".compareTo(" + o2 + ") > 0) && (" + o2 + ".compareTo(" + o1 + ") > 0 ) && ( " + o3 + ".compareTo(" + o1 + ") > 0 )", (o3.compareTo((T) o2) > 0) && (o2.compareTo((T) o1) > 0) && (o3.compareTo((T) o1) > 0));
    }

    /**
     * ensure that twinO1.compareTo(twinO2)==0 implies that sgn(twinO1.compareTo(differentO3)) == sgn(twinO2.compareTo(differentO3)), for all z.
     *
     * @param twinO1
     * @param twinO2
     * @param differentO3
     */
    public static <T> void assertConsistency(Comparable<T> twinO1, Comparable<T> twinO2, Comparable<T> differentO3) {
        assertTrue((twinO1.compareTo((T) twinO2) == 0) && (signum(twinO1.compareTo((T) differentO3)) == signum(twinO2.compareTo((T) differentO3))));
    }
}
