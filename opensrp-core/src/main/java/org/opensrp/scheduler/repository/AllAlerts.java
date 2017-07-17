package org.opensrp.scheduler.repository;

import org.apache.commons.lang3.StringUtils;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.opensrp.common.AllConstants;
import org.opensrp.dto.AlertStatus;
import org.opensrp.scheduler.Alert;
import org.opensrp.scheduler.Alert.AlertType;
import org.opensrp.scheduler.Alert.TriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.List;

@Repository
public class AllAlerts extends MotechBaseRepository<Alert> {
    private static Logger logger = LoggerFactory.getLogger(AllAlerts.class.toString());

    @Autowired
    protected AllAlerts(@Qualifier(AllConstants.OPENSRP_DATABASE_CONNECTOR) CouchDbConnector db) {
        super(Alert.class, db);
    }

    @View(name = "alert_by_provider_and_time", map = "function(doc) { if (doc.type === 'Alert') { emit([doc.providerId, doc.timeStamp], null); } }")
    public List<Alert> findByProviderAndTimestamp(String provider, long timeStamp) {
        ComplexKey startKey = ComplexKey.of(provider, timeStamp + 1);
        ComplexKey endKey = ComplexKey.of(provider, Long.MAX_VALUE);
        return db.queryView(createQuery("alert_by_provider_and_time").startKey(startKey).endKey(endKey).includeDocs(true), Alert.class);
    }

    @View(name = "alert_by_provider_and_time_active", map = "function(doc) { if (doc.type === 'Alert' && doc.isActive) { emit([doc.providerId, doc.timeStamp], null); } }")
    public List<Alert> findActiveByProviderAndTimestamp(String provider, long timeStamp) {
        ComplexKey startKey = ComplexKey.of(provider, timeStamp + 1);
        ComplexKey endKey = ComplexKey.of(provider, Long.MAX_VALUE);
        return db.queryView(createQuery("alert_by_provider_and_time_active").startKey(startKey).endKey(endKey).includeDocs(true), Alert.class);
    }
    
    @View(name = "alert_by_provider_entityId_trigger",
            map = "function(doc) { " +
                    "if(doc.type === 'Alert') {" +
                    "emit([doc.providerId, doc.entityId, doc.triggerName], null)} " +
                    "}")
    public List<Alert> findAlertByProviderEntityIdTriggerName(String provider, String entityId, String triggerName) {
        ComplexKey key = ComplexKey.of(provider, entityId, triggerName);
        return db.queryView(createQuery("alert_by_provider_entityId_trigger").key(key).includeDocs(true), Alert.class);
    }
    
    @View(name = "alert_by_provider_entityId_trigger_active",
            map = "function(doc) { " +
                    "if(doc.type === 'Alert' && doc.isActive) {" +
                    "emit([doc.providerId, doc.entityId, doc.triggerName], null)} " +
                    "}")
    public List<Alert> findActiveAlertByProviderEntityIdTriggerName(String provider, String entityId, String triggerName) {
        ComplexKey key = ComplexKey.of(provider, entityId, triggerName);
        return db.queryView(createQuery("alert_by_provider_entityId_trigger_active").key(key).includeDocs(true), Alert.class);
    }
    
    @View(name = "alert_by_entityId_trigger_active",
            map = "function(doc) { " +
                    "if(doc.type === 'Alert' && doc.isActive) {" +
                    "emit([doc.entityId, doc.triggerName], null)} " +
                    "}")
    public List<Alert> findActiveAlertByEntityIdTriggerName(String entityId, String triggerName) {
        ComplexKey key = ComplexKey.of(entityId, triggerName);
        return db.queryView(createQuery("alert_by_entityId_trigger_active").key(key).includeDocs(true), Alert.class);
    }
    
    @View(name = "alert_by_entityId_active",
            map = "function(doc) { " +
                    "if(doc.type === 'Alert' && doc.isActive) {" +
                    "emit(doc.entityId, null)} " +
                    "}")
    public List<Alert> findActiveAlertByEntityId(String entityId) {
        return db.queryView(createQuery("alert_by_entityId_active").key(entityId).includeDocs(true), Alert.class);
    }

    @View(name = "alert_by_entityId_and_trigger_and_time", map = "function(doc) { if (doc.type === 'Alert') { emit([doc.entityId, doc.triggerName, doc.timeStamp], null); } }")
    public List<Alert> findByEntityIdTriggerAndTimeStamp(String entityId, String trigger, DateTime start, DateTime end) {
        ComplexKey startKey = ComplexKey.of(entityId, trigger, start.getMillis() + 1);
        ComplexKey endKey = ComplexKey.of(entityId, trigger, end.getMillis());
        return db.queryView(createQuery("alert_by_entityId_and_trigger_and_time").startKey(startKey).endKey(endKey).includeDocs(true), Alert.class);
    }
    
    public void markAllAsClosedFor(String entityId, String reasonForClose) {
        List<Alert> actions = findActiveAlertByEntityId(entityId);
        for (Alert action : actions) {
            action.markAlertAsClosed(reasonForClose);;
        }
        db.executeBulk(actions);
    }

    @GenerateView
    private List<Alert> findByBeneficiaryType(String beneficiaryType) {
        return queryView("by_beneficiaryType", beneficiaryType);
    }
    
    @GenerateView
    private List<Alert> findByAlertType(String alertType) {
        return queryView("by_alertType", alertType);
    }
    
    @GenerateView
    private List<Alert> findByTriggerType(String triggerType) {
        return queryView("by_triggerType", triggerType);
    }

    @GenerateView
    private List<Alert> findByEntityId(String entityId) {
        return queryView("by_entityId", entityId);
    }

    public void markAlertAsClosedFor(String providerId, String entityId, String triggerName, String reasonForClose) {
        List<Alert> existingAlerts = findActiveAlertByProviderEntityIdTriggerName(providerId, entityId, triggerName);
        if (existingAlerts.size() > 1) {
            logger.warn(MessageFormat.format("Found more than one alert for the combination of providerId: {0}, entityId: {1} and triggerName : {2}. Alerts : {3}",
                    providerId, entityId, triggerName, existingAlerts));
        }
        for (Alert existingAlert : existingAlerts) {
            existingAlert.markAlertAsClosed(reasonForClose);
        }
        db.executeBulk(existingAlerts);
    }
    
    public void markAlertAsCompleteFor(String providerId, String entityId, String triggerName, String completionDate) {
        List<Alert> existingAlerts = findActiveAlertByProviderEntityIdTriggerName(providerId, entityId, triggerName);
        if (existingAlerts.size() > 1) {
            logger.warn(MessageFormat.format("Found more than one alert for the combination of providerId: {0}, entityId: {1} and triggerName : {2}. Alerts : {3}",
                    providerId, entityId, triggerName, existingAlerts));
        }
        for (Alert existingAlert : existingAlerts) {
            existingAlert.markAlertAsComplete(completionDate);
        }
        db.executeBulk(existingAlerts);
    }
    
    public void addOrUpdateScheduleNotificationAlert(String beneficiaryType, String entityId, String providerId, 
    		String triggerName, String triggerCode, AlertStatus alertStatus, DateTime startDate, DateTime expiryDate) {
        List<Alert> existingAlerts =  findActiveAlertByProviderEntityIdTriggerName(providerId, entityId, triggerName);
        if (existingAlerts.size() > 1) {
            logger.warn(MessageFormat.format("Found more than one active alerts for the combination of "
            		+ "providerId: {0}, entityId: {1} and triggerName: {2}", providerId, entityId, triggerName));
        }
        
        if(existingAlerts.size() == 0){
        	add(new Alert(providerId, entityId, beneficiaryType, AlertType.notification, TriggerType.schedule, triggerName, triggerCode, startDate, expiryDate, alertStatus, null));        	
        }
        else {
        	Alert al = existingAlerts.get(0);
        	// if visit code is same then update otherwise add another record
        	if(StringUtils.isNotBlank(al.triggerCode()) && StringUtils.isNotBlank(triggerCode) 
        			&& al.triggerCode().equalsIgnoreCase(triggerCode)){
        		al.setAlertStatus(alertStatus.name());
        		al.setStartDate(startDate.toString());
        		al.setExpiryDate(expiryDate.toString());
        		al.details().put(alertStatus.name()+":start", startDate.toString());
        		al.details().put(alertStatus.name()+":end", expiryDate.toString());
        		
        		update(al);
        	}
        	else {
            	add(new Alert(providerId, entityId, beneficiaryType, AlertType.notification, TriggerType.schedule, triggerName, triggerCode, startDate, expiryDate, alertStatus, null));        	
        	}
        }
    }
    
    public void markAlertAsCompleteFor(String entityId, String triggerName, String completionDate) {
        List<Alert> existingAlerts = findActiveAlertByEntityIdTriggerName(entityId, triggerName);
        if (existingAlerts.size() > 1) {
            logger.warn(MessageFormat.format("Found more than one alert for the combination of entityId: {0} and triggerName : {1}. Alerts : {2}",
                    entityId, triggerName, existingAlerts));
        }
        for (Alert existingAlert : existingAlerts) {
            existingAlert.markAlertAsComplete(completionDate);
        }
        db.executeBulk(existingAlerts);
    }
}
