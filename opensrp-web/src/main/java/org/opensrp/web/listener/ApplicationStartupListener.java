package org.opensrp.web.listener;

import java.util.concurrent.TimeUnit;

import org.opensrp.common.AllConstants;
import org.opensrp.common.AllConstants.DHIS2Constants;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.scheduler.RepeatingSchedule;
import org.opensrp.scheduler.TaskSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	public static final String APPLICATION_ID = "/opensrp";
	
	public static final String APPLICATION_ID_FULL = "org.springframework.web.context.WebApplicationContext:"
	        + APPLICATION_ID;
	
	private final TaskSchedulerService scheduler;
	
	private final RepeatingSchedule formSchedule;
	
	private final RepeatingSchedule eventsSchedule;
	
	//private RepeatingSchedule anmReportScheduler;
	//private RepeatingSchedule mctsReportScheduler;
	private RepeatingSchedule openmrsScheduleSyncerScheduler;
	
	private final RepeatingSchedule atomfeedSchedule;
	
	private final RepeatingSchedule encounterSchedule;
	
	private final RepeatingSchedule DHIS2Syncer;
	
	@Autowired
	public ApplicationStartupListener(TaskSchedulerService scheduler,
	    @Value("#{opensrp['form.poll.time.interval']}") int formPollInterval,
	    @Value("#{opensrp['mcts.poll.time.interval.in.minutes']}") int mctsPollIntervalInHours,
	    @Value("#{opensrp['openmrs.scheduletracker.syncer.interval-min']}") int openmrsSchSyncerMin) {
		this.scheduler = scheduler;
		formSchedule = new RepeatingSchedule(AllConstants.FORM_SCHEDULE_SUBJECT, 2, TimeUnit.MINUTES, formPollInterval,
		        TimeUnit.MINUTES);
		//anmReportScheduler = new RepeatingSchedule(DrishtiScheduleConstants.ANM_REPORT_SCHEDULE_SUBJECT, 10, TimeUnit.MINUTES, 6, TimeUnit.HOURS);
		//mctsReportScheduler = new RepeatingSchedule(DrishtiScheduleConstants.MCTS_REPORT_SCHEDULE_SUBJECT, 10, TimeUnit.MINUTES, mctsPollIntervalInHours, TimeUnit.HOURS);
		eventsSchedule = new RepeatingSchedule(AllConstants.EVENTS_SCHEDULE_SUBJECT, 2, TimeUnit.MINUTES, formPollInterval,
		        TimeUnit.MINUTES);
		
		// TODO openmrsScheduleSyncerScheduler = new RepeatingSchedule(OpenmrsConstants.SCHEDULER_TRACKER_SYNCER_SUBJECT, 2, TimeUnit.MINUTES, openmrsSchSyncerMin, TimeUnit.MINUTES);
		atomfeedSchedule = new RepeatingSchedule(OpenmrsConstants.SCHEDULER_OPENMRS_ATOMFEED_SYNCER_SUBJECT, 5,
		        TimeUnit.MINUTES, 1, TimeUnit.MINUTES);
		encounterSchedule = new RepeatingSchedule(OpenmrsConstants.SCHEDULER_OPENMRS_DATA_PUSH_SUBJECT, 5, TimeUnit.MINUTES,
		        1, TimeUnit.MINUTES);
		DHIS2Syncer = new RepeatingSchedule(DHIS2Constants.DHIS2_TRACK_DATA_SYNCER_SUBJECT, 1, TimeUnit.MINUTES, 1,
		        TimeUnit.MINUTES);
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		System.out.println(contextRefreshedEvent.getApplicationContext().getId());
		if (contextRefreshedEvent.getApplicationContext().getId().endsWith(APPLICATION_ID)) {
			scheduler.startJob(formSchedule);
			scheduler.startJob(eventsSchedule);
			//scheduler.startJob(anmReportScheduler);
			//scheduler.startJob(mctsReportScheduler);
			// scheduler.startJob(openmrsScheduleSyncerScheduler);
			scheduler.startJob(atomfeedSchedule);
			scheduler.startJob(encounterSchedule);
			scheduler.startJob(DHIS2Syncer);
			
			System.out.println("STARTED ALL SCHEDULES");
		}
	}
}
