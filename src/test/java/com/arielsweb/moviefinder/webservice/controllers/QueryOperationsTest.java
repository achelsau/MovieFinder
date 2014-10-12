package com.arielsweb.moviefinder.webservice.controllers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.service.UserService;
import com.arielsweb.moviefinder.service.impl.IndexEngineServiceImpl;
import com.arielsweb.moviefinder.webservice.dto.ResultInfoResponse;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidPersistentQueryException;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidPersistentQueryIdException;

/**
 * Provides <b>integration testing</b> for query operations
 * 
 * @author Ariel
 * 
 */
@DataSet("QueryOperationsTest.xml")
@Transactional(TransactionMode.ROLLBACK)
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
public class QueryOperationsTest {
	@SpringBeanByType
	private IndexEngine invertedIndexEngine;
	@SpringBeanByType
	private IndexEngineServiceImpl indexEngineController;
	@SpringBeanByType
	private QueryEngineController queryEngineController;
	@SpringBeanByType
	private PersistentQueryService persistentQueryService;
	@SpringBeanByType
	private MovieDescriptorService rdService;
	@SpringBeanByType
	private UserService userService;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private static MovieDescriptor rd4, rd5;
	private User user;

	@Before
	public void setUp() throws InvalidMovieDescriptorException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		// clear persistent queries' table
		persistentQueryService.deleteAll();

		user = new User();
		user.setUsername("TestUser");
		user.setPassword("qwerty");
		user.setId(1L);
		user.setIsOnline(true);
		userService.save(user);

		invertedIndexEngine.clearIndex();

		// construct the index
		rd4 = rdService.find(4L);
		indexEngineController.addEntry(rd4);
		rd5 = rdService.find(5L);
		indexEngineController.addEntry(rd5);
	}

	/**
	 * Scenario 1:
	 * 
	 * <pre>
	 * 1. Create and persist query
	 * 2. Check a) it was saved
	 * 	        b) initial set of results were retrieved
	 * </pre>
	 * 
	 * @throws InvalidPersistentQueryException
	 */
	@Test
	public void testPersistQuery() throws InvalidPersistentQueryException {
		// create a persistent query
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(1000L);
		persistentQuery.setQueryString("Lunar Landings");

		ResultInfoResponse resultInfoResponse = queryEngineController.persistQuery(persistentQuery, request, response,
				user);

		// 1. check that it was saved
		List<PersistentQuery> persistentQueries = persistentQueryService.list();
		PersistentQuery pq = persistentQueries.get(0);
		assertEquals(pq.getId(), persistentQuery.getId());
		assertEquals(pq.getInterval(), new Long(1000L));
		assertEquals(pq.getQueryString(), "Lunar Landings");

		// 2. because it was an operation that saved the query only the query id
		// will be returned
		Long queryId = resultInfoResponse.getQueryId();

		assertEquals(pq.getId(), queryId);

		List<ResultInfo> results = resultInfoResponse.getResults();

		assertNull(results);
	}

	/**
	 * Scenario 2:
	 * 
	 * <pre>
	 * 1. Create and persist query
	 * 2. Update it
	 * 2. Check a) it was updated
	 * 	        b) updated set of results were retrieved
	 * </pre>
	 * 
	 * @throws InvalidPersistentQueryException
	 */
	@Test
	public void testUpdateQuery() throws InvalidPersistentQueryException {
		// create a persistent query
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(1000L);
		persistentQuery.setQueryString("Lunar Landings");

		queryEngineController.persistQuery(persistentQuery, request, response, user);

		persistentQuery.setQueryString("Europa find life");

		ResultInfoResponse resultInfoResponse = queryEngineController.persistQuery(persistentQuery, request, response,
				user);

		// 1. check that it was saved
		List<PersistentQuery> persistentQueries = persistentQueryService.list();
		PersistentQuery pq = persistentQueries.get(0);
		assertEquals(pq.getId(), persistentQuery.getId());
		assertEquals(pq.getInterval(), new Long(1000L));
		assertEquals(pq.getQueryString(), "Europa find life");

		// 2. because it was an operation that saved the query only the query id
		// will be returned
		Long queryId = resultInfoResponse.getQueryId();

		assertEquals(pq.getId(), queryId);

		List<ResultInfo> results = resultInfoResponse.getResults();

		assertNull(results);
	}

	/**
	 * Scenario 3:
	 * 
	 * <pre>
	 * 1. Create and persist query
	 * 2. Remove it
	 * 3. Check it was removed
	 * </pre>
	 * 
	 * @throws InvalidPersistentQueryException
	 * @throws InvalidPersistentQueryIdException
	 */
	@Test
	public void testRemoveQuery() throws InvalidPersistentQueryException, InvalidPersistentQueryIdException {
		// 1. Create and persist query
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(1000L);
		persistentQuery.setQueryString("Lunar Landings");

		queryEngineController.persistQuery(persistentQuery, request, response, user);

		// 2. Remove it
		queryEngineController.removePersistQuery(persistentQuery.getId().toString(), request, response, user);

		// 3. check that it was removed
		List<PersistentQuery> persistentQueries = persistentQueryService.list();
		assertEquals(0, persistentQueries.size());
	}
}
