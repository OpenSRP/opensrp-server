package org.opensrp.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.motechproject.scheduler.domain.MotechEvent;

import com.google.gson.Gson;

public class SystemEvent <T>{

	public final String SUBJECT;

    protected T data;

    public SystemEvent(String subject, T data) {
    	this.SUBJECT = subject;
        this.data = data;
    }

    public SystemEvent(Enum subject, T data) {
    	this.SUBJECT = subject.name();
        this.data = data;
    }
    
    public MotechEvent toMotechEvent() {
        return toMotechEvent(null);
    }
    
    public MotechEvent toMotechEvent(Map<String, Object> parameters) {
    	if(parameters == null){
    		parameters = new HashMap<>();
    	}
        parameters.put("data", new Gson().toJson(data));
        return new MotechEvent(SUBJECT, parameters);
    }
}
