package com.arielsweb.moviefinder.service;

import com.arielsweb.moviefinder.crawler.dto.RTMovieDTO;
import com.arielsweb.moviefinder.crawler.dto.RTMovieDetailsDTO;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;

/**
 * Populates the database with {@link MovieDescriptor}s. Once it gets the info
 * from the APIs, it populates the model object and stores it into the database.
 * 
 * @author Ariel
 * 
 */
public interface MovieDatabasePopulatorService {

    /**
     * For a specific population run, either take a fresh copy of the
     * {@link MovieSource} from the database or insert a new one.
     * 
     * @param sourceName
     *            the name of the source from where to get the movies
     * @param url
     *            the url of the source (http://www.imdb.com,
     *            http://www.rottentomatoes.com, etc)
     * 
     * @return the source from which the movie was taken from
     */
    public abstract MovieSource getMovieSource(String name, String url);

    /**
     * Inserts the movie into the database
     * 
     * @param rtMovieDTO
     *            high-level details about the movie (name, year, synopsis, etc)
     * @param rtMovieDetail
     *            more in depth details about the movie (genre, posters, cast,
     *            etc)
     * @param owner
     *            the owner of the movie to be inserted
     */
    public abstract void insertMovieIntoDb(RTMovieDTO rtMovieDTO, RTMovieDetailsDTO rtMovieDetails, MovieSource source);

    /**
     * Setters for the services that insert things into DB
     */
    public abstract void setMovieSourceService(MovieSourceService movieOwnerService);

    public abstract void setMovieDescriptorService(MovieDescriptorService movieDescriptorService);

}