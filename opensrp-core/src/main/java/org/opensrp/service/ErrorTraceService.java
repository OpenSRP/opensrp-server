package org.opensrp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ektorp.DocumentNotFoundException;
import org.joda.time.DateTime;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.repository.AllErrorTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author muhammad.ahmed@ihsinformatics.com
 *         Created on May 25, 2015
 */
@Service
public class ErrorTraceService {

	private final AllErrorTrace allErrorTrace;

	@Autowired
	public ErrorTraceService(AllErrorTrace allErrorTrace) {
		this.allErrorTrace = allErrorTrace;
	}

	public void addError(ErrorTrace entity) {
		allErrorTrace.add(entity);
	}

	/**
	 * @param errorType
	 * @param documentType
	 * @param recordId
	 * @param stackTrace
	 * @param retryURL     this method is used for logs and it should be called on Exception Catch .
	 *                     retryURL should be given by developer, it is for resubmission or retry of that particular record .
	 */
	public void log(String errorType, String documentType, String recordId, String stackTrace, String retryURL) {
		ErrorTrace error = new ErrorTrace();
		error.setErrorType(errorType);
		error.setDocumentType(documentType);
		error.setRecordId(recordId);
		error.setStackTrace(stackTrace);
		error.setRetryUrl(retryURL);
		error.setDateOccurred(DateTime.now());
		addError(error);

	}

	public void updateError(ErrorTrace entity) {
		allErrorTrace.update(entity);
	}

	public List<ErrorTrace> getAllErrors() throws DocumentNotFoundException {

		List<ErrorTrace> allErrorList = allErrorTrace.findAllErrors();
		if (null == allErrorList || allErrorList.isEmpty()) {
			return null;

		}

		return allErrorList;

	}

	public List<ErrorTrace> getAllSolvedErrors() throws DocumentNotFoundException {

		List<ErrorTrace> allErrorList = allErrorTrace.findAllSolvedErrors();
		if (null == allErrorList || allErrorList.isEmpty()) {
			return null;

		}

		return allErrorList;

	}

	public List<ErrorTrace> getAllUnsolvedErrors() throws DocumentNotFoundException {

		List<ErrorTrace> allErrorList = allErrorTrace.findAllUnSolvedErrors();
		if (null == allErrorList || allErrorList.isEmpty()) {
			return null;

		}

		return allErrorList;

	}

	public ErrorTrace getError(String id) throws DocumentNotFoundException {

		return allErrorTrace.findById(id);

	}

}
