package org.opensrp.register.it;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensrp.form.domain.FormData;
import org.opensrp.form.domain.FormField;
import org.opensrp.form.domain.FormInstance;
import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.repository.AllFormSubmissions;
import org.opensrp.register.thrivepk.FormSubmissionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp-register.xml")
public class CreateFormSubmissions {

	@Autowired
	FormSubmissionView fsv;
		
	@Autowired
	AllFormSubmissions fService;
	
	@Test
	public void shouldEnrollChildrenUnder6Years() {
		for (int i = 100; i < 200; i++) {
			String insId = UUID.randomUUID().toString();
			String eid = UUID.randomUUID().toString();
			FormData form = new FormData("pkchild", "/model/instance/Child_Vaccination_Enrollment/", 
					generateChildFields(eid, insId, "Zaman Town", "demotest", "Homeopathic Center", DateTime.now().minusDays(2), i+1+new Random().nextInt(1850)), null);
			FormInstance formInstance = new FormInstance(form, "1");
			FormSubmission fs = new FormSubmission("demotest", insId, "child_enrollment", eid, DateTime.now().minusDays(5).getMillis(), "1", formInstance, DateTime.now().minusDays(5).getMillis());
			fService.add(fs);	
		}
		
	}
	
	private void addField(List<FormField> fields, String name, String value, String bindType) {
		fields.add(new FormField(name, value, bindType+"."+name));
	}
	
	private List<FormField> generateChildFields(String entityId, String instanceId, String uc, String provider, String center, DateTime regDate, int maxAge) {
		List<FormField> fields = new ArrayList<>();
		String df = "yyyy-MM-dd";
		String bindType = "pkchild";
		addField(fields, "id", entityId, bindType);
		addField(fields, "instanceID", "uid:"+instanceId, bindType);
		addField(fields, "provider_province", "Sindh", bindType);
		addField(fields, "provider_province", "Sindh", bindType);
		addField(fields, "provider_uc", uc, bindType);
		addField(fields, "provider_town", "Korangi", bindType);
		addField(fields, "provider_city", "Karachi", bindType);
		addField(fields, "provider_id", provider, bindType);
		addField(fields, "provider_location_id", center, bindType);
		addField(fields, "start", regDate.toString(), bindType);
		addField(fields, "end", regDate.toString(), bindType);
		addField(fields, "today", regDate.toString(df), bindType);
		addField(fields, "client_reg_date", regDate.toString(df), bindType);
		String id = UUID.randomUUID().getLeastSignificantBits()+"";
		addField(fields, "program_client_id", id, bindType);
		addField(fields, "existing_program_client_id", id, bindType);
		addField(fields, "epi_card_number", (new Random().nextInt(19999999)+80000000)+"", bindType);
		boolean male = new Random().nextInt(10)%2==0;
		addField(fields, "first_name", pickName(male), bindType);
		addField(fields, "last_name", pickName(true), bindType);
		addField(fields, "birth_date_known", new Random().nextInt(10)%2==0?"yes":"no", bindType);
		int age = new Random().nextInt(maxAge);
		DateTime bd = DateTime.now().minusDays(age);
		addField(fields, "birth_date", bd.toString(df), bindType);
		addField(fields, "age", age+"", bindType);
		addField(fields, "dob", bd.toString(df), bindType);
		addField(fields, "calc_dob_estimated", new Random().nextInt(10)%5==0?"true":"false", bindType);
		addField(fields, "gender", male ?"Male":"Female", bindType);
		addField(fields, "mother_name", pickName(false), bindType);
		addField(fields, "ethnicity", "sindhi", bindType);
		addField(fields, "province", "sindh", bindType);
		addField(fields, "city_village", "karachi", bindType);
		addField(fields, "town", "korangi", bindType);
		addField(fields, "union_council", "zaman_town", bindType);
		addField(fields, "address1", "437 n", bindType);
		addField(fields, "landmark", "nishani mark", bindType);
		addField(fields, "provincename", "Sindh", bindType);
		addField(fields, "city_villagename", "Karachi", bindType);
		addField(fields, "townname", "Korangi", bindType);
		addField(fields, "union_councilname", "Zaman Town", bindType);
		addField(fields, "address", "73 h", bindType);
		addField(fields, "child_was_suffering_from_a_disease_at_birth", new Random().nextInt(10)%2==0?"yes":"no", bindType);
		addField(fields, "reminders_approval", new Random().nextInt(10)%2==0?"yes":"no", bindType);
		addField(fields, "contact_phone_number", "03343800000", bindType);
		if(age > 12*30){
			addField(fields, "vaccines", "measles1", bindType);
		}
		else if(age > 14*7){
			addField(fields, "vaccines", "penta3 pcv3 opv3 ipv", bindType);
		}
		else if(age > 10*7){
			addField(fields, "vaccines", "penta2 pcv2 opv2", bindType);
		}
		else if(age > 6*7){
			addField(fields, "vaccines", "penta1 pcv1 opv1", bindType);
		}
		else{
			addField(fields, "vaccines", "bcg opv0", bindType);
		}
		
		addField(fields, "bcg_retro", age>42?bd.plusDays(12).toString(df):null, bindType);
		addField(fields, "opv0_retro", age>42?bd.plusDays(12).toString(df):null, bindType);
		addField(fields, "opv0_dose", "0", bindType);
		addField(fields, "pcv1_retro", age>70?bd.plusDays(45).toString(df):null, bindType);
		addField(fields, "pcv1_dose", "1", bindType);
		addField(fields, "opv1_retro", age>70?bd.plusDays(45).toString(df):null, bindType);
		addField(fields, "opv1_dose", "1", bindType);
		addField(fields, "penta1_retro", age>70?bd.plusDays(45).toString(df):null, bindType);
		addField(fields, "penta1_dose", "1", bindType);
		addField(fields, "pcv2_retro", age>98?bd.plusDays(80).toString(df):null, bindType);
		addField(fields, "pcv2_dose", "2", bindType);
		addField(fields, "opv2_retro", age>98?bd.plusDays(80).toString(df):null, bindType);
		addField(fields, "opv2_dose", "2", bindType);
		addField(fields, "penta2_retro", age>98?bd.plusDays(80).toString(df):null, bindType);
		addField(fields, "penta2_dose", "2", bindType);
		addField(fields, "pcv3_retro", age>150?bd.plusDays(105).toString(df):null, bindType);
		addField(fields, "pcv3_dose", "3", bindType);
		addField(fields, "opv3_retro", age>150?bd.plusDays(105).toString(df):null, bindType);
		addField(fields, "opv3_dose", "3", bindType);
		addField(fields, "penta3_retro", age>150?bd.plusDays(105).toString(df):null, bindType);
		addField(fields, "penta3_dose", "3", bindType);
		addField(fields, "ipv_retro", age>150?bd.plusDays(105).toString(df):null, bindType);
		addField(fields, "measles1_retro", age>300?bd.plusDays(270).toString(df):null, bindType);
		addField(fields, "measles1_dose", "1", bindType);


		
		if(age<42){
			addField(fields, "vaccines_2", "bcg opv0", bindType);
		}
		else if(age>40&&age<70){
			addField(fields, "vaccines_2", "penta1 opv1 pcv1", bindType);
		}
		else if(age>70&&age<98){
			addField(fields, "vaccines_2", "penta2 opv2 pcv2", bindType);
		}
		else if(age>98&&age<200){
			addField(fields, "vaccines_2", "penta3 opv3 pcv3 ipv", bindType);
		}
		else if(age>200&&age<400){
			addField(fields, "vaccines_2", "measles1", bindType);
		}
		else if(age>400){
			addField(fields, "vaccines_2", "measles2", bindType);
		}

		addField(fields, "bcg", age<42?regDate.toString(df):null, bindType);
		addField(fields, "opv0", age<42?regDate.toString(df):null, bindType);
		addField(fields, "opv0_dose_today", "0", bindType);
		addField(fields, "pcv1", age>40&&age<70?regDate.toString(df):null, bindType);
		addField(fields, "pcv1_dose_today", "1", bindType);
		addField(fields, "opv1", age>40&&age<70?regDate.toString(df):null, bindType);
		addField(fields, "opv1_dose_today", "1", bindType);
		addField(fields, "penta1", age>40&&age<70?regDate.toString(df):null, bindType);
		addField(fields, "penta1_dose_today", "1", bindType);
		addField(fields, "pcv2", age>70&&age<98?regDate.toString(df):null, bindType);
		addField(fields, "pcv2_dose_today", "2", bindType);
		addField(fields, "opv2", age>70&&age<98?regDate.toString(df):null, bindType);
		addField(fields, "opv2_dose_today", "2", bindType);
		addField(fields, "penta2", age>70&&age<98?regDate.toString(df):null, bindType);
		addField(fields, "penta2_dose_today", "2", bindType);
		addField(fields, "pcv3", age>98&&age<200?regDate.toString(df):null, bindType);
		addField(fields, "pcv3_dose_today", "3", bindType);
		addField(fields, "opv3", age>98&&age<200?regDate.toString(df):null, bindType);
		addField(fields, "opv3_dose_today", "3", bindType);
		addField(fields, "penta3", age>98&&age<200?regDate.toString(df):null, bindType);
		addField(fields, "penta3_dose_today", "3", bindType);
		addField(fields, "ipv", age>98&&age<200?regDate.toString(df):null, bindType);
		addField(fields, "measles1", age>200&&age<400?regDate.toString(df):null, bindType);
		addField(fields, "measles1_dose_today", "1", bindType);
		addField(fields, "measles2", age>400?regDate.toString(df):null, bindType);
		addField(fields, "measles2_dose_today", "2", bindType);
		return fields;
	}

	private String pickName(boolean male) {
		String[] ml = new String[]{"Ali", "Ahmed", "Isran", "Aurang", "Ibad", "Mohammad", "Omar", "Osman", 
				"Jamal", "Bilal", "Daood", "Eman", "Farman", "Ghous", "Hadi"};
		String[] fl = new String[]{"Isra", "Aiman", "Beena", "Durdana", "Zeest", "Minal", "Maneer", "Bisma", 
				"Jaisha", "Zuni", "Sitwat", "Eman", "Fareeha", "Fara", "Hareem"};
		return male?ml[new Random().nextInt(ml.length)]:fl[new Random().nextInt(fl.length)];
	}
}
