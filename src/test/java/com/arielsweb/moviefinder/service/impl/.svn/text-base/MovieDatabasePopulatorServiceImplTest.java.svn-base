package com.arielsweb.moviefinder.service.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.crawler.dto.RTAlternateIds;
import com.arielsweb.moviefinder.crawler.dto.RTMovieDTO;
import com.arielsweb.moviefinder.crawler.dto.RTMovieDetailsDTO;
import com.arielsweb.moviefinder.crawler.dto.RTMovieLinksDTO;
import com.arielsweb.moviefinder.crawler.dto.RTPosterLinks;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDatabasePopulatorService;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Tests {@link MovieDatabasePopulatorServiceImpl}
 * 
 * @author Ariel
 * 
 */
@DataSet
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class MovieDatabasePopulatorServiceImplTest {

    @SpringBeanByType
    private MovieDatabasePopulatorService service;
    @SpringBeanByType
    private MovieSourceService movieSourceService;
    @SpringBeanByType
    private MovieDescriptorService movieDescriptorService;

    /**
     * Tests the insertion of a movie into the database using the service that
     * receives information from the API client.
     */
    @Test
    public void testInsertMovieIntoDb() {
	// setup
	RTMovieDTO rtMovieDTO = new RTMovieDTO();
	rtMovieDTO.setId("1234");
	rtMovieDTO.setSynopsis("A nice long synopsis");
	rtMovieDTO.setCriticsConsensus("A nice not so long critics consensus");
	rtMovieDTO.setTitle("A beautiful mind");
	rtMovieDTO.setYear(2001);

	RTMovieLinksDTO movieLinks = new RTMovieLinksDTO();
	movieLinks.setAlternate("http://alernate_link");
	movieLinks.setSelf("http://self_link");
	rtMovieDTO.setLinks(movieLinks);

	RTAlternateIds alternateIds = new RTAlternateIds();
	alternateIds.setImdb("imdb123");

	RTMovieDetailsDTO detailsDTO = new RTMovieDetailsDTO();
	detailsDTO.setAlternateIds(alternateIds);

	HashSet<String> genres = new HashSet<String>();
	genres.add("SCIENCE_FICTION_FANTASY");
	genres.add("MYSTERY_SUSPENSE");
	detailsDTO.setGenres(genres.toArray(new String[0]));
	
	RTPosterLinks posters = new RTPosterLinks();
	posters.setThumbnail("link to thumbnail");
	detailsDTO.setPosters(posters);

	MovieSource source = new MovieSource();
	source.setId(1L);
	source.setLocation("rotten tomatoes location");
	source.setName("Rotten Tomatoes");
	movieSourceService.save(source);

	// execute
	service.insertMovieIntoDb(rtMovieDTO, detailsDTO, source);

	// verify
	List<MovieDescriptor> movies = movieDescriptorService.list();
	assertEquals(1, movies.size());
	
	MovieDescriptor movieDescriptor = movies.get(0);
	assertEquals(rtMovieDTO.getTitle(), movieDescriptor.getName());
	assertEquals(rtMovieDTO.getYear(), movieDescriptor.getYear());
	assertEquals(rtMovieDTO.getSynopsis(), movieDescriptor.getSynopsis());
	assertEquals(rtMovieDTO.getCriticsConsensus(), movieDescriptor.getAlternateSynopsis());
	assertEquals(rtMovieDTO.getId(), movieDescriptor.getRemoteId());
	assertEquals(source.getId(), movieDescriptor.getSource().getId());

	assertEquals(detailsDTO.getAlternateIds().getImdb(), movieDescriptor.getAlternateId());
	Genre[] movieGenres = movieDescriptor.getGenres().toArray(new Genre[0]);
	
	for (Genre movie : movieGenres) {
	    assertTrue(genres.contains(movie.toString()));
	}
    }

    @After
    public void tearDown() {
	// verify
	List<MovieDescriptor> movies = movieDescriptorService.list();
	assertEquals(1, movies.size());
    }
}
