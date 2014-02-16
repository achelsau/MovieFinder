package com.arielsweb.moviefinder.webservice.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
import com.arielsweb.moviefinder.model.PersistentQuery;
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
