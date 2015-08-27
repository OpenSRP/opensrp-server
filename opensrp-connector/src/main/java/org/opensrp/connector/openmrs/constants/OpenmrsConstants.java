
package org.opensrp.connector.openmrs.constants;

/**
 * Mappings in OpenSRP for OpenMRS entities and properties
 */
public class OpenmrsConstants {

	public interface OpenmrsEntity {
		public String entity();
		public String entityId();
		
	}
	
	public enum Person implements OpenmrsEntity{
		first_name,
		middle_name,
		last_name,
		gender,
		birthdate,
		birthdate_estimated,
		dead,
		deathdate,
		deathdate_estimated;
		
		public String entity(){return "person";}
		public String entityId(){return this.name();}
	}
	
	public enum PersonAddress implements OpenmrsEntity{
		;
		public String entity(){return "person_address";}
		public String entityId(){return this.name();}
	}
	
	public enum Encounter implements OpenmrsEntity{
		encounter_date,
		location_id,
		encounter_start,
		encounter_end;
		
		public String entity(){return "encounter";}
		public String entityId(){return this.name();}
	}
}