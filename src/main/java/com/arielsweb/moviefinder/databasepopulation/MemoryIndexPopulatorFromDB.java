package com.arielsweb.moviefinder.databasepopulation;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.util.IndexReadWriteHelper;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.service.MovieDescriptorService;

/**
 * Used for populating the MBI with data from DB
 * 
 * @author Ariel
 *
 */
@Component
public class MemoryIndexPopulatorFromDB {
    
    @Autowired
    private static MovieDescriptorService movieDescriptorService;

    @Autowired
    private static IndexEngine indexEngine;
    
    protected static org.apache.log4j.Logger log = Logger.getLogger(MemoryIndexPopulatorFromDB.class);

    private static long indexDataFromDB(long count) {
	// populate the index
	for (int i = 1; i <= count; i++) {
	    MovieDescriptor movieDescriptor = movieDescriptorService.find((long) i);
	    if (movieDescriptor != null) {
		try {
		    indexEngine.addEntry(movieDescriptor);

		    log.warn("indexed: " + i);
		} catch (InvalidMovieDescriptorException imde) {
		    log.warn("didn't index " + i + " because of not enough data");
		}
	    }
	}
	return count;
    }
    
    /**
     * Index data from the database
     * 
     * @throws IOException
     * @throws InvalidMovieDescriptorException
     * 
     */
    public static void indexFromDatabase() throws IOException, InvalidMovieDescriptorException {
	long start = System.currentTimeMillis();
	long end = System.currentTimeMillis();

	// 1. get movies from the database
	indexDataFromDB(16100);

	end = System.currentTimeMillis();

	log.warn("indexing took: " + (end - start));

	// 2. serialize the MBI
	IndexReadWriteHelper.serializeIndex(indexEngine);
    }
    
    /**
     * @param args
     * @throws InvalidMovieDescriptorException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, InvalidMovieDescriptorException {
	
	indexFromDatabase();
	
    }

}
