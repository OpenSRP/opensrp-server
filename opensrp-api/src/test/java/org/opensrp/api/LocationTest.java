package org.opensrp.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.opensrp.api.domain.Location;
import org.opensrp.api.util.LocationTree;

public class LocationTest {

	@Test
	public void testLocations(){
		Location l = new Location()
			.withLocationId("l1")
			.withAttribute("l1at", "latval")
			.withIdentifier("lid1", "iiii")
			.withName("location1")
			.withTag("HOSPITAL");
		
		assertEquals("", l.getLocationId(), "l1");
		assertEquals("", l.getAttribute("l1at"), "latval");
		assertEquals("", l.getIdentifier("lid1"), "iiii");
		assertEquals("", l.getName(), "location1");
		assertTrue("", l.hasTag("HOSPITAL"));
	}
	
	@Test
	public void testLocationTree2(){
		List<Location> ll = new ArrayList<Location>();
    	ll.add(new Location("1", "l1", null, null, null, null, null));
    	ll.add(new Location("2", "l2", null, null, null, null, null).withParentLocation(ll.get(0)));
    	ll.add(new Location("3", "l3", null, null, null, null, null).withParentLocation(ll.get(0)));
    	ll.add(new Location("4", "l4", null, null, null, null, null).withParentLocation(ll.get(0)));
    	
    	ll.add(new Location("5", "l5", null, null, null, null, null));
    	ll.add(new Location("6", "l6", null, null, null, null, null).withParentLocation(ll.get(4)));
    	ll.add(new Location("7", "l7", null, null, null, null, null).withParentLocation(ll.get(4)));
    	ll.add(new Location("8", "l8", null, null, null, null, null).withParentLocation(ll.get(4)));
    	
    	ll.add(new Location("9", "l9", null, null, null, null, null));
    	ll.add(new Location("10", "l10", null, null, null, null, null).withParentLocation(ll.get(8)));
    	ll.add(new Location("11", "l11", null, null, null, null, null).withParentLocation(ll.get(8)));
    	ll.add(new Location("12", "l12", null, null, null, null, null).withParentLocation(ll.get(8)));

    	ll.add(new Location("13", "l13", null, null, null, null, null).withParentLocation(ll.get(5)));
    	ll.add(new Location("14", "l14", null, null, null, null, null).withParentLocation(ll.get(5)));
    	ll.add(new Location("15", "l15", null, null, null, null, null).withParentLocation(ll.get(5)));
    	
    	ll.add(new Location("16", "l16", null, null, null, null, null).withParentLocation(ll.get(10)));
    	ll.add(new Location("17", "l17", null, null, null, null, null).withParentLocation(ll.get(10)));
    	ll.add(new Location("18", "l18", null, null, null, null, null).withParentLocation(ll.get(10)));
    	
    	ll.add(new Location("19", "l19", null, null, null, null, null).withParentLocation(ll.get(16)));
    	ll.add(new Location("20", "l20", null, null, null, null, null).withParentLocation(ll.get(17)));

    	LocationTree ltree = new LocationTree();
    	for (Location location : ll) {
			ltree.addLocation(location);
		}
    	
    	assertEquals("", ltree.findLocation("7"), ll.get(6));
    	assertTrue("", ltree.hasLocation("7"));
    	assertEquals("", ltree.findLocation("7").getParentLocation(), ll.get(6).getParentLocation());
    	assertTrue("", ltree.hasChildLocation("18", "20"));
	}
	
	@Test
	public void testLocationTree3(){
		List<Location> ll = new ArrayList<Location>();
    	ll.add(new Location("1", "l1", null, null, null, null, null));
    	ll.add(new Location("2", "l2", null, null, null, null, null).withParentLocation(ll.get(0)));
    	ll.add(new Location("3", "l3", null, null, null, null, null).withParentLocation(ll.get(0)));
    	ll.add(new Location("4", "l4", null, null, null, null, null).withParentLocation(ll.get(0)));
    	
    	ll.add(new Location("5", "l5", null, null, null, null, null));
    	ll.add(new Location("6", "l6", null, null, null, null, null).withParentLocation(ll.get(4)));
    	ll.add(new Location("7", "l7", null, null, null, null, null).withParentLocation(ll.get(4)));
    	ll.add(new Location("8", "l8", null, null, null, null, null).withParentLocation(ll.get(4)));
    	
    	ll.add(new Location("9", "l9", null, null, null, null, null));
    	ll.add(new Location("10", "l10", null, null, null, null, null).withParentLocation(ll.get(8)));
    	ll.add(new Location("11", "l11", null, null, null, null, null).withParentLocation(ll.get(8)));
    	ll.add(new Location("12", "l12", null, null, null, null, null).withParentLocation(ll.get(8)));

    	ll.add(new Location("13", "l13", null, null, null, null, null).withParentLocation(ll.get(5)));
    	ll.add(new Location("14", "l14", null, null, null, null, null).withParentLocation(ll.get(5)));
    	ll.add(new Location("15", "l15", null, null, null, null, null).withParentLocation(ll.get(5)));
    	
    	ll.add(new Location("16", "l16", null, null, null, null, null).withParentLocation(ll.get(10)));
    	ll.add(new Location("17", "l17", null, null, null, null, null).withParentLocation(ll.get(10)));
    	ll.add(new Location("18", "l18", null, null, null, null, null).withParentLocation(ll.get(10)));
    	
    	ll.add(new Location("19", "l19", null, null, null, null, null).withParentLocation(ll.get(16)));
    	ll.add(new Location("20", "l20", null, null, null, null, null).withParentLocation(ll.get(17)));

    	LocationTree ltree = new LocationTree();
    	ltree.buildTreeFromList(ll);;
    	
    	assertEquals("", ltree.findLocation("7"), ll.get(6));
    	assertTrue("", ltree.hasLocation("7"));
    	assertEquals("", ltree.findLocation("7").getParentLocation(), ll.get(6).getParentLocation());
    	assertTrue("", ltree.hasChildLocation("18", "20"));
	}
}
