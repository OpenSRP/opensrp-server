package org.opensrp.web.rest.it;

import org.opensrp.domain.Client;
import org.opensrp.domain.ErrorTrace;
import org.opensrp.repository.AllClients;
import org.opensrp.repository.AllErrorTrace;
import org.opensrp.scheduler.Action;
import org.opensrp.scheduler.Alert;
import org.opensrp.scheduler.repository.AllActions;
import org.opensrp.scheduler.repository.AllAlerts;

import java.util.List;

public final class ResourceTestUtility {

	private ResourceTestUtility() {
	}

	public static void createClients(List<Client> allClient, AllClients allClients) {
		for (Client client : allClient) {
			allClients.add(client);
		}
	}

	public static void createActions(List<Action> actions, AllActions allActions) {
		for (Action action : actions) {
			allActions.add(action);
		}
	}

	public static void createAlerts(List<Alert> alerts, AllAlerts allAlerts) {
		for (Alert alert : alerts) {
			allAlerts.add(alert);
		}
	}

	public static void createErrorTraces(List<ErrorTrace> errorTraces, AllErrorTrace allErrorTrace) {
		for (ErrorTrace errorTrace : errorTraces) {
			allErrorTrace.add(errorTrace);
		}
	}

}
