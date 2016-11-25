
package org.opensrp.common;

import java.text.SimpleDateFormat;

/**
 * Mappings in OpenSRP for OpenMRS entities and properties
 */
public class FormEntityConstants {

	public static final SimpleDateFormat FORM_DATE = new SimpleDateFormat("yyyy-MM-dd");

	public enum FieldType {
		concept,
		person,
		person_address,
		person_attribute,
		person_identifier,
	}
	
	public enum FieldDataType {
		
	}
	
	public interface FormEntity {
		public String entity();
		public String entityId();
		
	}
	
	public enum Person implements FormEntity{
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
	
	public enum PersonAddress implements FormEntity{
		;
		public String entity(){return "person_address";}
		public String entityId(){return this.name();}
	}
	
	public enum Encounter implements FormEntity{
		encounter_date,
		location_id,
		encounter_start,
		encounter_end;
		
		public String entity(){return "encounter";}
		public String entityId(){return this.name();}
	}
	
	public enum Member implements FormEntity{
		relationship_type,
		person_B,
		start_date,
		end_date;
		
		
		public String entity(){return "Relationship";}
		public String entityId(){return this.name();}
	}
}