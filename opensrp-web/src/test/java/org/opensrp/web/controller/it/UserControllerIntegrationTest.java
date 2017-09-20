package org.opensrp.web.controller.it;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.api.domain.Time;
import org.opensrp.domain.Location;
import org.opensrp.domain.User;
import org.opensrp.web.rest.it.BaseResourceTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.server.MvcResult;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends BaseResourceTest {

	@Value("#{opensrp['openmrs.url']}")
	private String opensrpSiteUrl;

	@Value("#{opensrp['openmrs.username']}")
	private String rootUserName;

	@Value("#{opensrp['openmrs.password']}")
	private String rootPassword;

	private String sessionCookie;
	@Test
	public void setUp() throws Exception {
	/*	String url = "http://192.168.22.152:8080/openmrs/loginServlet";
		Map<String, String> parameterForRootLogIn = new HashMap<>();
		parameterForRootLogIn.put("uname", rootUserName);
		parameterForRootLogIn.put("pw", rootPassword);
		MvcResult result = postCallWithFormUrlEncode(url, parameterForRootLogIn, status().isOk());
		Cookie[] cookies = result.getResponse().getCookies();
		sessionCookie = result.getResponse().getCookie("JSESSIONID").getValue();
		System.out.println(sessionCookie);*/
	}

	@Test
	@Ignore
	public void shouldAuthenticateUserWithValidUsernameAndPassword() throws Exception {
		String url = "/security/authenticate";
		JsonNode returnedJsonNode = postCallWithBasicAuthorizationHeader(url, "sumon", "Sumon@123", status().isOk());
		System.out.println(returnedJsonNode);
		//{"time":{"time":"2017-08-21 14:38:41","timeZone":"Asia/Dhaka"},"locations":{"locationsHierarchy":{"map":{"44221e79-b3f0-496f-9d3c-467216fa1d53":{"id":"44221e79-b3f0-496f-9d3c-467216fa1d53","label":"Bangladesh","node":{"locationId":"44221e79-b3f0-496f-9d3c-467216fa1d53","name":"Bangladesh","tags":["Country"],"voided":false},"children":{"69e70c98-3a55-4a5c-9808-1d763a243cd7":{"id":"69e70c98-3a55-4a5c-9808-1d763a243cd7","label":"Rangpur","node":{"locationId":"69e70c98-3a55-4a5c-9808-1d763a243cd7","name":"Rangpur","parentLocation":{"locationId":"44221e79-b3f0-496f-9d3c-467216fa1d53","name":"Bangladesh","voided":false},"tags":["Division"],"voided":false},"children":{"fde679fa-24ee-4bcc-a898-fe0e8734c5d5":{"id":"fde679fa-24ee-4bcc-a898-fe0e8734c5d5","label":"Gaibandha","node":{"locationId":"fde679fa-24ee-4bcc-a898-fe0e8734c5d5","name":"Gaibandha","parentLocation":{"locationId":"69e70c98-3a55-4a5c-9808-1d763a243cd7","name":"Rangpur","parentLocation":{"locationId":"44221e79-b3f0-496f-9d3c-467216fa1d53","name":"Bangladesh","voided":false},"voided":false},"tags":["District"],"voided":false},"children":{"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953":{"id":"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953","label":"Gaibandha Sadar","node":{"locationId":"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953","name":"Gaibandha Sadar","parentLocation":{"locationId":"fde679fa-24ee-4bcc-a898-fe0e8734c5d5","name":"Gaibandha","parentLocation":{"locationId":"69e70c98-3a55-4a5c-9808-1d763a243cd7","name":"Rangpur","voided":false},"voided":false},"tags":["Upazilla"],"voided":false},"children":{"128eae74-7f9f-4bd1-8617-ab246907168a":{"id":"128eae74-7f9f-4bd1-8617-ab246907168a","label":"Kuptala","node":{"locationId":"128eae74-7f9f-4bd1-8617-ab246907168a","name":"Kuptala","parentLocation":{"locationId":"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953","name":"Gaibandha Sadar","parentLocation":{"locationId":"fde679fa-24ee-4bcc-a898-fe0e8734c5d5","name":"Gaibandha","voided":false},"voided":false},"tags":["Union"],"voided":false},"children":{"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb":{"id":"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb","label":"Ward-1","node":{"locationId":"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb","name":"Ward-1","parentLocation":{"locationId":"128eae74-7f9f-4bd1-8617-ab246907168a","name":"Kuptala","parentLocation":{"locationId":"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953","name":"Gaibandha Sadar","voided":false},"voided":false},"tags":["Ward"],"voided":false},"children":{"56b7e88b-7eae-4288-a3be-c4f1275e6f78":{"id":"56b7e88b-7eae-4288-a3be-c4f1275e6f78","label":"1-KA","node":{"locationId":"56b7e88b-7eae-4288-a3be-c4f1275e6f78","name":"1-KA","parentLocation":{"locationId":"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb","name":"Ward-1","parentLocation":{"locationId":"128eae74-7f9f-4bd1-8617-ab246907168a","name":"Kuptala","voided":false},"voided":false},"tags":["Subunit"],"voided":false},"children":{"4309271d-0443-44cd-901b-c72acbe578e9":{"id":"4309271d-0443-44cd-901b-c72acbe578e9","label":"Chapadaha gorer matha","node":{"locationId":"4309271d-0443-44cd-901b-c72acbe578e9","name":"Chapadaha gorer matha","parentLocation":{"locationId":"56b7e88b-7eae-4288-a3be-c4f1275e6f78","name":"1-KA","parentLocation":{"locationId":"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb","name":"Ward-1","voided":false},"voided":false},"tags":["Mouzapara"],"voided":false},"parent":"56b7e88b-7eae-4288-a3be-c4f1275e6f78"}},"parent":"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb"}},"parent":"128eae74-7f9f-4bd1-8617-ab246907168a"}},"parent":"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953"}},"parent":"fde679fa-24ee-4bcc-a898-fe0e8734c5d5"}},"parent":"69e70c98-3a55-4a5c-9808-1d763a243cd7"}},"parent":"44221e79-b3f0-496f-9d3c-467216fa1d53"}}}},"parentChildren":{"3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb":["56b7e88b-7eae-4288-a3be-c4f1275e6f78"],"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953":["128eae74-7f9f-4bd1-8617-ab246907168a"],"44221e79-b3f0-496f-9d3c-467216fa1d53":["69e70c98-3a55-4a5c-9808-1d763a243cd7"],"56b7e88b-7eae-4288-a3be-c4f1275e6f78":["4309271d-0443-44cd-901b-c72acbe578e9"],"fde679fa-24ee-4bcc-a898-fe0e8734c5d5":["d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953"],"69e70c98-3a55-4a5c-9808-1d763a243cd7":["fde679fa-24ee-4bcc-a898-fe0e8734c5d5"],"128eae74-7f9f-4bd1-8617-ab246907168a":["3ebdd7da-9ed7-4b5a-82be-cd58c71d2ffb"]}}},"team":{"person":{"birthdateEstimated":false,"preferredName":{"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/person/46a3c163-3eb8-4b08-be62-e1400bdae7f5/name/96046948-db80-4a6e-a192-bfbe3c01b1ad"}],"display":"sumon sumon","uuid":"96046948-db80-4a6e-a192-bfbe3c01b1ad"},"deathdateEstimated":false,"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/person/46a3c163-3eb8-4b08-be62-e1400bdae7f5"},{"rel":"full","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/person/46a3c163-3eb8-4b08-be62-e1400bdae7f5?v\u003dfull"}],"display":"sumon sumon","resourceVersion":"1.11","voided":false,"gender":"M","uuid":"46a3c163-3eb8-4b08-be62-e1400bdae7f5","attributes":[],"dead":false},"patients":[],"teamMemberId":1.0,"location":[{"tags":[{"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/locationtag/31aecf10-bb74-4ddf-ab0e-7a80ee3639b5"}],"display":"Mouzapara","uuid":"31aecf10-bb74-4ddf-ab0e-7a80ee3639b5"}],"retired":false,"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/4309271d-0443-44cd-901b-c72acbe578e9"},{"rel":"full","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/4309271d-0443-44cd-901b-c72acbe578e9?v\u003dfull"}],"display":"Chapadaha gorer matha","parentLocation":{"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/56b7e88b-7eae-4288-a3be-c4f1275e6f78"}],"display":"1-KA","uuid":"56b7e88b-7eae-4288-a3be-c4f1275e6f78"},"resourceVersion":"1.9","childLocations":[],"name":"Chapadaha gorer matha","attributes":[],"uuid":"4309271d-0443-44cd-901b-c72acbe578e9"}],"resourceVersion":"1.8","isTeamLead":false,"uuid":"aaa237aa-54ea-451d-b961-73efada42462","team":{"teamName":"dghs-fwa","teamIdentifier":"123","location":{"tags":[{"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/locationtag/90237680-0772-497a-a88a-98fb8d01835b"}],"display":"Upazilla","uuid":"90237680-0772-497a-a88a-98fb8d01835b"}],"retired":false,"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953"},{"rel":"full","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953?v\u003dfull"}],"display":"Gaibandha Sadar","parentLocation":{"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/fde679fa-24ee-4bcc-a898-fe0e8734c5d5"}],"display":"Gaibandha","uuid":"fde679fa-24ee-4bcc-a898-fe0e8734c5d5"},"resourceVersion":"1.9","childLocations":[{"links":[{"rel":"self","uri":"http://192.168.22.152:8080/openmrs/ws/rest/v1/location/128eae74-7f9f-4bd1-8617-ab246907168a"}],"display":"Kuptala","uuid":"128eae74-7f9f-4bd1-8617-ab246907168a"}],"description":"Upazilla","name":"Gaibandha Sadar","attributes":[],"uuid":"d98a6aab-a5fd-4c3f-bcf4-9ff1a93da953"},"resourceVersion":"1.8","dateCreated":"2017-07-17T14:15:18.000+0600","display":"dghs-fwa","uuid":"1081acf6-a000-4a5c-889e-1d475264c69b"},"identifier":"4001"},"user":{"username":"sumon","status":"sumon sumon","roles":["Provider"],"preferredName":"sumon sumon","baseEntityId":"55bf584f-9aae-4c36-bce6-e2661d2e145f","attributes":{"_PERSON_UUID":"46a3c163-3eb8-4b08-be62-e1400bdae7f5"},"voided":false}}
		User expectedUser = mapper.treeToValue(returnedJsonNode.get("user"), User.class);
		Location location = mapper.treeToValue(returnedJsonNode.get("locations"), Location.class);
		Map<String, String> team = mapper.treeToValue(returnedJsonNode.get("team"), Map.class);
		Time expectedTime = mapper.treeToValue(returnedJsonNode.get("time"), Time.class);
	}
}
