package org.opensrp.web.controller.it;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opensrp.common.audit.AuditMessage;
import org.opensrp.common.audit.AuditMessageType;
import org.opensrp.common.audit.Auditor;
import org.opensrp.domain.Client;
import org.opensrp.web.controller.AuditMessageController;
import org.opensrp.web.rest.it.BaseResourceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.server.MvcResult;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

@Ignore
public class AuditMessageControllerIntegrationTest extends BaseResourceTest {

	@Autowired
	Auditor auditor;


/*
	@Test
	public void getAuditMessagesBasedOnPreviousIndex() throws Exception {
		String url = "/audit/messages";
		Auditor.AuditMessageBuilder auditMessageBuilder = new Auditor.AuditMessageBuilder(auditor, AuditMessageType.NORMAL);
		auditMessageBuilder.done();
		auditMessageBuilder = new Auditor.AuditMessageBuilder(auditor, AuditMessageType.SMS);
		auditMessageBuilder.done();

		MvcResult mvcResult = this.mockMvc.perform(get(url + "?previousAuditMessageIndex=1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		JsonNode actualObj = mapper.readTree(responseString);
		Client actualClient = mapper.treeToValue(actualObj, Client.class);

		assertEquals(Auditor.AuditMessageItem.from(auditor.messagesSince(1)), actualClient);
	}*/
}
