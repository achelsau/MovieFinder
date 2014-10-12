package com.arielsweb.moviefinder.index.impl;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Tests {@link RelevanceFeedbackEngine}
 * 
 * @author Ariel
 * 
 */
@DataSet("ClearDescriptorsGenresAndSources.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.DISABLED)
public class RelevanceFeedbackEngineTest {

	@SpringBeanByType
	private MovieDescriptorService movieDescriptorService;
	@SpringBeanByType
	private IndexEngine invertedIndexEngine;
	@SpringBeanByType
	private MovieSourceService movieSourceService;

	@SpringBeanByType
	private RelevanceFeedbackEngine relevanceFeedbackEngine;

	// /**
	// * Tests the results returned by the query method from the
	// * {@link IQueryEngine#queryIndex(String)}. The algorithm won't normalize
	// * the scores according to document length.
	// *
	// * @throws InvalidMovieDescriptorException
	// **/
	// @Test public void testRelevanceEngineForSuccessfulQuery() throws
	// InvalidMovieDescriptorException { // setup
	// saveDescriptorsIntoDatabase();
	//
	// // invertedIndexEngine.outputIndex();
	// invertedIndexEngine.clearIndex();
	//
	// invertedIndexEngine.addEntry(descriptor1Serenity);
	//
	// invertedIndexEngine.addEntry(descriptor2WorldOnWire);
	//
	// invertedIndexEngine.addEntry(descriptor3IronMan);
	//
	// invertedIndexEngine.addEntry(descriptor4HitchHiker);
	//
	// invertedIndexEngine.addEntry(descriptor5Alien);
	//
	// invertedIndexEngine.addEntry(descriptor6DayAfter);
	//
	// invertedIndexEngine.addEntry(descriptor7Prometheus);
	//
	// invertedIndexEngine.addEntry(descriptor8GhostShip);
	//
	// invertedIndexEngine.outputIndex();
	//
	// // execute String query = "nuclear battle on Earth";
	// List<ResultInfo> results = cosineQueryEngine.queryIndex(query);
	//
	// // verify
	// assertNotNull(results);
	//
	// Map<String, Float> queryTokens = new HashMap<String, Float>();
	// queryTokens.put("nuclear", 1f); queryTokens.put("battle", 1f);
	// queryTokens.put("earth", 1f); Long[] relevantDocuments = {
	// descriptor7Prometheus.getId() };
	//
	// Map<String, Float> newQueryVector =
	// relevanceFeedbackEngine.getRefinedQuery(queryTokens,
	// relevantDocuments, invertedIndexEngine.getInvertedIndex()); //
	// assertNotNull(newQueryVector);
	//
	// System.out.println();
	//
	// results = cosineQueryEngine.queryIndex(newQueryVector);
	// }

	// @Test
	// public void testRelevanceEngineForSuccessfulQuery2() throws
	// InvalidMovieDescriptorException {
	// // setup
	// saveDescriptorsIntoDatabase();
	//
	// // invertedIndexEngine.outputIndex();
	// invertedIndexEngine.clearIndex();
	//
	// invertedIndexEngine.addEntry(descriptor1Serenity);
	//
	// invertedIndexEngine.addEntry(descriptor2WorldOnWire);
	//
	// invertedIndexEngine.addEntry(descriptor3IronMan);
	//
	// invertedIndexEngine.addEntry(descriptor4HitchHiker);
	//
	// invertedIndexEngine.addEntry(descriptor5Alien);
	//
	// invertedIndexEngine.addEntry(descriptor6DayAfter);
	//
	// invertedIndexEngine.addEntry(descriptor7Prometheus);
	//
	// invertedIndexEngine.addEntry(descriptor8GhostShip);
	//
	// invertedIndexEngine.outputIndex();
	//
	// Map<String, Float> queryTokens = new HashMap<String, Float>();
	// queryTokens.put("nuclear", 1f);
	// queryTokens.put("battle", 1f);
	// queryTokens.put("earth", 1f);
	// Long[] relevantDocuments = { descriptor7Prometheus.getId() };
	//
	// // execute
	// Map<String, Float> newQueryVector =
	// relevanceFeedbackEngine.getRefinedQuery(queryTokens, relevantDocuments);
	// // verify
	// assertNotNull(newQueryVector);
	//
	// System.out.println();
	// }

	/**
	 * Test running the relevance engine with a regular query with a relevant
	 * document against a small index
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRelevance_RegularQuery_OneRelevantDocument_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		Map<String, Float> queryTokens = new HashMap<String, Float>();
		queryTokens.put("explor", 1f);
		queryTokens.put("earth", 1f);
		queryTokens.put("mankind", 1f);
		Long[] relevantDocuments = { descriptor3Prometheus.getId() };

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokens, relevantDocuments);

		// verify
		String[] parsedSynopsis = TextParsingHelper.parseText(descriptor3Prometheus.getSynopsis());

		assertEquals(parsedSynopsis.length, newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			if (queryVectorEntry.getKey().equals("explor") || queryVectorEntry.getKey().equals("earth")
					|| queryVectorEntry.getKey().equals("mankind")) {
				assertEquals(1.8788898f, queryVectorEntry.getValue());
			} else if (queryVectorEntry.getKey().equals("scott") || queryVectorEntry.getKey().equals("alien")
					|| queryVectorEntry.getKey().equals("ridley")) {
				assertEquals(0.3040988f, queryVectorEntry.getValue());
			} else {
				assertEquals(0.87888986f, queryVectorEntry.getValue());
			}
		}
	}

	/**
	 * Test running the relevance feedback algorithm against an empty query and
	 * with a relevant document
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRelevance_EmptyQuery_OneRelevantDocument_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		// empty query
		Map<String, Float> queryTokens = new HashMap<String, Float>();
		Long[] relevantDocuments = { descriptor3Prometheus.getId() };

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokens, relevantDocuments);

		// verify
		String[] parsedSynopsis = TextParsingHelper.parseText(descriptor3Prometheus.getSynopsis());

		assertEquals(parsedSynopsis.length, newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			if (queryVectorEntry.getKey().equals("scott") || queryVectorEntry.getKey().equals("alien")
					|| queryVectorEntry.getKey().equals("ridley")) {
				assertEquals(0.3040988f, queryVectorEntry.getValue());
			} else {
				assertEquals(0.87888986f, queryVectorEntry.getValue());
			}
		}
	}

	/**
	 * Regular non-empty query with more than one movie marked as relevant
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRelevance_RegularQuery_MoreRelevantDocuments_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		String query = "exploration of Earth and Mankind";

		String[] tokens = TextParsingHelper.parseText(query);
		Map<String, Float> queryTokensWithScores = new HashMap<String, Float>();
		for (String token : tokens) {
			queryTokensWithScores.put(token, 1f);

		}
		Long[] relevantDocuments = { descriptor3Prometheus.getId(), descriptor2Alien.getId() };

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokensWithScores,
				relevantDocuments);

		// verify
		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor3Prometheus
				.getSynopsis() + descriptor2Alien.getSynopsis())));

		assertEquals(parsedWords.size(), newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			if (queryVectorEntry.getKey().equals("explor") || queryVectorEntry.getKey().equals("earth")
					|| queryVectorEntry.getKey().equals("mankind")) {
				assertEquals(1.4394449f, queryVectorEntry.getValue());
			} else if (queryVectorEntry.getKey().equals("scott") || queryVectorEntry.getKey().equals("alien")
					|| queryVectorEntry.getKey().equals("ridley")) {
				assertEquals(0.32437208f, queryVectorEntry.getValue());
			} else {
				assertEquals(0.43944493f, queryVectorEntry.getValue());
			}
		}
	}

	/**
	 * Empty query with more than one movie marked as relevant
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRelevance_EmptyQuery_MoreRelevantDocuments_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		Map<String, Float> queryTokensWithScores = new HashMap<String, Float>();
		Long[] relevantDocuments = { descriptor3Prometheus.getId(), descriptor2Alien.getId() };

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokensWithScores,
				relevantDocuments);

		// verify
		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor3Prometheus
				.getSynopsis() + descriptor2Alien.getSynopsis())));

		assertEquals(parsedWords.size(), newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			if (queryVectorEntry.getKey().equals("scott") || queryVectorEntry.getKey().equals("alien")
					|| queryVectorEntry.getKey().equals("ridley")) {
				assertEquals(0.32437208f, queryVectorEntry.getValue());
			} else {
				assertEquals(0.43944493f, queryVectorEntry.getValue());
			}
		}
	}

	/**
	 * Regular (non-empty) query with no documents marked as relevant
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRelevance_RegularQuery_NoRelevantDocuments_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		String query = "exploration of Earth and Mankind";

		String[] tokens = TextParsingHelper.parseText(query);
		Map<String, Float> queryTokensWithScores = new HashMap<String, Float>();
		for (String token : tokens) {
			queryTokensWithScores.put(token, 1f);
		}
		// no relevant document
		Long[] relevantDocuments = new Long[0];

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokensWithScores,
				relevantDocuments);

		// verify
		assertEquals(3, newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			assertEquals(1.0f, queryVectorEntry.getValue());
		}
	}

	/**
	 * Tests running an empty query with an empty list of relevant documents
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void test_EmptyQuery_NoRelevantDocuments_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		// empty query (not NULL) and no empty list of relevant documents
		Map<String, Float> queryTokensWithScores = new HashMap<String, Float>();
		Long[] relevantDocuments = new Long[0];

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokensWithScores,
				relevantDocuments);

		// verify
		assertEquals(0, newQueryVector.size());
	}

	/**
	 * Tests the relevance feedback algorithm with a null query and one relevant
	 * document
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void test_NullQuery_OneRelevantEntry_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		// one document considered relevant
		Long[] relevantDocuments = { descriptor3Prometheus.getId() };

		// execute (null queryTokensWithScore are passed in)
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(null, relevantDocuments);

		// verify
		String[] parsedSynopsis = TextParsingHelper.parseText(descriptor3Prometheus.getSynopsis());

		assertEquals(parsedSynopsis.length, newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			if (queryVectorEntry.getKey().equals("scott") || queryVectorEntry.getKey().equals("alien")
					|| queryVectorEntry.getKey().equals("ridley")) {
				assertEquals(0.3040988f, queryVectorEntry.getValue());
			} else {
				assertEquals(0.87888986f, queryVectorEntry.getValue());
			}
		}
	}

	/**
	 * Tests relevance feedback with a regular query (non-empty) and null
	 * relevant documents
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void test_RegularQuery_NullRelevantDocuments_ExistingIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		invertedIndexEngine.addEntry(descriptor1IronMan);

		invertedIndexEngine.addEntry(descriptor2Alien);

		invertedIndexEngine.addEntry(descriptor3Prometheus);

		String query = "exploration of Earth and Mankind";

		String[] tokens = TextParsingHelper.parseText(query);
		Map<String, Float> queryTokensWithScores = new HashMap<String, Float>();
		for (String token : tokens) {
			queryTokensWithScores.put(token, 1f);
		}
		// null relevant documents
		Long[] relevantDocuments = null;

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokensWithScores,
				relevantDocuments);

		// verify
		assertEquals(3, newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			assertEquals(1.0f, queryVectorEntry.getValue());
		}
	}

	/**
	 * Tests the relevance feedback with a regular query, some relevant
	 * documents and an empty index
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void test_RegularQuery_MoreThanOneRelevantEntries_EmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		// make the index empty
		invertedIndexEngine.clearIndex();

		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1IronMan = new MovieDescriptor();
		descriptor1IronMan.setName("Iron Man");
		descriptor1IronMan.setYear(2008);
		descriptor1IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor1IronMan.setRemoteId("12347");
		descriptor1IronMan.setSource(movieSource);
		descriptor1IronMan
				.setSynopsis("An action-packed take on the tale of wealthy philanthropist Tony Stark, who develops an invulnerable robotic suit to fight the throes of evil.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor1IronMan);

		MovieDescriptor descriptor2Alien = new MovieDescriptor();
		descriptor2Alien.setName("Alien");
		descriptor2Alien.setYear(1979);
		descriptor2Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor2Alien.setRemoteId("12349");
		descriptor2Alien.setSource(movieSource);
		descriptor2Alien
				.setSynopsis("A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2Alien.setGenres(genres);
		movieDescriptorService.save(descriptor2Alien);

		MovieDescriptor descriptor3Prometheus = new MovieDescriptor();
		descriptor3Prometheus.setName("Prometheus");
		descriptor3Prometheus.setYear(2012);
		descriptor3Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor3Prometheus.setRemoteId("12351");
		descriptor3Prometheus.setSource(movieSource);
		descriptor3Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. A team of explorers discover a clue to the origins of mankind on Earth.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor3Prometheus);

		String query = "exploration of Earth and Mankind";

		String[] tokens = TextParsingHelper.parseText(query);
		Map<String, Float> queryTokensWithScores = new HashMap<String, Float>();
		for (String token : tokens) {
			queryTokensWithScores.put(token, 1f);
		}
		Long[] relevantDocuments = { descriptor3Prometheus.getId(), descriptor2Alien.getId() };

		// execute
		Map<String, Float> newQueryVector = relevanceFeedbackEngine.getRefinedQuery(queryTokensWithScores,
				relevantDocuments);

		// verify (the query is untouched because we wouldn't want to lose
		// information about what user previously liked)
		assertEquals(3, newQueryVector.size());

		for (Map.Entry<String, Float> queryVectorEntry : newQueryVector.entrySet()) {
			assertEquals(1.0f, queryVectorEntry.getValue());
		}
	}
}
