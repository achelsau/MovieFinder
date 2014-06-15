package com.arielsweb.moviefinder.databasepopulation;

import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.crawler.MovieListCrawler;
import com.arielsweb.moviefinder.model.MovieSource;

/**
 * Used for populating the DB with data from IMDB
 * 
 * @author Ariel
 * 
 */
@Component
public class DatabasePopulator {

    private static final int NUMBER_OF_MINUTES_TO_CRAWL = 180;
    private static int NUMBER_OF_MOVIES_TO_BE_INSERTED_IN_DB = 6000;
    public static int MINIMUM_ACCEPTED_RATING = 6;

    @Autowired
    private MovieListCrawler movieListCrawler;

    /**
     * Inserts a predefined number of movies into the DB or as much as it can in
     * 100 minutes
     * 
     * @throws IOException
     * @throws ParseException
     * @throws InterruptedException
     */
    public void populateDatabaseFromIMDB(int numberOfMoviesToBeInserted, int numberOfMinutesToCrawl)
	    throws IOException, ParseException, InterruptedException {
	MovieSource movieSource = movieListCrawler.getMovieSource("IMDB", "http://www.imdb.com");

	int i = 2851;

	DateTime startDate = DateTime.now();

	while (i < NUMBER_OF_MOVIES_TO_BE_INSERTED_IN_DB
		&& DateTime.now().isBefore(startDate.plusMinutes(NUMBER_OF_MINUTES_TO_CRAWL))) {
	    movieListCrawler.parseMovieList("http://www.imdb.com/search/title?at=0&sort=moviemeter,asc&start=" + i
		    + "&title_type=feature", movieSource);

	    Logger.getLogger(DatabasePopulator.class).info("Finished inserting page starting from " + i);

	    i += 50;
	}
    }

    /**
     * @param args
     * @throws InterruptedException
     * @throws ParseException
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
	DatabasePopulator databasePopulator = new DatabasePopulator();

	ApplicationContext appContext = new ClassPathXmlApplicationContext(
	        "WEB-INF/applicationContext.xml");
	
	appContext.getAutowireCapableBeanFactory().autowireBean(databasePopulator);

	databasePopulator.populateDatabaseFromIMDB(NUMBER_OF_MOVIES_TO_BE_INSERTED_IN_DB, NUMBER_OF_MINUTES_TO_CRAWL);

    }
}
