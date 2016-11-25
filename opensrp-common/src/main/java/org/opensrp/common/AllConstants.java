package org.opensrp.common;

import org.codehaus.jackson.annotate.JsonProperty;



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

    public static class BaseEntity{
    	public static final String BASE_ENTITY_ID = "baseEntityId";
    	public static final String ADDRESS_TYPE = "addressType";
    	public static final String START_DATE = "startDate";
    	public static final String END_DATE = "endDate";
    	public static final String LATITUDE = "latitude";
    	public static final String LONGITUTE = "longitute";
    	public static final String GEOPOINT = "geopoint";
    	public static final String POSTAL_CODE = "postalCode";
    	public static final String SUB_TOWN = "subTown";
    	public static final String TOWN = "town";
    	public static final String SUB_DISTRICT = "subDistrict";
    	public static final String COUNTY_DISTRICT = "countyDistrict";
    	public static final String CITY_VILLAGE = "cityVillage";
    	public static final String STATE_PROVINCE = "stateProvince";
    	public static final String COUNTRY = "country";
    	public static final String LAST_UPDATE = "lastEdited";
    	
    }
    public static class MultimediaData {
    	public static final String BASEENTITYID="baseEntityId";
    	public static final String NAME="name";
    	public static final String PROVIDERID="providerId";
    	public static final String CONTENTTYPE="contentType";
    	public static final String FILEPATH="fileType";
    	public static final String FILECATEGORY="fileCategory";
    	public static final String UPLOADDATE="uploadDate";
    	public static final String DESCRIPTION="description";
    	public static final String FILESIZE="fileSize";
    	public static final String IMAGE="image";
    	public static final String PREVIEWIMAGE="previewImage";
    	public static final String COMMENTS="comments";
    }

    public static class Client extends BaseEntity{
    	public static final String FIRST_NAME = "firstName";
    	public static final String MIDDLE_NAME = "middleName";
    	public static final String LAST_NAME = "lastName";
    	public static final String BIRTH_DATE = "birthdate";
    	public static final String DEATH_DATE = "deathdate";
    	public static final String BIRTH_DATE_APPROX = "birthdateApprox";
    	public static final String DEATH_DATE_APPROX = "deathdateApprox";
    	public static final String GENDER = "gender";
    }
    
    public static class Drug extends BaseEntity{
    	public static final String NAME="name";
    	public static final String NAMEUUID="nameUuid";
    	public static final String BASENAME="baseName";
    	public static final String BASENAMEUUID="baseNameUuid";
    	public static final String CREATOR="creator";
    	public static final String CREATORUUID="creatorUuid";
    	public static final String DOSESTRENGHT="doseStrenght";
    	public static final String ROUTE="route";
    	public static final String MAXDAILYDOSE="maxDailyDose";
    	public static final String MINIDAILYDOSE="miniDailyDose";
    	public static final String DESCRIPTION="Description";
    	public static final String COMBINATION="combination";
    }
    
    public static class DrugOrder extends BaseEntity{
    	public static final String orderType="orderType";
    	public static final String drugName="drugName";
    	public static final String orderNumber="orderNumber";
    	public static final String patientUuid="patientUuid";
    	public static final String drugUuid="drugUuid";
    	public static final String action="action";
    	public static final String careSettingUuid="careSettingUuid";
    	public static final String previousOrder="previousOrder";
    	public static final String dateActivated="dateActivated";
    	public static final String dateStopped="dateStopped";
    	public static final String autoExpireDate="autoExpireDate";
    	public static final String encounterUuid="encounterUuid";
    	public static final String ordererUuid="ordererUuid";
    	public static final String urgency="urgency";
    	public static final String instructions="urgency";
    	public static final String orderReason="orderReason";
    	public static final String dosingType="dosingType";
    	public static final String dose="dose";
    	public static final String doseUnitsUuid="doseUnitsUuid";
    	public static final String descriptions="descriptions";
    	public static final String drugFrequencyUuid="drugFrequencyUuid";
    	public static final String quantity="quantity";
    	}
    
    public static class Event {
    	public static final String FORM_SUBMISSION_ID = "formSubmissionId";
    	public static final String EVENT_TYPE = "eventType";
    	public static final String EVENT_ID = "eventId";
    	public static final String LOCATION_ID = "locationId";
    	public static final String EVENT_DATE = "eventDate";
    	public static final String PROVIDER_ID = "providerId";
    	public static final String ENTITY_TYPE = "entityType";

    }
    
    public static class Form {
        public static final String ENTITY_ID = "entityId";
        public static final String ANM_ID = "anmId";
        public static final String FORM_NAME = "formName";
        public static final String INSTANCE_ID = "instanceId";
        public static final String CLIENT_VERSION = "clientVersion";
        public static final String SERVER_VERSION = "serverVersion";
    }
    public static class HTTP {
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
        public static final String WWW_AUTHENTICATE_HEADER = "www-authenticate";
    }

	public static class OpenSRPEvent{
		public static final String FORM_SUBMISSION = "FORM_SUBMISSION";
	}

	public enum Config {
		FORM_ENTITY_PARSER_LAST_SYNCED_FORM_SUBMISSION
	}

	public static final String FORM_SCHEDULE_SUBJECT = "FORM-SCHEDULE";
	
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

}
