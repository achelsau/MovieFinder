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

    /**
     * The Settings
     */
    private static final int MAX_NUMBER_OF_MINUTES_TO_CRAWL = 100;
    public static final int MINIMUM_ACCEPTED_RATING = 6;
    private static final int START_CRAWL_FROM_INDEX = 7701;

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
    public void populateDatabaseFromIMDB(int numberOfMinutesToCrawl)
	    throws IOException, ParseException, InterruptedException {
	MovieSource movieSource = movieListCrawler.getMovieSource("IMDB", "http://www.imdb.com");

	int i = START_CRAWL_FROM_INDEX, count = 0;

	DateTime startDate = DateTime.now();
	DateTime endDate = startDate.plusMinutes(MAX_NUMBER_OF_MINUTES_TO_CRAWL);
	
	while (DateTime.now().isBefore(endDate)) {
	    count += movieListCrawler.parseMovieList("http://www.imdb.com/search/title?at=0&sort=moviemeter,asc&start="
		    + i + "&title_type=feature", movieSource);

	    Logger.getLogger(DatabasePopulator.class).info(
		    "Finished inserting page starting from " + i + " and reached count " + count);

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

	ApplicationContext appContext = new ClassPathXmlApplicationContext("WEB-INF/applicationContext.xml");

	appContext.getAutowireCapableBeanFactory().autowireBean(databasePopulator);

	databasePopulator.populateDatabaseFromIMDB(MAX_NUMBER_OF_MINUTES_TO_CRAWL);

    }
}
