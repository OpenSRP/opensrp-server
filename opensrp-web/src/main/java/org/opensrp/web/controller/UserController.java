package org.opensrp.web.controller;

import static org.opensrp.web.HttpHeaderFactory.allowOrigin;
import static org.opensrp.web.rest.RestUtils.getStringFilter;
import static org.springframework.http.HttpStatus.OK;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Time;
import org.opensrp.api.domain.User;
import org.opensrp.common.domain.UserDetail;
import org.opensrp.connector.openmrs.service.OpenmrsLocationService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.opensrp.service.LocationService;
import org.opensrp.web.security.DrishtiAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class UserController {
	
	@Value("#{opensrp['opensrp.site.url']}")
	private String opensrpSiteUrl;
	
	@Value("#{opensrp['opensrp.web.url']}")
	protected String OPENSRP_BASE_URL;
	
	@Value("#{opensrp['opensrp.web.username']}")
	protected String OPENSRP_USER;
	
	@Value("#{opensrp['opensrp.web.password']}")
	protected String OPENSRP_PWD;
	
	//	@Value("#{opensrp['opensrp.role.ss']}")
	private Integer childRoleId = 29;
	
	private Integer locationTagId = 33;
	
	@Autowired
	private LocationService locationService;
	
	private DrishtiAuthenticationProvider opensrpAuthenticationProvider;
	
	private OpenmrsLocationService openmrsLocationService;
	
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	public UserController(OpenmrsLocationService openmrsLocationService, OpenmrsUserService openmrsUserService,
	    DrishtiAuthenticationProvider opensrpAuthenticationProvider) {
		this.openmrsLocationService = openmrsLocationService;
		this.openmrsUserService = openmrsUserService;
		this.opensrpAuthenticationProvider = opensrpAuthenticationProvider;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/authenticate-user")
	public ResponseEntity<HttpStatus> authenticateUser() {
		return new ResponseEntity<>(null, allowOrigin(opensrpSiteUrl), OK);
	}
	
	public static String user = "{\"locations\":{\"locationsHierarchy\":{\"map\":{\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\":{\"id\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\",\"label\":\"BANGLADESH\",\"node\":{\"locationId\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\",\"name\":\"BANGLADESH\",\"tags\":[\"Country\"],\"voided\":false},\"children\":{\"5fd18bb2-17f1-4660-93f8-89cfec797d19\":{\"id\":\"5fd18bb2-17f1-4660-93f8-89cfec797d19\",\"label\":\"DHAKA\",\"node\":{\"locationId\":\"5fd18bb2-17f1-4660-93f8-89cfec797d19\",\"name\":\"DHAKA\",\"parentLocation\":{\"locationId\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\",\"name\":\"BANGLADESH\",\"voided\":false},\"tags\":[\"Division\"],\"voided\":false},\"children\":{\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\":{\"id\":\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\",\"label\":\"DHAKA:9266\",\"node\":{\"locationId\":\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\",\"name\":\"DHAKA:9266\",\"parentLocation\":{\"locationId\":\"5fd18bb2-17f1-4660-93f8-89cfec797d19\",\"name\":\"DHAKA\",\"parentLocation\":{\"locationId\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\",\"name\":\"BANGLADESH\",\"voided\":false},\"voided\":false},\"tags\":[\"District\"],\"voided\":false},\"children\":{\"239c4ba5-d543-4263-b9da-d544e7bf246b\":{\"id\":\"239c4ba5-d543-4263-b9da-d544e7bf246b\",\"label\":\"DHAKA NORTH CITY CORPORATION:9267\",\"node\":{\"locationId\":\"239c4ba5-d543-4263-b9da-d544e7bf246b\",\"name\":\"DHAKA NORTH CITY CORPORATION:9267\",\"parentLocation\":{\"locationId\":\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\",\"name\":\"DHAKA:9266\",\"parentLocation\":{\"locationId\":\"5fd18bb2-17f1-4660-93f8-89cfec797d19\",\"name\":\"DHAKA\",\"voided\":false},\"voided\":false},\"tags\":[\"City Corporation Upazila\"],\"voided\":false},\"children\":{\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\":{\"id\":\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\",\"label\":\"NOT POURASABHA:9268\",\"node\":{\"locationId\":\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\",\"name\":\"NOT POURASABHA:9268\",\"parentLocation\":{\"locationId\":\"239c4ba5-d543-4263-b9da-d544e7bf246b\",\"name\":\"DHAKA NORTH CITY CORPORATION:9267\",\"parentLocation\":{\"locationId\":\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\",\"name\":\"DHAKA:9266\",\"voided\":false},\"voided\":false},\"tags\":[\"Pourasabha\"],\"voided\":false},\"children\":{\"309db0e6-b01b-485f-853c-066098f77c2a\":{\"id\":\"309db0e6-b01b-485f-853c-066098f77c2a\",\"label\":\"WARD NO. 20 (PART):9269\",\"node\":{\"locationId\":\"309db0e6-b01b-485f-853c-066098f77c2a\",\"name\":\"WARD NO. 20 (PART):9269\",\"parentLocation\":{\"locationId\":\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\",\"name\":\"NOT POURASABHA:9268\",\"parentLocation\":{\"locationId\":\"239c4ba5-d543-4263-b9da-d544e7bf246b\",\"name\":\"DHAKA NORTH CITY CORPORATION:9267\",\"voided\":false},\"voided\":false},\"tags\":[\"Union Ward\"],\"voided\":false},\"children\":{\"b5fab836-05dc-487c-9366-d89bed77190d\":{\"id\":\"b5fab836-05dc-487c-9366-d89bed77190d\",\"label\":\"ICDDRB (CHOLERA) HOSPITAL-ISD VITORE (MASJIDPARA):9319\",\"node\":{\"locationId\":\"b5fab836-05dc-487c-9366-d89bed77190d\",\"name\":\"ICDDRB (CHOLERA) HOSPITAL-ISD VITORE (MASJIDPARA):9319\",\"parentLocation\":{\"locationId\":\"309db0e6-b01b-485f-853c-066098f77c2a\",\"name\":\"WARD NO. 20 (PART):9269\",\"parentLocation\":{\"locationId\":\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\",\"name\":\"NOT POURASABHA:9268\",\"voided\":false},\"voided\":false},\"tags\":[\"Village\"],\"voided\":false},\"parent\":\"309db0e6-b01b-485f-853c-066098f77c2a\"}},\"parent\":\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\"}},\"parent\":\"239c4ba5-d543-4263-b9da-d544e7bf246b\"}},\"parent\":\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\"}},\"parent\":\"5fd18bb2-17f1-4660-93f8-89cfec797d19\"}},\"parent\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"}}}},\"parentChildren\":{\"309db0e6-b01b-485f-853c-066098f77c2a\":[\"b5fab836-05dc-487c-9366-d89bed77190d\"],\"239c4ba5-d543-4263-b9da-d544e7bf246b\":[\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\"],\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\":[\"309db0e6-b01b-485f-853c-066098f77c2a\"],\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\":[\"239c4ba5-d543-4263-b9da-d544e7bf246b\"],\"5fd18bb2-17f1-4660-93f8-89cfec797d19\":[\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\"],\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\":[\"5fd18bb2-17f1-4660-93f8-89cfec797d19\"]}}},\"team\":{\"display\":\"Meherina Akter\",\"patients\":[],\"resourceVersion\":\"1.8\",\"team\":{\"teamName\":\"HNPP-BRAC\",\"display\":\"HNPP-BRAC\",\"supervisorTeamUuid\":\"\",\"resourceVersion\":\"1.8\",\"uuid\":\"edf76e9c-b4fd-4eed-995c-7947915c99d0\",\"auditInfo\":{\"creator\":{\"display\":\"brachnpp\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/user/88347bc8-2ff0-420b-91a7-c872314603e9\"}],\"uuid\":\"88347bc8-2ff0-420b-91a7-c872314603e9\"},\"dateCreated\":\"2019-10-16T18:04:44.000+0530\"},\"supervisorTeam\":\"\",\"supervisorUuid\":\"\",\"members\":8219.0,\"voided\":false,\"location\":{\"uuid\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\",\"auditInfo\":{\"creator\":{\"display\":\"brachnpp\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/user/88347bc8-2ff0-420b-91a7-c872314603e9\"}],\"uuid\":\"88347bc8-2ff0-420b-91a7-c872314603e9\"},\"dateCreated\":\"2019-10-16T16:32:26.000+0530\"},\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"}],\"display\":\"BANGLADESH\",\"resourceVersion\":\"2.0\",\"tags\":[{\"display\":\"Country\",\"resourceVersion\":\"1.8\",\"name\":\"Country\",\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/f02bc4b0-7b91-4f30-95ff-134efbcbcaa1\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/f02bc4b0-7b91-4f30-95ff-134efbcbcaa1?v\u003dfull\"}],\"uuid\":\"f02bc4b0-7b91-4f30-95ff-134efbcbcaa1\"}],\"name\":\"BANGLADESH\",\"attributes\":[],\"childLocations\":[{\"parentLocation\":{\"display\":\"BANGLADESH\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"}],\"uuid\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"},\"uuid\":\"5fd18bb2-17f1-4660-93f8-89cfec797d19\",\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/5fd18bb2-17f1-4660-93f8-89cfec797d19\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/5fd18bb2-17f1-4660-93f8-89cfec797d19?v\u003dfull\"}],\"display\":\"DHAKA\",\"resourceVersion\":\"2.0\",\"tags\":[{\"display\":\"Division\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/d2f9a146-b498-456f-96c5-4acf1d975f28\"}],\"uuid\":\"d2f9a146-b498-456f-96c5-4acf1d975f28\"}],\"name\":\"DHAKA\",\"attributes\":[],\"childLocations\":[{\"display\":\"DHAKA:9266\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/106c3f13-1ffe-4dad-a1c0-2979ec39c851\"}],\"uuid\":\"106c3f13-1ffe-4dad-a1c0-2979ec39c851\"},{\"display\":\"MANIKGANJ:9266\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/8f394968-2b89-49ab-9acf-2bb3f29affab\"}],\"uuid\":\"8f394968-2b89-49ab-9acf-2bb3f29affab\"}]},{\"parentLocation\":{\"display\":\"BANGLADESH\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"}],\"uuid\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"},\"uuid\":\"d6d8db31-e1ec-4b9c-92f9-2388c3355df4\",\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d6d8db31-e1ec-4b9c-92f9-2388c3355df4\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d6d8db31-e1ec-4b9c-92f9-2388c3355df4?v\u003dfull\"}],\"display\":\"RAJSHAHI\",\"resourceVersion\":\"2.0\",\"tags\":[{\"display\":\"Division\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/d2f9a146-b498-456f-96c5-4acf1d975f28\"}],\"uuid\":\"d2f9a146-b498-456f-96c5-4acf1d975f28\"}],\"name\":\"RAJSHAHI\",\"attributes\":[],\"childLocations\":[{\"display\":\"BOGRA:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/8e2d87c3-3016-4b93-b170-42f4e6a80ad7\"}],\"uuid\":\"8e2d87c3-3016-4b93-b170-42f4e6a80ad7\"},{\"display\":\"CHAPAINABABGANJ:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/9b478bf4-9cf2-44f3-8b78-0eee1b879c9f\"}],\"uuid\":\"9b478bf4-9cf2-44f3-8b78-0eee1b879c9f\"},{\"display\":\"JOYPURHAT:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/90b496f7-4597-455a-b561-0076fba19eb5\"}],\"uuid\":\"90b496f7-4597-455a-b561-0076fba19eb5\"},{\"display\":\"NAOGAON:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/280db403-4a59-45ae-8112-c1787dc1cdc1\"}],\"uuid\":\"280db403-4a59-45ae-8112-c1787dc1cdc1\"},{\"display\":\"NATORE:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/077fa455-f78c-4bac-8483-67fffb5dd0c7\"}],\"uuid\":\"077fa455-f78c-4bac-8483-67fffb5dd0c7\"},{\"display\":\"PABNA:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/487d4216-c2e2-4bed-84a2-7a19f131df7b\"}],\"uuid\":\"487d4216-c2e2-4bed-84a2-7a19f131df7b\"},{\"display\":\"RAJSHAHI:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/ad39a695-fc91-4e3e-b5f4-464e98e162a0\"}],\"uuid\":\"ad39a695-fc91-4e3e-b5f4-464e98e162a0\"},{\"display\":\"SIRAJGANJ:10349\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/bca9c44a-91f6-442a-ba84-72e0812b2aec\"}],\"uuid\":\"bca9c44a-91f6-442a-ba84-72e0812b2aec\"}]},{\"parentLocation\":{\"display\":\"BANGLADESH\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"}],\"uuid\":\"d0ad6c70-d2ec-4f39-b206-b1f7a7abd4cd\"},\"uuid\":\"db078816-728d-4ab4-b564-d409b3eed815\",\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/db078816-728d-4ab4-b564-d409b3eed815\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/db078816-728d-4ab4-b564-d409b3eed815?v\u003dfull\"}],\"display\":\"RANGPUR\",\"resourceVersion\":\"2.0\",\"tags\":[{\"display\":\"Division\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/d2f9a146-b498-456f-96c5-4acf1d975f28\"}],\"uuid\":\"d2f9a146-b498-456f-96c5-4acf1d975f28\"}],\"name\":\"RANGPUR\",\"attributes\":[],\"childLocations\":[{\"display\":\"DINAJPUR:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/8ce82351-e517-4fbe-b3a3-3323f76d56ab\"}],\"uuid\":\"8ce82351-e517-4fbe-b3a3-3323f76d56ab\"},{\"display\":\"GAIBANDHA:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/9e2f7008-5111-4b2a-8caa-c35695e12b63\"}],\"uuid\":\"9e2f7008-5111-4b2a-8caa-c35695e12b63\"},{\"display\":\"KURIGRAM:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/6b759433-eb66-4075-8b9e-2690ac7a69ea\"}],\"uuid\":\"6b759433-eb66-4075-8b9e-2690ac7a69ea\"},{\"display\":\"LALMONIRHAT:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/b8178d1c-1c23-48e7-9955-30f54cf36b84\"}],\"uuid\":\"b8178d1c-1c23-48e7-9955-30f54cf36b84\"},{\"display\":\"NILPHAMARI:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/51907243-2ed5-4497-b877-0b08e9a3d92b\"}],\"uuid\":\"51907243-2ed5-4497-b877-0b08e9a3d92b\"},{\"display\":\"PANCHAGARH:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/3c84965c-6694-4818-abff-99f53a740487\"}],\"uuid\":\"3c84965c-6694-4818-abff-99f53a740487\"},{\"display\":\"RANGPUR:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/af5c0f1f-1031-40e6-a59f-25ae3df01d41\"}],\"uuid\":\"af5c0f1f-1031-40e6-a59f-25ae3df01d41\"},{\"display\":\"THAKURGAON:10376\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/6a6d2204-224a-461d-9210-5d0656cd85c3\"}],\"uuid\":\"6a6d2204-224a-461d-9210-5d0656cd85c3\"}]}]},\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/team/team/edf76e9c-b4fd-4eed-995c-7947915c99d0\"}],\"supervisorIdentifier\":\"\",\"teamIdentifier\":\"HNPP-BRAC\",\"supervisor\":\"\"},\"uuid\":\"13c83ceb-e8a4-47b3-9388-c3427e48c471\",\"subTeamRoles\":[],\"auditInfo\":{\"creator\":{\"display\":\"brachnpp\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/user/88347bc8-2ff0-420b-91a7-c872314603e9\"}],\"uuid\":\"88347bc8-2ff0-420b-91a7-c872314603e9\"},\"dateCreated\":\"2019-10-16T18:58:48.000+0530\"},\"person\":{\"addresses\":[],\"birthdate\":\"2017-01-01T00:00:00.000+0530\",\"gender\":\"M\",\"display\":\"Meherina Akter\",\"resourceVersion\":\"1.11\",\"dead\":false,\"uuid\":\"fd89ee6c-a786-4733-8bfc-f50c435c6ebb\",\"auditInfo\":{\"creator\":{\"display\":\"brachnpp\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/user/88347bc8-2ff0-420b-91a7-c872314603e9\"}],\"uuid\":\"88347bc8-2ff0-420b-91a7-c872314603e9\"},\"dateCreated\":\"2019-10-16T18:58:47.000+0530\"},\"birthdateEstimated\":true,\"deathdateEstimated\":false,\"names\":[{\"display\":\"Meherina Akter\",\"givenName\":\"Meherina\",\"familyName\":\"Akter\",\"resourceVersion\":\"1.8\",\"voided\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/person/fd89ee6c-a786-4733-8bfc-f50c435c6ebb/name/a243343d-90da-40e3-b6fd-c160ff62511d\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/person/fd89ee6c-a786-4733-8bfc-f50c435c6ebb/name/a243343d-90da-40e3-b6fd-c160ff62511d?v\u003dfull\"}],\"uuid\":\"a243343d-90da-40e3-b6fd-c160ff62511d\"}],\"attributes\":[],\"voided\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/person/fd89ee6c-a786-4733-8bfc-f50c435c6ebb\"}],\"preferredName\":{\"display\":\"Meherina Akter\",\"givenName\":\"Meherina\",\"familyName\":\"Akter\",\"resourceVersion\":\"1.8\",\"voided\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/person/fd89ee6c-a786-4733-8bfc-f50c435c6ebb/name/a243343d-90da-40e3-b6fd-c160ff62511d\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/person/fd89ee6c-a786-4733-8bfc-f50c435c6ebb/name/a243343d-90da-40e3-b6fd-c160ff62511d?v\u003dfull\"}],\"uuid\":\"a243343d-90da-40e3-b6fd-c160ff62511d\"},\"age\":2.0},\"voided\":false,\"isDataProvider\":true,\"locations\":[{\"parentLocation\":{\"parentLocation\":{\"display\":\"NOT POURASABHA:9268\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d2fe212b-c82f-4f72-acff-8c84b8f9b740\"}],\"uuid\":\"d2fe212b-c82f-4f72-acff-8c84b8f9b740\"},\"uuid\":\"309db0e6-b01b-485f-853c-066098f77c2a\",\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/309db0e6-b01b-485f-853c-066098f77c2a\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/309db0e6-b01b-485f-853c-066098f77c2a?v\u003dfull\"}],\"display\":\"WARD NO. 20 (PART):9269\",\"resourceVersion\":\"2.0\",\"tags\":[{\"display\":\"Union Ward\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/81870b1f-5a42-4025-82e1-1a7a29670507\"}],\"uuid\":\"81870b1f-5a42-4025-82e1-1a7a29670507\"}],\"name\":\"WARD NO. 20 (PART):9269\",\"attributes\":[],\"childLocations\":[{\"display\":\"ICDDRB (CHOLERA) HOSPITAL-ISD VITORE (MASJIDPARA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/b5fab836-05dc-487c-9366-d89bed77190d\"}],\"uuid\":\"b5fab836-05dc-487c-9366-d89bed77190d\"},{\"display\":\"NIKETAN-NIKETON:9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/c204ad43-989d-4de8-8cc8-a1a19bc66e5b\"}],\"uuid\":\"c204ad43-989d-4de8-8cc8-a1a19bc66e5b\"},{\"display\":\"SAATTALA-12 TOLAR SHAMNE (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/04adae36-2964-430e-9bd9-9899961e0521\"}],\"uuid\":\"04adae36-2964-430e-9bd9-9899961e0521\"},{\"display\":\"SAATTALA-BAZAR GATE (VANGA BARI):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/7b1f5adb-c6c8-429b-aae8-9d001673bb9a\"}],\"uuid\":\"7b1f5adb-c6c8-429b-aae8-9d001673bb9a\"},{\"display\":\"SAATTALA-BMRC SAMNE (MASJIDPARA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/adc4e417-974e-4883-acbf-80dcc1d189ca\"}],\"uuid\":\"adc4e417-974e-4883-acbf-80dcc1d189ca\"},{\"display\":\"SAATTALA-BOTTOLA PASHE (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/3a7a39b6-57d8-4cad-aff1-b62a999e287f\"}],\"uuid\":\"3a7a39b6-57d8-4cad-aff1-b62a999e287f\"},{\"display\":\"SAATTALA-CHOWDHURY PARA(VANGA BARI):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/2daa14bc-42bd-4c49-80bb-eeae9be76e5a\"}],\"uuid\":\"2daa14bc-42bd-4c49-80bb-eeae9be76e5a\"},{\"display\":\"SAATTALA-GAREJER VITOR (MASJIDPARA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/3989777f-db4b-478f-ac2c-7f7cce92d919\"}],\"uuid\":\"3989777f-db4b-478f-ac2c-7f7cce92d919\"},{\"display\":\"SAATTALA-GHORAR MATH (MASJIDPARA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/4703f672-c7fc-423f-a1da-c44cd3a76387\"}],\"uuid\":\"4703f672-c7fc-423f-a1da-c44cd3a76387\"},{\"display\":\"SAATTALA-HINDU PARA(VANGA BARI):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/b9c1b90c-6870-4ff4-8a22-34d501d43bc5\"}],\"uuid\":\"b9c1b90c-6870-4ff4-8a22-34d501d43bc5\"},{\"display\":\"SAATTALA-KABORSTHAN (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/91c5b206-dda3-4339-b0fd-85af2dd240f3\"}],\"uuid\":\"91c5b206-dda3-4339-b0fd-85af2dd240f3\"},{\"display\":\"SAATTALA-KAMUR MORE (MASJIDPARA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/f828612c-681c-4b9d-9f6b-898ab0b9ba18\"}],\"uuid\":\"f828612c-681c-4b9d-9f6b-898ab0b9ba18\"},{\"display\":\"SAATTALA-LAL MATI BAZAR (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/b168aa1a-b4a8-43e7-b4d2-234833b243d9\"}],\"uuid\":\"b168aa1a-b4a8-43e7-b4d2-234833b243d9\"},{\"display\":\"SAATTALA-MADRASAR PISONE (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/1972a1d6-d79e-409f-a748-7411b79d6770\"}],\"uuid\":\"1972a1d6-d79e-409f-a748-7411b79d6770\"},{\"display\":\"SAATTALA-MAS BAZAR PASHE (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/35502349-aedf-49f3-9aeb-37a74e40a519\"}],\"uuid\":\"35502349-aedf-49f3-9aeb-37a74e40a519\"},{\"display\":\"SAATTALA-MONDIRER PASHE (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/7b51c40a-0643-4775-a757-46c5c1214418\"}],\"uuid\":\"7b51c40a-0643-4775-a757-46c5c1214418\"},{\"display\":\"SAATTALA-MOSJIDER PASHE (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/7ab2d144-4fee-4c9b-b765-e9f83cafd805\"}],\"uuid\":\"7ab2d144-4fee-4c9b-b765-e9f83cafd805\"},{\"display\":\"SAATTALA-POLICE FARI (VANGA BARI):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/a4bcf0a7-4d3d-4c28-a13b-57e2c2d24813\"}],\"uuid\":\"a4bcf0a7-4d3d-4c28-a13b-57e2c2d24813\"},{\"display\":\"SAATTALA-PORA BARI (VANGA BARI):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/d6b4e3b2-8e9a-449f-9c43-ab29e9f61d09\"}],\"uuid\":\"d6b4e3b2-8e9a-449f-9c43-ab29e9f61d09\"},{\"display\":\"SAATTALA-PORA BOSTI (STAFF MOHOLLA):9319\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/43217d87-3ea4-4880-8809-a21303fabaf2\"}],\"uuid\":\"43217d87-3ea4-4880-8809-a21303fabaf2\"}]},\"uuid\":\"b5fab836-05dc-487c-9366-d89bed77190d\",\"auditInfo\":{\"creator\":{\"display\":\"brachnpp\",\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/user/88347bc8-2ff0-420b-91a7-c872314603e9\"}],\"uuid\":\"88347bc8-2ff0-420b-91a7-c872314603e9\"},\"dateCreated\":\"2019-10-16T16:33:15.000+0530\"},\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/location/b5fab836-05dc-487c-9366-d89bed77190d\"}],\"display\":\"ICDDRB (CHOLERA) HOSPITAL-ISD VITORE (MASJIDPARA):9319\",\"resourceVersion\":\"2.0\",\"tags\":[{\"display\":\"Village\",\"resourceVersion\":\"1.8\",\"name\":\"Village\",\"retired\":false,\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/c350ce9d-f2a0-4183-a42e-1bb9cfd47fc4\"},{\"rel\":\"full\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/locationtag/c350ce9d-f2a0-4183-a42e-1bb9cfd47fc4?v\u003dfull\"}],\"uuid\":\"c350ce9d-f2a0-4183-a42e-1bb9cfd47fc4\"}],\"name\":\"ICDDRB (CHOLERA) HOSPITAL-ISD VITORE (MASJIDPARA):9319\",\"attributes\":[],\"childLocations\":[]}],\"links\":[{\"rel\":\"self\",\"uri\":\"http://localhost/openmrs/ws/rest/v1/team/teammember/13c83ceb-e8a4-47b3-9388-c3427e48c471\"}],\"subTeams\":[]},\"time\":{\"time\":\"2019-12-12 10:58:16\",\"timeZone\":\"Asia/Kolkata\"},\"user\":{\"username\":\"01313049425\",\"status\":\"Meherina Akter\",\"roles\":[\"Provider\"],\"preferredName\":\"Meherina Akter\",\"baseEntityId\":\"10da681d-e1c4-4a7a-8436-8b10a0d3fd83\",\"attributes\":{\"_PERSON_UUID\":\"fd89ee6c-a786-4733-8bfc-f50c435c6ebb\"},\"voided\":false}}";
	
	public Authentication getAuthenticationAdvisor(HttpServletRequest request) {
		final String authorization = request.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.decode(base64Credentials.getBytes()), Charset.forName("UTF-8"));
			// credentials = username:password
			final String[] values = credentials.split(":", 2);
			
			return new UsernamePasswordAuthenticationToken(values[0], values[1]);
		}
		return null;
	}
	
	public DrishtiAuthenticationProvider getAuthenticationProvider() {
		return opensrpAuthenticationProvider;
	}
	
	public User currentUser(HttpServletRequest request) {
		Authentication a = getAuthenticationAdvisor(request);
		return getAuthenticationProvider().getUser(a, a.getName());
	}
	
	public Time getServerTime() {
		return new Time(Calendar.getInstance().getTime(), TimeZone.getDefault());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/user-details")
	public ResponseEntity<UserDetail> userDetail(@RequestParam("anm-id") String anmIdentifier, HttpServletRequest request) {
		Authentication a = getAuthenticationAdvisor(request);
		User user = opensrpAuthenticationProvider.getDrishtiUser(a, anmIdentifier);
		return new ResponseEntity<>(new UserDetail(user.getUsername(), user.getRoles()), allowOrigin(opensrpSiteUrl), OK);
	}
	
	@RequestMapping("/security/authenticate")
	@ResponseBody
	public ResponseEntity<String> authenticate(HttpServletRequest request) throws JSONException {
		User u = currentUser(request);
		//		String lid = "";
		//		JSONObject tm = null;
		//		try {
		//			tm = openmrsUserService.getTeamMember(u.getAttribute("_PERSON_UUID").toString());
		//			JSONArray locs = tm.getJSONArray("locations");
		//			for (int i = 0; i < locs.length(); i++) {
		//				lid += locs.getJSONObject(i).getString("uuid") + ";;";
		//			}
		//		}
		//		catch (Exception e) {
		//			System.out.println("USER Location info not mapped in team management module. Now trying Person Attribute");
		//		}
		//		if (StringUtils.isEmptyOrWhitespaceOnly(lid)) {
		//			lid = (String) u.getAttribute("Location");
		//			if (StringUtils.isEmptyOrWhitespaceOnly(lid)) {
		//				String lids = (String) u.getAttribute("Locations");
		//
		//				if (lids == null) {
		//					throw new RuntimeException(
		//					        "User not mapped on any location. Make sure that user have a person attribute Location or Locations with uuid(s) of valid OpenMRS Location(s) separated by ;;");
		//				}
		//
		//				lid = lids;
		//			}
		//		}
		//		LocationTree l = openmrsLocationService.getLocationTreeOf(lid.split(";;"));
		//		Map<String, Object> map = new HashMap<>();
		//		map.put("user", u);
		//		try {
		//			CustomQuery customQuery = clientService.findTeamInfo(u.getUsername());
		//
		//			tm.getJSONObject("team").put("teamName", customQuery.getName());
		//			tm.getJSONObject("team").put("display", customQuery.getName());
		//			tm.getJSONObject("team").put("uuid", customQuery.getUuid());
		//
		//			Map<String, Object> tmap = new Gson().fromJson(tm.toString(), new TypeToken<HashMap<String, Object>>() {
		//
		//			}.getType());
		//			map.put("team", tmap);
		//		}
		//		catch (Exception e) {
		//			e.printStackTrace();
		//		}
		//		map.put("locations", l);
		//		Time t = getServerTime();
		//		map.put("time", t);
		//		return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
		
		JSONObject userInfo = new JSONObject(user);
		//		JSONObject userJson = new JSONObject(u);
		//		userInfo.put("user", userJson);
		JSONObject uss = new JSONObject();
		uss.put("username", u.getUsername());
		uss.put("preferredName", u.getPreferredName());
		JSONArray roles = new JSONArray();
		roles.put("Provider");
		
		uss.put("roles", roles);
		JSONObject attri = new JSONObject();
		attri.put("_PERSON_UUID", u.getAttribute("_PERSON_UUID"));
		uss.put("attributes", attri);
		userInfo.put("user", uss);
		
		Time t = getServerTime();
		userInfo.put("time", new JSONObject(t));
		uss.put("status", u.getStatus());
		return new ResponseEntity<>(userInfo.toString(), allowOrigin(opensrpSiteUrl), OK);
	}
	
	@RequestMapping("/security/configuration")
	@ResponseBody
	public ResponseEntity<String> configuration() throws JSONException {
		Map<String, Object> map = new HashMap<>();
		map.put("serverDatetime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
	}
	
	@RequestMapping(value = "/provider/location-tree", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getLocationTree(@RequestParam("username") String username) throws JSONException {
		
		CustomQuery user = eventService.getUser(username);
		List<CustomQuery> treeDTOS = clientService.getProviderLocationTreeByChildRole(user.getId(), childRoleId);
		JSONArray array = new JSONArray();
		try {
			array = locationService.convertLocationTreeToJSON(treeDTOS,
			    (user.getEnable() == null) ? false : user.getEnable());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(array.toString(), OK);
	}
	
	@RequestMapping(value = "/deviceverify/get")
	@ResponseBody
	public ResponseEntity<String> verifyIMEI(@RequestParam("imei") String imei) {
		CustomQuery query = clientService.imeiCheck(imei);
		Boolean imeiAvailable = query.getAvailable();
		//		String res = "false";
		//		HttpResponse op = HttpUtil.get(OPENSRP_BASE_URL+"/user/check-imei?imei="+imei, "", OPENSRP_USER, OPENSRP_PWD);
		//		res = op.body();
		return new ResponseEntity<>(imeiAvailable.toString(), OK);
	}
	
	@RequestMapping(value = "/household/generated-code", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getHouseholdUniqueId(@RequestParam("username") String username,
	                                                   @RequestParam("villageId") String villageId,
	                                                   @RequestParam(value = "device_imei", required = false) String deviceImei,
	                                                   @RequestParam(value = "uuid", required = false) String uuid)
	    throws Exception {
		int[] villageIds = new int[1000];
		String[] ids = villageId.split(",");
		for (int i = 0; i < ids.length; i++) {
			villageIds[i] = Integer.parseInt(ids[i]);
		}
		
		if (villageIds[0] == 0) {
			CustomQuery user = clientService.getUserId(username);
			List<CustomQuery> locationIds = clientService.getVillageByProviderId(user.getId(), childRoleId, locationTagId);
			int i = 0;
			for (CustomQuery locationId : locationIds) {
				villageIds[i++] = locationId.getId();
			}
		}
		JSONArray array = new JSONArray();
		array = eventService.generateHouseholdId(villageIds);
		
		//		HttpResponse op = HttpUtil.get(OPENSRP_BASE_URL+"/household/generated-code?username="+username+"&villageId="+villageId, "", OPENSRP_USER, OPENSRP_PWD);
		//		JSONArray res = new JSONArray(op.body());
		return new ResponseEntity<>(array.toString(), OK);
	}
	
	@RequestMapping(value = "/user/status")
	@ResponseBody
	public ResponseEntity<String> userStatus(HttpServletRequest request) {
		try {
			String username = getStringFilter("username", request);
			String version = getStringFilter("version", request);
			String res = "false";
			CustomQuery query = clientService.getUserStatus(username);
			
			res = query.getEnable() + "";
			try {
				
				clientService.updateAppVersion(username, version);
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseEntity<>(res, OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/user/app-version")
	@ResponseBody
	public ResponseEntity<String> updateAppVersion(@RequestParam("username") String username,
	                                               @RequestParam("version") String version) {
		try {
			clientService.updateAppVersion(username, version);
			return new ResponseEntity<>("success", OK);
		}
		catch (Exception e) {
			return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
