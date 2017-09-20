package org.opensrp.web.controller.it;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import org.opensrp.web.rest.it.BaseResourceTest;
import org.opensrp.web.utils.TestResourceLoader;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class FormDownloadIntegrationTest extends BaseResourceTest {

	private String BASE_URL = "/form/";

	@Test
	public void shouldFetchAllAvailableVersion() throws Exception {
		String url = "latest-form-versions";

		JsonNode actualObj = getCallAsJsonNode(BASE_URL + url, "", status().isOk());

		assertEquals(currentFormVersions(), actualObj);
	}

	@Test
	public void shouldDownloadCurrentChildFollowupForms() throws Exception {
		String url = "form-files";

		byte[] responseZipFile = getCallAsByeArray(BASE_URL + url, "formDirName=child_followup", status().isOk());
		byte[] expectedZipFile = new TestResourceLoader().getFormDirectoryAsZip("child_followup");

		assertArrayEquals(expectedZipFile, responseZipFile);
	}

	@Test(expected = NestedServletException.class)
	public void shouldThrowExceptionForInvalidFormDirectoryName() throws Exception {
		String url = "form-files";
		getCallAsByeArray(BASE_URL + url, "formDirName=invalid", status().isOk());
	}

	/**
	 * Change the return type if any form name, directory or version changes.
	 *
	 * @return JsonNode
	 * @throws IOException
	 */
	private JsonNode currentFormVersions() throws IOException {
		String currentFormVersionList = "{\"formVersions\":[{\"formName\":\"Offsite_Child_Vaccination_Followup\",\"formDirName\":\"offsite_child_followup\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"Woman_TT_Followup_Form\",\"formDirName\":\"woman_followup\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"Child_Vaccination_Followup\",\"formDirName\":\"child_followup\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"Child_Vaccination_Enrollment\",\"formDirName\":\"child_enrollment\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"Vaccine_Stock_Position\",\"formDirName\":\"vaccine_stock_position\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"FWNewH\",\"formDirName\":\"new_household_registration\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"Woman_TT_Enrollment_Form\",\"formDirName\":\"woman_enrollment\",\"formDataDefinitionVersion\":\"1\"},{\"formName\":\"Offsite_Woman_Followup_Form\",\"formDirName\":\"offsite_woman_followup\",\"formDataDefinitionVersion\":\"1\"}]}";
		return mapper.readTree(currentFormVersionList);
	}
}
