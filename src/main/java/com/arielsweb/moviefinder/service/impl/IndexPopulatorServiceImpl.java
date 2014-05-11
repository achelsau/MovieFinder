package com.arielsweb.moviefinder.service.impl;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.exceptions.InvalidIndexPopulationException;

/**
 * Selects all {@link MovieDescriptor}s from the datastore and populate the
 * inverted index. This one must be singleton so in case someone wants to
 * Autowire it in different classes only 1 instance of it is created.
 * 
 * @author Ariel
 * 
 */
@Service("indexPopulatorServiceImpl")
@Scope("singleton")
public class IndexPopulatorServiceImpl {

    private MovieDescriptorService movieDescriptorService;

    protected org.apache.log4j.Logger log = Logger.getLogger(IndexPopulatorServiceImpl.class);

    /* Prevent direct access to the constructor */
    private IndexPopulatorServiceImpl() {
    }

    /**
     * Populates the memory based index (inverted-index)
     * 
     * @throws InvalidIndexPopulationException
     *             thrown if the index alredy contains data
     * @throws InvalidMovieDescriptorException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @PostConstruct
    public void populateIndex() throws InvalidIndexPopulationException, InvalidMovieDescriptorException, IOException,
	    ClassNotFoundException {
	movieDescriptorService.populateIndex();
    }

    @Autowired
    public void setMovieDescriptor(MovieDescriptorService movieDescriptorService) {
	this.movieDescriptorService = movieDescriptorService;
    }
}
