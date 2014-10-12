package com.arielsweb.moviefinder.webservice.controllers;

import java.util.List;
import java.util.Map;

import javax.management.relation.InvalidRelationIdException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arielsweb.moviefinder.index.impl.RelevanceFeedbackEngine;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.service.UserService;
import com.arielsweb.moviefinder.webservice.dto.RelevanceFeedbackRequest;

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

	private RelevanceFeedbackEngine relevanceFeedbackEngine;

	private PersistentQueryService persistentQueryService;

	@RequestMapping(value = "/markIt", method = RequestMethod.POST, headers = "content-type=json/application")
	@ResponseBody
	public List<PersistentQueryToken> markIt(@RequestBody RelevanceFeedbackRequest relevanceFeedbackRequest,
			HttpServletRequest request, HttpServletResponse resp, User user) throws InvalidRelationIdException {
		Long movieIdLong = 0L;
		Long persistentQueryLong = 0L;
		try {
			movieIdLong = Long.parseLong(relevanceFeedbackRequest.getRelevantMovieId());
			persistentQueryLong = Long.parseLong(relevanceFeedbackRequest.getPersistentQueryId());
		} catch (NumberFormatException ex) {
			throw new InvalidRelationIdException(ex.getMessage());
		}

		MovieDescriptor movieDescriptor = movieDescriptorService.find(movieIdLong);
		userService.saveRelevantResult(user.getId(), movieDescriptor);

		PersistentQuery persistentQuery = persistentQueryService.find(persistentQueryLong);
		Map<String, Float> queryWeights = TextParsingHelper.getQueryWeights(persistentQuery.getTokens());

		Long[] relevantEntries = new Long[] { movieIdLong };
		queryWeights = relevanceFeedbackEngine.getRefinedQuery(queryWeights, relevantEntries);

		List<PersistentQueryToken> persistentQueryTokens = TextParsingHelper.getQueryTokensListFromMap(persistentQuery,
				queryWeights);

		persistentQuery.setTokens(persistentQueryTokens);
		persistentQueryService.update(persistentQuery);

		return persistentQueryTokens;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setMovieDescriptorService(MovieDescriptorService movieDescriptorService) {
		this.movieDescriptorService = movieDescriptorService;
	}

	@Autowired
	public void setRelevanceFeedbackEngine(RelevanceFeedbackEngine relevanceFeedbackEngine) {
		this.relevanceFeedbackEngine = relevanceFeedbackEngine;
	}

	@Autowired
	public void setPersistentQueryService(PersistentQueryService persistentQueryService) {
		this.persistentQueryService = persistentQueryService;
	}
}
