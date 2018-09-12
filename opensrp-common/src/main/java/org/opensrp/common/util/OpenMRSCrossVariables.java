package org.opensrp.common.util;

public enum OpenMRSCrossVariables {

	TEAM_MEMBER_URL {
		public String makeVariable(String openMRSVersion) {
			if (openMRSVersion.startsWith("1")) {
				return "ws/rest/v1/teammodule/member";
			} else {
				return "ws/rest/v1/team/teammember";
			}
		}
	},

	LOCATIONS_JSON_KEY {
		public String makeVariable(String openMRSVersion) {
			if (openMRSVersion.startsWith("1")) {
				return "location";
			} else {
				return "locations";
			}
		}
	};

	public abstract String makeVariable(String openMRSVersion);

}
