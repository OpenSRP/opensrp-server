package org.opensrp.api.contract;

import org.opensrp.api.domain.Location;
import org.opensrp.api.domain.User;
import org.opensrp.api.util.LocationTree;
import org.opensrp.api.util.TreeNode;

public interface LocationManager {
	
	Location getLocation(String locationId);
	
	TreeNode<String, Location> getLocationWithTree(String startinglocationId);

	LocationTree getLocationHierarchy();
	
	void verifyLocationAccess(Location location, User user);
}
