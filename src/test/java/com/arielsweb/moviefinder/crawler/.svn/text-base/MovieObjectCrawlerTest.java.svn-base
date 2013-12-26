package com.arielsweb.moviefinder.crawler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateMidnight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.crawler.dto.MovieDTO;
import com.arielsweb.moviefinder.crawler.exception.InvalidMovieException;
import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.model.MovieCrewPersonType;
import com.arielsweb.moviefinder.model.MovieDescriptor;

/**
 * Tests {@link MovieObjectCrawler}
 * 
 * @author Ariel
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
public class MovieObjectCrawlerTest {

    @SpringBeanByType
    private MovieObjectCrawler movieObjectCrawler;

    /**
     * Tests getting a {@link MovieDTO} based on "The Matrix" from
     * http://www.arielsweb.com/utils/tt0133093.
     * 
     * @throws IOException
     * @throws InvalidMovieException
     */
    @Test
    public void testGettingMatrixMovie() throws IOException, InvalidMovieException {
	// execute
	MovieDescriptor movieDescriptor = movieObjectCrawler.getMovieObject("http://www.arielsweb.com/utils/",
		"tt0133093");

	// verify
	assertNotNull(movieDescriptor);

	// movie name, id, remote path, image path
	assertEquals("The Matrix", movieDescriptor.getName());
	assertEquals("http://www.arielsweb.com/utils/tt0133093", movieDescriptor.getRemotePath());
	assertEquals("tt0133093", movieDescriptor.getRemoteId());
	assertEquals(
		"./Matrix (1999) - IMDb_files/MV5BMjEzNjg1NTg2NV5BMl5BanBnXkFtZTYwNjY3MzQ5._V1_SY317_CR6,0,214,317_.jpg",
		movieDescriptor.getImagePath());

	// movie year and release date
	assertEquals(new Integer(1999), movieDescriptor.getYear());
	assertEquals(new DateMidnight(1999, 3, 31).toDate(), movieDescriptor.getReleaseDate());

	// cast
	Set<MovieCrewPerson> actors = new HashSet<MovieCrewPerson>();
	actors.add(new MovieCrewPerson("Keanu Reeves", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Laurence Fishburne", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Carrie-Anne Moss", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Hugo Weaving", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Gloria Foster", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Joe Pantoliano", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Marcus Chong", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Julian Arahanga", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Matt Doran", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Belinda McClory", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Anthony Ray Parker", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Paul Goddard", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Robert Taylor", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("David Aston", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Marc Aden Gray", MovieCrewPersonType.ACTOR));
	assertEquals(actors, movieDescriptor.getActors());
	
	// directors
	Set<MovieCrewPerson> directors = new HashSet<MovieCrewPerson>();
	directors.add(new MovieCrewPerson("Andy Wachowski", MovieCrewPersonType.DIRECTOR));
	directors.add(new MovieCrewPerson("Lana Wachowski", MovieCrewPersonType.DIRECTOR));
	assertEquals(directors, movieDescriptor.getDirectors());
	
	// screenwriters
	Set<MovieCrewPerson> screenwriters = new HashSet<MovieCrewPerson>();
	screenwriters.add(new MovieCrewPerson("Andy Wachowski", MovieCrewPersonType.SCREENWRITER));
	screenwriters.add(new MovieCrewPerson("Lana Wachowski", MovieCrewPersonType.SCREENWRITER));
	assertEquals(screenwriters, movieDescriptor.getScreenWriters());

	// synopsis
	assertEquals("Thomas A. Anderson is a man living two lives. By day he is an average computer programmer "
		+ "and by night a hacker known as Neo. Neo has always questioned his reality, but the truth "
		+ "is far beyond his imagination. Neo finds himself targeted by the police when he is contacted "
		+ "by Morpheus, a legendary computer hacker branded a terrorist by the government. Morpheus "
		+ "awakens Neo to the real world, a ravaged wasteland where most of humanity have been captured "
		+ "by a race of machines that live off of the humans' body heat and electrochemical energy and "
		+ "who imprison their minds within an artificial reality known as the Matrix. As a rebel against "
		+ "the machines, Neo must return to the Matrix and confront the agents: super-powerful computer "
		+ "programs devoted to snuffing out Neo and the entire human rebellion. Written by redcommander27",
		movieDescriptor.getSynopsis());

	assertEquals("A computer hacker learns from mysterious rebels about the true nature"
		+ " of his reality and his role in the war against its controllers.",
		movieDescriptor.getAlternateSynopsis());

	assertEquals(Double.valueOf(8.7), movieDescriptor.getRating());
    }

    /**
     * Tests getting a {@link MovieDTO} based on "Guang dong xiao lao hu" from
     * http://www.imdb.com/title/tt0068000/.
     * 
     * @throws IOException
     * @throws InvalidMovieException
     */
    @Test
    public void testGettingMovie_WithoutCompleteReleaseDate1() throws IOException, InvalidMovieException {
	// execute
	MovieDescriptor movieDescriptor = movieObjectCrawler.getMovieObject("http://www.imdb.com/title/", "tt0068000");

	// verify
	assertNotNull(movieDescriptor);

	// movie year and release date
	assertEquals(new Integer(1979), movieDescriptor.getYear());
	assertEquals(new DateMidnight(1979, 1, 1).toDate(), movieDescriptor.getReleaseDate());
    }

    /**
     * Tests getting a {@link MovieDTO} based on "The Toy Tiger" from
     * http://www.imdb.com/title/tt0049873/.
     * 
     * @throws IOException
     * @throws InvalidMovieException
     */
    @Test
    public void testGettingMovie_WithoutCompleteReleaseDate2() throws IOException, InvalidMovieException {
	// execute
	MovieDescriptor movieDescriptor = movieObjectCrawler.getMovieObject("http://www.imdb.com/title/", "tt0049873");

	// verify
	assertNotNull(movieDescriptor);

	// movie year and release date
	assertEquals(new Integer(1956), movieDescriptor.getYear());
	assertEquals(new DateMidnight(1956, 7, 1).toDate(), movieDescriptor.getReleaseDate());
    }

    /**
     * Tests getting a {@link MovieDTO} from
     * http://www.imdb.com/title/tt0144744/.
     * 
     * @throws IOException
     * @throws InvalidMovieException
     */
    @Test
    public void testGettingMovie_WithoutReleaseDate() throws IOException, InvalidMovieException {
	// execute
	MovieDescriptor movieDescriptor = movieObjectCrawler.getMovieObject("http://www.imdb.com/title/", "tt0144744");

	// verify
	assertNotNull(movieDescriptor);

	// movie year and release date
	assertEquals(new Integer(1966), movieDescriptor.getYear());
	assertEquals(new DateMidnight(1966, 1, 1).toDate(), movieDescriptor.getReleaseDate());
    }
}
