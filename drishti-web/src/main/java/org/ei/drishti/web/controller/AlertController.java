package org.ei.drishti.web.controller;

import ch.lambdaj.function.convert.Converter;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ei.drishti.domain.AlertAction;
import org.ei.drishti.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static ch.lambdaj.collection.LambdaCollections.with;

@Controller("alertControllerForWeb")
public class AlertController {
    private AlertService alertService;

    @Autowired
    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/alerts")
    @ResponseBody
    public List<AlertActionItem> getNewAlertsForANM(@RequestParam("anmIdentifier") String anmIdentifier, @RequestParam("timeStamp") Long timeStamp){
        List<AlertAction> alertActions = alertService.getNewAlertsForANM(anmIdentifier, timeStamp);
        return with(alertActions).convert(new Converter<AlertAction, AlertActionItem>() {
            @Override
            public AlertActionItem convert(AlertAction alertAction) {
                return AlertActionItem.from(alertAction);
            }
        });
    }

    protected static class AlertActionItem {
        @JsonProperty
        private String anmIdentifier;
        @JsonProperty
        private String caseID;
        @JsonProperty
        private Map<String, String> data;
        @JsonProperty
        private String alertType;
        @JsonProperty
        private long timeStamp;

        public AlertActionItem(String anmIdentifier, String caseID, Map<String, String> data, long timeStamp, String alertType) {
            this.anmIdentifier = anmIdentifier;
            this.caseID = caseID;
            this.data = data;
            this.timeStamp = timeStamp;
            this.alertType = alertType;
        }

        public static AlertActionItem from(AlertAction alertAction){
            return new AlertActionItem(alertAction.anmIdentifier(), alertAction.caseID(), alertAction.data(), alertAction.timestamp(), alertAction.alertType());
        }
    }
}
