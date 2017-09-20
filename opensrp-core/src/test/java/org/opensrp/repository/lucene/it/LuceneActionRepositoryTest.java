package org.opensrp.repository.lucene.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.dto.ActionData;
import org.opensrp.dto.AlertStatus;
import org.opensrp.repository.lucene.LuceneActionRepository;
import org.opensrp.scheduler.Action;
import org.opensrp.scheduler.repository.AllActions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Arrays.asList;
import static org.opensrp.util.SampleFullDomainObject.*;
import static org.utils.AssertionUtil.assertTwoListAreSameIgnoringOrder;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class LuceneActionRepositoryTest extends BaseIntegrationTest {

	@Autowired
	private AllActions allActions;

	@Autowired
	private LuceneActionRepository luceneActionRepository;

	@Before
	public void setUp() {
		allActions.removeAll();
	}

	@After
	public void cleanUp() {
		//allActions.removeAll();
	}

	@Test
	public void shouldFindByAllCriteria() {
		ActionData actionData = ActionData
				.createAlert("beneficiaryType", "scheduleName", "visitCode", AlertStatus.normal, EPOCH_DATE_TIME,
						EPOCH_DATE_TIME);
		Action expectedAction = new Action(BASE_ENTITY_ID, PROVIDER_ID, actionData);
		Action expectedAction2 = new Action(DIFFERENT_BASE_ENTITY_ID, DIFFERENT_BASE_ENTITY_ID, actionData);
		List<Action> expectedActions = asList(expectedAction, expectedAction2);
		addObjectToRepository(expectedActions, allActions);

		String teamIds = PROVIDER_ID + "," + DIFFERENT_BASE_ENTITY_ID;
		List<Action> actualActions = luceneActionRepository
				.getByCriteria(teamIds, PROVIDER_ID, EPOCH_DATE_TIME.getMillis(), null, "desc", 100);

		assertTwoListAreSameIgnoringOrder(expectedActions, actualActions);
	}

	//TODO: fix source
	@Test(expected = NullPointerException.class)
	public void throwExceptionIfNoSortOrderSpecified() {
		ActionData actionData = ActionData
				.createAlert("beneficiaryType", "scheduleName", "visitCode", AlertStatus.normal, EPOCH_DATE_TIME,
						EPOCH_DATE_TIME);
		Action expectedAction = new Action(BASE_ENTITY_ID, PROVIDER_ID, actionData);
		Action expectedAction2 = new Action(DIFFERENT_BASE_ENTITY_ID, DIFFERENT_BASE_ENTITY_ID, actionData);
		List<Action> expectedActions = asList(expectedAction, expectedAction2);
		addObjectToRepository(expectedActions, allActions);

		String teamIds = PROVIDER_ID + "," + DIFFERENT_BASE_ENTITY_ID;
		luceneActionRepository.getByCriteria(teamIds, PROVIDER_ID, EPOCH_DATE_TIME.getMillis(), null, null, 100);
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowExceptionIfNoFilterAdded() {
		luceneActionRepository.getByCriteria(null, null, null, null, null, 100);
	}
}
