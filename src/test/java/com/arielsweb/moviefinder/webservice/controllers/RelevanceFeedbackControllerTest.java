package com.arielsweb.moviefinder.webservice.controllers;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import javax.management.relation.InvalidRelationIdException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.UserService;

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

    private MovieDescriptorService mockRDService;
    private UserService mockUserService;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private User mockUser;

    @Before
    public void setUp() {
	mockUser = mock(User.class);
	mockRDService = mock(MovieDescriptorService.class);
	mockUserService = mock(UserService.class);

	controller = new RelevanceFeedbackController();
	controller.setMovieDescriptorService(mockRDService);
	controller.setUserService(mockUserService);
    }
    
    @Test
    public void testMarkIt() throws InvalidRelationIdException {
	// execute
	String movieId = "1234";
	controller.markIt(movieId, mockRequest, mockResponse, mockUser);

	// verify
	InOrder order = inOrder(mockRDService, mockUserService);
	order.verify(mockRDService).find(Long.parseLong(movieId));
	order.verify(mockUserService).save(mockUser);
    }
}
