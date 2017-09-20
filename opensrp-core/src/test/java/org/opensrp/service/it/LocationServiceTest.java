package org.opensrp.service.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.BaseIntegrationTest;
import org.opensrp.domain.Location;
import org.opensrp.repository.AllLocations;
import org.opensrp.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.opensrp.util.SampleFullDomainObject.getApiLocation;
import static org.opensrp.util.SampleFullDomainObject.getDomainLocation;
import static org.utils.AssertionUtil.assertTwoDifferentTypeLocationSame;
import static org.utils.CouchDbAccessUtils.addObjectToRepository;

public class LocationServiceTest extends BaseIntegrationTest {

	@Autowired
	public AllLocations allLocations;

	@Autowired
	public LocationService locationService;

	@Before
	public void setUp() {
		allLocations.removeAll();
	}

	@After
	public void cleanUp() {
		allLocations.removeAll();
	}

	@Test
	public void shouldGetAllLocation() {
		Location location = getDomainLocation();
		Location expectedLocation = getDomainLocation();
		expectedLocation.setParentLocation(location);
		List<Location> expectedLocations = Collections.singletonList(expectedLocation);
		addObjectToRepository(expectedLocations, allLocations);

		List<org.opensrp.api.domain.Location> actualLocations = locationService.getAllLocations();

		assertEquals(1, actualLocations.size());
		assertTwoDifferentTypeLocationSame(expectedLocation, actualLocations.get(0));

	}

	@Test
	public void shouldAddLocation() {
		org.opensrp.api.domain.Location expectedLocation = getApiLocation();

		locationService.addLocation(expectedLocation);

		List<Location> actualLocations = allLocations.getAll();
		assertEquals(1, actualLocations.size());
		assertTwoDifferentTypeLocationSame(actualLocations.get(0), expectedLocation);
	}

	@Test
	@Ignore
	public void shouldUpdateLocation() {
		allLocations.add(getDomainLocation().withParentLocation(getDomainLocation()));
		org.opensrp.api.domain.Location expectedLocation = locationService.getAllLocations().get(0);
		expectedLocation.addAttribute("name", "value");

		locationService.updateLocation(expectedLocation);

		List<Location> actualLocations = allLocations.getAll();
		assertEquals(1, actualLocations.size());
		assertTwoDifferentTypeLocationSame(actualLocations.get(0), expectedLocation);
	}

}
