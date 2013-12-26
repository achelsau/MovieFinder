package com.arielsweb.moviefinder.service;

import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.exceptions.InvalidIndexPopulationException;

/**
 * Interface for implementing the service that will interact with 
 * the data-store (either through MySql or some other fancier stuff
 * such as MongoDB)
 * 
 * @author Ariel
 */
public interface MovieDescriptorService extends GenericService<MovieDescriptor> {
    /**
     * Based on remote id and the owner, it will be determined if the movie is
     * duplicated in the database. If, for some reason, no remote id is received
     * from the source, then the method will calculate the uniqueness based on
     * name and year, as a last resort option.
     * 
     * @param remoteId
     *            the id of the movie from the source site
     * @param ownerId
     *            the id of the {@link MovieSource} of the movie to add
     * @param movieName
     *            the name of the movie taken from the source site
     * @param year
     *            the year of release for the movie (taken, as well, from the
     *            source)
     */
    boolean isUnique(String remoteId, Long ownerId, String movieName, String year);

    /**
     * When the application loads into memory this method is responsible for
     * populating the memory based index (inverted-index).
     * 
     * This method should be called only once, when the server starts.
     * 
     * The main usage would be for index recovery from the database if the
     * memory index is lost. For example, in case of a power outage, the MBI
     * disappears but it still has a copy in the descriptors from the database
     * so, having those it can be recreated using this method.
     * 
     * @throws InvalidIndexPopulationException
     *             thrown when the index is already populated
     */
    void populateIndex() throws InvalidIndexPopulationException, InvalidMovieDescriptorException;
}
