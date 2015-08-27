package org.opensrp.scheduler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RepeatingCronSchedule {
	public final String SUBJECT;
	public final int START_DELAY;
    public final TimeUnit START_DELAY_UNIT;
    public final String CRON;
	protected Date endTime;
	protected Map<String, Object> data;
    
	public RepeatingCronSchedule(String subject, int startDelay, TimeUnit startDelayUnit
			, String cronExpression) {
		this.SUBJECT = subject;
		this.START_DELAY = startDelay;
		this.START_DELAY_UNIT = startDelayUnit;   
		this.CRON = cronExpression;
	}
	
	public RepeatingCronSchedule(String subject, int startDelay, TimeUnit startDelayUnit
			, String cronExpression, Date endTime, Map<String, Object> data) {
		this.SUBJECT = subject;
		this.START_DELAY = startDelay;
		this.START_DELAY_UNIT = startDelayUnit;   
		this.CRON = cronExpression;
		this.endTime = endTime;
		this.data = data;
	}

	public Date getEndTime() {
		return endTime;
	}
	
	public RepeatingCronSchedule withEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public RepeatingCronSchedule addData(String key, Object value) {
		data.put(key, value);
		return this;
	}
	
	public RepeatingCronSchedule withData(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	public long getStartDelayMilis() {
		return START_DELAY*millisConverter(START_DELAY_UNIT);
	}
	
	protected long millisConverter(TimeUnit unit){
		long millis = 1L;
		switch (unit) {
		case DAYS:
			millis = 1000*60*60*24L;
			break;
		case HOURS:
			millis = 1000*60*60L;
			break;
		case MICROSECONDS:
			break;
		case MILLISECONDS:
			millis = 1;
			break;
		case MINUTES:
			millis = 1000*60L;
			break;
		case NANOSECONDS:
			break;
		case SECONDS:
			millis = 1000;
			break;
		default:
			millis = 1;
			break;
		}
		
		return millis;
	}
}
