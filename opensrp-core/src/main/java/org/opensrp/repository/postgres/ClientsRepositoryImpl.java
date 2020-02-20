package org.opensrp.repository.postgres;

import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.opensrp.common.AllConstants;
import org.opensrp.domain.Address;
import org.opensrp.domain.Client;
import org.opensrp.domain.postgres.ClientMetadata;
import org.opensrp.domain.postgres.ClientMetadataExample;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.repository.ClientsRepository;
import org.opensrp.repository.postgres.mapper.custom.CustomClientMapper;
import org.opensrp.repository.postgres.mapper.custom.CustomClientMetadataMapper;
import org.opensrp.search.AddressSearchBean;
import org.opensrp.search.ClientSearchBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository("clientsRepositoryPostgres")
public class ClientsRepositoryImpl extends BaseRepositoryImpl<Client> implements ClientsRepository {
	
	private static Logger logger = LoggerFactory.getLogger(ClientsRepository.class.toString());

	@Value("#{opensrp['address.type']}")
	private String addressType;

	@Value("#{opensrp['use.address.client.in.metadata']}")
	private Boolean userAddressInClientMetadata;

	@Autowired
	private CustomClientMetadataMapper clientMetadataMapper;
	
	@Autowired
	private CustomClientMapper clientMapper;
	
	@Override
	public Client get(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		
		org.opensrp.domain.postgres.Client pgClient = clientMetadataMapper.selectByDocumentId(id);
		if (pgClient == null) {
			return null;
		}
		return convert(pgClient);
	}
	
	@Override
	public void add(Client entity) {
		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		if (retrievePrimaryKey(entity) != null) { //Client already added
			return;
		}
		
		if (entity.getId() == null)
			entity.setId(UUID.randomUUID().toString());
		
		setRevision(entity);
		
		org.opensrp.domain.postgres.Client pgClient = convert(entity, null);
		if (pgClient == null) {
			return;
		}
		
		int rowsAffected = clientMapper.insertSelectiveAndSetId(pgClient);
		if (rowsAffected < 1 || pgClient.getId() == null) {
			return;
		}
		
		ClientMetadata clientMetadata = createMetadata(entity, pgClient.getId());
		if (clientMetadata != null) {
			System.out.println("<-client meta data->");
			System.out.println(clientMetadata);
			clientMetadataMapper.insertSelective(clientMetadata);
		}
	}
	
	@Override
	public void update(Client entity) {

		System.out.println("<-Entity->");
		System.out.println(entity);

		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		if (id == null) { // Client not added
			return;
		}
		
		setRevision(entity);
		
		org.opensrp.domain.postgres.Client pgClient = convert(entity, id);
		if (pgClient == null) {
			return;
		}
		
		ClientMetadata clientMetadata = createMetadata(entity, id);
		if (clientMetadata == null) {
			return;
		}
		
		int rowsAffected = clientMapper.updateByPrimaryKey(pgClient);
		if (rowsAffected < 1) {
			return;
		}
		
		ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
		clientMetadataExample.createCriteria().andClientIdEqualTo(id).andDateDeletedIsNull();
		clientMetadata.setId(clientMetadataMapper.selectByExample(clientMetadataExample).get(0).getId());
		clientMetadataMapper.updateByPrimaryKey(clientMetadata);
	}
	
	@Override
	public List<Client> getAll() {
		ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
		clientMetadataExample.createCriteria().andDateDeletedIsNull();
		List<org.opensrp.domain.postgres.Client> clients = clientMetadataMapper.selectMany(clientMetadataExample, 0,
		    DEFAULT_FETCH_SIZE);
		return convert(clients);
	}
	
	@Override
	public void safeRemove(Client entity) {
		if (entity == null || entity.getBaseEntityId() == null) {
			return;
		}
		
		Long id = retrievePrimaryKey(entity);
		if (id == null) {
			return;
		}
		
		Date dateDeleted = entity.getDateVoided() == null ? new Date() : entity.getDateVoided().toDate();
		ClientMetadata clientMetadata = new ClientMetadata();
		clientMetadata.setDateDeleted(dateDeleted);
		
		ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
		clientMetadataExample.createCriteria().andClientIdEqualTo(id).andDateDeletedIsNull();
		
		int rowsAffected = clientMetadataMapper.updateByExampleSelective(clientMetadata, clientMetadataExample);
		if (rowsAffected < 1) {
			return;
		}
		
		org.opensrp.domain.postgres.Client pgClient = new org.opensrp.domain.postgres.Client();
		pgClient.setId(id);
		pgClient.setDateDeleted(dateDeleted);
		clientMapper.updateByPrimaryKeySelective(pgClient);
	}
	
	@Override
	public Client findByBaseEntityId(String baseEntityId) {
		if (StringUtils.isBlank(baseEntityId)) {
			return null;
		}
		org.opensrp.domain.postgres.Client pgClient = clientMetadataMapper.selectOne(baseEntityId);
		return convert(pgClient);
	}
	
	@Override
	public List<Client> findAllClients() {
		return getAll();
	}
	
	@Override
	public List<Client> findAllByIdentifier(String identifier) {
		List<org.opensrp.domain.postgres.Client> clients = clientMapper.selectByIdentifier(identifier);
		return convert(clients);
	}
	
	@Override
	public List<Client> findAllByIdentifier(String identifierType, String identifier) {
		List<org.opensrp.domain.postgres.Client> clients = clientMapper.selectByIdentifierOfType(identifierType, identifier);
		return convert(clients);
	}
	
	@Override
	public List<Client> findAllByAttribute(String attributeType, String attribute) {
		List<org.opensrp.domain.postgres.Client> clients = clientMapper.selectByAttributeOfType(attributeType, attribute);
		return convert(clients);
	}
	
	@Override
	public List<Client> findAllByMatchingName(String nameMatches) {
		List<org.opensrp.domain.postgres.Client> clients = clientMetadataMapper.selectByName(nameMatches, 0,
		    DEFAULT_FETCH_SIZE);
		return convert(clients);
	}
	
	@Override
	public List<Client> findByRelationshipIdAndDateCreated(String relationalId, String dateFrom, String dateTo) {
		List<org.opensrp.domain.postgres.Client> clients = clientMapper.selectByRelationshipIdAndDateCreated(relationalId,
		    new DateTime(dateFrom).toDate(), new DateTime(dateTo).toDate());
		return convert(clients);
	}
	
	public List<Client> findByRelationshipId(String relationshipType, String entityId) {
		List<org.opensrp.domain.postgres.Client> clients = clientMapper.selectByRelationshipIdOfType(relationshipType,
		    entityId);
		return convert(clients);
	}
	
	@Override
	public List<Client> findByCriteria(ClientSearchBean searchBean, AddressSearchBean addressSearchBean) {
		return convert(clientMetadataMapper.selectBySearchBean(searchBean, addressSearchBean, 0, DEFAULT_FETCH_SIZE));
	}
	
	@Override
	public List<Client> findByDynamicQuery(String query) {
		throw new IllegalArgumentException("Method not supported");
	}
	
	@Override
	public List<Client> findByCriteria(ClientSearchBean searchBean) {
		return findByCriteria(searchBean, new AddressSearchBean());
	}
	
	@Override
	public List<Client> findByCriteria(AddressSearchBean addressSearchBean, DateTime lastEditFrom, DateTime lastEditTo) {
		ClientSearchBean clientSearchBean = new ClientSearchBean();
		clientSearchBean.setLastEditFrom(lastEditFrom);
		clientSearchBean.setLastEditTo(lastEditTo);
		return findByCriteria(clientSearchBean, addressSearchBean);
	}
	
	@Override
	public List<Client> findByRelationShip(String relationIndentier) {
		List<org.opensrp.domain.postgres.Client> clients = clientMapper.selectByRelationShip(relationIndentier);
		return convert(clients);
	}
	
	@Override
	public List<Client> findByEmptyServerVersion() {
		ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
		clientMetadataExample.createCriteria().andServerVersionIsNull().andDateDeletedIsNull();
		clientMetadataExample.setOrderByClause("client_id ASC");
		
		List<org.opensrp.domain.postgres.Client> clients = clientMetadataMapper.selectMany(clientMetadataExample, 0,
		    DEFAULT_FETCH_SIZE);
		return convert(clients);
	}
	
	@Override
	public List<Client> findByServerVersion(long serverVersion) {
		ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
		clientMetadataExample.createCriteria().andServerVersionGreaterThanOrEqualTo(serverVersion + 1)
		        .andDateDeletedIsNull();
		clientMetadataExample.setOrderByClause("server_version ASC");
		
		List<org.opensrp.domain.postgres.Client> clients = clientMetadataMapper.selectMany(clientMetadataExample, 0,
		    DEFAULT_FETCH_SIZE);
		return convert(clients);
	}
	
	@Override
	public List<Client> findByFieldValue(String field, List<String> ids) {
		if (field.equals(BASE_ENTITY_ID) && ids != null && !ids.isEmpty()) {
			ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
			clientMetadataExample.createCriteria().andBaseEntityIdIn(ids).andDateDeletedIsNull();
			List<org.opensrp.domain.postgres.Client> clients = clientMetadataMapper.selectMany(clientMetadataExample, 0,
			    DEFAULT_FETCH_SIZE);
			return convert(clients);
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<Client> notInOpenMRSByServerVersion(long serverVersion, Calendar calendar) {
		long serverStartKey = serverVersion + 1;
		long serverEndKey = calendar.getTimeInMillis();
		if (serverStartKey < serverEndKey) {
			ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
			clientMetadataExample.createCriteria().andOpenmrsUuidIsNull()
			        .andServerVersionBetween(serverStartKey, serverEndKey).andDateDeletedIsNull();
			
			List<org.opensrp.domain.postgres.Client> clients = clientMetadataMapper.selectMany(clientMetadataExample, 0,
			    DEFAULT_FETCH_SIZE);
			return convert(clients);
		}
		return new ArrayList<>();
	}

	@Override
	public List<Client> findAllClientByUpazila(String name) {
		return convert(clientMapper.getClientByUpazila(name));
	}

	@Override
	public CustomQuery findTeamInfo(String username) {
		return clientMapper.getTeamInfo(username);
	}

	@Override
	public List<CustomQuery> getProviderLocationTreeByChildRole(int memberId, int childRoleId) {
		return clientMapper.getProviderLocationTreeByChildRole(memberId, childRoleId);
	}

	@Override
	public List<CustomQuery> getProviderLocationIdByChildRole(int memberId, int childRoleId, int locationTagId) {
		return clientMapper.getProviderLocationIdByChildRole(memberId, childRoleId, locationTagId);
	}

	// Private Methods
	protected List<Client> convert(List<org.opensrp.domain.postgres.Client> clients) {
		if (clients == null || clients.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<Client> convertedClients = new ArrayList<>();
		for (org.opensrp.domain.postgres.Client client : clients) {
			Client convertedClient = convert(client);
			if (convertedClient != null) {
				convertedClients.add(convertedClient);
			}
		}
		
		return convertedClients;
	}
	
	private Client convert(org.opensrp.domain.postgres.Client client) {
		if (client == null || client.getJson() == null || !(client.getJson() instanceof Client)) {
			return null;
		}
		
		return (Client) client.getJson();
	}
	
	private org.opensrp.domain.postgres.Client convert(Client client, Long primaryKey) {
		if (client == null) {
			return null;
		}
		
		org.opensrp.domain.postgres.Client pgClient = new org.opensrp.domain.postgres.Client();
		pgClient.setId(primaryKey);
		pgClient.setJson(client);
		
		return pgClient;
	}
	
	private ClientMetadata createMetadata(Client client, Long clientId) {
		try {

			System.out.println("Client DATA:->");
			System.out.println(client);

			ClientMetadata clientMetadata = new ClientMetadata();
			clientMetadata.setDocumentId(client.getId());
			clientMetadata.setBaseEntityId(client.getBaseEntityId());
			if (client.getBirthdate() != null) {
				clientMetadata.setBirthDate(client.getBirthdate().toDate());
			}
			clientMetadata.setClientId(clientId);
			clientMetadata.setFirstName(client.getFirstName());
			clientMetadata.setMiddleName(client.getMiddleName());
			clientMetadata.setLastName(client.getLastName());
			Map<String, String> addressFields = client.getAddresses().get(0).getAddressFields();

			if (userAddressInClientMetadata) {
				Address requiredAddress = new Address();

				for (Address address : client.getAddresses()) {
					if (address.getAddressType().equalsIgnoreCase(this.addressType)) {
						requiredAddress = address;
						break;
					}
				}

				if (requiredAddress != null) {
					clientMetadata.setAddress1(requiredAddress.getAddressField("address1"));
					clientMetadata.setAddress2(requiredAddress.getAddressField("address2"));
					clientMetadata.setAddress3(requiredAddress.getCityVillage());
					if (requiredAddress.getAddressField("address8") != null)
						clientMetadata.setVillageId(Long.valueOf(requiredAddress.getAddressField("address8")));
				}
			}

			clientMetadata.setDivision(addressFields.get("division"));
			clientMetadata.setDistrict(addressFields.get("district"));
			clientMetadata.setCityCorporation(addressFields.get("cityCorporation"));
			clientMetadata.setUpazila(addressFields.get("upazila"));
			clientMetadata.setCityUnion("union");
			clientMetadata.setWard("ward");
			clientMetadata.setVillage("village");
			String relationalId = null;
			Map<String, List<String>> relationShips = client.getRelationships();
			if (relationShips != null && !relationShips.isEmpty()) {
				for (Map.Entry<String, List<String>> maEntry : relationShips.entrySet()) {
					List<String> values = maEntry.getValue();
					if (values != null && !values.isEmpty()) {
						relationalId = values.get(0);
						break;
					}
				}
			}
			clientMetadata.setRelationalId(relationalId);
			
			String uniqueId = null;
			String openmrsUUID = null;
			Map<String, String> identifiers = client.getIdentifiers();
			if (identifiers != null && !identifiers.isEmpty()) {
				for (Map.Entry<String, String> entry : identifiers.entrySet()) {
					String value = entry.getValue();
					if (StringUtils.isNotBlank(value)) {
						if (AllConstants.Client.OPENMRS_UUID_IDENTIFIER_TYPE.equalsIgnoreCase(entry.getKey())) {
							openmrsUUID = value;
						} else {
							uniqueId = value;
						}
					}
				}
			}
			
			clientMetadata.setUniqueId(uniqueId);
			clientMetadata.setOpenmrsUuid(openmrsUUID);
			clientMetadata.setServerVersion(client.getServerVersion());
			if (client.getDateVoided() != null)
				clientMetadata.setDateDeleted(client.getDateVoided().toDate());
			return clientMetadata;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected Long retrievePrimaryKey(Client t) {
		Object uniqueId = getUniqueField(t);
		if (uniqueId == null) {
			return null;
		}
		
		String baseEntityId = uniqueId.toString();
		
		ClientMetadataExample clientMetadataExample = new ClientMetadataExample();
		clientMetadataExample.createCriteria().andBaseEntityIdEqualTo(baseEntityId).andDateDeletedIsNull();
		
		org.opensrp.domain.postgres.Client pgClient = clientMetadataMapper.selectOne(baseEntityId);
		if (pgClient == null) {
			return null;
		}
		return pgClient.getId();
	}
	
	@Override
	protected Object getUniqueField(Client t) {
		if (t == null) {
			return null;
		}
		return t.getBaseEntityId();
	}

	@Override
	public CustomQuery findUserStatus(String username) {
		// TODO Auto-generated method stub
		return clientMapper.selectUserStatus(username);
	}

	@Override
	public void updateAppVersion(String username,String version) {
		clientMapper.updateAppVersion(username, version);
		
	}

	@Override
	public Integer findClientIdByBaseEntityId(String baseEntityId) {
		// TODO Auto-generated method stub
		return clientMetadataMapper.findClientIdByBaseEntityId(baseEntityId);
	}

	@Override
	public Client findClientByClientId(Integer clientId) {
		if (clientId == null) {
			return null;
		}

		org.opensrp.domain.postgres.Client pgClient = clientMetadataMapper.findClientByClientId(clientId);
		if(pgClient != null){
			return convert(pgClient);
		}
		return null;
	}

	@Override
	public List<CustomQuery> getDistrictAndUpazila(Integer parentLocationTag) {
		return clientMapper.getDistrictAndUpazila(parentLocationTag);
	}


}
