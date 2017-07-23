package org.opensrp.service;

import java.util.List;
import org.joda.time.DateTime;
import org.opensrp.domain.Report;
import org.opensrp.repository.AllReports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
	
	private final AllReports allReports;
	
	private static Logger logger = LoggerFactory.getLogger(ReportService.class.toString());
	
	@Autowired
	public ReportService(AllReports allReports) {
		this.allReports = allReports;
	}
	
	public List<Report> findAllByIdentifier(String identifier) {
		return allReports.findAllByIdentifier(identifier);
	}
	
	public List<Report> findByServerVersion(long serverVersion) {
		return allReports.findByServerVersion(serverVersion);
	}
	
	public Report getById(String id) {
		return allReports.findById(id);
	}
	
	public List<Report> getAll() {
		return allReports.getAll();
	}
	
	public Report find(String uniqueId) {
		List<Report> reportList = allReports.findAllByIdentifier(uniqueId);
		if (reportList.size() > 1) {
			throw new IllegalArgumentException("Multiple reports with identifier " + uniqueId + " exist.");
		} else if (reportList.size() != 0) {
			return reportList.get(0);
		}
		return null;
	}
	
	public Report find(Report report) {
		for (String idt : report.getIdentifiers().keySet()) {
			List<Report> reportList = allReports.findAllByIdentifier(report.getIdentifier(idt));
			if (reportList.size() > 1) {
				throw new IllegalArgumentException(
				        "Multiple reports with identifier type " + idt + " and ID " + report.getIdentifier(idt) + " exist.");
			} else if (reportList.size() != 0) {
				return reportList.get(0);
			}
		}
		return null;
	}
	
	public Report findById(String reportId) {
		try {
			if (reportId == null || reportId.isEmpty()) {
				return null;
			}
			return allReports.findById(reportId);
		}
		catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	public synchronized Report addReport(Report report) {
		Report existingReport = find(report);
		if (existingReport != null) {
			throw new IllegalArgumentException(
			        "An report already exists with given list of identifiers. Consider updating data.[" + existingReport
			                + "]");
		}
		
		if (report.getFormSubmissionId() != null
		        && getByBaseEntityAndFormSubmissionId(report.getBaseEntityId(), report.getFormSubmissionId()) != null) {
			throw new IllegalArgumentException(
			        "An report already exists with given baseEntity and formSubmission combination. Consider updating");
		}
		
		report.setDateCreated(DateTime.now());
		allReports.add(report);
		return report;
	}
	
	public void updateReport(Report updatedReport) {
		// If update is on original entity
		if (updatedReport.isNew()) {
			throw new IllegalArgumentException(
			        "Report to be updated is not an existing and persisting domain object. Update database object instead of new pojo");
		}
		
		updatedReport.setDateEdited(DateTime.now());
		
		allReports.update(updatedReport);
	}
	
	public synchronized Report addorUpdateReport(Report report) {
		Report existingReport = findById(report.getId());
		if (existingReport != null) {
			report.setDateEdited(DateTime.now());
			report.setServerVersion(null);
			report.setRevision(existingReport.getRevision());
			allReports.update(report);
			
		} else {
			allReports.add(report);
			
		}
		
		return report;
	}
	
	public Report getByBaseEntityAndFormSubmissionId(String baseEntityId, String formSubmissionId) {
		List<Report> reportList = allReports.findByBaseEntityAndFormSubmissionId(baseEntityId, formSubmissionId);
		if (reportList.size() > 1) {
			throw new IllegalStateException("Multiple reports for baseEntityId and formSubmissionId combination ("
			        + baseEntityId + "," + formSubmissionId + ")");
		}
		if (reportList.size() == 0) {
			return null;
		}
		return reportList.get(0);
	}
	
	public List<Report> findByBaseEntityId(String baseEntityId) {
		return allReports.findByBaseEntityId(baseEntityId);
	}
	
	public List<Report> findReports(String team, String providerId, String locationId, Long serverVersion, String sortBy,
	                                String sortOrder, int limit) {
		return allReports.findReports(team, providerId, locationId, null, serverVersion, sortBy, sortOrder, limit);
	}
	
	public List<Report> findReports(String team, String providerId, String locationId, String baseEntityId,
	                                Long serverVersion, String sortBy, String sortOrder, int limit) {
		return allReports.findReports(team, providerId, locationId, baseEntityId, serverVersion, sortBy, sortOrder, limit);
	}
	
}
