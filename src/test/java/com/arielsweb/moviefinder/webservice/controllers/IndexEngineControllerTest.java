package com.arielsweb.moviefinder.webservice.controllers;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.impl.IndexEngineServiceImpl;

/**
 * Tests the methods of IndexEngineController (using mocks)
 * 
 * @author Ariel
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class IndexEngineControllerTest {

	private IndexEngineServiceImpl indexEngineController;
	private MovieDescriptorService mockMovieDescriptorService;
	private IndexEngine mockIndexEngine;

	@Before
	public void setUp() {
		mockMovieDescriptorService = mock(MovieDescriptorService.class);
		mockIndexEngine = mock(IndexEngine.class);

		indexEngineController = new IndexEngineServiceImpl();
		indexEngineController.setMovieDescriptorService(mockMovieDescriptorService);
		indexEngineController.setIndexingEngine(mockIndexEngine);
	}

	@Test
	public void testAddEntry() throws InvalidMovieDescriptorException {
		// setup
		MovieSource source = new MovieSource();
		source.setLocation("http://www.imdb.com");
		source.setName("IMDB");
		source.setId(1L);

		MovieDescriptor movieDescriptor = new MovieDescriptor();
		movieDescriptor.setSynopsis("ABCD");
		movieDescriptor.setName("A");
		movieDescriptor.setSource(source);
		movieDescriptor.setRemoteId("123");

		// execute
		indexEngineController.addEntry(movieDescriptor);

		// verify
		InOrder order = inOrder(mockMovieDescriptorService, mockIndexEngine);
		order.verify(mockMovieDescriptorService).save(movieDescriptor);
		order.verify(mockIndexEngine).addEntry(movieDescriptor);
	}

	@Test
	public void testRemoveEntry() throws InvalidMovieDescriptorException {
		// execute
		indexEngineController.removeEntry("1");

		// verify
		InOrder order = inOrder(mockMovieDescriptorService, mockIndexEngine);
		order.verify(mockMovieDescriptorService).delete(1L);
		order.verify(mockIndexEngine).removeEntry(1L);
	}

	@Test
	public void testUpdateEntry() throws InvalidMovieDescriptorException {
		// setup
		User user = new User();
		user.setId(1L);

		MovieSource source = new MovieSource();
		source.setLocation("http://www.rottenttomatoes.com");
		source.setName("Rotten Tomatoes");

		MovieDescriptor movieDescriptor = new MovieDescriptor();
		movieDescriptor.setSynopsis("ABCD");
		movieDescriptor.setName("A");
		movieDescriptor.setId(1L);
		movieDescriptor.setSource(source);

		// execute
		indexEngineController.updateEntry(movieDescriptor);

		// verify
		InOrder order = inOrder(mockMovieDescriptorService, mockIndexEngine);
		order.verify(mockMovieDescriptorService).update(movieDescriptor);
		order.verify(mockIndexEngine).updateEntry(movieDescriptor);
	}
}
