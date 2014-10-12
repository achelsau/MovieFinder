package com.arielsweb.moviefinder.index.impl;

import java.util.Date;

import junit.framework.Assert;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
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

	@Before
	public void setUp() {

	}

	@Test
	public void testExtractHeap() {
		// setup
		ResultsExtractor resultsExtractor = new ResultsExtractor();

		Float score = .15f;
		for (int i = 0; i < results.length; i++) {
			results[i] = new ResultInfo();
			results[i].setId((long) i);
			results[i].setScore(score * i);
			results[i].setReleaseDate(DateTime.now().toDate());
		}

		// execute
		resultsExtractor.buildMaxHeap(results);

		Float[] actual = new Float[results.length];
		Float[] expected = new Float[results.length];

		long start = System.currentTimeMillis();
		for (int i = 0; i < results.length; i++) {
			actual[i] = resultsExtractor.heapExtractMax(results).getScore();
			expected[i] = (results.length - 1 - i) * score;
		}
		long end = System.currentTimeMillis();
		System.out.println("Took: " + (end - start));

		// verify
		for (int i = 0; i < results.length; i++) {
			Assert.assertEquals(expected[i], actual[i]);
		}
	}

	@Ignore
	@Test
	public void testExtractHeapWhereScoresAreEqual() {
		// setup
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

		// execute
		resultsExtractor.buildMaxHeap(results);

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

		// verify
		for (int i = 0; i < results.length; i++) {
			Assert.assertEquals(expected[i], actual[i]);
		}
	}

	@Ignore
	@Test
	public void testExtractHeapWherePartOfScoresAreEqual() {
		// setup
		ResultsExtractor resultsExtractor = new ResultsExtractor();

		ResultInfo[] resultsInfo = new ResultInfo[5];
		ResultInfo resultInfo1 = new ResultInfo();
		resultInfo1.setId(1L);
		resultInfo1.setScore(1f);
		resultInfo1.setReleaseDate(new DateMidnight(1990, 11, 11).toDate());

		ResultInfo resultInfo2 = new ResultInfo();
		resultInfo2.setId(2L);
		resultInfo2.setScore(1f);
		resultInfo2.setReleaseDate(new DateMidnight(1995, 11, 11).toDate());

		ResultInfo resultInfo3 = new ResultInfo();
		resultInfo3.setId(3L);
		resultInfo3.setScore(1f);
		resultInfo3.setReleaseDate(new DateMidnight(2000, 11, 11).toDate());

		ResultInfo resultInfo4 = new ResultInfo();
		resultInfo4.setId(4L);
		resultInfo4.setScore(2f);
		resultInfo4.setReleaseDate(new DateMidnight(2001, 11, 11).toDate());

		ResultInfo resultInfo5 = new ResultInfo();
		resultInfo5.setId(5L);
		resultInfo5.setScore(2f);
		resultInfo5.setReleaseDate(new DateMidnight(2005, 11, 11).toDate());

		resultsInfo = new ResultInfo[] { resultInfo1, resultInfo2, resultInfo3, resultInfo4, resultInfo5 };

		// execute
		resultsExtractor.buildMaxHeap(resultsInfo);

		Date[] actual = new Date[resultsInfo.length];
		Date[] expected = new Date[] { resultInfo5.getReleaseDate(), resultInfo4.getReleaseDate(),
				resultInfo3.getReleaseDate(), resultInfo2.getReleaseDate(), resultInfo1.getReleaseDate() };

		for (int i = 0; i < 5; i++) {
			actual[i] = resultsExtractor.heapExtractMax(resultsInfo).getReleaseDate();
		}

		// verify
		for (int i = 0; i < results.length; i++) {
			Assert.assertEquals(expected[i], actual[i]);
		}
	}
}
