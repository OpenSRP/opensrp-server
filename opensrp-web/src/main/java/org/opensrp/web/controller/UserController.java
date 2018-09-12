package org.opensrp.web.controller;

import static org.opensrp.web.HttpHeaderFactory.allowOrigin;
import static org.springframework.http.HttpStatus.OK;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Time;
import org.opensrp.api.domain.User;
import org.opensrp.api.util.LocationTree;
import org.opensrp.common.domain.UserDetail;
import org.opensrp.common.util.OpenMRSCrossVariables;
import org.opensrp.connector.openmrs.service.OpenmrsLocationService;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.web.security.DrishtiAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;

@Controller
public class UserController {
    private String opensrpSiteUrl;
    private DrishtiAuthenticationProvider opensrpAuthenticationProvider;
	private OpenmrsLocationService openmrsLocationService;
	private OpenmrsUserService openmrsUserService;
	@Value("#{opensrp['openmrs.version']}")
	protected String OPENMRS_VERSION;
	
    @Autowired
    public UserController(OpenmrsLocationService openmrsLocationService, OpenmrsUserService openmrsUserService, 
            DrishtiAuthenticationProvider opensrpAuthenticationProvider) {
		this.openmrsLocationService = openmrsLocationService;
		this.openmrsUserService = openmrsUserService;
        this.opensrpAuthenticationProvider = opensrpAuthenticationProvider;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/authenticate-user")
    public ResponseEntity<HttpStatus> authenticateUser() {
        return new ResponseEntity<>(null, allowOrigin(opensrpSiteUrl), OK);
    }

    public Authentication getAuthenticationAdvisor(HttpServletRequest request) {
    	final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.decode(base64Credentials.getBytes()), Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":",2);
    		
            return new UsernamePasswordAuthenticationToken(values[0], values[1]);
        }
		return null;	
	}
    
    public DrishtiAuthenticationProvider getAuthenticationProvider() {
		return opensrpAuthenticationProvider;
	}
    
    public User currentUser(HttpServletRequest request) {
    	Authentication a = getAuthenticationAdvisor(request);
    	return getAuthenticationProvider().getDrishtiUser(a, a.getName());
    }

    public Time getServerTime() {
    	return new Time(Calendar.getInstance().getTime(), TimeZone.getDefault());
	}

    @RequestMapping(method = RequestMethod.GET, value = "/user-details")
    public ResponseEntity<UserDetail> userDetail(@RequestParam("anm-id") String anmIdentifier, HttpServletRequest request) {
    	Authentication a = getAuthenticationAdvisor(request);
        User user = opensrpAuthenticationProvider.getDrishtiUser(a, anmIdentifier);
        return new ResponseEntity<>(new UserDetail(user.getUsername(), user.getRoles()), allowOrigin(opensrpSiteUrl), OK);
    }

	@RequestMapping("/security/authenticate")
	@ResponseBody
	public ResponseEntity<String> authenticate(HttpServletRequest request) throws JSONException {
        User u = currentUser(request);
        String lid = "";
        JSONObject tm = null;
        try{
        	tm = openmrsUserService.getTeamMember(u.getAttribute("_PERSON_UUID").toString());
	        JSONArray locs = tm.getJSONArray(OpenMRSCrossVariables.LOCATIONS_JSON_KEY.makeVariable(OPENMRS_VERSION));
        	for (int i = 0; i < locs.length(); i++) {
				lid += locs.getJSONObject(i).getString("uuid")+";;";
			}
        }
        catch(Exception e){
        	System.out.println("USER Location info not mapped in team management module. Now trying Person Attribute");;
        }
        if(StringUtils.isEmptyOrWhitespaceOnly(lid)){
	        lid = (String) u.getAttribute("Location");
	        if(StringUtils.isEmptyOrWhitespaceOnly(lid)){
	            String lids = (String) u.getAttribute("Locations");
	            
	            if(lids == null){
	            	throw new RuntimeException("User not mapped on any location. Make sure that user have a person attribute Location or Locations with uuid(s) of valid OpenMRS Location(s) separated by ;;");
	            }
	            
	            lid = lids;
	        }
        }
		LocationTree l = openmrsLocationService.getLocationTreeOf(lid.split(";;"));
		Map<String, Object> map = new HashMap<>();
		map.put("user", u);
		try{
			Map<String, Object> tmap = new Gson().fromJson(tm.toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
			map.put("team", tmap);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		map.put("locations", l);
		Time t = getServerTime();
		map.put("time", t);
        return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
	}
	
	@RequestMapping("/security/configuration")
	@ResponseBody
	public ResponseEntity<String> configuration() throws JSONException {
		Map<String, Object> map = new HashMap<>();
		map.put("serverDatetime", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        return new ResponseEntity<>(new Gson().toJson(map), allowOrigin(opensrpSiteUrl), OK);
	}
}
