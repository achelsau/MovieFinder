package com.arielsweb.moviefinder.crawler;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Tests {@link MovieListCrawler}
 * 
 * @author Ariel
 * 
 */
@DataSet("MovieListCrawlerTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class MovieListCrawlerTest {

    @SpringBeanByType
    public MovieListCrawler movieListCrawler;
    @SpringBeanByType
    public MovieDescriptorService movieDescriptorService;
    @SpringBeanByType
    public MovieSourceService movieSourceService;

    /**
     * Tests parsing http://www.arielsweb.com/utils/list_of_movies.html
     * 
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    @Test
    public void testParseListOfMovies() throws IOException, ParseException, InterruptedException {
	// setup
	MovieSource movieSource = movieListCrawler.getMovieSource("IMDB", "http://www.imdb.com");

	// execute
	movieListCrawler.parseMovieList("http://www.arielsweb.com/utils/list_of_movies.html", movieSource);

	// verify
	List<MovieDescriptor> movieDescriptors = movieDescriptorService.list();
	
	assertEquals(5, movieDescriptors.size());

	MovieDescriptor movieDescriptor1 = movieDescriptors.get(0);
	assertEquals("Man of Steel", movieDescriptor1.getName());
	assertEquals("tt0770828", movieDescriptor1.getRemoteId());

	MovieDescriptor movieDescriptor2 = movieDescriptors.get(1);
	assertEquals("World War Z", movieDescriptor2.getName());
	assertEquals("tt0816711", movieDescriptor2.getRemoteId());

	MovieDescriptor movieDescriptor3 = movieDescriptors.get(2);
	assertEquals("The Lone Ranger", movieDescriptor3.getName());
	assertEquals("tt1210819", movieDescriptor3.getRemoteId());

	MovieDescriptor movieDescriptor4 = movieDescriptors.get(3);
	assertEquals("Despicable Me 2", movieDescriptor4.getName());
	assertEquals("tt1690953", movieDescriptor4.getRemoteId());

	MovieDescriptor movieDescriptor5 = movieDescriptors.get(4);
	assertEquals("Evil Dead", movieDescriptor5.getName());
	assertEquals("tt1288558", movieDescriptor5.getRemoteId());
    }

    
}
