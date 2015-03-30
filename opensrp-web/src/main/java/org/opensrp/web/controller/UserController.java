package org.opensrp.web.controller;

import static org.opensrp.web.HttpHeaderFactory.allowOrigin;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.opensrp.api.domain.Location;
import org.opensrp.api.domain.User;
import org.opensrp.common.domain.UserDetail;
import org.opensrp.connector.openmrs.service.LocationService;
import org.opensrp.web.security.DrishtiAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class UserController {
    private String opensrpSiteUrl;
    private DrishtiAuthenticationProvider opensrpAuthenticationProvider;
	private LocationService openmrsLocationService;
	
    @Autowired
    public UserController(LocationService openmrsLocationService,
            DrishtiAuthenticationProvider opensrpAuthenticationProvider) {
		this.openmrsLocationService = openmrsLocationService;
        this.opensrpAuthenticationProvider = opensrpAuthenticationProvider;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authenticate-user")
    public ResponseEntity<HttpStatus> authenticateUser() {
        return new ResponseEntity<>(null, allowOrigin(opensrpSiteUrl), OK);
    }

    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return opensrpAuthenticationProvider.getDrishtiUser(authentication);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user-details")
    public ResponseEntity<UserDetail> userDetail(@RequestParam("anm-id") String anmIdentifier) {
        User user = opensrpAuthenticationProvider.getDrishtiUser(anmIdentifier);
        return new ResponseEntity<>(new UserDetail(user.getUsername(), user.getRoles()), allowOrigin(opensrpSiteUrl), OK);
    }

	@RequestMapping("/security/authenticate")
	@ResponseBody
	public ResponseEntity<String> authenticate() throws JSONException {
        User u = currentUser();

		Location l = openmrsLocationService.getLocation((String) u.getBaseEntity().getAttribute("Location"));
		Map<String, Object> map = new HashMap<>();
		map.put("user", u);
		map.put("location", l);
        return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
	}
}
