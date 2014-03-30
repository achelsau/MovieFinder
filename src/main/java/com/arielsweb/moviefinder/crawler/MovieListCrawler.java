package com.arielsweb.moviefinder.crawler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arielsweb.moviefinder.crawler.exception.InvalidMovieException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Given the paginated list of movies from http://www.imdb.com, this crawler
 * takes the link to each movie and hands it to {@link MovieObjectCrawler} to
 * produce a brand new {@link MovieDescriptor}.
 * 
 * @author Ariel
 * 
 */
@Service("movieListCrawler")
public class MovieListCrawler {

    @Autowired
    private MovieDescriptorService movieDescriptorService;
    @Autowired
    private MovieObjectCrawler movieObjectCrawler;
    /** The logger. */
    protected org.apache.log4j.Logger log = Logger.getLogger(MovieListCrawler.class);

    @Autowired
    private MovieSourceService movieSourceService;

    /**
     * Creates a new movie list crawler
     */
    public MovieListCrawler() {

    }

    /**
     * For a specific population run, either take a fresh copy of the
     * {@link MovieSource} from the database or insert a new one.
     */
    public MovieSource getMovieSource(String sourceName, String url) {
	List<MovieSource> sources = movieSourceService.list();
	MovieSource source = null;
	if (sources.size() == 1) {
	    source = sources.get(0);
	} else {
	    source = new MovieSource();
	    source.setLocation(url);
	    source.setName(sourceName);
	    movieSourceService.save(source);
	}

	return source;
    }

    /**
     * Parse the list of movies from a specified URL
     * 
     * @param url
     *            the url to parse
     * @param movieSource
     *            the {@link MovieSource} such as IMDB, Rotten Tomatoes, etc.
     * @throws IOException
     * @throws InterruptedException
     * @return the number of movies that were inserted
     */
    public int parseMovieList(String url, MovieSource source) throws IOException, InterruptedException {
	Document doc = null;
	try {
	    doc = Jsoup.connect(url.toString())
		.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
		.get();
	} catch (SocketTimeoutException ex) {
	    // retry if a Socket time out exception occured
	    return this.parseMovieList(url, source);
	}

	Elements movieLinks = doc.select("#main .results .title");
	int counter = 0;

	for (int i = 0; i < movieLinks.size(); i++) {
	    Element element = movieLinks.get(i).select(">a").get(0);
	    Elements ratingIneligible = movieLinks.get(i).select(".rating-ineligible>a");
	    if (ratingIneligible.size() > 0 && ratingIneligible.text().equals("Not yet released")) {
		continue;
	    }
	    String linkToMovie = element.attr("href");

	    String movieId = linkToMovie.substring(linkToMovie.indexOf("title") + 6, linkToMovie.length() - 1);
	    if (movieDescriptorService.isUnique(movieId, source.getId(), null, null)) {
		Thread.sleep(1000);

		try {
		    MovieDescriptor movieDescriptor = movieObjectCrawler.getMovieObject("http://www.imdb.com/title/",
			    movieId);

		    if (movieDescriptor != null) {
			movieDescriptor.setSource(source);
			movieDescriptorService.save(movieDescriptor);
			log.info("Added " + movieDescriptor.getName());

			counter++;
		    }
		} catch (InvalidMovieException ex) {
		    // do nothing here and jump to next movie
		    continue;
		}
	    }
	}
	
	return counter;
    }

    public void setMovieSourceService(MovieSourceService movieSourceService) {
	this.movieSourceService = movieSourceService;
    }
}
