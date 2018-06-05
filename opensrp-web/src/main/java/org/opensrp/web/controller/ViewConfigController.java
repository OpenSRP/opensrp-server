package org.opensrp.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/viewconfig")
public class ViewConfigController {

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView viewConfig(HttpServletRequest request){
		return new ModelAndView("view_config");
	}
}