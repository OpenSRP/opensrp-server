package org.opensrp.repository.lucene;

import static org.opensrp.common.AllConstants.BaseEntity.*;
import static org.opensrp.common.AllConstants.BaseEntity.CITY_VILLAGE;
import static org.opensrp.common.AllConstants.BaseEntity.COUNTRY;
import static org.opensrp.common.AllConstants.BaseEntity.COUNTY_DISTRICT;
import static org.opensrp.common.AllConstants.BaseEntity.STATE_PROVINCE;
import static org.opensrp.common.AllConstants.BaseEntity.SUB_DISTRICT;
import static org.opensrp.common.AllConstants.BaseEntity.SUB_TOWN;
import static org.opensrp.common.AllConstants.BaseEntity.TOWN;
import static org.opensrp.common.AllConstants.Client.BIRTH_DATE;
import static org.opensrp.common.AllConstants.Client.DEATH_DATE;
import static org.opensrp.common.AllConstants.Client.FIRST_NAME;
import static org.opensrp.common.AllConstants.Client.GENDER;
import static org.opensrp.common.AllConstants.Client.LAST_NAME;
import static org.opensrp.common.AllConstants.Client.MIDDLE_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.opensrp.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.ldriscoll.ektorplucene.CouchDbRepositorySupportWithLucene;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import com.github.ldriscoll.ektorplucene.LuceneResult;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.FullText;
import com.github.ldriscoll.ektorplucene.designdocument.annotation.Index;
import com.mysql.jdbc.StringUtils;

@FullText({
        @Index(name = "by_all_criteria", analyzer = "perfield:{baseEntityId:\"keyword\",mother:\"keyword\"}", index = "function (doc) {  if(doc.type !== 'Client') return null;  var docl = new Array();  var len = doc.addresses ? doc.addresses.length : 1;  for(var al = 0; al < len; al++) {    var arr1 = ['firstName', 'middleName', 'lastName', 'gender'];    var arr2 = ['addressType', 'country', 'stateProvince', 'cityVillage', 'countyDistrict', 'subDistrict', 'town', 'subTown'];    var ret = new Document(); var baseEntityId = doc.baseEntityId;ret.add(baseEntityId, {'field': 'baseEntityId'});    for(var i in arr1) {      ret.add(doc[arr1[i]], {'field' : arr1[i]});    }    for(var key in doc.attributes) {      ret.add(doc.attributes[key], {'field' : key});    } if (doc.relationships) { for (var key in doc.relationships) { ret.add(doc.relationships[key], { 'field': key }); }}    if(doc.addresses) {      var ad = doc.addresses[al];      if(ad){        for(var i in arr2) {          ret.add(ad[arr2[i]], {'field' : arr2[i]});        }      }              }    var bd = doc.birthdate.substring(0, 19);    ret.add(bd, {'field' : 'birthdate','type' : 'date'});        var crd = doc.dateCreated.substring(0, 19);    ret.add(crd, {'field' : 'lastEdited','type' : 'date'});        if(doc.dateEdited){    var led = doc.dateEdited.substring(0, 19);    ret.add(led, {'field' : 'lastEdited','type' : 'date'});        }        docl.push(ret);    }  return docl; }"),
        @Index(name = "by_all_criteria_v2", analyzer = "perfield:{baseEntityId:\"keyword\",mother:\"keyword\"}",
                //        index = "function (doc) {  if(doc.type !== 'Client') return null;  var docl = new Array();  var len = doc.addresses ? doc.addresses.length : 1;  for(var al = 0; al < len; al++) {    var arr1 = ['firstName', 'middleName', 'lastName', 'gender'];    var arr2 = ['addressType', 'country', 'stateProvince', 'cityVillage', 'countyDistrict', 'subDistrict', 'town', 'subTown'];    var ret = new Document(); var baseEntityId = doc.baseEntityId;ret.add(baseEntityId, {'field': 'baseEntityId'});    for(var i in arr1) {      ret.add(doc[arr1[i]], {'field' : arr1[i]});    }      for (var key in doc.identifiers) { ret.add(doc.identifiers[key], {'field': key}); }      for(var key in doc.attributes) {      ret.add(doc.attributes[key], {'field' : key});    }    if(doc.addresses) {      var ad = doc.addresses[al];      if(ad){        for(var i in arr2) {          ret.add(ad[arr2[i]], {'field' : arr2[i]});        }      }              }    var bd = doc.birthdate.substring(0, 19);    ret.add(bd, {'field' : 'birthdate','type' : 'date'});        var crd = doc.dateCreated.substring(0, 19);    ret.add(crd, {'field' : 'lastEdited','type' : 'date'});        if(doc.dateEdited){    var led = doc.dateEdited.substring(0, 19);    ret.add(led, {'field' : 'lastEdited','type' : 'date'});        }        docl.push(ret);    }  return docl; }"
                index = "function (doc) {  if(doc.type !== 'Client') return null;  var docl = new Array();  var len = doc.addresses ? doc.addresses.length : 1;  for(var al = 0; al < len; al++) {    var arr1 = ['firstName', 'middleName', 'lastName', 'gender'];    var arr2 = ['addressType', 'country', 'stateProvince', 'cityVillage', 'countyDistrict', 'subDistrict', 'town', 'subTown'];    var ret = new Document(); var baseEntityId = doc.baseEntityId;ret.add(baseEntityId, {'field': 'baseEntityId'});    for(var i in arr1) {      ret.add(doc[arr1[i]], {'field' : arr1[i]});    }    for(var key in doc.attributes) {      ret.add(doc.attributes[key], {'field' : key});    } if (doc.relationships) {for (var key in doc.relationships) {ret.add(doc.relationships[key][0], {'field': key });}}    if(doc.addresses) {      var ad = doc.addresses[al];      if(ad){        for(var i in arr2) {          ret.add(ad[arr2[i]], {'field' : arr2[i]});        }      }              }    var bd = doc.birthdate.substring(0, 19);    ret.add(bd, {'field' : 'birthdate','type' : 'date'});        var crd = doc.dateCreated.substring(0, 19);    ret.add(crd, {'field' : 'lastEdited','type' : 'date'});        if(doc.dateEdited){    var led = doc.dateEdited.substring(0, 19);    ret.add(led, {'field' : 'lastEdited','type' : 'date'});        }        docl.push(ret);    }  return docl; }"
		
		) })
@Component
public class LuceneClientRepository extends CouchDbRepositorySupportWithLucene<Client> {
	
	private LuceneDbConnector ldb;
	
	@Autowired
	protected LuceneClientRepository(LuceneDbConnector db) {
		super(Client.class, db);
		this.ldb = db;
		initStandardDesignDocument();
	}
	
	public List<Client> getByCriteria(String nameLike, String gender, DateTime birthdateFrom, DateTime birthdateTo,
	                                  DateTime deathdateFrom, DateTime deathdateTo, String attributeType,
	                                  String attributeValue, DateTime lastEditFrom, DateTime lastEditTo,
	                                  String motherIdentifier) {
		return getByCriteria(nameLike, gender, birthdateFrom, birthdateTo, deathdateFrom, deathdateTo, attributeType,
		    attributeValue, null, null, null, null, null, null, null, null, lastEditFrom, lastEditTo, motherIdentifier);
	}
	
	public List<Client> getByCriteria(String addressType, String country, String stateProvince, String cityVillage,
	                                  String countyDistrict, String subDistrict, String town, String subTown,
	                                  DateTime lastEditFrom, DateTime lastEditTo, String motherIdentifier) {
		return getByCriteria(null, null, null, null, null, null, null, null, addressType, country, stateProvince,
		    cityVillage, countyDistrict, subDistrict, town, subTown, lastEditFrom, lastEditTo, motherIdentifier);
	}
	
	public List<Client> getByCriteria(String nameLike, String gender, DateTime birthdateFrom, DateTime birthdateTo,
	                                  DateTime deathdateFrom, DateTime deathdateTo, String attributeType,
	                                  String attributeValue, String addressType, String country, String stateProvince,
	                                  String cityVillage, String countyDistrict, String subDistrict, String town,
	                                  String subTown, DateTime lastEditFrom, DateTime lastEditTo, String motherIdentifier) {
		// create a simple query against the view/search function that we've created
		LuceneQuery query = new LuceneQuery("Client", "by_all_criteria");
		
		Query q = new Query(FilterType.OR);
		if (!StringUtils.isEmptyOrWhitespaceOnly(nameLike)) {
			q.like(FIRST_NAME, nameLike);
			q.like(MIDDLE_NAME, nameLike);
			q.like(LAST_NAME, nameLike);
		}
		Query qf = new Query(FilterType.AND, q);
		if (!StringUtils.isEmptyOrWhitespaceOnly(gender)) {
			qf.eq(GENDER, gender);
		}
		if (birthdateFrom != null && birthdateTo != null) {
			qf.between(BIRTH_DATE, birthdateFrom, birthdateTo);
		}
		if (deathdateFrom != null && deathdateTo != null) {
			qf.between(DEATH_DATE, deathdateFrom, deathdateTo);
		}
		if (lastEditFrom != null & lastEditTo != null) {
			qf.between(LAST_UPDATE, lastEditFrom, lastEditTo);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(attributeType)) {
			qf.eq(attributeType, attributeValue);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(addressType)) {
			qf.eq(ADDRESS_TYPE, addressType);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(country)) {
			qf.eq(COUNTRY, country);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(stateProvince)) {
			qf.eq(STATE_PROVINCE, stateProvince);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(cityVillage)) {
			qf.eq(CITY_VILLAGE, cityVillage);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(countyDistrict)) {
			qf.eq(COUNTY_DISTRICT, countyDistrict);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(subDistrict)) {
			qf.eq(SUB_DISTRICT, subDistrict);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(town)) {
			qf.eq(TOWN, town);
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(subTown)) {
			qf.eq(SUB_TOWN, subTown);
		}
		if (StringUtils.isEmptyOrWhitespaceOnly(qf.query())) {
			throw new RuntimeException("Atleast one search filter must be specified");
		}
		query.setQuery(qf.query());
		// stale must not be ok, as we've only just loaded the docs
		query.setStaleOk(false);
		query.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(query);
			return ldb.asList(result, Client.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Client> getByCriteria(String query) {
		// create a simple query against the view/search function that we've created
		LuceneQuery lq = new LuceneQuery("Client", "by_all_criteria");
		
		lq.setQuery(query);
		// stale must not be ok, as we've only just loaded the docs
		lq.setStaleOk(false);
		lq.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(lq);
			return ldb.asList(result, Client.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Client> getByClientByMother(String field, String value) {
		// create a simple query against the view/search function that we've created
		if (value == null) {
			return new ArrayList<Client>();
		}
		LuceneQuery lq = new LuceneQuery("Client", "by_all_criteria_v2");
		Query query = new Query(FilterType.AND);
		if (field.equals(MOTHERS_INDENTIFIER)) {
			query.eq(field, value);
		}
		lq.setQuery(query.query());
		lq.setStaleOk(false);
		lq.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(lq);
			return ldb.asList(result, Client.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Client> getByFieldValue(String field, String value) {
		// create a simple query against the view/search function that we've created
		if (value == null) {
			return new ArrayList<Client>();
		}
		LuceneQuery lq = new LuceneQuery("Client", "by_all_criteria_v2");
		Query query = new Query(FilterType.AND);
		if (field.equals(BASE_ENTITY_ID)) {
			query.eq(field, value);
		}
		lq.setQuery(query.query());
		lq.setStaleOk(false);
		lq.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(lq);
			return ldb.asList(result, Client.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Client> getByFieldValue(String field, List<String> ids) {
		// create a simple query against the view/search function that we've created
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<Client>();
		}
		LuceneQuery lq = new LuceneQuery("Client", "by_all_criteria_v2");
		Query query = new Query(FilterType.AND);
		if (field.equals(BASE_ENTITY_ID)) {
			query.inList(field, ids);
		}
		lq.setQuery(query.query());
		lq.setLimit(ids.size());
		// stale must not be ok, as we've only just loaded the docs
		lq.setStaleOk(false);
		lq.setIncludeDocs(true);
		
		try {
			LuceneResult result = db.queryLucene(lq);
			return ldb.asList(result, Client.class);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
