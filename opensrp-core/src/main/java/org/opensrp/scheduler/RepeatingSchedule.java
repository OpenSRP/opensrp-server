package org.opensrp.scheduler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RepeatingSchedule {
	public final String SUBJECT;
	public final int START_DELAY;
    public final int REPEAT_INTERVAL;
    public final TimeUnit START_DELAY_UNIT;
    public final TimeUnit REPEAT_INTERVAL_UNIT;
	protected Date endTime;
	protected Map<String, Object> data;
    
	public RepeatingSchedule(String subject, int startDelay, TimeUnit startDelayUnit
			, int repeatInterval, TimeUnit repeatIntervalUnit) {
		this.SUBJECT = subject;
		this.START_DELAY = startDelay;
		this.REPEAT_INTERVAL = repeatInterval;
		this.START_DELAY_UNIT = startDelayUnit;   
		this.REPEAT_INTERVAL_UNIT = repeatIntervalUnit;
	}
	
	public RepeatingSchedule(String subject, int startDelay, TimeUnit startDelayUnit
			, int repeatInterval, TimeUnit repeatIntervalUnit, Date endTime, Map<String, Object> data) {
		this.SUBJECT = subject;
		this.START_DELAY = startDelay;
		this.REPEAT_INTERVAL = repeatInterval;
		this.START_DELAY_UNIT = startDelayUnit;   
		this.REPEAT_INTERVAL_UNIT = repeatIntervalUnit;
		this.endTime = endTime;
		this.data = data;
	}

	public Date getEndTime() {
		return endTime;
	}
	
	public RepeatingSchedule withEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public RepeatingSchedule addData(String key, Object value) {
		data.put(key, value);
		return this;
	}
	
	public RepeatingSchedule withData(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	public long getStartDelayMilis() {
		return START_DELAY*millisConverter(START_DELAY_UNIT);
	}
	
	public long getRepeatIntervalMilis() {
		return REPEAT_INTERVAL*millisConverter(REPEAT_INTERVAL_UNIT);
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
