package com.jhomlala.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jhomlala.services.DashboardService;
import com.jhomlala.services.PersonService;

@Controller
public class ProfileController {

	@Autowired
	PersonService personService;
	
	@Autowired
	DashboardService dashboardService;
	
	@Transactional(readOnly = true)
	@RequestMapping(value = { "/profile/{id}" }, method = RequestMethod.GET)
	public ModelAndView personProfile(@PathVariable String id) {
		ModelAndView model = new ModelAndView();
		
		int idInt = Integer.valueOf(id);
		model.addObject("person",personService.findById(idInt));
		model.addObject("personPosts",dashboardService.getPostsForPersonWithId(idInt, 5));
		
		model.setViewName("Profile");
		
		return model;

	}
}
