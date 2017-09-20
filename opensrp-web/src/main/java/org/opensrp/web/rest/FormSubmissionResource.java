package org.opensrp.web.rest;

import static org.opensrp.common.AllConstants.BaseEntity.BASE_ENTITY_ID;
import static org.opensrp.common.AllConstants.Event.EVENT_TYPE;
import static org.opensrp.common.AllConstants.Event.PROVIDER_ID;
import static org.opensrp.web.rest.RestUtils.getStringFilter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.opensrp.form.domain.FormSubmission;
import org.opensrp.form.service.FormSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/rest/formSubmission")
public class FormSubmissionResource extends RestResource<FormSubmission> {

	private FormSubmissionService fsService;

	@Autowired
	public FormSubmissionResource(FormSubmissionService fsService) {
		this.fsService = fsService;
	}

	@Override
	public FormSubmission getByUniqueId(String uniqueId) {
		return fsService.findByInstanceId(uniqueId);
	}
	
/*	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public Event getByBaseEntityAndFormSubmissionId(@RequestParam String baseEntityId, @RequestParam String formSubmissionId) {
		return eventService.getByBaseEntityAndFormSubmissionId(baseEntityId, formSubmissionId);
	}*/

	@Override
	public FormSubmission create(FormSubmission o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> requiredProperties() {
		List<String> p = new ArrayList<>();
		p.add(BASE_ENTITY_ID);
		//p.add(FORM_SUBMISSION_ID);
		p.add(EVENT_TYPE);
		//p.add(LOCATION_ID);
		//p.add(EVENT_DATE);
		p.add(PROVIDER_ID);
		//p.add(ENTITY_TYPE);
		return p;
	}

	@Override
	public FormSubmission update(FormSubmission entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<FormSubmission> search(HttpServletRequest request) throws ParseException {
		String formName = getStringFilter("formName", request);
		//String entityId = getStringFilter("entityId", request);
		String version = getStringFilter("version", request);//TODO
		long v = version == null ? 0L : Long.parseLong(version);

		return fsService.findByFormName(formName, v);
	}

	@Override
	public List<FormSubmission> filter(String query) {
		throw new UnsupportedOperationException();
	}

}
