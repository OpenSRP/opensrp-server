package org.opensrp.api.util;

import java.util.List;
import java.util.Map;

import org.opensrp.api.domain.Location;

/**
 * {@link LocationTree} is an specification of {@link Tree} which provides helper methods for creating a Tree for 
 * {@link Location}. The key is the locationId and label is name of {@link Location} and the data for tree node 
 * is location object itself. Each node has locationId if its parent location and has a {@link TreeNode} map of 
 * children of the location. This way it allows to create a complete tree of location starting from root nodes to 
 * the end or lowest level in the form of tree. A root node is one with parent location null.
 */
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
