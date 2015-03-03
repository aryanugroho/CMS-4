package com.jhomlala.web.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.jhomlala.dao.PersonDao;
import com.jhomlala.dao.PostDao;
import com.jhomlala.model.FileUpload;
import com.jhomlala.model.Person;
import com.jhomlala.model.Post;
import com.jhomlala.services.DashboardService;
import com.jhomlala.services.PostService;

@Controller
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	PostService postService;

	@Transactional(readOnly = true)
	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("postList", postService.loadPosts(5));
		model.setViewName("Index");

		return model;

	}

	@RequestMapping(value = "/dashboard**", method = RequestMethod.GET)
	public ModelAndView dashboardMainPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This page is for ROLE_ADMIN only!");
		model.setViewName("Dashboard");

		return model;

	}

	@RequestMapping(value = "/dashboard/add", method = RequestMethod.GET)
	public ModelAndView dashboardAddPost() {

		ModelAndView model = new ModelAndView();

		model.setViewName("DashboardPost");

		return model;

	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/dashboard/add", method = RequestMethod.POST)
	public ModelAndView dashboarddAdPostReceivePostFromUser(
			@RequestParam String message) {
		List<String> errorList = postService.addPost(message);
		ModelAndView model = new ModelAndView();
		model.addObject("errorList", errorList);

		if (errorList.size() == 0)
			model.addObject("success", "success`");
		model.setViewName("DashboardPost");

		return model;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error",
					getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}

		if (logout != null) {
			model.addObject("msg", "You've been logged out successfully.");
		}
		model.setViewName("login");

		return model;

	}

	// customize the error message
	private String getErrorMessage(HttpServletRequest request, String key) {

		Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}

		return error;
	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);

			model.addObject("username", userDetail.getUsername());

		}

		model.setViewName("403");
		return model;

	}

	@RequestMapping(value = "/dashboard/upload", method = RequestMethod.GET)
	public String crunchifyDisplayForm(Map<String, Object> model) {
		FileUpload uploadForm = new FileUpload();
		model.put("uploadForm", uploadForm);

		return "uploadfile";
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/dashboard/upload", method = RequestMethod.POST)
	public String crunchifySave(
			@ModelAttribute("uploadForm") FileUpload uploadForm, Model model)
			throws IllegalStateException, IOException

	{

		List<String> errorList = dashboardService.addAvatar(uploadForm);

		model.addAttribute("errorList", errorList);

		return "uploadfile";
	}

}
