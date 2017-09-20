package org.opensrp.service;

import java.util.ArrayList;
import java.util.List;

import org.opensrp.api.domain.Location;
import org.opensrp.repository.AllLocations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

	private final AllLocations allLocations;

	@Autowired
	public LocationService(AllLocations allLocations) {
		this.allLocations = allLocations;
	}

	public List<Location> getAllLocations() {
		ArrayList<Location> apiLocations = new ArrayList<>();

		List<org.opensrp.domain.Location> locations = allLocations.findAllLocations();

		for (org.opensrp.domain.Location location : locations) {
			org.opensrp.domain.Location parentLocation = location.getParentLocation();

			Location apiParentLocation = new Location().withLocationId(parentLocation.getLocationId())
					.withName(parentLocation.getName()).withTags(parentLocation.getTags())
					//.withAddress(parentLocation.getAddress())
					.withAttributes(parentLocation.getAttributes()).withIdentifiers(parentLocation.getIdentifiers());

			Location apiLocation = new Location().withLocationId(location.getLocationId()).withName(location.getName())
					.withParentLocation(apiParentLocation).withTags(location.getTags())
					//  .withAddress(location.getAddress())
					.withIdentifiers(location.getIdentifiers()).withAttributes(location.getAttributes());
			
			
				/*
					      apiLocation.withCreator(location.getCreator());
					      apiLocation.withEditor(location.getEditor());
						  apiLocation.withVoider(location.getVoider());
						  apiLocation.withDateCreated(location.getDateCreated());
						  apiLocation.withDateEdited(location.getDateEdited());
						  apiLocation.withDateVoided(location.getDateVoided());
						  apiLocation.withVoided(location.getVoided());
						  apiLocation.withVoidReason(location.getRevision());*/

			apiLocations.add(apiLocation);

		}

		return apiLocations;

	}

	public void addLocation(Location location) {
		org.opensrp.domain.Location domainLocation = new org.opensrp.domain.Location()
				.withLocationId(location.getLocationId()).withName(location.getName()).withTags(location.getTags())
				//.withAddress(location.getAddress())
				.withIdentifiers(location.getIdentifiers()).withAttributes(location.getAttributes());
		allLocations.add(domainLocation);

	}

	// FIXME: 8/28/17 api.Location to domain.Location doens't set id field.
	public void updateLocation(Location location) {
		org.opensrp.domain.Location domainLocation = new org.opensrp.domain.Location()
				.withLocationId(location.getLocationId()).withName(location.getName()).withTags(location.getTags())
				//.withAddress(location.getAddress())
				.withIdentifiers(location.getIdentifiers()).withAttributes(location.getAttributes());
		allLocations.update(domainLocation);

	}

}
