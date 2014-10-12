package com.arielsweb.moviefinder.webservice.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

import com.arielsweb.moviefinder.index.IQueryEngine;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidPersistentQueryException;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidPersistentQueryIdException;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidQuickQueryException;

/**
 * Tests {@link QueryEngineController} using mocks
 * 
 * @author Ariel
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class QueryEngineControllerTest {
	private QueryEngineController queryEngineController;

	// mocks
	private HttpServletRequest request;
	private HttpServletResponse response;
	private IQueryEngine mockQueryEngine;
	private PersistentQueryService mockPersistentQueryService;
	private User mockUser;

	@Before
	public void setUp() {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		mockQueryEngine = mock(IQueryEngine.class);
		mockPersistentQueryService = mock(PersistentQueryService.class);
		mockUser = mock(User.class);

		queryEngineController = new QueryEngineController();
		queryEngineController.setQueryEngine(mockQueryEngine);
		queryEngineController.setPersistentQueryService(mockPersistentQueryService);
	}

	@Test
	public void testQuickQuery() throws InvalidQuickQueryException {
		// execute
		queryEngineController.quickQuery("dummy query", request, response, mockUser);

		// verify
		verify(mockQueryEngine).queryIndex("dummy query");
	}

	@Test(expected = InvalidQuickQueryException.class)
	public void testInvalidQuickQuery() throws InvalidQuickQueryException {
		// execute
		queryEngineController.quickQuery("", request, response, mockUser);
	}

	@Test
	public void testPersistQuery() throws InvalidPersistentQueryException {
		// setup
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(10L);
		persistentQuery.setQueryString("Dummy Query");

		// execute
		queryEngineController.persistQuery(persistentQuery, request, response, mockUser);

		// verify
		verify(mockPersistentQueryService).save(persistentQuery);
	}

	@Test
	public void testSearchByPersistentQueryWithoutTokens() throws InvalidPersistentQueryException {
		// setup
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(10L);
		persistentQuery.setQueryString("Dummy Query");

		// execute
		queryEngineController.searchPersistentQuery(persistentQuery, request, response, mockUser);

		// verify
		verify(mockQueryEngine).queryIndex("Dummy Query");
	}

	@Test
	public void testSearchByPersistentQueryWithTokens() throws InvalidPersistentQueryException {
		// setup
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(10L);
		persistentQuery.setQueryString("Dummy Query");

		PersistentQueryToken persistentQueryToken1 = new PersistentQueryToken();
		persistentQueryToken1.setToken("Dummy");
		persistentQueryToken1.setWeight(1f);

		PersistentQueryToken persistentQueryToken2 = new PersistentQueryToken();
		persistentQueryToken2.setToken("Query");
		persistentQueryToken2.setWeight(1f);
		persistentQuery.setTokens(Arrays.asList(persistentQueryToken1, persistentQueryToken2));

		// execute
		queryEngineController.searchPersistentQuery(persistentQuery, request, response, mockUser);

		// verify
		verify(mockQueryEngine).queryIndex(TextParsingHelper.getQueryWeights(persistentQuery.getTokens()));
	}

	@Test
	public void testGetAllPersistedQueriesForUser() throws InvalidPersistentQueryException,
			InvalidPersistentQueryIdException {
		// setup
		long userId = 1L;
		PersistentQuery persistentQuery1 = new PersistentQuery();
		persistentQuery1.setQueryString("moon landing");
		PersistentQuery persistentQuery2 = new PersistentQuery();
		persistentQuery2.setQueryString("solar system");
		List<PersistentQuery> queries = Arrays.asList(persistentQuery1, persistentQuery2);

		when(mockPersistentQueryService.getQueriesForUser(userId)).thenReturn(queries);

		// execute
		queryEngineController.getAllPersistedQueriesForUser("1", request, response, mockUser);

		// verify
		verify(mockPersistentQueryService).getQueriesForUser(userId);
	}

	@Test(expected = InvalidPersistentQueryException.class)
	public void testPersistInvalidQuery_EmptyQuery() throws InvalidPersistentQueryException {
		// setup
		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setInterval(10L);
		persistentQuery.setQueryString("");

		// execute
		try {
			queryEngineController.persistQuery(persistentQuery, request, response, mockUser);
		} catch (InvalidPersistentQueryException ex) {
			throw ex;
		}

		// verify
		verify(mockPersistentQueryService, never()).save(persistentQuery);
		verify(mockQueryEngine, never()).queryIndex(persistentQuery.getQueryString());
	}

	@Test
	public void testRemovePersistentQuery() throws InvalidPersistentQueryIdException {
		// execute
		queryEngineController.removePersistQuery("10", request, response, mockUser);

		// verify
		verify(mockPersistentQueryService).delete(10L);
	}

	@Test(expected = InvalidPersistentQueryIdException.class)
	public void testRemovePersistentQuery_InvalidId() throws InvalidPersistentQueryIdException {
		// execute
		try {
			queryEngineController.removePersistQuery("ab10", request, response, mockUser);
		} catch (InvalidPersistentQueryIdException ex) {
			throw ex;
		}

		// verify
		verify(mockPersistentQueryService, never()).delete(Mockito.anyLong());
	}

}
