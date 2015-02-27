package com.jhomlala.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jhomlala.services.DashboardService;



@Controller
public class PostController {

	@Autowired
	DashboardService dashboardService;
	
	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}" }, method = RequestMethod.GET)
	public ModelAndView showPost(@PathVariable String id) {
		ModelAndView model = new ModelAndView();
		
		int idInt = Integer.valueOf(id);
		model.addObject("post",dashboardService.getPostWithId(idInt));
		
		model.setViewName("Post");
		
		return model;

	}
	
	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}/addComment" }, method = RequestMethod.GET)
	public ModelAndView postAddComment(@PathVariable String id) {
		ModelAndView model = new ModelAndView();
		model.addObject("id",id);
		
		int idInt = Integer.valueOf(id);
		model.addObject("post",dashboardService.getPostWithId(idInt));
		
		model.setViewName("PostAdd");
		
		return model;

	}
	
	
	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}/addComment" }, method = RequestMethod.POST)
	public ModelAndView postAddCommentReceiveComment(@PathVariable String id,@RequestParam String message) {
		ModelAndView model = new ModelAndView();
		model.addObject("id",id);
		
		List <String> errorList = dashboardService.addComment(id,message);
		model.addObject("errorList",errorList);
		
		if (errorList.size() == 0) model.addObject("success","success`");

		
		model.setViewName("PostAdd");
		
		return model;

	}
	
	
	
}
