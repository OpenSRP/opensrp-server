package org.opensrp.web.rest.it;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.MvcResult;
import org.springframework.test.web.server.ResultMatcher;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestWebContextLoader.class, locations = {
		"classpath:spring/applicationContext-opensrp-web.xml" })
public abstract class BaseResourceTest {

	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;

	protected ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setMockMvc() {
		this.mockMvc = MockMvcBuilders.webApplicationContextSetup(this.wac).build();
	}

	protected JsonNode getCallAsJsonNode(String url, String parameter, ResultMatcher expectedStatus) throws Exception {
		String finalUrl = url;
		if (!parameter.isEmpty()) {
			finalUrl = finalUrl + "?" + parameter;
		}

		MvcResult mvcResult = this.mockMvc.perform(get(finalUrl).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(expectedStatus).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		if (responseString.isEmpty()) {
			return null;
		}
		JsonNode actualObj = mapper.readTree(responseString);
		return actualObj;
	}

	protected byte[] getCallAsByeArray(String url, String parameterQuery, ResultMatcher expectedStatus) throws Exception {
		String finalUrl = url;
		if (!parameterQuery.isEmpty()) {
			finalUrl = finalUrl + "?" + parameterQuery;
		}

		MvcResult mvcResult = this.mockMvc.perform(get(finalUrl).accept(MediaType.APPLICATION_JSON))
				.andExpect(expectedStatus).andReturn();

		return mvcResult.getResponse().getContentAsByteArray();
	}

	protected JsonNode postCallWithJsonContent(String url, String data, ResultMatcher expectedStatus) throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(
				post(url).contentType(MediaType.APPLICATION_JSON).body(data.getBytes()).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(expectedStatus).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		if (responseString.isEmpty()) {
			return null;
		}
		JsonNode actualObj = mapper.readTree(responseString);
		return actualObj;
	}

	protected MvcResult postCallWithFormUrlEncode(String url, Map<String, String> parameters, ResultMatcher expectedStatus)
			throws Exception {
		List<BasicNameValuePair> paramList = new ArrayList<>();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		MvcResult mvcResult = this.mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
				.body(EntityUtils.toString(new UrlEncodedFormEntity(paramList)).getBytes())
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(expectedStatus).andReturn();

		return mvcResult;
	}

	protected JsonNode postCallWithBasicAuthorizationHeader(String url, String userName, String password,
	                                                        ResultMatcher expectedStatus) throws Exception {
		String basicAuthCredentials = new String(Base64.encode((userName + ":" + password).getBytes()));
		System.out.println(basicAuthCredentials);
		MvcResult mvcResult = this.mockMvc.perform(
				post(url).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Basic " + basicAuthCredentials)
						.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(expectedStatus).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		if (responseString.isEmpty()) {
			return null;
		}
		JsonNode actualObj = mapper.readTree(responseString);
		return actualObj;
	}

	protected static <T, R extends MotechBaseRepository> void addObjectToRepository(List<T> objectList, R repository) {
		for (T object : objectList) {
			repository.add(object);
		}
	}

	protected <T> List<T> createObjectListFromJson(JsonNode jsonList, Class<T> classOfT) throws IOException {
		final List<T> objectList = new ArrayList<>();
		for (int i = 0; i < jsonList.size(); i++) {
			T object = mapper.treeToValue(jsonList.get(i), classOfT);
			objectList.add(object);
		}
		return objectList;
	}

	protected <T> void assertTwoListAreSameIgnoringOrder(List<T> expectedList, List<T> actualList) {
		assertEquals(expectedList.size(), actualList.size());
		assertTrue("expected: " + expectedList.toString() + "\n" + "actual: " + actualList.toString(),
				expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
	}

	//TODO: bug in `assetClassHasAllRequiredFields` method. should check base class for property.
	protected <T> void assetClassHasAllRequiredFields(Class<T> classOfT, List<String> requiredProperties) {
		Field[] allFields = classOfT.getDeclaredFields();
		List<String> nameOfFieldsOfT = new ArrayList<>();
		for (Field field : allFields) {
			if (!field.isSynthetic()) {
				nameOfFieldsOfT.add(field.getName().trim().toLowerCase());
			}
		}

		for (String requiredProperty : requiredProperties) {
			assertTrue("'" + requiredProperty + "' not Found.",
					nameOfFieldsOfT.contains(requiredProperty.trim().toLowerCase()));
		}
	}
}
