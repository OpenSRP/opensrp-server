package org.opensrp.web.rest;

import static org.opensrp.common.AllConstants.BaseEntity.*;
import static org.opensrp.common.AllConstants.Client.BIRTH_DATE;
import static org.opensrp.common.AllConstants.Client.DEATH_DATE;
import static org.opensrp.common.AllConstants.Client.FIRST_NAME;
import static org.opensrp.common.AllConstants.Client.GENDER;
import static org.opensrp.web.rest.RestUtils.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.opensrp.domain.Client;
import org.opensrp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping(value = "/rest/client")
public class ClientResource extends RestResource<Client>{
	private ClientService clientService;
	
	@Autowired
	public ClientResource(ClientService clientService) {
		this.clientService = clientService;
	}

	@Override
	public Client getByUniqueId(String uniqueId) {
		return clientService.find(uniqueId);
	}
	
	@Override
    public Client create(Client o) {
		return clientService.addClient(o);
	}

	@Override
	public List<String> requiredProperties() {
		List<String> p = new ArrayList<>();
		p.add(FIRST_NAME);
		p.add(BIRTH_DATE);
		p.add(GENDER);
		p.add(BASE_ENTITY_ID);
		return p;
	}

	@Override
	public Client update(Client entity) {//TODO check if send property and id matches
		return clientService.mergeClient(entity);//TODO update should only be based on baseEntityId
	}
	
	@Override
	public List<Client> search(HttpServletRequest request) throws ParseException {//TODO search should not call different url but only add params
		String nameLike = getStringFilter("name", request);
		String gender = getStringFilter(GENDER, request);
		DateTime[] birthdate = getDateRangeFilter(BIRTH_DATE, request);//TODO add ranges like fhir do http://hl7.org/fhir/search.html
		DateTime[] deathdate = getDateRangeFilter(DEATH_DATE, request);
		String addressType = getStringFilter(ADDRESS_TYPE, request);
		String country = getStringFilter(COUNTRY, request);
		String stateProvince = getStringFilter(STATE_PROVINCE, request);
		String cityVillage = getStringFilter(CITY_VILLAGE, request);
		String countyDistrict = getStringFilter(COUNTY_DISTRICT, request);
		String subDistrict = getStringFilter(SUB_DISTRICT, request);
		String town = getStringFilter(TOWN, request);
		String subTown = getStringFilter(SUB_TOWN, request);
		DateTime[] lastEdit = getDateRangeFilter(LAST_UPDATE, request);//TODO client by provider id
		//TODO lookinto Swagger https://slack-files.com/files-pri-safe/T0EPSEJE9-F0TBD0N77/integratingswagger.pdf?c=1458211183-179d2bfd2e974585c5038fba15a86bf83097810a
		
		String attributes = getStringFilter("attribute", request);
		String attributeType = StringUtils.isEmptyOrWhitespaceOnly(attributes)?null:attributes.split(":",-1)[0];
		String attributeValue = StringUtils.isEmptyOrWhitespaceOnly(attributes)?null:attributes.split(":",-1)[1];

		return clientService.findByCriteria(nameLike, gender, 
				birthdate==null?null:birthdate[0], birthdate==null?null:birthdate[1], 
				deathdate == null?null:deathdate[0], deathdate == null?null:deathdate[1], attributeType, attributeValue,
				addressType, country, stateProvince, cityVillage, countyDistrict, subDistrict, town, subTown,
				lastEdit==null?null:lastEdit[0], lastEdit==null?null:lastEdit[1]);
	}

	@Override
	public List<Client> filter(String query) {
		return clientService.findByDynamicQuery(query);
	}

}
