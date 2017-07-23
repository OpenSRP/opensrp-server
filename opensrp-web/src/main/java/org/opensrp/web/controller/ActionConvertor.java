package org.opensrp.web.controller;

import org.opensrp.dto.Action;
import org.opensrp.dto.ActionData;

public class ActionConvertor {
    public static Action from(org.opensrp.scheduler.Action action){
        return new Action(action.baseEntityId(), action.getTarget(), action.getActionType(), action.data(), String.valueOf(action.getTimestamp()), action.getIsActionActive(), action.getDetails());
    }

    public static org.opensrp.scheduler.Action toAction(Action actionItem, String anmIdentifier) {
        return new org.opensrp.scheduler.Action(actionItem.getCaseID(), anmIdentifier, ActionData.from(actionItem.getActionType(), actionItem.getActionTarget(), actionItem.getData(), actionItem.getDetails()));
    }
}
