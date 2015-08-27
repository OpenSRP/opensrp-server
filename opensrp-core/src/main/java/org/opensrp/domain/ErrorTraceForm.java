package org.opensrp.domain;

import java.util.ArrayList;
import java.util.List;

public class ErrorTraceForm {
	
	private ErrorTrace errorTrace;

	private List<String> statusOptions=new ArrayList<String>();
	
	public ErrorTraceForm() {
		if(null==statusOptions){
			
			statusOptions=new ArrayList<String>();
		}
		statusOptions.add("solved");
		statusOptions.add("unsolved");
		statusOptions.add("failed");
		statusOptions.add("closed");
		statusOptions.add("acknowledged");
	}
	
	public ErrorTrace getErrorTrace() {
		return errorTrace;
	}

	public void setErrorTrace(ErrorTrace errorTrace) {
		this.errorTrace = errorTrace;
	}

	public List<String> getStatusOptions() {
		return statusOptions;
	}

	public void setStatusOptions(List<String> statusOptions) {
		this.statusOptions = statusOptions;
	}


}
