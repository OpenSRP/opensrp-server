package org.opensrp.repository.lucene.it;

import com.sun.org.apache.regexp.internal.RE;
import org.ektorp.DbAccessException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.Report;
import org.opensrp.repository.AllReports;
import org.opensrp.repository.lucene.LuceneReportRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class LuceneReportRepositoryTest extends BaseIntegrationTest {

	@Autowired
	public AllReports allReports;

	@Autowired
	public LuceneReportRepository luceneReportRepository;

	@Before
	public void setUp() {
		allReports.removeAll();
	}

	@After
	public void cleanUp() {
		//allReports.removeAll();
	}

	@Test
	public void shouldSearchUsingAllCriteria() {
		Report expectedReport = getReport();
		expectedReport.setDateCreated(EPOCH_DATE_TIME);
		expectedReport.setDateEdited(EPOCH_DATE_TIME);
		addObjectToRepository(Collections.singletonList(expectedReport), allReports);

		List<Report> actualReports = luceneReportRepository
				.getByCriteria(BASE_ENTITY_ID, EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC), REPORT_TYPE, PROVIDER_ID,
						LOCATION_ID, EPOCH_DATE_TIME, new DateTime(DateTimeZone.UTC));

		assertEquals(1, actualReports.size());
		assertEquals(expectedReport, actualReports.get(0));
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionIfNoParameterSpecified() {
		Report expectedReport = getReport();
		expectedReport.setDateCreated(EPOCH_DATE_TIME);
		expectedReport.setDateEdited(EPOCH_DATE_TIME);
		addObjectToRepository(Collections.singletonList(expectedReport), allReports);

		luceneReportRepository.getByCriteria(null, null, null, null, null, null, null, null);
	}

	@Test
	public void findByAllCriteriaV2() {
		Report expectedReport = getReport();
		expectedReport.setDateCreated(EPOCH_DATE_TIME);
		expectedReport.setDateEdited(EPOCH_DATE_TIME);
		expectedReport.setServerVersion(EPOCH_DATE_TIME.getMillis());

		Report expectedReport2 = getReport();
		expectedReport2.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		expectedReport2.setDateCreated(EPOCH_DATE_TIME);
		expectedReport2.setDateEdited(EPOCH_DATE_TIME);
		expectedReport2.setServerVersion(EPOCH_DATE_TIME.getMillis());

		Report expectedReport3 = getReport();
		expectedReport3.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		expectedReport3.setDateCreated(EPOCH_DATE_TIME);
		expectedReport3.setDateEdited(EPOCH_DATE_TIME);
		expectedReport3.setProviderId(DIFFERENT_BASE_ENTITY_ID);
		expectedReport3.setServerVersion(EPOCH_DATE_TIME.getMillis());
		List<Report> expectedReports = asList(expectedReport, expectedReport2, expectedReport3);
		addObjectToRepository(expectedReports, allReports);

		String teamIds = PROVIDER_ID + "," + DIFFERENT_BASE_ENTITY_ID;
		String baseEntityIds = BASE_ENTITY_ID + "," + DIFFERENT_BASE_ENTITY_ID;
		List<Report> actualReports = luceneReportRepository
				.getByCriteria(teamIds, PROVIDER_ID, LOCATION_ID, baseEntityIds, EPOCH_DATE_TIME.getMillis(), null, "DESC",
						100);

		assertTwoListAreSameIgnoringOrder(expectedReports, actualReports);
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowExceptionIfSortOrderNotSpecifiedInV2() {
		Report expectedReport = getReport();
		expectedReport.setDateCreated(EPOCH_DATE_TIME);
		expectedReport.setDateEdited(EPOCH_DATE_TIME);
		addObjectToRepository(Collections.singletonList(expectedReport), allReports);
		luceneReportRepository
				.getByCriteria(null, PROVIDER_ID, LOCATION_ID, BASE_ENTITY_ID, EPOCH_DATE_TIME.getMillis(), null, null, 100);
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionIfNoQueryParameterSpecifiedInV2() {
		Report expectedReport = getReport();
		expectedReport.setDateCreated(EPOCH_DATE_TIME);
		expectedReport.setDateEdited(EPOCH_DATE_TIME);
		addObjectToRepository(Collections.singletonList(expectedReport), allReports);
		luceneReportRepository.getByCriteria(null, null, null, null, null, null, null, 100);
	}

	@Test
	public void shouldFindByStringQuery() {
		Report expectedReport = getReport();
		expectedReport.setDateCreated(EPOCH_DATE_TIME);
		expectedReport.setDateEdited(EPOCH_DATE_TIME);
		expectedReport.setServerVersion(EPOCH_DATE_TIME.getMillis());

		Report expectedReport2 = getReport();
		expectedReport2.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		expectedReport2.setDateCreated(EPOCH_DATE_TIME);
		expectedReport2.setDateEdited(EPOCH_DATE_TIME);
		expectedReport2.setServerVersion(EPOCH_DATE_TIME.getMillis());

		Report expectedReport3 = getReport();
		expectedReport3.setBaseEntityId(DIFFERENT_BASE_ENTITY_ID);
		expectedReport3.setDateCreated(EPOCH_DATE_TIME);
		expectedReport3.setDateEdited(EPOCH_DATE_TIME);
		expectedReport3.setProviderId(DIFFERENT_BASE_ENTITY_ID);
		expectedReport3.setServerVersion(EPOCH_DATE_TIME.getMillis());
		List<Report> expectedReports = asList(expectedReport, expectedReport2, expectedReport3);
		addObjectToRepository(expectedReports, allReports);

		String query = "serverVersion:[0 TO 9223372036854775807]AND providerId:(providerId OR differentBaseEntityId)AND locationId:locationId AND (baseEntityId:(baseEntityId OR differentBaseEntityId))";
		List<Report> actualReports = luceneReportRepository.getByCriteria(query);

		assertTwoListAreSameIgnoringOrder(expectedReports, actualReports);
	}

	@Test(expected = DbAccessException.class)
	public void shouldThrowExceptionIfDateCreatedFileNotPresent() {
		Report expectedReport = new Report();
		expectedReport.setBaseEntityId(BASE_ENTITY_ID);

		addObjectToRepository(Collections.singletonList(expectedReport), allReports);

		luceneReportRepository.getByCriteria(BASE_ENTITY_ID, null, null, null, null, null, null, null);
	}
}
