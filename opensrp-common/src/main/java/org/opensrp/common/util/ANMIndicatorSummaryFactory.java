package org.opensrp.common.util;

import org.opensrp.common.domain.ANMIndicatorSummary;
import org.opensrp.common.domain.MonthSummary;

import static java.util.Arrays.asList;

//TODO: Document this class purpose and work methodology.
public class ANMIndicatorSummaryFactory {
    public static ANMIndicatorSummary createSummaryForIUD() {
        return new ANMIndicatorSummary("IUD", "40", asList(new MonthSummary("4", "2012", "2", "2", asList("CASE 1", "CASE 2")),
                new MonthSummary("5", "2012", "2", "4", asList("CASE 3", "CASE 4"))));
    }

    public static ANMIndicatorSummary createSummaryForIUD(String annualTarget) {
        return new ANMIndicatorSummary("IUD", annualTarget, asList(new MonthSummary("4", "2012", "2", "2", asList("CASE 1", "CASE 2")),
                new MonthSummary("5", "2012", "2", "4", asList("CASE 3", "CASE 4"))));
    }

    public static ANMIndicatorSummary createSummaryForANC() {
        return new ANMIndicatorSummary("ANC", "30", asList(new MonthSummary("6", "2012", "2", "2", asList("CASE 5", "CASE 6"))));
    }

    public static ANMIndicatorSummary createSummaryForCondom() {
        return new ANMIndicatorSummary("CONDOM", "20", asList(new MonthSummary("3", "2013", "2", "2", asList("CASE 7"))));
    }
}
