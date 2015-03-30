package org.opensrp.api.util;

import java.util.List;
import java.util.Map;

import org.opensrp.api.domain.Location;

public class LocationTree {

	Tree<String, Location> locationsHierarchy;
	
	public LocationTree() {	
		this.locationsHierarchy = new Tree<String, Location>();
	}
	
	public void addLocation(Location l){
    	if(!locationsHierarchy.hasNode(l.getLocationId())){
	    	if(l.getParentLocation() == null){
	    		locationsHierarchy.addNode(l.getLocationId(), l.getName(), l, null);
	    	}
	    	else {
	    		if(!locationsHierarchy.hasNode(l.getParentLocation().getLocationId())){
	    			addLocation(l.getParentLocation());
	    		}
	
	    		locationsHierarchy.addNode(l.getLocationId(), l.getName(), l, l.getParentLocation().getLocationId());
	    	}
    	}
    }

	/**
	 * WARNING: Overrides existing locations
	 * @param locations
	 */
	public void buildTreeFromList(List<Location> locations){
		for (Location location : locations) {
			addLocation(location);
		}
	}
	
	public Location findLocation(String locationId){
		return locationsHierarchy.getNode(locationId).getNode();
	}
	
	public boolean hasLocation(String locationId){
		return locationsHierarchy.hasNode(locationId);
	}
	
	public boolean hasChildLocation(String locationId, String childLocationId){
		return locationsHierarchy.getNode(locationId).findChild(childLocationId)!=null;
	}
	
	public Map<String, TreeNode<String, Location>> getLocationsHierarchy() {
		return locationsHierarchy.getTree();
	}
}
