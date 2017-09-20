package org.opensrp.web.controller;

import static ch.lambdaj.collection.LambdaCollections.with;
import static org.opensrp.common.AllConstants.Event.PROVIDER_ID;
import static org.opensrp.web.rest.RestUtils.getIntegerFilter;
import static org.opensrp.web.rest.RestUtils.getStringFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.opensrp.common.AllConstants.BaseEntity;
import org.opensrp.domain.Client;
import org.opensrp.dto.Action;
import org.opensrp.repository.AllClients;
import org.opensrp.scheduler.Alert;
import org.opensrp.scheduler.repository.AllAlerts;
import org.opensrp.scheduler.service.ActionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import ch.lambdaj.function.convert.Converter;

@Controller
public class ActionController {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ActionController.class.toString());

    private ActionService actionService;
    private AllClients allClients;
    private AllAlerts allAlerts;

    @Autowired
    public ActionController(ActionService actionService, AllClients c, AllAlerts allAlerts) {
        this.actionService = actionService;
        this.allClients = c;
        this.allAlerts = allAlerts;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/actions")
    @ResponseBody
    public List<Action> getNewActionForANM(@RequestParam("anmIdentifier") String anmIdentifier, @RequestParam("timeStamp") Long timeStamp){
        List<org.opensrp.scheduler.Action> actions = actionService.getNewAlertsForANM(anmIdentifier, timeStamp);
        return with(actions).convert(new Converter<org.opensrp.scheduler.Action, Action>() {
            @Override
            public Action convert(org.opensrp.scheduler.Action action) {
                return ActionConvertor.from(action);
            }
        });
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/useractions")
    @ResponseBody
    public List<Action> getNewActionForClient(@RequestParam("baseEntityId") String baseEntityId, @RequestParam("timeStamp") Long timeStamp){
        List<org.opensrp.scheduler.Action> actions = actionService.findByCaseIdAndTimeStamp(baseEntityId, timeStamp);
        return with(actions).convert(new Converter<org.opensrp.scheduler.Action, Action>() {
            @Override
            public Action convert(org.opensrp.scheduler.Action action) {
                return ActionConvertor.from(action);
            }
        });
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/alert_delete")
    @ResponseBody
    public void deleteDuplicateAlerts(@RequestParam("key") String key){
    	if(!key.equalsIgnoreCase("20160727KiSafaiMuhim")){
    		throw new RuntimeException("Invalid Key");
    	}
        for (Client c : allClients.findAllClients()) {
			List<Alert> al = allAlerts.findActiveAlertByEntityId(c.getBaseEntityId());
			Logger.getLogger(getClass()).warn(al.size()+" Alerts for "+c.getBaseEntityId());
			Map<String, Alert> am = new HashMap<>();
			for (Alert a : al) {
				if(am.containsKey(a.triggerName())){
					Logger.getLogger(getClass()).warn("Removing trigger "+a.triggerName());
					allAlerts.safeRemove(a);
				}
				else {
					am.put(a.triggerName(), a);					
				}
			}
		}
    }
    /**
	 * Fetch actions ordered by serverVersion ascending order
	 * 
	 * @param request
	 * @return a map response with actions, clients and optionally msg when an error occurs
	 */
	@RequestMapping(value = "/actions/sync", method = RequestMethod.GET)
	@ResponseBody
	protected ResponseEntity<String> sync(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			String providerId = getStringFilter(PROVIDER_ID, request);
			Long lastSyncedServerVersion = Long.valueOf(getStringFilter(BaseEntity.SERVER_VERSIOIN, request)) + 1;
			String team = getStringFilter("team", request);
			Integer limit = getIntegerFilter("limit", request);
			if(limit == null || limit.intValue() == 0){
				limit = 25;
			}
			
			List<org.opensrp.scheduler.Action> actions = new ArrayList<org.opensrp.scheduler.Action>();
			if (team != null || providerId != null ) {
				actions = actionService.findByCriteria(team, providerId, lastSyncedServerVersion, org.opensrp.common.AllConstants.Action.TIMESTAMP, "asc", limit);
				
			}
			response.put("actions", actions);
			response.put("no_of_actions", actions.size());
			
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.OK);

		}
		catch (Exception e) {
			response.put("msg", "Error occurred");
			logger.error("", e);
			return new ResponseEntity<>(new Gson().toJson(response), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/test")
	@ResponseBody
	public String test(){
		return "I am here for test";
	}
}

