package org.opensrp.common.util;


public class ComparableVerifier<T> {

    Class classToTest;
    Comparable<T> smallOne;
    Comparable<T> mediumOne;
    Comparable<T> biggestOne;

    public ComparableVerifier (Class<T> classToTest, Comparable<T> smallOne, Comparable<T> mediumOne, Comparable<T> biggestOne) {
        this.classToTest = classToTest;
        this.smallOne = smallOne;
        this.mediumOne = mediumOne;
        this.biggestOne = biggestOne;
    }

    public boolean verify() {
       testComparisonConsistency();
       testComparisonConsistencyWithEqual();
       testComparisonNullPointerException();
       testComparisonReversal();
       testComparisonTransitivity();
        return false;
    }

    private void testComparisonReversal() {
        ComparableTester.assertComparisonReversal(smallOne, smallOne);
        ComparableTester.assertComparisonReversal(smallOne, mediumOne);
        ComparableTester.assertComparisonReversal(smallOne, mediumOne);
    }

    private void testComparisonConsistencyWithEqual() {
        Comparable<T> sameAsOne = smallOne;
        ComparableTester.assertConsistencyWithEqual(smallOne, sameAsOne);
    }


    private void testComparisonNullPointerException(){
        ComparableTester.assertNullPointerException(smallOne);
    }

    private void testComparisonTransitivity() {
        ComparableTester.assertTransitivity(smallOne, mediumOne, biggestOne);
    }


    private void testComparisonConsistency() {
        Comparable<T> sameAsOne = smallOne;
        ComparableTester.assertConsistency(smallOne, sameAsOne, mediumOne);
    }
}
