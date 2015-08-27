package org.opensrp.common;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class AllConstants {
	public static final String OPENSRP_FORM_DATABASE_CONNECTOR = "opensrpFormDatabaseConnector";
    public static final String OPENSRP_DATABASE_CONNECTOR = "opensrpDatabaseConnector";
    public static final String OPENSRP_MCTS_DATABASE_CONNECTOR = "opensrpMCTSDatabaseConnector";
    public static final String SPACE = " ";
    public static final String BOOLEAN_TRUE_VALUE = "true";
    public static final String BOOLEAN_FALSE_VALUE = "false";
    public static final String AUTO_CLOSE_PNC_CLOSE_REASON = "Auto Close PNC";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String EMPTY_STRING = "";
    public static final String OPENSRP_ERRORTRACE_DATABASE="opensrpErrorTraceDatabaseConnector";

    public static class Form {
        public static final String ENTITY_ID = "entityId";
        public static final String ANM_ID = "anmId";
        public static final String FORM_NAME = "formName";
        public static final String INSTANCE_ID = "instanceId";
        public static final String CLIENT_VERSION = "clientVersion";
        public static final String SERVER_VERSION = "serverVersion";
        public static final String EC_REGISTRATION = "ec_registration";
        public static final String FP_COMPLICATIONS = "fp_complications";
        public static final String FP_CHANGE = "fp_change";
        public static final String RENEW_FP_PRODUCT = "renew_fp_product";
        public static final String FP_FOLLOWUP_PRODUCT = "fp_followup";
        public static final String FP_REFERRAL_FOLLOWUP = "fp_referral_followup";
        public static final String EC_CLOSE = "ec_close";
        public static final String EC_EDIT = "ec_edit";
        public static final String ANC_REGISTRATION = "anc_registration";
        public static final String ANC_REGISTRATION_OA = "anc_registration_oa";
        public static final String ANC_VISIT = "anc_visit";
        public static final String ANC_INVESTIGATIONS = "anc_investigations";
        public static final String ANC_CLOSE = "anc_close";
        public static final String IFA = "ifa";
        public static final String HB_TEST = "hb_test";
        public static final String DELIVERY_OUTCOME = "delivery_outcome";
        public static final String DELIVERY_PLAN = "delivery_plan";
        public static final String PNC_REGISTRATION_OA = "pnc_registration_oa";
        public static final String PNC_CLOSE = "pnc_close";
        public static final String PNC_VISIT = "pnc_visit";
        public static final String CHILD_REGISTRATION_EC = "child_registration_ec";
        public static final String CHILD_REGISTRATION_OA = "child_registration_oa";
        public static final String CHILD_IMMUNIZATIONS = "child_immunizations";
        public static final String CHILD_ILLNESS = "child_illness";
        public static final String CHILD_CLOSE = "child_close";
        public static final String TT = "tt";
        public static final String TT_BOOSTER = "tt_booster";
        public static final String TT_1 = "tt_1";
        public static final String TT_2 = "tt_2";
        public static final String BOOLEAN_TRUE_VALUE = "yes";
        public static final String BOOLEAN_FALSE_VALUE = "no";
        public static final String PNC_REGISTRATION_OA_SUB_FORM_NAME = "child_registration_oa";
        public static final String PNC_VISIT_CHILD_SUB_FORM_NAME = "child_pnc_visit";
        public static final String VITAMIN_A = "vitamin_a";
        public static final String PPFP = "postpartum_family_planning";
        public static final String RECORD_ECPS = "record_ecps";
    }

    public static class Report {
        public static final int FIRST_REPORT_MONTH_OF_YEAR = 3;
        public static final int REPORTING_MONTH_START_DAY = 26;
        public static final int REPORTING_MONTH_END_DAY = 25;
        public static final double LOW_BIRTH_WEIGHT_THRESHOLD = 2.5;
        public static final int INFANT_MORTALITY_THRESHOLD_IN_YEARS = 1;
        public static final int CHILD_MORTALITY_THRESHOLD_IN_YEARS = 5;
        public static final int CHILD_EARLY_NEONATAL_MORTALITY_THRESHOLD_IN_DAYS = 7;
        public static final int CHILD_NEONATAL_MORTALITY_THRESHOLD_IN_DAYS = 28;
        public static final int CHILD_DIARRHEA_THRESHOLD_IN_YEARS = 5;
    }

    public static class ReportDataParameters {
        public static final String ANM_IDENTIFIER = "anmIdentifier";
        public static final String SERVICE_PROVIDED_DATA_TYPE = "serviceProvided";
        public static final String ANM_REPORT_DATA_TYPE = "anmReportData";
        public static final String SERVICE_PROVIDER_TYPE = "serviceProviderType";
        public static final String EXTERNAL_ID = "externalId";
        public static final String INDICATOR = "indicator";
        public static final String SERVICE_PROVIDED_DATE = "date";
        public static final String DRISTHI_ENTITY_ID = "dristhiEntityId";
        public static final String VILLAGE = "village";
        public static final String SUB_CENTER = "subCenter";
        public static final String PHC = "phc";
        public static final String QUANTITY = "quantity";
        public static final String SERVICE_PROVIDER_ANM = "ANM";
    }

    public static class ECRegistrationFields {
        public static final String CASTE = "caste";
        public static final String RELIGION = "religion";
        public static final String ECONOMIC_STATUS = "economicStatus";
        public static final String ECONOMIC_STATUS_APL_VALUE = "apl";
        public static final String ECONOMIC_STATUS_BPL_VALUE = "bpl";
        public static final String AADHAR_NUMBER = "aadharNumber";
        public static final String HOUSEHOLD_ADDRESS = "householdAddress";
        public static final String WIFE_AGE = "wifeAge";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String WIFE_EDUCATIONAL_LEVEL = "educationalLevel";
        public static final String HUSBAND_EDUCATION_LEVEL = "husbandEducationLevel";
        public static final String BPL_CARD_NUMBER = "bplCardNumber";
        public static final String NUMBER_OF_PREGNANCIES = "numberOfPregnancies";
        public static final String PARITY = "parity";
        public static final String NUMBER_OF_LIVING_CHILDREN = "numberOfLivingChildren";
        public static final String NUMBER_OF_STILL_BIRTHS = "numberOfStillBirths";
        public static final String NUMBER_OF_ABORTIONS = "numberOfAbortions";
        public static final String YOUNGEST_CHILD_DOB = "youngestChildDOB";
        public static final String YOUNGEST_CHILD_AGE = "youngestChildAge";
        public static final String WIFE_DOB = "womanDOB";
        public static final String HUSBAND_AGE = "husbandAge";
        public static final String HOUSEHOLD_NUMBER = "householdNumber";
        public static final String HEAD_OF_HOUSEHOLD = "headOfHousehold";
        public static final String NUMBER_OF_LIVING_MALE_CHILDREN = "numberOfLivingMaleChildren";
        public static final String NUMBER_OF_LIVING_FEMALE_CHILDREN = "numberOfLivingFemaleChild";
        public static final String SC_VALUE = "sc";
        public static final String ST_VALUE = "st";
        public static final String C_OTHERS_VALUE = "c_others";
        public static final String IS_OUT_OF_AREA_FALSE = "false";
        public static final String IS_OUT_OF_AREA = "isOutOfArea";

    }

    public static class DeliveryOutcomeFields {
        public static final String DELIVERY_OUTCOME = "deliveryOutcome";
        public static final String DELIVERY_PLACE = "deliveryPlace";
        public static final String DID_WOMAN_SURVIVE = "didWomanSurvive";
        public static final String DID_MOTHER_SURVIVE = "didMotherSurvive";
        public static final String DID_BREAST_FEEDING_START = "didBreastfeedingStart";
        public static final String DELIVERY_COMPLICATIONS = "deliveryComplications";
        public static final String LIVE_BIRTH_FIELD_VALUE = "live_birth";
        public static final String PHC_FIELD_VALUE = "phc";
        public static final String CHC_FIELD_VALUE = "chc";
        public static final String SDH_FIELD_VALUE = "sdh";
        public static final String DH_FIELD_VALUE = "dh";
        public static final String PRIVATE_FACILITY_FIELD_VALUE = "private_facility";
        public static final String CHILD_REGISTRATION_SUB_FORM_NAME = "child_registration";
        public static final String STILL_BIRTH_VALUE = "still_birth";
        public static final String DELIVERY_TYPE = "deliveryType";
        public static final String CESAREAN_VALUE = "cesarean";
        public static final String IS_SKILLED_DELIVERY = "isSkilledDelivery";
        public static final String IMMUNIZATIONS_AT_BIRTH = "immunizationsAtBirth";
    }

    public static class ANCRegistrationFormFields {
        public static final String JSY_BENEFICIARY = "isJSYBeneficiary";
        public static final String ANC_NUMBER = "ancNumber";
        public static final String EDD = "edd";
        public static final String HEIGHT = "height";
        public static final String BLOOD_GROUP = "bloodGroup";
    }

    public static class ANCFormFields {
        public static final String MOTHER_ID = "motherId";
        public static final String REGISTRATION_DATE = "registrationDate";
        public static final String ANC_VISIT_DATE_FIELD = "ancVisitDate";
        public static final String ANC_VISIT_NUMBER_FIELD = "ancVisitNumber";
        public static final String TT1_DOSE_VALUE = "tt1";
        public static final String TT2_DOSE_VALUE = "tt2";
        public static final String TT_BOOSTER_DOSE_VALUE = "ttbooster";
        public static final String TT_DOSE_FIELD = "ttDose";
        public static final String TT_DATE_FIELD = "ttDate";
        public static final String THAYI_CARD_NUMBER = "thayiCardNumber";
    }

    public static class ANCVisitFormFields {
        public static final String BP_DIASTOLIC = "bpDiastolic";
        public static final String PREVIOUS_BP_DIASTOLIC = "previousBpDiastolic";
        public static final String PREVIOUS_BP_SYSTOLIC = "previousBpSystolic";
        public static final String BP_SYSTOLIC = "bpSystolic";
        public static final String IS_HYPERTENSION_DETECTED_FOR_FIRST_TIME = "isHypertensionDetectedForFirstTime";
        public static final String RISK_OBSERVED_DURING_ANC = "riskObservedDuringANC";
        public static final String BLEEDING_VALUE = "bleeding";
        public static final String WEIGHT = "weight";
    }

    public static class HbTestFormFields {
        public static final String HB_TEST_DATE_FIELD = "hbTestDate";
        public static final String HB_LEVEL_FIELD = "hbLevel";
        public static final String ANAEMIC_STATUS_FIELD = "anaemicStatus";
        public static final String PREVIOUS_ANAEMIC_STATUS_FIELD = "previousAnaemicStatus";
    }

    public static class ANCInvestigationsFormFields {
        public static final String TEST_DATE = "testDate";
        public static final String TESTS_RESULTS_TO_ENTER = "testResultsToEnter";
        public static final String WOMAN_BLOOD_GROUP = "womanBloodGroup";
        public static final String RH_INCOMPATIBLE_COUPLE = "rhIncompatibleCouple";
        public static final String TESTS_POSITIVE_RESULTS = "testsResultPositive";
        public static final String BILE_SALTS = "bileSalts";
        public static final String BILE_PIGMENTS = "bilePigments";
        public static final String URINE_SUGAR_VALUE = "urine_sugar";
        public static final String MALARIA_VALUE = "mp";
    }

    public static class PNCVisitFormFields {
        public static final String VISIT_DATE_FIELD_NAME = "pncVisitDate";
        public static final String VISIT_DAY_FIELD_NAME = "pncVisitDay";
        public static final String VISIT_DATES_FIELD_NAME = "pncVisitDates";
        public static final String VISIT_PLACE_FIELD_NAME = "pncVisitPlace";
        public static final String VISIT_PERSON_FIELD_NAME = "pncVisitPerson";
        public static final String URINE_STOOL_PROBLEMS = "urineStoolProblems";
        public static final String ACTIVITY_PROBLEMS = "activityProblems";
        public static final String BREATHING_PROBLEMS = "breathingProblems";
        public static final String SKIN_PROBLEMS = "skinProblems";
        public static final String DIFFICULTIES_FIELD_NAME = "difficulties1";
        public static final String ABDOMINAL_PROBLEMS_FIELD_NAME = "abdominalProblems";
        public static final String VAGINAL_PROBLEMS_FIELD_NAME = "vaginalProblems";
        public static final String URINAL_PROBLEMS_FIELD_NAME = "difficulties2";
        public static final String BREAST_PROBLEMS = "breastProblems";
        public static final String DISCHARGE_DATE = "dischargeDate";
        public static final String HEAVY_BLEEDING_VALUE = "heavy_bleeding";
        public static final String BAD_SMELL_LOCHEA_VALUE = "bad_smell_lochea";
        public static final String HAS_FEVER_FIELD = "hasFever";
        public static final String IMMEDIATE_REFERRAL = "immediateReferral";
    }

    public static class CommonFormFields {
        public static final String SUBMISSION_DATE_FIELD_NAME = "submissionDate";
        public static final String ID = "id";
        public static final String SERVICE_PROVIDED_PLACE = "serviceProvidedPlace";
        public static final String SERVICE_PROVIDED_DATE = "serviceProvidedDate";
        public static final String SUB_CENTER_SERVICE_PROVIDED_PLACE_VALUE = "sub_center";
        public static final String SUBCENTER_SERVICE_PROVIDED_PLACE_VALUE = "subcenter";
        public static final String HOME_FIELD_VALUE = "home";
        public static final String REFERENCE_DATE = "referenceDate";
        public static final String IS_HIGH_RISK = "isHighRisk";
    }

    public static class CommonChildFormFields {
        public static final String DIARRHEA_VALUE = "diarrhea";
        public static final String FEMALE_VALUE = "female";
        public static final String MALE_VALUE = "male";
        public static final String GENDER = "gender";
    }

    public static class ChildRegistrationFormFields {
        public static final String BF_POSTBIRTH = "didBreastfeedingStart";
        public static final String DATE_OF_BIRTH = "dateOfBirth";
        public static final String CHILD_VITAMIN_A_HISTORY = "childVitaminAHistory";
        public static final String DATE = "Date";
        public static final String VITAMIN = "vitamin";
        public static final String SHOULD_CLOSE_MOTHER = "shouldCloseMother";
        public static final String THAYI_CARD = "thayiCard";
    }

    public static class ChildCloseFormFields {
        public static final String CLOSE_REASON_FIELD_NAME = "closeReason";
        public static final String IS_IMMUNIZATION_DEATH = "isImmunizationDeath";
        public static final String DEATH_OF_CHILD_VALUE = "death_of_child";
        public static final String DATE_OF_DEATH_FIELD_NAME = "deathDate";
        public static final String DEATH_CAUSE = "deathCause";
        public static final String PERMANENT_RELOCATION_VALUE = "permanent_relocation";
        public static final String WITHIN_24HRS_VALUE = "within_24hrs";
        public static final String SEPSIS_VALUE = "sepsis";
        public static final String ASPHYXIA_VALUE = "asphyxia";
        public static final String DIARRHEA_VALUE = "diarrhea";
        public static final String LBW_VALUE = "lbw";
        public static final String FEVER_RELATED_VALUE = "fever_related";
        public static final String PNEUMONIA_VALUE = "pneumonia";
        public static final List<String> OTHERS_LIST = new ArrayList<String>() {{
            add("cause_not_identified");
            add("others");
        }};
        public static final List<String> OTHERS_VALUE_LIST = new ArrayList<String>() {{
            add("malnutrition");
            add("ari");
            add("malaria");
        }};
    }

    public static class EntityCloseFormFields {
        public static final String CLOSE_REASON_FIELD_NAME = "closeReason";
        public static final String ANC_DEATH_DATE_FIELD_NAME = "maternalDeathDate";
        public static final String IS_MATERNAL_LEAVE_FIELD_NAME = "isMaternalDeath";
        public static final String WRONG_ENTRY_VALUE = "wrong_entry";
    }

    public static class ANCCloseFields {
        public static final String CLOSE_MTP_DATE_FIELD_NAME = "dateOfInducedAbortion";
        public static final String CLOSE_SPONTANEOUS_ABORTION_DATE_FIELD_NAME = "dateOfSpontaneousAbortion";
        public static final String CLOSE_MTP_TIME_FIELD_NAME = "timeOfInducedAbortion";
        public static final String MTP_GREATER_THAN_12_WEEKS_FIELD_NAME = "greater_12wks";
        public static final String MTP_LESS_THAN_12_WEEKS_FIELD_NAME = "less_12wks";
        public static final String DEATH_OF_WOMAN_VALUE = "death_of_woman";
        public static final String PERMANENT_RELOCATION_VALUE = "relocation_permanent";
        public static final String SPONTANEOUS_ABORTION_VALUE = "spontaneous_abortion";
    }

    public static class PNCCloseFields {
        public static final String DEATH_DATE_FIELD_NAME = "deathDate";
        public static final String DEATH_OF_MOTHER_VALUE = "death_of_mother";
        public static final String PERMANENT_RELOCATION_VALUE = "permanent_relocation";
    }

    public static class ECCloseFields {
        public static final String IS_EC_CLOSE_CONFIRMED_FIELD_NAME = "isECCloseConfirmed";
        public static final String IS_STERILIZATION_DEATH_FIELD_NAME = "isSterilizationDeath";
        public static final String MATERNAL_DEATH_CAUSE_FIELD_NAME = "maternalDeathCause";
        public static final String ABORTION_VALUE = "abortion";
        public static final String PROLONGED_OBSTRUCTED_LABOR_VALUE = "prolonged_obstructed_labor";
        public static final String HYPERTENSION_FITS_VALUE = "hypertension_fits";
        public static final String BLEEDING_VALUE = "bleeding_hemorrhage";
        public static final String HIGH_FEVER_VALUE = "fever_infection";
    }

    public static class FamilyPlanningFormFields {
        public static final String LMP_DATE = "lmpDate";
        public static final String UPT_RESULT = "uptResult";
        public static final String CURRENT_FP_METHOD_FIELD_NAME = "currentMethod";
        public static final String FP_METHOD_CHANGE_DATE_FIELD_NAME = "familyPlanningMethodChangeDate";
        public static final String DMPA_INJECTION_DATE_FIELD_NAME = "dmpaInjectionDate";
        public static final String DMPA_INJECTABLE_FP_METHOD_VALUE = "dmpa_injectable";
        public static final String OCP_REFILL_DATE_FIELD_NAME = "ocpRefillDate";
        public static final String NUMBER_OF_OCP_STRIPS_SUPPLIED_FIELD_NAME = "numberOfOCPDelivered";
        public static final String NUMBER_OF_CONDOMS_SUPPLIED_FIELD_NAME = "numberOfCondomsSupplied";
        public static final String NUMBER_OF_CENTCHROMAN_PILLS_SUPPLIED_FIELD_NAME = "numberOfCentchromanPillsDelivered";
        public static final String NUMBER_OF_ECPS_GIVEN_FIELD_NAME = "numberOfECPsGiven";
        public static final String FP_FOLLOWUP_DATE_FIELD_NAME = "fpFollowupDate";
        public static final String STERILIZATION_SIDE_EFFECT_FIELD_NAME = "sterilizationSideEffect";
        public static final String IS_STERILIZATION_FAILURE_FIELD_NAME = "isSterilizationFailure";
        public static final String OCP_FP_METHOD_VALUE = "ocp";
        public static final String CONDOM_FP_METHOD_VALUE = "condom";
        public static final String FEMALE_STERILIZATION_FP_METHOD_VALUE = "female_sterilization";
        public static final String MALE_STERILIZATION_FP_METHOD_VALUE = "male_sterilization";
        public static final String CENTCHROMAN_FP_METHOD_VALUE = "centchroman";
        public static final String COMPLICATION_DATE_FIELD_NAME = "complicationDate";
        public static final String NEEDS_FOLLOWUP_FIELD_NAME = "needsFollowup";
        public static final String NEEDS_REFERRAL_FOLLOWUP_FIELD_NAME = "needsReferralFollowup";
        public static final String IUD_FP_METHOD_VALUE = "iud";
        public static final String REFERRAL_FOLLOW_UP_DATE_FIELD_NAME = "referralFollowupDate";
        public static final String PREVIOUS_FP_METHOD_FIELD_NAME = "currentMethod";
        public static final String NEW_FP_METHOD_FIELD_NAME = "newMethod";
        public static final String IUD_REMOVAL_PLACE = "iudRemovalPlace";
        public static final String IUD_PLACE = "iudPlace";
        public static final String FEMALE_STERILIZATION_TYPE = "femaleSterilizationType";
        public static final String MALE_STERILIZATION_TYPE = "maleSterilizationType";
        public static final String FP_RENEW_METHOD_VISIT_DATE = "fpRenewMethodVisitDate";
    }

    public static class ChildImmunizationFields {
        public static final String IMMUNIZATIONS_GIVEN_FIELD_NAME = "immunizationsGiven";
        public static final String PREVIOUS_IMMUNIZATIONS_FIELD_NAME = "previousImmunizations";
        public static final String IMMUNIZATION_DATE_FIELD_NAME = "immunizationDate";

        public static final String BCG_VALUE = "bcg";

        public static final String DPT_BOOSTER_1_VALUE = "dptbooster_1";
        public static final String DPT_BOOSTER_2_VALUE = "dptbooster_2";

        public static final String HEPATITIS_0_VALUE = "hepb_0";

        public static final String MEASLES_VALUE = "measles";
        public static final String MEASLES_BOOSTER_VALUE = "measlesbooster";

        public static final String OPV_0_VALUE = "opv_0";
        public static final String OPV_1_VALUE = "opv_1";
        public static final String OPV_2_VALUE = "opv_2";
        public static final String OPV_3_VALUE = "opv_3";
        public static final String OPV_BOOSTER_VALUE = "opvbooster";

        public static final String PENTAVALENT_1_VALUE = "pentavalent_1";
        public static final String PENTAVALENT_2_VALUE = "pentavalent_2";
        public static final String PENTAVALENT_3_VALUE = "pentavalent_3";
        public static final List<String> IMMUNIZATIONS_VALUE_LIST = new ArrayList<String>() {{
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

        public static final List<String> IMMUNIZATIONS_WITH_MMR_VALUE_LIST = new ArrayList<String>() {{
            add(BCG_VALUE);
            add(PENTAVALENT_1_VALUE);
            add(PENTAVALENT_2_VALUE);
            add(PENTAVALENT_3_VALUE);
            add(OPV_0_VALUE);
            add(OPV_1_VALUE);
            add(OPV_2_VALUE);
            add(OPV_3_VALUE);
            add(MMR_VALUE);
        }};

        public static final String MMR_VALUE = "mmr";
        public static final String JE_VALUE = "je";
    }

    public static class VitaminAFields {
        public static final String VITAMIN_A_DOSE = "vitaminADose";
        public static final String VITAMIN_A_DATE = "vitaminADate";
        public static final String VITAMIN_A_DOSE_1_VALUE = "1";
        public static final String VITAMIN_A_DOSE_2_VALUE = "2";
        public static final String VITAMIN_A_DOSE_3_VALUE = "3";
        public static final String VITAMIN_A_DOSE_4_VALUE = "4";
        public static final String VITAMIN_A_DOSE_5_VALUE = "5";
        public static final String VITAMIN_A_DOSE_6_VALUE = "6";
        public static final String VITAMIN_A_DOSE_7_VALUE = "7";
        public static final String VITAMIN_A_DOSE_8_VALUE = "8";
        public static final String VITAMIN_A_DOSE_9_VALUE = "9";
        public static final List<String> VITAMIN_A_DOSES_1_2_5_9 =
                asList(
                        VITAMIN_A_DOSE_1_VALUE,
                        VITAMIN_A_DOSE_2_VALUE,
                        VITAMIN_A_DOSE_5_VALUE,
                        VITAMIN_A_DOSE_9_VALUE);
        public static final String VITAMIN_A_DOSE_PREFIX = "dose";
    }

    public static class IFAFields {
        public static final String NUMBER_OF_IFA_TABLETS_GIVEN = "numberOfIFATabletsGiven";
        public static final String IFA_TABLETS_DATE = "ifaTabletsDate";
        public static final String TOTAL_NUMBER_OF_IFA_TABLETS_GIVEN = "totalNumberOfIFATabletsGiven";
    }

    public static class ChildIllnessFields {
        public static final String CHILD_SIGNS = "childSigns";
        public static final String SICK_VISIT_DATE = "sickVisitDate";
        public static final String REPORT_CHILD_DISEASE = "reportChildDisease";
        public static final String REPORT_CHILD_DISEASE_DATE = "reportChildDiseaseDate";
        public static final String DIARRHEA_DEHYDRATION_VALUE = "diarrhea_dehydration";
        public static final String MALARIA_VALUE = "malaria";
    }

    public static class FormEntityTypes {
        public static final String CHILD_TYPE = "child";
        public static final String MOTHER_TYPE = "mother";
        public static final String ELIGIBLE_COUPLE_TYPE = "eligible_couple";
        public static final String MCTS_REPORT_TYPE = "MCTSReport";
    }

    public static class HTTP {
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
        public static final String WWW_AUTHENTICATE_HEADER = "www-authenticate";
    }
}
