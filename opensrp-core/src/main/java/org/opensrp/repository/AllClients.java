package org.opensrp.repository;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.ektorp.util.Assert;
import org.ektorp.util.Documents;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Client;
import org.opensrp.repository.lucene.LuceneClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.StringUtils;

@Repository
public class AllClients extends MotechBaseRepository<Client> {
	
	private LuceneClientRepository lcr;

	@Autowired
	protected AllClients(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db, 
			LuceneClientRepository lcr) {
		super(Client.class, db);
		this.lcr = lcr;
	}

	@GenerateView
	public Client findByBaseEntityId(String baseEntityId) {
		if(StringUtils.isEmptyOrWhitespaceOnly(baseEntityId))
			return null;
		List<Client> clients = queryView("by_baseEntityId", baseEntityId);
		if (clients == null || clients.isEmpty()) {
			return null;
		}
		return clients.get(0);
	}
	
	@GenerateView
	public Client findByBaseEntityId(CouchDbConnector targetDb,String baseEntityId) {
		if(StringUtils.isEmptyOrWhitespaceOnly(baseEntityId))
			return null;
		List<Client> clients = queryView(targetDb,"by_baseEntityId", baseEntityId);
		if (clients == null || clients.isEmpty()) {
			return null;
		}
		return clients.get(0);
	}

	@View(name = "all_clients", map = "function(doc) { if (doc.type === 'Client') { emit(doc.baseEntityId); } }")
	public List<Client> findAllClients() {
		return db.queryView(createQuery("all_clients").includeDocs(true), Client.class);
	}
	
	@View(name = "all_clients_by_identifier", map = "function(doc) {if (doc.type === 'Client') {for(var key in doc.identifiers) {emit(doc.identifiers[key]);}}}")
	public List<Client> findAllByIdentifier(String identifier) {
		return db.queryView(createQuery("all_clients_by_identifier").key(identifier).includeDocs(true), Client.class);
	}
	
	@View(name = "all_clients_by_identifier", map = "function(doc) {if (doc.type === 'Client') {for(var key in doc.identifiers) {emit(doc.identifiers[key]);}}}")
	public List<Client> findAllByIdentifier(CouchDbConnector targetDb,String identifier) {
		return targetDb.queryView(createQuery("all_clients_by_identifier").key(identifier).includeDocs(true), Client.class);
	}

	@View(name = "all_clients_by_identifier_of_type", map = "function(doc) {if (doc.type === 'Client') {for(var key in doc.identifiers) {emit([key, doc.identifiers[key]]);}}}")
	public List<Client> findAllByIdentifier(String identifierType, String identifier) {
		ComplexKey ckey = ComplexKey.of(identifierType, identifier);
		return db.queryView(createQuery("all_clients_by_identifier_of_type").key(ckey).includeDocs(true), Client.class);
	}
	
	@View(name = "all_clients_by_attribute_of_type", map = "function(doc) {if (doc.type === 'Client') {for(var key in doc.attributes) {emit([key, doc.attributes[key]]);}}}")
	public List<Client> findAllByAttribute(String attributeType, String attribute) {
		ComplexKey ckey = ComplexKey.of(attributeType, attribute);
		return db.queryView(createQuery("all_clients_by_attribute_of_type").key(ckey).includeDocs(true), Client.class);
	}
	
	@View(name = "all_clients_by_matching_name", map = "function(doc) {if(doc.type === 'Client'){emit(doc.firstName, doc);emit(doc.lastName, doc);}}")
	public List<Client> findAllByMatchingName(String nameMatches) {
		return db.queryView(createQuery("all_clients_by_matching_name").startKey(nameMatches).endKey(nameMatches+"z").includeDocs(true), Client.class);
	}
	/**
	 * Find a client based on the relationship id and between a range of date created dates e.g given mother's id get children born at a given time
	 * @param relationalId
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	@View(name = "client_by_relationship_id_and_date_created", map = "function(doc) { if (doc.type === 'Client' && doc.relationships) {for (var key in doc.relationships) { var entityid=doc.relationships[key][0]; emit([entityid, doc.dateCreated.substring(0,10)], null); }} }")
    public List<Client> findByRelationshipIdAndDateCreated(String relationalId, String dateFrom,String dateTo) {
        ComplexKey startKey = ComplexKey.of(relationalId, dateFrom);
        ComplexKey endKey = ComplexKey.of(relationalId, dateTo);
        List<Client> clients = db.queryView(createQuery("client_by_relationship_id_and_date_created").startKey(startKey).endKey(endKey).includeDocs(true), Client.class);
        return clients;
    }
	
//	@View(name = "client_by_relationship", map = "function(doc) {if (doc.type === 'Client') {for(var key in doc.relationships) {emit([key, doc.relationships[key]]);}}}")
//	@View(name = "client_by_relationship", map = "function(doc) { if(doc.type == 'Client' && doc.relationships.mother[0]) {emit(null, doc._id)} }")
	@View(name = "client_by_relationship", map = "function(doc) { if(doc.type === 'Client' && doc.relationships) { for (var key in doc.relationships) { var entityid = doc.relationships[key][0]; if (key === 'mother') {emit([key, entityid], doc);}}}}")
	
	public List<Client> findByRela3tionshipId(String relationshipType, String entityId) {
		return db.queryView(createQuery("client_by_relationship").startKey(entityId).endKey(entityId).includeDocs(true), Client.class);
	}
	
//	@View(name = "clients_by_relationship", map = "function(doc) {if (doc.type === 'Client' && doc.relationships.mother) {for(var key in doc.relationships) {emit(doc.relationships.mother[key]);}}}")
//	public List<Client> findByRelationshipId(String identifier) {
//		return db.queryView(createQuery("clients_by_relationship").key(identifier).includeDocs(true), Client.class);
//	}
	public List<Client> findByCriteria(String nameLike, String gender, DateTime birthdateFrom, DateTime birthdateTo, 
			DateTime deathdateFrom, DateTime deathdateTo, String attributeType, String attributeValue, 
			String addressType, String country, String stateProvince, String cityVillage, String countyDistrict, 
			String  subDistrict, String town, String subTown, DateTime lastEditFrom, DateTime lastEditTo) {
		return lcr.getByCriteria(nameLike, gender, birthdateFrom, birthdateTo, deathdateFrom, deathdateTo, attributeType, attributeValue, addressType, country, stateProvince, cityVillage, countyDistrict, subDistrict, town, subTown, lastEditFrom, lastEditTo,null);//db.queryView(q.includeDocs(true), Client.class);
	}
	
	public List<Client> findByDynamicQuery(String query) {
		return lcr.getByCriteria(query);//db.queryView(q.includeDocs(true), Client.class);
	}
	
	public List<Client> findByCriteria(String nameLike, String gender, DateTime birthdateFrom, DateTime birthdateTo, 
			DateTime deathdateFrom, DateTime deathdateTo, String attributeType, String attributeValue, DateTime lastEditFrom, DateTime lastEditTo){
		return lcr.getByCriteria(nameLike, gender, birthdateFrom, birthdateTo, deathdateFrom, deathdateTo, attributeType, attributeValue, null, null, null, null, null, null, null, null, lastEditFrom, lastEditTo,null);
	}
	
	public List<Client> findByCriteria(String addressType, String country, String stateProvince, String cityVillage, String countyDistrict,
			String  subDistrict, String town, String subTown, DateTime lastEditFrom, DateTime lastEditTo) {
		return lcr.getByCriteria(null, null, null, null, null, null, null, null, addressType, country, stateProvince, cityVillage, countyDistrict, subDistrict, town, subTown, lastEditFrom, lastEditTo,null);
	}
	
	public List<Client> findByRelationShip(String motherIndentier) {
		return lcr.getByClientByMother("mother",motherIndentier);
	}
	
	/**
	 * Query view from the specified db
	 * @param targetDb
	 * @param viewName
	 * @param key
	 * @return
	 */
	public List<Client> queryView(CouchDbConnector targetDb,String viewName, String key) {
		return targetDb.queryView(createQuery(viewName)
								.includeDocs(true)
								.key(key),
							Client.class);
	}
	/**
	 * Save client to the specified db
	 * @param targetDb
	 * @param client
	 */
	public void add(CouchDbConnector targetDb,Client client) {
		Assert.isTrue(Documents.isNew(client), "entity must be new");
		targetDb.create(client);
	}
	/**
	 * Get all clients without a server version
	 * 
	 * @return
	 */
	@View(name = "clients_by_empty_server_version", map = "function(doc) { if ( doc.type == 'Client' && !doc.serverVersion) { emit(doc._id, doc); } }")
	public List<Client> findByEmptyServerVersion() {
		return db.queryView(createQuery("clients_by_empty_server_version").limit(200).includeDocs(true), Client.class);
	}
	@View(name = "events_by_version", map = "function(doc) { if (doc.type === 'Client') { emit([doc.serverVersion], null); } }")
	public List<Client> findByServerVersion(long serverVersion) {
		ComplexKey startKey = ComplexKey.of(serverVersion + 1);
		ComplexKey endKey = ComplexKey.of(System.currentTimeMillis());
		return db.queryView(createQuery("events_by_version").startKey(startKey).endKey(endKey).includeDocs(true),
		    Client.class);
	}
	
	public List<Client> findByFieldValue(String field, List<String> ids) {
		return lcr.getByFieldValue(field, ids);
	}
}
