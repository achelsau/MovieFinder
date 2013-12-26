package com.arielsweb.moviefinder.webservice.controllers;

import javax.management.relation.InvalidRelationIdException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.UserService;

/**
 * Controls server-side operations for:
 * 
 * - marking a relevant movie
 * 
 * @author Ariel
 * @version 1.0, 25/11/2012
 */
@Controller
@RequestMapping("/relevanceFeedback")
public class RelevanceFeedbackController {

    private UserService userService;

    private MovieDescriptorService movieDescriptorService;

    @RequestMapping(value = "/markIt/{movieId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void markIt(@PathVariable("movieId") String movieId, HttpServletRequest request,
	    HttpServletResponse resp, User user) throws InvalidRelationIdException {
	Long movieIdLong = 0L;
	try {
	    movieIdLong = Long.parseLong(movieId);
	} catch (NumberFormatException ex) {
	    throw new InvalidRelationIdException(ex.getMessage());
	}
	
	MovieDescriptor movieDescriptor = movieDescriptorService.find(movieIdLong);

	user.getRelevantMovies().add(movieDescriptor);
	userService.save(user);
    }

    @Autowired
    public void setUserService(UserService userService) {
	this.userService = userService;
    }

    @Autowired
    public void setMovieDescriptorService(MovieDescriptorService movieDescriptorService) {
	this.movieDescriptorService = movieDescriptorService;
    }
}
