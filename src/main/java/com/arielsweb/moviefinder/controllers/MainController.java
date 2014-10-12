package com.arielsweb.moviefinder.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * As an Admin you view the page rendered by this controller when you first
 * access the server into the browser
 * 
 * @author achelsau
 * 
 */
@Controller
public class MainController {

	@RequestMapping("home.html")
	public ModelAndView handleHomePage(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("home");
	}
}
