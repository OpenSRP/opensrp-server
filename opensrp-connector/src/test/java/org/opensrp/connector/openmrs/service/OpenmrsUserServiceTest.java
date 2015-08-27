package org.opensrp.connector.openmrs.service;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import static org.mockito.Mockito.*;
import org.opensrp.common.util.HttpResponse;
import org.opensrp.connector.HttpUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpUtil.class })
public class OpenmrsUserServiceTest extends TestResourceLoader{

	public OpenmrsUserServiceTest() throws IOException {
		super();
	}

	OpenmrsUserService ls;

	@Before
	public void setup(){
		PowerMockito.mockStatic(HttpUtil.class);
		ls = new OpenmrsUserService(openmrsOpenmrsUrl, openmrsUsername, openmrsPassword);
	}
	
	@Test
	public void testAuthentication() throws JSONException {
        BDDMockito.given(HttpUtil.get(any(String.class), eq(""), any(String.class), any(String.class))).willReturn(new HttpResponse(true, "{\"authenticated\":\"true\"}"));
        
		assertTrue(ls.authenticate(openmrsUsername, openmrsPassword));
	}
	
	@Test
	public void testUser() throws JSONException {
        BDDMockito.given(HttpUtil.get(any(String.class), any(String.class), any(String.class), any(String.class))).willReturn(new HttpResponse(true, "{\"results\":[{\"uuid\":\"baa5c5d3-cebe-11e4-9a12-040144de7001\",\"display\":\"admin\",\"username\":\"admin\",\"systemId\":\"admin\",\"privileges\":[],\"roles\":[],\"userProperties\":{\"showRetired\":\"false\",\"defaultLocation\":\"\",\"showVerbose\":\"false\",\"notification\":\"\",\"notificationAddress\":\"\",\"loginAttempts\":\"0\"},\"person\":{\"uuid\":\"aeb5ecd0-cebe-11e4-9a12-040144de7001\",\"display\":\"Super User\",\"gender\":\"M\",\"preferredName\":{\"uuid\":\"aebebad3-cebe-11e4-9a12-040144de7001\",\"display\":\"Super User\"},\"attributes\":[{\"uuid\":\"65040584-b558-4b2a-b73d-f6e681839492\",\"display\":\"Health Center = 2\"},{\"uuid\":\"0f60bb3d-abf8-407e-88fd-2da4b49afef9\",\"display\":\"Location = cd4ed528-87cd-42ee-a175-5e7089521ebd\"}]}}]}"));

        assertTrue(ls.getUser("admin").getUsername().equalsIgnoreCase("admin"));
	}
}
