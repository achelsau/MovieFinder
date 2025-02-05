package com.arielsweb.moviefinder.webservice.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.arielsweb.moviefinder.index.IQueryEngine;
import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.webservice.dto.ResultInfoResponse;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidPersistentQueryException;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidPersistentQueryIdException;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidQuickQueryException;
import com.arielsweb.moviefinder.webservice.security.ReturnCode;

/**
 * Controls server-side operations for:
 * 
 * <pre>
 * 1. running a quick-query against the DB
 * 2. persisting a query, thus making it persistent query
 * 3. updating a persistent query
 * 4. remove a persistent query
 * </pre>
 * 
 * @author Ariel
 * @version 1.0, 13/10/2012
 */
@Controller
@RequestMapping("/query")
public class QueryEngineController {

	private PersistentQueryService persistentQueryService;

	private IQueryEngine queryEngine;

	@RequestMapping(value = "/quickQuery", method = RequestMethod.POST, headers = "content-type=text/plain")
	@ResponseBody
	public ResultInfoResponse quickQuery(@RequestBody String queryString, HttpServletRequest request,
			HttpServletResponse resp, User user) throws InvalidQuickQueryException {
		if (queryString == null || queryString.isEmpty()) {
			throw new InvalidQuickQueryException("The query string is invalid");
		}

		List<ResultInfo> queryResults = queryEngine.queryIndex(queryString);
		ResultInfoResponse resultInfoResponse = new ResultInfoResponse(queryResults);
		return resultInfoResponse;
	}

	@RequestMapping(value = "/searchPersistentQuery", method = RequestMethod.POST, headers = "content-type=json/application")
	@ResponseBody
	public ResultInfoResponse searchPersistentQuery(@RequestBody PersistentQuery persistentQuery,
			HttpServletRequest request, HttpServletResponse response, User user) throws InvalidPersistentQueryException {
		if (persistentQuery.getQueryString() == null || persistentQuery.getQueryString().isEmpty()) {
			throw new InvalidPersistentQueryException("The persistent query is malformed");
		}

		List<ResultInfo> queryResults = null;
		if (persistentQuery.getTokens() != null && persistentQuery.getTokens().size() > 0) {
			List<PersistentQueryToken> persistentQueryTokens = persistentQuery.getTokens();

			Map<String, Float> queryWeights = TextParsingHelper.getQueryWeights(persistentQueryTokens);

			queryResults = queryEngine.queryIndex(queryWeights);
		} else {
			queryResults = queryEngine.queryIndex(persistentQuery.getQueryString());
		}

		// return both the results and the id of the query
		ResultInfoResponse resultInfoResponse = new ResultInfoResponse(queryResults, persistentQuery.getId());
		return resultInfoResponse;
	}

	@RequestMapping(value = "/storePersistentQuery", method = RequestMethod.POST, headers = "content-type=json/application")
	@ResponseBody
	public ResultInfoResponse persistQuery(@RequestBody PersistentQuery persistentQuery, HttpServletRequest request,
			HttpServletResponse response, User user) throws InvalidPersistentQueryException {
		if (persistentQuery.getQueryString() == null || persistentQuery.getQueryString().isEmpty()) {
			throw new InvalidPersistentQueryException("The persistent query is malformed");
		}

		// bind the user performing the op. (since only him can share movies for
		// himself)
		persistentQuery.setOwner(user);

		List<PersistentQueryToken> persistentQueryTokens = persistentQueryService.getQueryTokens(persistentQuery);

		persistentQuery.setTokens(persistentQueryTokens);

		// service operation
		Long queryId = (Long) persistentQueryService.save(persistentQuery);

		// return both the results and the id of the query
		ResultInfoResponse resultInfoResponse = new ResultInfoResponse(null, queryId);
		return resultInfoResponse;
	}

	@RequestMapping(value = "/updatePersistentQuery/", method = RequestMethod.POST, headers = "content-type=json/application")
	@ResponseBody
	public ResultInfoResponse updatePersistentQuery(@RequestBody PersistentQuery persistentQuery,
			HttpServletRequest request, HttpServletResponse response, User user) throws InvalidPersistentQueryException {
		if (persistentQuery.getQueryString() == null || persistentQuery.getQueryString().isEmpty()
				|| persistentQuery.getInterval() < 0) {
			throw new InvalidPersistentQueryException("The persistent query is malformed");
		}

		persistentQuery.setOwner(user);

		// service operation
		persistentQueryService.update(persistentQuery);

		// actual query operation
		List<ResultInfo> queryResults = queryEngine.queryIndex(persistentQuery.getQueryString());
		ResultInfoResponse resultInfoResponse = new ResultInfoResponse(queryResults, persistentQuery.getId());
		return resultInfoResponse;
	}

	@RequestMapping(value = "/deletePersistentQuery/{persistentQueryId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removePersistQuery(@PathVariable("persistentQueryId") String queryIdStr, HttpServletRequest request,
			HttpServletResponse response, User user) throws InvalidPersistentQueryIdException {
		Long queryId = null;
		try {
			queryId = Long.parseLong(queryIdStr);
		} catch (NumberFormatException nfe) {
			throw new InvalidPersistentQueryIdException("The persistent query id is malformed");
		}

		// service operation
		persistentQueryService.delete(queryId);
	}

	@RequestMapping(value = "/getAllQueries/{userId}", method = RequestMethod.GET, headers = "content-type=text/plain")
	@ResponseBody
	public List<PersistentQuery> getAllPersistedQueriesForUser(@PathVariable("userId") String userIdStr,
			HttpServletRequest request, HttpServletResponse response, User user)
			throws InvalidPersistentQueryIdException {
		Long userId = null;
		try {
			userId = Long.parseLong(userIdStr);
		} catch (NumberFormatException nfe) {
			throw new InvalidPersistentQueryIdException("The persistent query id is malformed");
		}

		// service operation
		List<PersistentQuery> queriesForUser = persistentQueryService.getQueriesForUser(userId);
		return queriesForUser;
	}

	/**
	 * Error handling operations
	 */
	@ExceptionHandler(InvalidQuickQueryException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = ReturnCode.QUERY_NULL_OR_EMPTY)
	public void handleInvalidQuickQuery(InvalidQuickQueryException invalidQueryException) {

	}

	@ExceptionHandler(InvalidPersistentQueryException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = ReturnCode.QUERY_NULL_OR_EMPTY)
	public void handleInvalidPersistentQuery(InvalidPersistentQueryException invalidQueryException) {

	}

	@ExceptionHandler(InvalidPersistentQueryIdException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = ReturnCode.MALFORMED_QUERY_ID)
	public void handleInvalidPersistentQueryId(InvalidPersistentQueryIdException invalidQueryIdException) {
	}

	@Autowired
	public void setQueryEngine(IQueryEngine queryEngine) {
		this.queryEngine = queryEngine;
	}

	@Autowired
	public void setPersistentQueryService(PersistentQueryService persistentQueryService) {
		this.persistentQueryService = persistentQueryService;
	}
}
