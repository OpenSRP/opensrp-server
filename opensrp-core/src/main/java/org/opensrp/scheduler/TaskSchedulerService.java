package org.opensrp.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.scheduler.domain.RepeatingSchedulableJob;
import org.motechproject.scheduler.gateway.OutboundEventGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskSchedulerService {
	private MotechSchedulerService motechSchedulerService;
	private OutboundEventGateway gateway;
	private AlertRouter router;
   
	@Autowired
	public TaskSchedulerService(MotechSchedulerService schedulerService, OutboundEventGateway gateway, AlertRouter router) {
		this.motechSchedulerService = schedulerService;
		this.gateway = gateway;
		this.router = router;
	}
	
	public void startJob(RepeatingSchedulableJob job) {
		motechSchedulerService.safeScheduleRepeatingJob(job);
    }
	
	public void startJob(CronSchedulableJob job) {
		motechSchedulerService.safeScheduleJob(job);
    }
	
	public void startJob(RepeatingSchedule job) {
		Date startTime = DateTime.now().plusMillis((int) job.getStartDelayMilis()).toDate();
		Map<String, Object> data = job.getData();
		if(data == null){
			data = new HashMap<>();
		}
        MotechEvent event = new MotechEvent(job.SUBJECT, data);
        startJob(new RepeatingSchedulableJob(event, startTime, job.getEndTime(), job.getRepeatIntervalMilis()));
    }
	
	public void startJob(RepeatingCronSchedule job) {
		Date startTime = DateTime.now().plusMillis((int) job.getStartDelayMilis()).toDate();
		Map<String, Object> data = job.getData();
		if(data == null){
			data = new HashMap<>();
		}
        MotechEvent event = new MotechEvent(job.SUBJECT, data);
        startJob(new CronSchedulableJob(event, job.CRON, startTime, job.getEndTime()));
    }
	
	public void notifyEvent(SystemEvent<?> event){
		gateway.sendEventMessage(event.toMotechEvent());
	}
	
	public void notifyEvent(MotechEvent event){
		gateway.sendEventMessage(event);
	}
	
	public Route addHookedEvent(Matcher scheduleMatcher, Matcher milestoneMatcher, Matcher windowMatcher, HookedEvent action){
		return addHookedEvent(scheduleMatcher, milestoneMatcher, windowMatcher, action, null);
	}
	
	public Route addHookedEvent(Matcher scheduleMatcher, Matcher milestoneMatcher, Matcher windowMatcher, 
			HookedEvent action, Map<String, String> extraData){
        Route route = new Route(scheduleMatcher, milestoneMatcher, windowMatcher, action, extraData);
        return router.addRoute(route);
	}
}
