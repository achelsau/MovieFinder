package com.arielsweb.moviefinder.index.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.MovieDetailsDTO;
import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.service.MovieDescriptorService;

/**
 * Test indexing almost 7000 movie synopsis and critics consensus
 * 
 * @author Ariel
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
public class QueryLargeDatabaseTest {
    @SpringBeanByType
    private MovieDescriptorService movieDescriptorService;

    @SpringBeanByType
    private IndexEngine indexEngine;

    @SpringBeanByType
    private CosineScoreQueryEngine queryEngine;
    
    @SpringBeanByType
    private RelevanceFeedbackEngine relevanceFeedbackEngine;

    protected org.apache.log4j.Logger log = Logger.getLogger(QueryLargeDatabaseTest.class);

    @Test
    @Ignore
    public void testIndexMovies() throws IOException, ClassNotFoundException {
	long startAll = 0;
	long end = 0;

	startAll = System.currentTimeMillis();
	FileInputStream inStream = new FileInputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer\\dbscript\\index_serialized"));
	ObjectInputStream ois = new ObjectInputStream(inStream);

	HashMap<String, IndexEntry> invertedIndex = readInvertedIndex(ois);
	HashMap<Long, MovieDetailsDTO> movieDetails = readMovieDetails(ois);

	indexEngine.setCorpus(invertedIndex);
	indexEngine.setMovieDetails(movieDetails);

	indexEngine.writeIndexToFile();

	// 3. query index
	long startQuery = System.currentTimeMillis();
	List<ResultInfo> results = queryEngine.queryIndex("dog waiting for his master train station");
	for (ResultInfo resultInfo : results) {
	    log.warn(resultInfo.getScore() + ", " + resultInfo.getId() + ", " + resultInfo.getTitle() + ", "
		    + resultInfo.getDescription());
	}
	end = System.currentTimeMillis();
	log.warn("querying took: " + (end - startQuery));

	log.warn("everything took: " + (end - startAll));

	// indexEngine.writeIndexToFile();
    }
    
    @Test
    @Ignore
    public void testIndexingLargeDatabase() throws InvalidMovieDescriptorException, FileNotFoundException, IOException, ClassNotFoundException {
	long end = 0;

	/**
	 * 1. Setup (put the index into memory)
	 */
	FileInputStream inStream = new FileInputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer\\dbscript\\index_serialized"));
	ObjectInputStream ois = new ObjectInputStream(inStream);

	HashMap<String, IndexEntry> invertedIndex = readInvertedIndex(ois);
	HashMap<Long, MovieDetailsDTO> movieDetails = readMovieDetails(ois);

	indexEngine.setCorpus(invertedIndex);
	indexEngine.setMovieDetails(movieDetails);

	/**
	 * 2. Run the query 1st time
	 */
	long startQuery = System.currentTimeMillis();

	log.warn("Results: ");
	queryEngine.queryIndex("dog found by his master at a train station");
	end = System.currentTimeMillis();
	log.warn("Querying took: " + (end - startQuery));

	/**
	 * 3. Mark two results as relevant
	 */
	Long[] relevantDocuments = { 846L, 3228L };
	Map<String, Float> queryTokens = queryEngine.getQueryWeights();

	Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokens, relevantDocuments);

	/**
	 * 4. Run the query again
	 */
	queryEngine.queryIndex(newQueryVector);
    }

    @SuppressWarnings("unused")
    private long indexDataFromDB(long count) throws InvalidMovieDescriptorException {
	// populate the index
	for (int i = 1; i <= 28026; i++) {
	    MovieDescriptor movieDescriptor = movieDescriptorService.find((long) i);
	    if (movieDescriptor != null) {
		indexEngine.addEntry(movieDescriptor);

		log.warn("indexed: " + count);
		count++;
	    }
	}
	return count;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, IndexEntry> readInvertedIndex(ObjectInputStream ois) throws IOException,
	    ClassNotFoundException {
	HashMap<String, IndexEntry> invertedIndex = (HashMap<String, IndexEntry>) ois.readObject();

	return invertedIndex;
    }

    @SuppressWarnings("unchecked")
    private HashMap<Long, MovieDetailsDTO> readMovieDetails(ObjectInputStream ois) throws IOException,
	    ClassNotFoundException {

	HashMap<Long, MovieDetailsDTO> movieDetails = (HashMap<Long, MovieDetailsDTO>) ois.readObject();

	return movieDetails;
    }

    /**
     * Index data from the database
     * 
     * @throws IOException
     * @throws InvalidMovieDescriptorException
     * 
     */
    @SuppressWarnings("unused")
    private void indexFromDatabase() throws IOException, InvalidMovieDescriptorException {
	long start = System.currentTimeMillis();
	long end = System.currentTimeMillis();

	// 1. get movies from the database
	List<MovieDescriptor> movieDescriptors = movieDescriptorService.list();
	end = System.currentTimeMillis();
	log.warn("querying the whole movies took: " + (end - start));

	// 2. add them to the memory based index
	start = System.currentTimeMillis();
	for (MovieDescriptor movieDescriptor : movieDescriptors) {
	    indexEngine.addEntry(movieDescriptor);
	}
	end = System.currentTimeMillis();

	serializeIndex();

	log.warn("indexing took: " + (end - start));
    }

    private void serializeIndex() throws FileNotFoundException, IOException {
	FileOutputStream outStream = new FileOutputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer\\dbscript\\index_serialized"));
	ObjectOutputStream oos = new ObjectOutputStream(outStream);
	oos.writeObject(indexEngine.getInvertedIndex());
	oos.writeObject(indexEngine.getMovieDetails());
    }
}
