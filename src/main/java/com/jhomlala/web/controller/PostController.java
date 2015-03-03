package com.jhomlala.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jhomlala.model.Post;
import com.jhomlala.services.PostService;

@Controller
public class PostController {

	@Autowired
	PostService postService;

	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}" }, method = RequestMethod.GET)
	public ModelAndView showPost(@PathVariable String id) {
		ModelAndView model = new ModelAndView();

		int idInt = 0;
		List<String> errorList = new ArrayList<String>();
		try {
			idInt = Integer.valueOf(id);
			Post post = postService.getPostWithId(idInt);
			if (post == null) {

				errorList.add("There is no post with this id.");
				model.addObject("errorList", errorList);
			}
			model.addObject("post", post);
		} catch (NumberFormatException exc) {
			errorList.add("There is no post with this id");
			model.addObject("errorList", errorList);
		}

		model.setViewName("Post");

		return model;

	}

	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}/addComment" }, method = RequestMethod.GET)
	public ModelAndView postAddComment(@PathVariable String id) {
		ModelAndView model = new ModelAndView();

		int idInt = 0;
		List<String> errorList = new ArrayList<String>();
		try {
			idInt = Integer.valueOf(id);
			Post post = postService.getPostWithId(idInt);
			if (post == null) {

				errorList.add("There is no post with this id.");
				model.addObject("errorList", errorList);
			}
			model.addObject("post", post);
		} catch (NumberFormatException exc) {
			errorList.add("There is no post with this id");
			model.addObject("errorList", errorList);
		}

		model.setViewName("PostAdd");

		return model;

	}

	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}/addComment" }, method = RequestMethod.POST)
	public ModelAndView postAddCommentReceiveComment(@PathVariable String id,
			@RequestParam String message) {
		ModelAndView model = new ModelAndView();
		model.addObject("id", id);

		List<String> errorList = postService.addComment(id, message);
		model.addObject("errorList", errorList);

		if (errorList.size() == 0)
			model.addObject("success", "success`");

		model.setViewName("PostAdd");

		return model;

	}

	@Transactional(readOnly = true)
	@RequestMapping(value = { "/post/{id}/vote/plus" }, method = RequestMethod.GET)
	public ModelAndView postVotePlus(@PathVariable String id) {
		ModelAndView model = new ModelAndView();

		return model;
	}

}
