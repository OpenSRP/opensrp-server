package org.opensrp.web.controller.it;

import org.codehaus.jackson.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.form.FormSubmissionDTO;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.repository.AllFormSubmissions;
import org.opensrp.form.service.FormSubmissionConverter;
import org.opensrp.repository.MultimediaRepository;
import org.opensrp.web.rest.it.BaseResourceTest;
import org.opensrp.web.utils.TestResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class FormSubmissionIntegrationTest extends BaseResourceTest {

	@Autowired
	private AllFormSubmissions allFormSubmissions;

	@Autowired
	private MultimediaRepository multimediaRepository;

	private TestResourceLoader testResourceLoader;

	@Before
	public void setUp() throws IOException {
		allFormSubmissions.removeAll();
		multimediaRepository.removeAll();
		testResourceLoader = new TestResourceLoader();
	}

	@After
	public void cleanUp() {
		allFormSubmissions.removeAll();
		multimediaRepository.removeAll();
	}

	@Test
	public void shouldFetchFormBasedOnAnmIdAndTimestamp() throws Exception {
		String url = "/form-submissions";
		FormSubmission expectedFormSubmission = testResourceLoader.getFormSubmissionFor("new_household_registration", 1);
		String anmId = expectedFormSubmission.anmId();
		allFormSubmissions.add(expectedFormSubmission);

		JsonNode responseObject = getCallAsJsonNode(url,
				"anm-id=" + anmId + "&timestamp=" + (expectedFormSubmission.serverVersion() - 1l), status().isOk());
		FormSubmissionDTO actualFormSubmissionDto = mapper.treeToValue(responseObject.get(0), FormSubmissionDTO.class);

		assertEquals(FormSubmissionConverter.from(expectedFormSubmission), actualFormSubmissionDto);
	}

	@Test
	public void shouldFetchFormBasedOnTimestamp() throws Exception {
		String url = "/all-form-submissions";
		FormSubmission expectedFormSubmission = testResourceLoader.getFormSubmissionFor("new_household_registration", 1);

		allFormSubmissions.add(expectedFormSubmission);

		JsonNode responseObject = getCallAsJsonNode(url, "&timestamp=" + (expectedFormSubmission.serverVersion() - 1l),
				status().isOk());
		FormSubmissionDTO actualFormSubmissionDto = mapper.treeToValue(responseObject.get(0), FormSubmissionDTO.class);

		assertEquals(FormSubmissionConverter.from(expectedFormSubmission), actualFormSubmissionDto);
	}

	/**
	 * This test if a form is submitted to openSRP db successfully.
	 *
	 * @throws Exception
	 */
	@Test
	public void shouldSubmitForm() throws Exception {
		String url = "/form-submissions";
		FormSubmission expectedFormSubmission = testResourceLoader.getFormSubmissionFor("new_household_registration", 10);
		// This call is made to generate mapOfFieldsByName field in FormData.class.
		// This field is kept as json string inside formInstance field in FormSubmissionDTO.class
		// So without generating this field expected FormSubmission won't match actual FormSubmission
		expectedFormSubmission.instance().getField("id");

		FormSubmissionDTO expectedFormSubmissionDto = FormSubmissionConverter.from(expectedFormSubmission);

		assertEquals(0, allFormSubmissions.getAll().size());
		String parameterObject = mapper.writeValueAsString(asList(expectedFormSubmissionDto));
		postCallWithJsonContent(url, parameterObject, status().isCreated());

		//Give time to run motech event
		TimeUnit.SECONDS.sleep(10);

		List<FormSubmission> formSubmissions = allFormSubmissions.getAll();
		FormSubmission actualFormSubmission = formSubmissions.get(0);

		expectedFormSubmission.setServerVersion(0);
		actualFormSubmission.setServerVersion(0);


		assertEquals(1, formSubmissions.size());
		assertEquals(expectedFormSubmission, actualFormSubmission);

	}

	@Test
	public void shouldReturnBadRequestForEmptyFormInSubmitForm() throws Exception {
		String url = "/form-submissions";
		String parameterObject = mapper.writeValueAsString(new ArrayList<FormSubmissionDTO>());
		JsonNode responseObject = postCallWithJsonContent(url, parameterObject, status().isBadRequest());
	}

	@Test
	public void shouldGetMultimediaBasedOnProviderId() throws Exception {
		String url = "/multimedia-file";
		Multimedia multimedia = new Multimedia("caseId", "providerId", "contentType", "filePath", "fileCategory");
		multimediaRepository.add(multimedia);
		MultimediaDTO expectedMultimediaDto = new MultimediaDTO(multimedia.getCaseId(), multimedia.getProviderId(),
				multimedia.getContentType(), multimedia.getFilePath(), multimedia.getFileCategory());

		JsonNode responseObject = getCallAsJsonNode(url, "anm-id=providerId", status().isOk());

		MultimediaDTO actualMultimediaDTO = mapper.treeToValue(responseObject.get(0), MultimediaDTO.class);

		assertEquals(expectedMultimediaDto, actualMultimediaDTO);

	}

}
