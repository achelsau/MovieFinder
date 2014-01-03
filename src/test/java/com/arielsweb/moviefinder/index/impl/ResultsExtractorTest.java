package com.arielsweb.moviefinder.index.impl;

import java.util.Date;
import java.util.LinkedList;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;

import com.arielsweb.moviefinder.index.dto.ResultInfo;

/**
 * @author Ariel
 *
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class ResultsExtractorTest {

    private ResultInfo[] results = new ResultInfo[1000];
    private LinkedList<ResultInfo> resultsLL = new LinkedList<ResultInfo>();
    
    @Before
    public void setUp() {

    }

    @Test
    public void testExtractHeap() {
	ResultsExtractor resultsExtractor = new ResultsExtractor();

	Float score = .15f;
	for (int i = 0; i < results.length; i++) {
	    results[i] = new ResultInfo();
	    results[i].setId((long)i);
	    results[i].setScore(score * i);
	    results[i].setReleaseDate(DateTime.now().toDate());
	}
	resultsExtractor.buildMaxHeap(results);
	
	for (int i = 0; i < results.length; i++) {
	    resultsLL.add(results[i]);
	}

	Float[] actual = new Float[results.length];
	Float[] expected = new Float[results.length];

	long start = System.currentTimeMillis();
	for (int i = 0; i < results.length; i++) {
	    actual[i] = resultsExtractor.heapExtractMax(results).getScore();
	    expected[i] = (results.length  - 1 - i) * score;
	}
	long end = System.currentTimeMillis();
	System.out.println("Took: " + (end - start));
	
	for (int i = 0; i < results.length; i++) {
	    Assert.assertEquals(expected[i], actual[i]);
	}
    }

    @Test
    public void testExtractHeapWhereScoresAreEqual() {
	ResultsExtractor resultsExtractor = new ResultsExtractor();

	Float score = .15f;
	
	DateTime now = DateTime.now();
	
	for (int i = 0; i < results.length; i++) {
	    results[i] = new ResultInfo();
	    results[i].setId((long) i);
	    results[i].setScore(score);

	    now = now.plusMonths(1);
	    results[i].setReleaseDate(now.toDate());
	}
	now = new DateTime(results[results.length - 1].getReleaseDate());

	resultsExtractor.buildMaxHeap(results);

	for (int i = 0; i < results.length; i++) {
	    resultsLL.add(results[i]);
	}

	Date[] actual = new Date[results.length];
	Date[] expected = new Date[results.length];

	long start = System.currentTimeMillis();


	for (int i = 0; i < results.length; i++) {
	    actual[i] = resultsExtractor.heapExtractMax(results).getReleaseDate();
	    expected[i] = now.toDate();

	    now = now.minusMonths(1);
	}
	long end = System.currentTimeMillis();
	System.out.println("Took: " + (end - start));

	for (int i = 0; i < results.length; i++) {
	    Assert.assertEquals(expected[i], actual[i]);
	}
    }
}
