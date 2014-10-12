package com.arielsweb.moviefinder.service.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.service.MovieCrewPersonService;

/**
 * Tests {@link MovieCrewPersonServiceImpl}
 * 
 * @author Ariel
 * 
 */
@DataSet("MovieDatabasePopulatorServiceImplTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class MovieCrewPersonServiceImplTest {
	@SpringBeanByType
	public MovieCrewPersonService movieCrewPersonService;

	/**
	 * Tests getting a {@link MovieCrewPerson} by username
	 */
	@Test
	public void testGetMovieCrewPersonByUsername() {
		// setup
		MovieCrewPerson movieCrewPerson = new MovieCrewPerson();
		movieCrewPerson.setFullName("Alain Delon");
		movieCrewPersonService.save(movieCrewPerson);

		// execute
		MovieCrewPerson actualMovieCrewPerson = movieCrewPersonService.getMovieCrewPersonByName(movieCrewPerson
				.getFullName());

		// verify
		assertEquals(movieCrewPerson, actualMovieCrewPerson);
	}

	/**
	 * Tests getting a {@link MovieCrewPerson} by username with empty database
	 */
	@Test
	public void testGetMovieCrewPersonByUsername_EmptyDB() {
		// no setup

		// execute
		MovieCrewPerson actualMovieCrewPerson = movieCrewPersonService.getMovieCrewPersonByName("Alain Delon");

		// verify
		assertNull(actualMovieCrewPerson);
	}
}
