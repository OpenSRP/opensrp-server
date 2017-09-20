package org.opensrp.util;

import com.sun.org.apache.regexp.internal.RE;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensrp.api.domain.*;
import org.opensrp.domain.*;
import org.opensrp.domain.Address;
import org.opensrp.domain.BaseEntity;
import org.opensrp.domain.Client;
import org.opensrp.domain.Event;
import org.opensrp.domain.Location;
import org.opensrp.domain.Obs;
import org.opensrp.domain.Provider;
import org.opensrp.domain.User;
import org.opensrp.dto.form.MultimediaDTO;

import java.util.*;

import static java.util.Arrays.asList;

public class SampleFullDomainObject {

	//*** ADDRESS RELATED
	public static final String addressType = "addressType";

	public static final String country = "country";

	public static final String stateProvince = "stateProvince";

	public static final String cityVillage = "cityVillage";

	public static final String countryDistrict = "countryDistrict";

	public static final String subDistrict = "subDistrict";

	public static final String town = "town";
	//**

	public static final String name = "name";

	public static final String male = "male";

	public static final DateTime birthDate = new DateTime(0l, DateTimeZone.UTC);

	public static final DateTime deathDate = new DateTime(1l, DateTimeZone.UTC);

	public static final String FULL_NAME = "full name";

	public static final String LOCATION_NAME = "locationName";

	public static Map<String, String> identifier = new HashMap<>();

	public static final Map<String, Object> attributes = new HashMap<>();

	public static final String IDENTIFIER_TYPE = "identifierType";

	public static final String IDENTIFIER_VALUE = "identifierValue";

	public static final String ATTRIBUTES_VALUE = "attributesValue";

	public static final String ATTRIBUTES_TYPE = "attributesType";

	static {
		identifier.put(IDENTIFIER_TYPE, IDENTIFIER_VALUE);
		attributes.put(ATTRIBUTES_TYPE, ATTRIBUTES_VALUE);
	}

	public static final String BASE_ENTITY_ID = "baseEntityId";

	public static final String DIFFERENT_BASE_ENTITY_ID = "differentBaseEntityId";

	public static final String FIRST_NAME = "firstName";

	public static final String MIDDLE_NAME = "middleName";

	public static final String LAST_NAME = "lastName";

	public static final boolean BIRTH_DATE_APPROX = true;

	public static final boolean DEATH_DATE_APPROX = false;

	public static final String FEMALE = "female";

	//** EVENT RELATED
	public static final String EVENT_TYPE = "eventType";

	public static final DateTime EVENT_DATE = new DateTime(0l, DateTimeZone.UTC);

	public static final String ENTITY_TYPE = "entityType";

	public static final String PROVIDER_ID = "providerId";

	public static final String LOCATION_ID = "locationId";

	public static final String FORM_SUBMISSION_ID = "formSubmissionId";

	public static final Map<String, String> DETAILS = new HashMap<>();

	public static final String DETAIL_KEY = "detailKey";

	public static final String DETAIL_VALUE = "detailValue";

	static {
		DETAILS.put(DETAIL_KEY, DETAIL_VALUE);
	}
	//**

	//** OBS RELATED
	public static final String CONCEPT = "concept";

	public static final String FIELD_DATA_TYPE = "fieldDataTyp";

	public static final String FIELD_CODE = "fieldCode";

	public static final String PARENT_CODE = null;

	public static final String VALUE = "value";

	public static final String COMMENTS_TEST = "commentsTest";

	public static final String FORM_SUBMISSION_FIELD = "formSubmissionField";
	//**

	//** ERROR TRACE RELATED
	public static final String RECORD_ID = "recordId";

	public static final DateTime EPOCH_DATE_TIME = new DateTime(0l, DateTimeZone.UTC);

	public static final String ERROR_NAME = "errorName";

	public static final String OCCURED_AT = "occuredAt";

	public static final String STACK_TRACE = "stackTrace";

	public static final String SOLVED = "solved";

	//** APP STATE TOKEN RELATED
	public enum AppStateTokenName {
		APP_STATE_TOKEN_NAME("appStateTokenName"), DIFFERENT_APP_STATE_TOKEN_NAME("differentAppStateToken");

		String value;

		AppStateTokenName(String value) {
			this.value = value;
		}

	}

	public static final int LAST_EDIT_DATE = 1222;

	public static final String APP_STATE_TOKEN_DESCRIPTION = "description";
	//**

	//** LOCATION RELATED
	public static Set<String> locationTags = new HashSet<>(asList("tags1", "tags2"));
	//**

	//** HIA2 INDICATOR RELATED
	public static final String INDICATOR_CODE = "indicatorCode";

	public static final String HIA2_INDICATOR_LABEL = "hia2IndicatorLabel";

	public static final String DHIS_ID = "dhisId";

	public static final String HIA_2_DESCRIPTION = "hia2Description";

	public static final String HIA2_CATEGORY = "hia2Category";

	public static final String HIA2_VALUE = "hia2Value";

	public static final String HIA2_UPDATED_AT = "hia2UpdatedAt";
	//**

	//** REPORT RELATED
	public static final String REPORT_TYPE = "reportType";

	public static final String REPORT_STATUS = "reportStatus";

	public static final int REPORT_VERSION = 222;

	public static final int REPORT_DURATION = 3434;
	//**

	//** USER RELATED
	public static final String USER_NAME = "userName";

	public static final String PASSWORD = "password";

	public static final String SALT = "salt";

	public static final String USER_STATUS = "userStatus";

	public static final List<String> ROLES = asList("role1", "role2");

	public static final List<String> PERMISSIONS = asList("permission1", "permission2");
	//**

	//** MULTIMEDIA RELATED
	public static final String FILE_CATEGORY = "fileCategory";

	public static final String FILE_PATH = "filePath";

	public static final String CONTENT_TYPE = "contentType";

	public static final String CASE_ID = "caseId";
	//**

	public static Address getAddress() {
		Address address = new Address().withAddressType(addressType).withCountry(country).withStateProvince(stateProvince)
				.withCityVillage(cityVillage).withCountyDistrict(countryDistrict).withSubDistrict(subDistrict)
				.withTown(town);
		return address;
	}

	public static Client getClient() {
		Client client = new Client(BASE_ENTITY_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, birthDate, deathDate,
				BIRTH_DATE_APPROX, DEATH_DATE_APPROX, FEMALE, Collections.singletonList(getAddress()), identifier,
				attributes);
		return client;
	}


	public static Obs getObs() {
		Obs obs = new Obs(CONCEPT, FIELD_DATA_TYPE, FIELD_CODE, PARENT_CODE, VALUE, COMMENTS_TEST, FORM_SUBMISSION_FIELD);
		return obs;
	}

	public static Event getEvent() {
		Event event = new Event(BASE_ENTITY_ID, EVENT_TYPE, EVENT_DATE, ENTITY_TYPE, PROVIDER_ID, LOCATION_ID,
				FORM_SUBMISSION_ID);
		event.setIdentifiers(identifier);
		event.setDetails(DETAILS);
		event.addObs(getObs());
		return event;
	}

	public static AppStateToken getAppStateToken() {
		AppStateToken appStateToken = new AppStateToken(AppStateTokenName.APP_STATE_TOKEN_NAME.name(), VALUE, LAST_EDIT_DATE,
				APP_STATE_TOKEN_DESCRIPTION);
		return appStateToken;
	}

	public static ErrorTrace getErrorTrace() {
		ErrorTrace errorTrace = new ErrorTrace(RECORD_ID, EPOCH_DATE_TIME, ERROR_NAME, OCCURED_AT, STACK_TRACE, SOLVED);
		return errorTrace;
	}

	public static Provider getProvider() {
		Provider provider = new Provider(BASE_ENTITY_ID, FULL_NAME);
		return provider;
	}

	public static Location getDomainLocation() {
		Location location = new Location(LOCATION_ID, LOCATION_NAME, getAddress(), identifier, null, locationTags,
				attributes);
		return location;
	}

	public static org.opensrp.api.domain.Location getApiLocation() {
		org.opensrp.api.domain.Location location = new org.opensrp.api.domain.Location(LOCATION_ID, LOCATION_NAME, null,
				identifier, null, locationTags, attributes);
		return location;
	}

	public static Hia2Indicator getHia2Indicator() {
		Hia2Indicator hia2Indicator = new Hia2Indicator(INDICATOR_CODE, HIA2_INDICATOR_LABEL, DHIS_ID, HIA_2_DESCRIPTION,
				HIA2_CATEGORY, HIA2_VALUE, PROVIDER_ID, HIA2_UPDATED_AT);
		return hia2Indicator;
	}

	public static Report getReport() {
		Report report = new Report(BASE_ENTITY_ID, LOCATION_ID, EPOCH_DATE_TIME, REPORT_TYPE, FORM_SUBMISSION_ID,
				PROVIDER_ID, REPORT_STATUS, REPORT_VERSION, REPORT_DURATION, asList(getHia2Indicator()));
		report.setIdentifiers(identifier);
		return report;
	}

	public static User getUser() {
		User user = new User(BASE_ENTITY_ID, USER_NAME, PASSWORD, SALT, USER_STATUS, ROLES, PERMISSIONS);
		return user;
	}

	public static BaseEntity getBaseEntity() {
		BaseEntity baseEntity = new BaseEntity(BASE_ENTITY_ID, identifier, attributes, asList(getAddress()));
		return baseEntity;
	}

	public static Multimedia getMultimedia() {
		Multimedia multimedia = new Multimedia(CASE_ID, PROVIDER_ID, CONTENT_TYPE, FILE_PATH, FILE_CATEGORY);
		return multimedia;
	}

	public static MultimediaDTO getMultimediaDTO(String contentType) {
		MultimediaDTO multimediaDTO = new MultimediaDTO(CASE_ID, PROVIDER_ID, contentType, FILE_PATH, FILE_CATEGORY);
		return multimediaDTO;
	}
}
