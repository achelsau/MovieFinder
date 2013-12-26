package com.arielsweb.moviefinder.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.exceptions.InvalidIndexPopulationException;
import com.arielsweb.moviefinder.utilities.Utils;

/**
 * Deals with operations at the level of movie descriptors
 * 
 * @author Ariel
 * @data 1/12/2011
 */
@Repository("movieDescriptorServiceImpl")
public class MovieDescriptorServiceImpl extends GenericServiceImpl<MovieDescriptor> implements MovieDescriptorService {
    private IndexEngine invertedIndexEngine;

    @Override
    @Transactional(readOnly = false)
    public void populateIndex() throws InvalidIndexPopulationException, InvalidMovieDescriptorException {
	if (invertedIndexEngine.getInvertedIndex().size() > 0) {
	    throw new InvalidIndexPopulationException("The index already has data in it! Don't spoil it!");
	}

	// query for the Movie Descriptors
	List<MovieDescriptor> movieDescriptors = super.list();

	invertedIndexEngine.clearIndex();

	for (MovieDescriptor movie : movieDescriptors) {
	    invertedIndexEngine.addEntry(movie);
	}

	log.info("Inverted index has " + invertedIndexEngine.getNumberOfDocuments() + " documents with "
		+ invertedIndexEngine.getInvertedIndex().size() + " words in them.");
    }

    @Override
    protected String getTableName() {
	return Utils.getTableForEntity(MovieDescriptor.class);
    }

    /**
     * Based on remote id and the owner, it will be determined if the movie is
     * duplicated in the database. If, for some reason, no remote id is received
     * from the source, then the method will calculate the uniqueness based on
     * name and year, as a last resort option.
     **/
    @Override
    @Transactional(readOnly = true)
    public boolean isUnique(String remoteId, Long sourceId, String movieName, String year) {
	StringBuilder uniquenessQuery = new StringBuilder("SELECT count(movie.id) FROM " + getTableName() + " movie ");

	if (remoteId != null && sourceId != null) {
	    uniquenessQuery.append(" WHERE movie.remote_id = '" + remoteId + "' ");
	    uniquenessQuery.append(" AND movie.source = '" + sourceId + "' ");
	} else {
	    // escape single quotes and double quotes
	    String escapedName = movieName.replace("'", "''");
	    escapedName = escapedName.replace(":", "\\:");

	    uniquenessQuery.append("WHERE movie.name = '" + escapedName + "'");
	    if (year != null) {
		uniquenessQuery.append(" AND movie.year = '" + year + "' ");
	    }
	}

	SQLQuery query = createSQLQuery(uniquenessQuery.toString());

	return ((BigInteger) query.uniqueResult()).compareTo(BigInteger.ZERO) == 0;
    }

    @Autowired
    public void setInvertedIndexEngine(IndexEngine invertedIndexEngine) {
	this.invertedIndexEngine = invertedIndexEngine;
    }
}
