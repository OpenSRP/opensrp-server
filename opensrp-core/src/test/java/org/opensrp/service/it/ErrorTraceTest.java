package org.opensrp.service.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.repository.AllErrorTrace;
import org.opensrp.service.ErrorTraceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.opensrp.util.SampleFullDomainObject.EPOCH_DATE_TIME;
import static org.opensrp.util.SampleFullDomainObject.getErrorTrace;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class ErrorTraceTest extends BaseIntegrationTest {

	@Autowired
	public AllErrorTrace allErrorTrace;

	@Autowired
	public ErrorTraceService errorTraceService;

	@Before
	public void setUp() {
		allErrorTrace.removeAll();
	}

	@After
	public void cleanUp() {
		allErrorTrace.removeAll();
	}

	@Test
	public void shouldGetErrorTraceById() {
		addObjectToRepository(Collections.singletonList(getErrorTrace()), allErrorTrace);
		ErrorTrace expectedErrorTrace = allErrorTrace.getAll().get(0);

		ErrorTrace actualErrorTrace = errorTraceService.getError(expectedErrorTrace.getId());

		assertEquals(expectedErrorTrace, actualErrorTrace);
	}

	@Test
	public void shouldGetAllUnsolvedErrors() {
		ErrorTrace errorTrace = getErrorTrace();
		errorTrace.setStatus("unsolved");
		ErrorTrace errorTrace1 = getErrorTrace();
		errorTrace1.setStatus("unsolved");
		ErrorTrace invalidErrorTrace = getErrorTrace();
		invalidErrorTrace.setStatus("solved");
		addObjectToRepository(asList(errorTrace, errorTrace1, invalidErrorTrace), allErrorTrace);
		List<ErrorTrace> expectedErrorTraces = asList(errorTrace, errorTrace1);

		List<ErrorTrace> actualErrorTraces = errorTraceService.getAllUnsolvedErrors();

		assertTwoListAreSameIgnoringOrder(expectedErrorTraces, actualErrorTraces);
	}

	@Test
	public void shouldReturnNullIfNoUnsolvedErrorFound() {
		assertNull(errorTraceService.getAllUnsolvedErrors());
	}

	@Test
	public void shouldGetAllSolvedErrors() {
		ErrorTrace errorTrace = getErrorTrace();
		errorTrace.setStatus("solved");
		ErrorTrace errorTrace1 = getErrorTrace();
		errorTrace1.setStatus("solved");
		ErrorTrace invalidErrorTrace = getErrorTrace();
		invalidErrorTrace.setStatus("unsolved");
		addObjectToRepository(asList(errorTrace, errorTrace1, invalidErrorTrace), allErrorTrace);
		List<ErrorTrace> expectedErrorTraces = asList(errorTrace, errorTrace1);

		List<ErrorTrace> actualErrorTraces = errorTraceService.getAllSolvedErrors();

		assertTwoListAreSameIgnoringOrder(expectedErrorTraces, actualErrorTraces);
	}

	@Test
	public void shouldReturnNullIfNoSolvedErrorFound() {
		assertNull(errorTraceService.getAllSolvedErrors());
	}

	@Test
	public void shouldGetAllAllErrors() {
		ErrorTrace errorTrace = getErrorTrace();
		errorTrace.setStatus("unsolved");
		ErrorTrace errorTrace1 = getErrorTrace();
		errorTrace1.setStatus("unsolved");
		ErrorTrace errorTrace2 = getErrorTrace();
		errorTrace2.setStatus("solved");
		List<ErrorTrace> expectedErrorTraces = asList(errorTrace, errorTrace1, errorTrace2);

		assertEquals(0, allErrorTrace.getAll().size());
		addObjectToRepository(expectedErrorTraces, allErrorTrace);
		List<ErrorTrace> actualErrorTraces = errorTraceService.getAllErrors();

		assertTwoListAreSameIgnoringOrder(expectedErrorTraces, actualErrorTraces);
	}

	@Test
	public void shouldReturnNullIfNoErrorFound() {
		assertNull(errorTraceService.getAllErrors());
	}

	@Test
	public void shouldAddErrorTrace() {
		ErrorTrace expectedErrorTrace = getErrorTrace();

		errorTraceService.addError(expectedErrorTrace);

		List<ErrorTrace> actualErrorTraces = allErrorTrace.getAll();

		assertEquals(1, actualErrorTraces.size());
		assertEquals(expectedErrorTrace, actualErrorTraces.get(0));
	}

	@Test
	public void shouldLogError() {
		ErrorTrace expectedErrorTrace = getErrorTrace();
		expectedErrorTrace.setOccurredAt(null);
		expectedErrorTrace.setStatus(null);
		expectedErrorTrace.setRetryUrl("retryUrl");

		errorTraceService.log(expectedErrorTrace.getErrorType(), expectedErrorTrace.getDocumentType(),
				expectedErrorTrace.getRecordId(), expectedErrorTrace.getStackTrace(), "retryUrl");

		List<ErrorTrace> actualErrorTraces = allErrorTrace.getAll();

		assertEquals(1, actualErrorTraces.size());
		assertNotNull(actualErrorTraces.get(0).getDateOccurred());
		actualErrorTraces.get(0).setDateOccurred(EPOCH_DATE_TIME);
		assertEquals(expectedErrorTrace, actualErrorTraces.get(0));

	}

	@Test
	public void shouldUpdateError() {
		addObjectToRepository(Collections.singletonList(getErrorTrace()), allErrorTrace);
		ErrorTrace updatedErrorTrace = allErrorTrace.getAll().get(0);
		updatedErrorTrace.setRetryUrl("retryUrl");
		updatedErrorTrace.setStatus("solved");

		errorTraceService.updateError(updatedErrorTrace);

		List<ErrorTrace> actualErrorTraces = allErrorTrace.getAll();

		assertEquals(1, actualErrorTraces.size());
		assertEquals(updatedErrorTrace, actualErrorTraces.get(0));
	}
}
