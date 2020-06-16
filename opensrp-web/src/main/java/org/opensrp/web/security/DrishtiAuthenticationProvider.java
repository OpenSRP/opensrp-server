package org.opensrp.web.security;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.opensrp.api.domain.User;
import org.opensrp.connector.openmrs.service.OpenmrsUserService;
import org.opensrp.domain.postgres.CustomQuery;
import org.opensrp.service.ClientService;
import org.opensrp.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.Converter;

@Component
public class DrishtiAuthenticationProvider implements AuthenticationProvider {
	
	private static Logger logger = LoggerFactory.getLogger(DrishtiAuthenticationProvider.class.toString());
	
	public static final String USER_NOT_FOUND = "The username or password you entered is incorrect. Please enter the correct credentials.";
	
	public static final String USER_NOT_ACTIVATED = "The user has been registered but not activated. Please contact your local administrator.";
	
	public static final String INTERNAL_ERROR = "Failed to authenticate user due to internal server error.";
	
	private static final String AUTH_HASH_KEY = "_auth";
	
	private static final String provider = "Provider";
	
	//private AllOpenSRPUsers allOpenSRPUsers;
	private PasswordEncoder passwordEncoder;
	
	private OpenmrsUserService openmrsUserService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Authentication> hashOps;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private ClientService clientService;
	
	@Autowired
	private EventService eventService;
	
	@Value("#{opensrp['opensrp.authencation.cache.ttl']}")
	private int cacheTTL;
	
	@Autowired
	public DrishtiAuthenticationProvider(OpenmrsUserService openmrsUserService,
	    @Qualifier("shaPasswordEncoder") PasswordEncoder passwordEncoder, ClientService clientService) {
		this.openmrsUserService = openmrsUserService;
		this.passwordEncoder = passwordEncoder;
		this.clientService = clientService;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userAddress = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
		String key = userAddress + authentication.getName();
		CustomQuery customQuery = clientService.getUserStatus(authentication.getName());
		if (hashOps.hasKey(key, AUTH_HASH_KEY)) {
			Authentication auth = hashOps.get(key, AUTH_HASH_KEY);
			//if credentials is same as cached returned cached else eject cached authentication
			if (auth.getCredentials().equals(authentication.getCredentials()))
				return auth;
			else
				hashOps.delete(key, AUTH_HASH_KEY);
			
		}
		User user = getDrishtiUser(authentication, authentication.getName());
		// get user after authentication
		if (user == null) {
			throw new BadCredentialsException(USER_NOT_FOUND);
		}
		
		if (customQuery != null && !customQuery.getEnable()) {
			throw new BadCredentialsException(USER_NOT_ACTIVATED);
		}
		
		Authentication auth = new UsernamePasswordAuthenticationToken(authentication.getName(),
		        authentication.getCredentials(), getRolesAsAuthorities(user));
		hashOps.put(key, AUTH_HASH_KEY, auth);
		redisTemplate.expire(key, cacheTTL, TimeUnit.SECONDS);
		return auth;
		
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
		        && authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private List<SimpleGrantedAuthority> getRolesAsAuthorities(User user) {
		return Lambda.convert(user.getRoles(), new Converter<String, SimpleGrantedAuthority>() {
			
			@Override
			public SimpleGrantedAuthority convert(String role) {
				return new SimpleGrantedAuthority("ROLE_OPENMRS");
			}
		});
	}
	
	/*public User getDrishtiUser(Authentication authentication, String username) {
		User user = null;
		try {
			if (openmrsUserService.authenticate(authentication.getName(), authentication.getCredentials().toString())) {
				boolean response = openmrsUserService.deleteSession(authentication.getName(),
				    authentication.getCredentials().toString());
				user = openmrsUserService.getUser(username);
				if (!response) {
					logger.error(format("{0}. Exception: {1}", INTERNAL_ERROR, "Unable to clear session"));
					
				}
			}
		}
		catch (Exception e) {
			logger.error(format("{0}. Exception: {1}", INTERNAL_ERROR, e));
			e.printStackTrace();
			throw new BadCredentialsException(INTERNAL_ERROR);
		}
		return user;
	}*/
	
	public Boolean getAuthentication(Authentication authentication, CustomQuery userInfo) {
		/*CustomQuery userInfo = eventService.getUser(authentication.getName());*/
		Boolean match = false;
		if (userInfo != null) {
			List<CustomQuery> roles = eventService.getRoles(userInfo.getId());
			
			if (!isProvider(roles)) {
				return false;
			}
		}
		if (userInfo != null) {
			match = bcryptPasswordEncoder.matches(authentication.getCredentials().toString(), userInfo.getPassword());
		}
		
		return match;
	}
	
	public User getDrishtiUser(Authentication authentication, String username) {
		User user = null;
		
		CustomQuery userInfo = eventService.getUser(authentication.getName());
		if (getAuthentication(authentication, userInfo)) {
			
			//user = openmrsUserService.getUser(username);
			user = new User(userInfo.getUserUUID(), userInfo.getName(), null, userInfo.getFullName(), null,
			        userInfo.getFullName(), null, null);
			
			user.addAttribute("_PERSON_UUID", userInfo.getPersonUUID());
			user.addRole("Provider");
			user.addPermission(null);
			
		}
		
		return user;
	}
	
	public User getUser(Authentication authentication, String username) {
		User user = null;
		
		CustomQuery userInfo = eventService.getUser(authentication.getName());
		
		if (userInfo != null) {
			user = new User(userInfo.getUserUUID(), userInfo.getName(), null, userInfo.getFullName(), null,
			        userInfo.getFullName(), null, null);
			
			user.addAttribute("_PERSON_UUID", userInfo.getPersonUUID());
			user.addRole("Provider");
			user.addPermission(null);
			
			System.out.println("user:" + user);
		}
		return user;
	}
	
	private Boolean isProvider(List<CustomQuery> roles) {
		if (roles != null) {
			for (CustomQuery role : roles) {
				if (role.getName().equalsIgnoreCase(provider)) {
					return true;
				}
			}
		}
		return false;
	}
}
