package org.opensrp.camp.dto;

import org.codehaus.jackson.annotate.JsonProperty;

public class CampDateDTO {
	
	@JsonProperty("session_date")
	private String session_date;	
	@JsonProperty("status")
	private String status;
	@JsonProperty("timestamp")
	private long timestamp;
	
	public CampDateDTO() {
		// TODO Auto-generated constructor stub
	}

	public CampDateDTO(String session_date, String status, long timestamp) {
	    super();
	    this.session_date = session_date;
	    this.status = status;
	    this.timestamp = timestamp;
    }

	
    public String getSession_date() {
    	return session_date;
    }

	
    public void setSession_date(String session_date) {
    	this.session_date = session_date;
    }

	
    public String getStatus() {
    	return status;
    }

	
    public void setStatus(String status) {
    	this.status = status;
    }

	
    public long getTimestamp() {
    	return timestamp;
    }

	
    public void setTimestamp(long timestamp) {
    	this.timestamp = timestamp;
    }

	@Override
    public String toString() {
	    return "CampDateDTO [session_date=" + session_date + ", status=" + status + ", timestamp=" + timestamp + "]";
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((session_date == null) ? 0 : session_date.hashCode());
	    result = prime * result + ((status == null) ? 0 : status.hashCode());
	    result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    CampDateDTO other = (CampDateDTO) obj;
	    if (session_date == null) {
		    if (other.session_date != null)
			    return false;
	    } else if (!session_date.equals(other.session_date))
		    return false;
	    if (status == null) {
		    if (other.status != null)
			    return false;
	    } else if (!status.equals(other.status))
		    return false;
	    if (timestamp != other.timestamp)
		    return false;
	    return true;
    }
	
}
