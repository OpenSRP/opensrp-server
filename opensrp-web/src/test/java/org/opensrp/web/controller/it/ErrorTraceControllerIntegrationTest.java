package org.opensrp.web.controller.it;

import org.codehaus.jackson.JsonNode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.domain.ErrorTraceForm;
import org.opensrp.repository.AllErrorTrace;
import org.opensrp.web.rest.it.BaseResourceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.delivery.schedule.util.SameItems.hasSameItemsAs;
import static org.opensrp.web.rest.it.ResourceTestUtility.createErrorTraces;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class ErrorTraceControllerIntegrationTest extends BaseResourceTest {

	private final static String BASE_URL = "/errorhandler";

	@Autowired
	AllErrorTrace allErrorTrace;

	ErrorTraceForm errorTraceForm;

	@Before
	public void setUP() {
		allErrorTrace.removeAll();
		errorTraceForm = new ErrorTraceForm();
	}

	@After
	public void cleanUp() {
		//allErrorTrace.removeAll();
	}

	@Test
	@Ignore
	public void shouldReturnErrorIndex() throws Exception {
		String url = BASE_URL + "/index";
		JsonNode returnedObject = getCallAsJsonNode(url, "", status().isOk());

		System.out.println(returnedObject);

		ModelAndView modelAndView = mapper.treeToValue(returnedObject, ModelAndView.class);
		Map<String, Object> actualModel = modelAndView.getModelMap();
		List<String> actualStatusOptions = mapper
				.treeToValue(mapper.readTree((String) actualModel.get("statusOptions")), List.class);

		assertEquals("home_error", modelAndView.getViewName());
		assertEquals("all", actualModel.get("type"));
		assertEquals(errorTraceForm.getStatusOptions(), hasSameItemsAs(actualStatusOptions));

	}

	@Test
	public void shouldReturnAllError() throws Exception {
		String url = BASE_URL + "/errortrace";
		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(0l, DateTimeZone.UTC), "documentType", "errorType",
				"occuredAt", "stackTrace", "status");
		createErrorTraces(asList(expectedErrorTrace), allErrorTrace);

		JsonNode returnedObject = getCallAsJsonNode(url, "", status().isOk());
		ErrorTrace actualErrorTrace = mapper.treeToValue(returnedObject.get(0), ErrorTrace.class);

		assertEquals(expectedErrorTrace, actualErrorTrace);
	}

	@Test
	@Ignore
	public void shouldReturnAllErrorWithDifferentURl() throws Exception {
		String url = BASE_URL + "/allerrors";
		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(0l, DateTimeZone.UTC), "documentType", "errorType",
				"occuredAt", "stackTrace", "status");
		createErrorTraces(asList(expectedErrorTrace), allErrorTrace);

		JsonNode returnedObject = getCallAsJsonNode(url, "", status().isOk());
		ErrorTrace actualErrorTrace = mapper.treeToValue(returnedObject.get(0), ErrorTrace.class);

		assertEquals(expectedErrorTrace, actualErrorTrace);
	}

	@Test
	public void shouldReturnAllSolvedError() throws Exception {
		String url = BASE_URL + "/solvederrors";
		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "solved", "documentType");
		ErrorTrace unsolvedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "unsolved", "documentType");
		ErrorTrace randomStatusErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "random", "documentType");
		createErrorTraces(asList(expectedErrorTrace, unsolvedErrorTrace, randomStatusErrorTrace), allErrorTrace);

		JsonNode returnedObject = getCallAsJsonNode(url, "", status().isOk());
		ErrorTrace actualErrorTrace = mapper.treeToValue(returnedObject.get(0), ErrorTrace.class);

		assertEquals(expectedErrorTrace, actualErrorTrace);

	}

	@Test
	public void shouldReturnAllUnSolvedError() throws Exception {
		String url = BASE_URL + "/unsolvederrors";
		ErrorTrace solvedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT", "stackTrace",
				"solved", "documentType");
		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "unsolved", "documentType");
		ErrorTrace randomStatusErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "random", "documentType");
		createErrorTraces(asList(expectedErrorTrace, solvedErrorTrace, randomStatusErrorTrace), allErrorTrace);

		JsonNode returnedObject = getCallAsJsonNode(url, "", status().isOk());
		ErrorTrace actualErrorTrace = mapper.treeToValue(returnedObject.get(0), ErrorTrace.class);

		assertEquals(expectedErrorTrace, actualErrorTrace);
	}

	@Test
	public void shouldReturnAllErrorStatus() throws Exception {
		String url = BASE_URL + "/getstatusoptions";

		JsonNode returnedObject = getCallAsJsonNode(url, "", status().isOk());
		List<String> actualStatusOptions = mapper.treeToValue(returnedObject, List.class);

		assertEquals(errorTraceForm.getStatusOptions(), actualStatusOptions);

	}

	@Test
	@Ignore
	public void shouldUpdateErrorTraceStatus() throws Exception {
		String url = BASE_URL + "/update_errortrace";

		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "unsolved", "documentType");
		createErrorTraces(asList(expectedErrorTrace), allErrorTrace);

		expectedErrorTrace = allErrorTrace.getAll().get(0);
		System.out.println(expectedErrorTrace);
		expectedErrorTrace.setStatus("solved");
		ErrorTraceForm errorTraceForm = new ErrorTraceForm();
		errorTraceForm.setErrorTrace(expectedErrorTrace);

		postCallWithJsonContent(url, mapper.writeValueAsString(errorTraceForm), status().isOk());
		ErrorTrace actualErrorTrace = allErrorTrace.get(expectedErrorTrace.getId());

		assertEquals(expectedErrorTrace, actualErrorTrace);
	}

	@Test
	public void shouldUpdateErrorTraceStatusUsingGetMethod() throws Exception {
		String url = BASE_URL + "/update_status";

		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "unsolved", "documentType");
		createErrorTraces(asList(expectedErrorTrace), allErrorTrace);
		expectedErrorTrace = allErrorTrace.getAll().get(0);
		expectedErrorTrace.setStatus("solved");
		String param = "id=" + expectedErrorTrace.getId() + "&status=solved";

		getCallAsJsonNode(url, param, status().isOk());
		ErrorTrace actualErrorTrace = allErrorTrace.get(expectedErrorTrace.getId());

		assertEquals(expectedErrorTrace, actualErrorTrace);
	}

	@Test
	public void shouldGetErrorById() throws Exception {
		String url = BASE_URL + "/viewerror";

		ErrorTrace expectedErrorTrace = new ErrorTrace(new DateTime(DateTimeZone.UTC), "errorType", "occuredAT",
				"stackTrace", "unsolved", "documentType");
		createErrorTraces(asList(expectedErrorTrace), allErrorTrace);
		expectedErrorTrace = allErrorTrace.getAll().get(0);

		String param = "id=" + expectedErrorTrace.getId();

		JsonNode returnedObject = getCallAsJsonNode(url, param, status().isOk());
		ErrorTraceForm actualErrorTraceForm = mapper.treeToValue(returnedObject, ErrorTraceForm.class);
		assertEquals(expectedErrorTrace, actualErrorTraceForm.getErrorTrace());
	}


}
