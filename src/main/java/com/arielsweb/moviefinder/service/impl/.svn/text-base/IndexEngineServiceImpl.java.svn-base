package com.arielsweb.moviefinder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.service.MovieDescriptorService;

/**
 * Index Engine Controller that provides a an interface with the data access
 * layer and the indexing engine
 * 
 * @author Ariel
 * @version 1.0, 15/07/2012
 */
@Service("indexEngineServiceImpl")
public class IndexEngineServiceImpl {
    private MovieDescriptorService movieDescriptorService;
    private IndexEngine indexingEngine;

    /**
     * Adds a movie description to the DB and MBI
     * 
     * @param movieDescriptor
     *            the {@link MovieDescriptor} to add to both DB and MBI
     * @return the id of the newly added movie descriptor
     * @throws InvalidMovieDescriptorException
     */
    public Long addEntry(MovieDescriptor movieDescriptor) throws InvalidMovieDescriptorException {
	// first save the entity into the database (the id will be populated
	// after that)
	movieDescriptorService.save(movieDescriptor);

	// add to MBI
	indexingEngine.addEntry(movieDescriptor);

	return movieDescriptor.getId();
    }

    /**
     * Removes a {@link MovieDescriptor} from the DB and MBI
     * 
     * @param id
     *            the id of the movie descriptor to remove
     * @throws InvalidMovieDescriptorException
     */
    public void removeEntry(@PathVariable("movieId") String id) throws InvalidMovieDescriptorException {
	Long movieId = Long.valueOf(id);
	movieDescriptorService.delete(movieId);

	indexingEngine.removeEntry(movieId);
    }

    /**
     * Updates a {@link MovieDescriptor} from the DB and MBI
     * 
     * @param movieDescriptor
     *            the {@link MovieDescriptor} to update (important: it needs a
     *            valid id from the DB in order for the update to be performed)
     * @throws InvalidMovieDescriptorException
     */
    public void updateEntry(@RequestBody MovieDescriptor movieDescriptor) throws InvalidMovieDescriptorException {
	movieDescriptorService.update(movieDescriptor);

	indexingEngine.updateEntry(movieDescriptor);
    }

    @Autowired
    public void setMovieDescriptorService(MovieDescriptorService movieDescriptorService) {
	this.movieDescriptorService = movieDescriptorService;
    }

    @Autowired
    public void setIndexingEngine(IndexEngine indexingEngine) {
	this.indexingEngine = indexingEngine;
    }
}
