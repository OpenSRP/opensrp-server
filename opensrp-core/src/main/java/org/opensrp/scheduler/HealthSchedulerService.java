package org.opensrp.scheduler;

import static org.opensrp.dto.AlertStatus.normal;
import static org.opensrp.dto.AlertStatus.upcoming;
import static org.opensrp.dto.AlertStatus.urgent;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.scheduletracking.api.domain.WindowName;
import org.motechproject.scheduletracking.api.service.EnrollmentRecord;
import org.opensrp.dto.AlertStatus;
import org.opensrp.dto.BeneficiaryType;
import org.opensrp.scheduler.service.ActionService;
import org.opensrp.scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HealthSchedulerService {
	private ActionService actionService;
	private final ScheduleService scheduleService;

	@Autowired
	public HealthSchedulerService(ActionService actionService, ScheduleService scheduleService) {
		this.actionService = actionService;
		this.scheduleService = scheduleService;
	}
	
	public void enrollIntoSchedule(String entityId, String schedule, LocalDate referenceDate) {
		scheduleService.enroll(entityId, schedule, referenceDate.toString());
	}
	
	public void enrollIntoSchedule(String entityId, String schedule, String referenceDate) {
		scheduleService.enroll(entityId, schedule, referenceDate);
	}
	
	public void enrollIntoSchedule(String entityId, String schedule, String milestone, String referenceDate) {
		scheduleService.enroll(entityId, schedule, milestone, referenceDate);
	}
	
	public void fullfillMilestoneAndCloseAlert(String entityId, String providerId, String scheduleName, String milestone, LocalDate completionDate) {
		scheduleService.fulfillMilestone(entityId, scheduleName, completionDate);
        actionService.markAlertAsClosed(entityId, providerId, milestone, completionDate.toString());
	}
	
	public void fullfillMilestoneAndCloseAlert(String entityId, String providerId, String scheduleName, LocalDate completionDate) {
		scheduleService.fulfillMilestone(entityId, scheduleName, completionDate);
        actionService.markAlertAsClosed(entityId, providerId, scheduleName, completionDate.toString());
	}
	
	public void unEnrollFromSchedule(String entityId, String providerId, String scheduleName) {
		scheduleService.unenroll(entityId, scheduleName);
        actionService.markAlertAsInactive(providerId, entityId, scheduleName);
    }
	
	public void unEnrollAndCloseSchedule(String entityId, String providerId, String scheduleName, LocalDate completionDate) {
		scheduleService.unenroll(entityId, scheduleName);
        actionService.markAlertAsClosed(providerId, entityId, scheduleName, completionDate.toString());
    }
	
	public void unEnrollFromAllSchedules(String entityId) {
        List<String> activeSchedules = scheduleService.findOpenEnrollmentNames(entityId);

		scheduleService.unenroll(entityId, activeSchedules);
        actionService.markAllAlertsAsInactive(entityId);
    }
	
	public void closeBeneficiary(BeneficiaryType beneficiary, String caseId, String anmIdentifier, String reasonForClose) {
		actionService.closeBeneficiary(beneficiary, caseId, anmIdentifier, reasonForClose);
    }
	
	public List<EnrollmentRecord> findActiveEnrollments(String entityId) {
        return scheduleService.findOpenEnrollments(entityId);
	}
	
	public List<String> findActiveSchedules(String entityId) {
        return scheduleService.findOpenEnrollmentNames(entityId);
	}
	
	public boolean isNotEnrolled(String entityId, String scheduleName) {
        return scheduleService.getEnrollment(entityId, scheduleName) == null;
    }
	
	public EnrollmentRecord getEnrollment(String entityId, String scheduleName) {
        return scheduleService.getEnrollment(entityId, scheduleName);
    }
	
	public void alertFor(String windowName, BeneficiaryType beneficiaryType, String entityId, 
			String providerId, String schedule, String milestone, 
			DateTime startOfDueWindow, DateTime startOfLateWindow, DateTime startOfMaxWindow) {
		if (WindowName.late.toString().equals(windowName)) {
            actionService.alertForBeneficiary(beneficiaryType, entityId, providerId, schedule, milestone, urgent, startOfLateWindow, startOfMaxWindow);
        } else if (WindowName.earliest.toString().equals(windowName)) {
            actionService.alertForBeneficiary(beneficiaryType, entityId, providerId, schedule, milestone, upcoming, startOfDueWindow, startOfLateWindow);
        } else {
            actionService.alertForBeneficiary(beneficiaryType, entityId, providerId, schedule, milestone, normal, startOfDueWindow, startOfLateWindow);
        }
	}
	
	public void alertFor(BeneficiaryType beneficiaryType, String entityId, String providerId, String schedule, 
			String milestone, AlertStatus alertStatus, DateTime startDate, DateTime expiryDate) {
		actionService.alertForBeneficiary(beneficiaryType, entityId, providerId, schedule, milestone, alertStatus,
				startDate, expiryDate);
	}
}
