package com.arielsweb.moviefinder.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.util.IndexReadWriteHelper;
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
    public void populateIndex() throws InvalidIndexPopulationException, InvalidMovieDescriptorException, IOException,
	    ClassNotFoundException {
	if (invertedIndexEngine.getInvertedIndex().size() > 0) {
	    throw new InvalidIndexPopulationException("The index already has data in it! Don't overwrite it!");
	}

	if (IndexReadWriteHelper.serializedIndexExists("index_serialized")) {
	    IndexReadWriteHelper.setCorpusAndMovieDetails(invertedIndexEngine, "index_serialized");

	    return;
	}

	// query for the Movie Descriptors
	List<Long> ids = getMovieIds();

	invertedIndexEngine.clearIndex();

	for (Long id : ids) {
	    MovieDescriptor movieDescriptor = super.find(id);

	    invertedIndexEngine.addEntry(movieDescriptor);

	    log.info("Added movie " + movieDescriptor.getId() + " to MBI");
	}

	log.info("Inverted index has " + invertedIndexEngine.getNumberOfDocuments() + " documents with "
		+ invertedIndexEngine.getInvertedIndex().size() + " words in them.");

	IndexReadWriteHelper.serializeIndex(invertedIndexEngine);
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getMovieIds() {
	SQLQuery query = createSQLQuery("SELECT movie.id FROM " + getTableName() + " movie ").addScalar("id",
		LongType.INSTANCE);
	
	return query.list();
    }
}
