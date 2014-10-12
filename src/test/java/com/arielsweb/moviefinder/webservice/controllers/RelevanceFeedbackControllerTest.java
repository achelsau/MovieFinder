package com.arielsweb.moviefinder.webservice.controllers;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.management.relation.InvalidRelationIdException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

import com.arielsweb.moviefinder.index.impl.RelevanceFeedbackEngine;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.service.UserService;
import com.arielsweb.moviefinder.webservice.dto.RelevanceFeedbackRequest;

/**
 * Tests the methods of {@link RelevanceFeedbackController} (using mocks)
 * 
 * @author Ariel
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class RelevanceFeedbackControllerTest {

	private RelevanceFeedbackController controller;

	private MovieDescriptorService mockMDService;
	private RelevanceFeedbackEngine mockRelevanceFeedbackEngine;
	private PersistentQueryService mockPersistentQueryService;
	private UserService mockUserService;
	private HttpServletRequest mockRequest;
	private HttpServletResponse mockResponse;
	private User mockUser;

	@Before
	public void setUp() {
		mockUser = mock(User.class);
		mockMDService = mock(MovieDescriptorService.class);
		mockUserService = mock(UserService.class);
		mockRelevanceFeedbackEngine = mock(RelevanceFeedbackEngine.class);
		mockPersistentQueryService = mock(PersistentQueryService.class);

		when(mockUser.getId()).thenReturn(1L);

		controller = new RelevanceFeedbackController();
		controller.setMovieDescriptorService(mockMDService);
		controller.setUserService(mockUserService);
		controller.setRelevanceFeedbackEngine(mockRelevanceFeedbackEngine);
		controller.setPersistentQueryService(mockPersistentQueryService);
	}

	@Test
	public void testMarkIt() throws InvalidRelationIdException {
		// setup
		String movieId = "1234";

		PersistentQuery persistentQuery = new PersistentQuery();
		persistentQuery.setId(1L);
		persistentQuery.setInterval(1000L);
		persistentQuery.setQueryString("dummy query");
		persistentQuery.setOwner(mockUser);

		PersistentQueryToken persistentQueryToken1 = new PersistentQueryToken();
		persistentQueryToken1.setParentQuery(persistentQuery);
		persistentQueryToken1.setToken("dummy");
		persistentQueryToken1.setWeight(1f);

		PersistentQueryToken persistentQueryToken2 = new PersistentQueryToken();
		persistentQueryToken2.setParentQuery(persistentQuery);
		persistentQueryToken2.setToken("query");
		persistentQueryToken2.setWeight(1f);

		Map<String, Float> queryWeights = new HashMap<String, Float>();
		queryWeights.put("dummy", 1f);
		queryWeights.put("query", 1f);

		Long[] movieIds = new Long[] { Long.parseLong(movieId) };

		persistentQuery.setTokens(Arrays.asList(persistentQueryToken1, persistentQueryToken2));

		RelevanceFeedbackRequest relevanceFeedbackRequest = new RelevanceFeedbackRequest(persistentQuery.getId()
				.toString(), movieId);
		when(mockMDService.find(Long.parseLong(movieId))).thenReturn(new MovieDescriptor());
		when(mockPersistentQueryService.find(persistentQuery.getId())).thenReturn(persistentQuery);

		// execute
		controller.markIt(relevanceFeedbackRequest, mockRequest, mockResponse, mockUser);

		// verify
		InOrder order = inOrder(mockMDService, mockUserService, mockRelevanceFeedbackEngine, mockPersistentQueryService);
		order.verify(mockMDService).find(Long.parseLong(movieId));
		order.verify(mockUserService).saveRelevantResult(Mockito.eq(1L), Mockito.any(MovieDescriptor.class));

		order.verify(mockRelevanceFeedbackEngine).getRefinedQuery(Mockito.eq(queryWeights), Mockito.eq(movieIds));
		order.verify(mockPersistentQueryService).update(Mockito.eq(persistentQuery));
	}
}
