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
 * Test indexing almost 14000 movie data
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
		"D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\index_serialized_full_name_cast"));
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
    }
    
    @Test
    @Ignore
    public void testIndexingLargeDatabase() throws InvalidMovieDescriptorException, FileNotFoundException, IOException, ClassNotFoundException {
	long end = 0;

	/**
	 * 1. Setup (put the index into memory)
	 */
	FileInputStream inStream = new FileInputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\index_serialized_full_name_cast"));
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
	queryEngine.queryIndex("orphaned dinosaur raised by lemurs");
	end = System.currentTimeMillis();
	log.warn("Querying took: " + (end - startQuery));

	/**
	 * 3. Mark one result as relevant
	 */
	Long[] relevantDocuments = { 1473L };
	Map<String, Float> queryTokens = new HashMap<String, Float>();

	queryTokens.put("orphan", 1f);
	queryTokens.put("dinosaur", 1f);
	queryTokens.put("raise", 1f);
	queryTokens.put("lemur", 1f);

	/**
	 * queryTokens.put("dog", 1f); queryTokens.put("found", 1f);
	 * queryTokens.put("master", 1f); queryTokens.put("train", 1f);
	 * queryTokens.put("station", 1f);
	 **/

	Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokens, relevantDocuments);

	/**
	 * 4. Run the query again
	 */
	queryEngine.queryIndex(newQueryVector);
    }

    private long indexDataFromDB(long count) {
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
    @Test
    @Ignore
    public void indexFromDatabase() throws IOException, InvalidMovieDescriptorException {
	long start = System.currentTimeMillis();
	long end = System.currentTimeMillis();

	// 1. get movies from the database
	indexDataFromDB(16100);

	end = System.currentTimeMillis();

	log.warn("indexing took: " + (end - start));

	// 2. serialize the MBI
	serializeIndex();
    }

    private void serializeIndex() throws FileNotFoundException, IOException {
	FileOutputStream outStream = new FileOutputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\index_serialized"));
	ObjectOutputStream oos = new ObjectOutputStream(outStream);
	oos.writeObject(indexEngine.getInvertedIndex());
	oos.writeObject(indexEngine.getMovieDetails());
    }
}
