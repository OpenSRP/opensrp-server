package org.opensrp.integration;

import java.util.ArrayList;

public class ChildImmunizationFields {
        public static final String IMMUNIZATIONS_GIVEN_FIELD_NAME = "immunizationsGiven";
        public static final String PREVIOUS_IMMUNIZATIONS_FIELD_NAME = "previousImmunizations";
        public static final String IMMUNIZATION_DATE_FIELD_NAME = "immunizationDate";

        public static final String BCG_VALUE = "bcg";

        public static final String DPT_BOOSTER_1_VALUE = "dptbooster_1";
        public static final String DPT_BOOSTER_2_VALUE = "dptbooster_2";

        public static final String HEPATITIS_0_VALUE = "hepb_0";

        public static final String MEASLES_VALUE = "measles1";
        public static final String MEASLES_BOOSTER_VALUE = "measles2";

        public static final String OPV_0_VALUE = "opv_0";
        public static final String OPV_1_VALUE = "opv_1";
        public static final String OPV_2_VALUE = "opv_2";
        public static final String OPV_3_VALUE = "opv_3";
        public static final String OPV_BOOSTER_VALUE = "opvbooster";

        public static final String PENTAVALENT_1_VALUE = "pentavalent_1";
        public static final String PENTAVALENT_2_VALUE = "pentavalent_2";
        public static final String PENTAVALENT_3_VALUE = "pentavalent_3";
        public static final ArrayList<String> IMMUNIZATIONS_VALUE_LIST = new ArrayList<String>() {{
            add(BCG_VALUE);
            add(PENTAVALENT_1_VALUE);
            add(PENTAVALENT_2_VALUE);
            add(PENTAVALENT_3_VALUE);
            add(OPV_0_VALUE);
            add(OPV_1_VALUE);
            add(OPV_2_VALUE);
            add(OPV_3_VALUE);
            add(MEASLES_VALUE);

        }};
}