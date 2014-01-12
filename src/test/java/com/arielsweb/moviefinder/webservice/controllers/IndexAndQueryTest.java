package com.arielsweb.moviefinder.webservice.controllers;

import static junit.framework.Assert.assertEquals;
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
import com.arielsweb.moviefinder.index.dto.ResultInfoResponse;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;
import com.arielsweb.moviefinder.service.UserService;
import com.arielsweb.moviefinder.service.impl.IndexEngineServiceImpl;
import com.arielsweb.moviefinder.webservice.exceptions.InvalidQuickQueryException;

/**
 * Integration tests for the indexing + querying operations
 * 
 * @author Ariel
 * 
 */
@DataSet("IndexAndQueryTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class IndexAndQueryTest {
    @SpringBeanByType
    private IndexEngineServiceImpl indexEngineController;
    @SpringBeanByType
    private QueryEngineController queryEngineController;
    @SpringBeanByType
    private MovieDescriptorService rdService;
    @SpringBeanByType
    private MovieSourceService roService;
    @SpringBeanByType
    private UserService userService;

    @SpringBeanByType
    private IndexEngine invertedIndexEngine;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MovieDescriptor rd1, rd2, rd3, rd4, rd5;
    private User user;

    @Before
    public void setUp() throws InvalidMovieDescriptorException {
	request = mock(HttpServletRequest.class);
	response = mock(HttpServletResponse.class);

	user = new User();
	user.setUsername("TestUser");
	user.setPassword("qwerty");
	user.setId(1L);
	user.setIsOnline(true);
	
	userService.save(user);

	invertedIndexEngine.clearIndex();
	rd1 = rdService.find(1L);
	invertedIndexEngine.addEntry(rd1);
	rd2 = rdService.find(2L);
	invertedIndexEngine.addEntry(rd2);
	rd3 = rdService.find(3L);
	invertedIndexEngine.addEntry(rd3);
	rd4 = rdService.find(4L);
	invertedIndexEngine.addEntry(rd4);
	rd5 = rdService.find(5L);
	invertedIndexEngine.addEntry(rd5);
    }

    /**
     * Scenario 1:
     * 
     * <pre>
     * 1. Index movie from a fictitious source
     * 2. Make a query by applying the exact words from its description. 
     * 3. Check the results.
     * </pre>
     * 
     * @throws InvalidQuickQueryException
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void indexMovieAndQueryForIt() throws InvalidQuickQueryException, InvalidMovieDescriptorException {
	// index movie
	MovieSource source = new MovieSource();
	source.setLocation("http://rottentomatoes.com");
	source.setName("Rotten Tomatoes");
	roService.save(source);

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setSynopsis("A cool project about distributed operating systems");
	movieDescriptor.setName("MyCoolProject.zip");
	movieDescriptor.setSource(source);
	movieDescriptor.setRemoteId("987670");
	movieDescriptor.setYear(2012);
	movieDescriptor.setRemotePath("C:\\Windows");

	indexEngineController.addEntry(movieDescriptor);

	// query the MBI
	ResultInfoResponse resultInfoResponse = queryEngineController.quickQuery(
		"cool project about distributed operating systems", request, response, user);

	// check results
	List<ResultInfo> results = resultInfoResponse.getResults();

	// Because the test data might change in the future it is advisable to
	// assume that many results will be received here, although at the
	// moment of this test's creation only the above added is. Among these
	// there must be the movie shared in this test. When it is found it's
	// exact properties are checked.
	for (ResultInfo entry : results) {
	    if (entry.getId().equals(movieDescriptor.getId())) {
		assertEquals(movieDescriptor.getName(), entry.getTitle());
		assertEquals(movieDescriptor.getSynopsis(), entry.getDescription());
		assertEquals(movieDescriptor.getSource().getName(), entry.getSource());
		assertEquals(movieDescriptor.getRemotePath(), entry.getRemotePath());
	    }
	}
    }

    /**
     * Scenario 2:
     * 
     * <pre>
     * 1. Index movie from a fictitious source
     * 2. Remove that movie from the index
     * 3. Make a query for it
     * 3. Check the results.
     * </pre>
     * 
     * @throws InvalidQuickQueryException
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void indexMovieRemoveItAndQueryForIt() throws InvalidQuickQueryException, InvalidMovieDescriptorException {
	// 1. Index movie from a fictitious source
	MovieSource source = new MovieSource();
	source.setLocation("http://www.rottentomatoes.com");
	source.setName("Rotten");
	roService.save(source);

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setSynopsis("A cool project about distributed operating systems");
	movieDescriptor.setName("MyCoolProject.zip");
	movieDescriptor.setYear(2012);
	movieDescriptor.setRemoteId("98769");
	movieDescriptor.setRemotePath("C:\\Windows");
	movieDescriptor.setSource(source);

	indexEngineController.addEntry(movieDescriptor);

	// remove the movie from the index
	indexEngineController.removeEntry(movieDescriptor.getId().toString());

	// query for that movie
	ResultInfoResponse resultInfo = queryEngineController.quickQuery(
		"cool project about distributed operating systems", request, response, user);
	assertEquals(0, resultInfo.getResults().size());
    }

    /**
     * Scenario 3:
     * 
     * <pre>
     * 1. Index movie from a fictitious source
     * 2. Update the movie's description
     * 3. Make a query for it
     * 3. Check the results.
     * </pre>
     * 
     * @throws InvalidQuickQueryException
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void indexMovieUpdateItAndQueryForIt() throws InvalidQuickQueryException, InvalidMovieDescriptorException {
	// 1. Index movie from a fictitious source
	MovieSource source = new MovieSource();
	source.setLocation("http://www.rottentomatoes.com");
	source.setName("Rotten Tomatoes");
	roService.save(source);

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setSynopsis("A cool project about distributed operating systems");
	movieDescriptor.setName("MyCoolProject.zip");
	movieDescriptor.setSource(source);
	movieDescriptor.setRemoteId("987671");
	movieDescriptor.setYear(2012);
	movieDescriptor.setRemotePath("C:\\Windows");

	Long movieDescriptorId = indexEngineController.addEntry(movieDescriptor);

	assertEquals(movieDescriptorId, movieDescriptor.getId());

	movieDescriptor.setSynopsis("A cool project about regular operating systems");

	// remove the movie from the index
	indexEngineController.updateEntry(movieDescriptor);

	// query for that movie
	ResultInfoResponse resultInfoResponse = queryEngineController.quickQuery(
		"cool project about regular operating systems", request, response, user);

	// check results
	List<ResultInfo> results = resultInfoResponse.getResults();

	// Because the test data might change in the future it is advisable to
	// assume that many results will be received here, although at the
	// moment of this test's creation only the above added is. Among these
	// there must be the movie added in this test. When it is found it's
	// exact properties are checked.
	for (ResultInfo entry : results) {
	    if (entry.getId().equals(movieDescriptor.getId())) {
		assertEquals(movieDescriptor.getName(), entry.getTitle());
		assertEquals(movieDescriptor.getSynopsis(), entry.getDescription());
		assertEquals(movieDescriptor.getSource().getName(), entry.getSource());
		assertEquals(movieDescriptor.getRemotePath(), entry.getRemotePath());
	    }
	}
    }
}
