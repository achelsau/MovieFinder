package com.arielsweb.moviefinder.index.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.util.IndexReadWriteHelper;

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
		IndexReadWriteHelper.setCorpusAndMovieDetails(indexEngine, "index_serialized_full_name_cast");

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
	public void testIndexingLargeDatabase() throws InvalidMovieDescriptorException, FileNotFoundException, IOException,
			ClassNotFoundException {
		long end = 0;

		/**
		 * 1. Setup (put the index into memory)
		 */
		IndexReadWriteHelper.setCorpusAndMovieDetails(indexEngine, "index_serialized_full_name_cast");

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

}
