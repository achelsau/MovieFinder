package com.arielsweb.moviefinder.index.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.MovieDetailsDTO;
import com.arielsweb.moviefinder.index.dto.Posting;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.model.MovieCrewPersonType;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;
import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * Tests the {@link IndexEngine}'s default implementation
 * 
 * @author Ariel
 * 
 */
@DataSet("ClearDescriptorsGenresAndSources.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class InvertedIndexEngineTest {
	@SpringBeanByType
	private static MovieDescriptorService movieDescriptorService;
	@SpringBeanByType
	private static IndexEngine invertedIndexEngine;
	private MovieDescriptor descriptor1Serenity, descriptor2WorldOnWire, descriptor3IronMan, descriptor4HitchHiker,
			descriptor5Alien, descriptor6DayAfter, descriptor7Prometheus, descriptor8GhostShip;
	@SpringBeanByType
	private MovieSourceService movieSourceService;

	@Before
	public void setUp() {

	}

	/**
	 * Saves {@link MovieDescriptor}s into database
	 */
	private void saveDescriptorsIntoDatabase() {
		// save the source
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		descriptor1Serenity = new MovieDescriptor();
		descriptor1Serenity.setId(1L);
		descriptor1Serenity.setName("Serenity");
		descriptor1Serenity.setYear(2005);
		descriptor1Serenity.setRemotePath("http://www.rottentomatoes.com/m/serenity/");
		descriptor1Serenity.setRemoteId("12345");
		descriptor1Serenity.setSource(movieSource);
		descriptor1Serenity
				.setSynopsis("A band of renegades on the run in outer space get in more hot water than they anticipated in this sci-fi action-adventure adapted from the television series Firefly. In the 26th century, the galaxy has been colonized by a military force known as the Alliance, but its leadership has not gone unquestioned. The Alliance was once challenged by a league of rebels known as the Independents, but the Alliance emerged victorious after a brutal civil war, with the surviving Independents scattering around the galaxy.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1Serenity.setGenres(genres);
		movieDescriptorService.save(descriptor1Serenity);

		descriptor2WorldOnWire = new MovieDescriptor();
		descriptor2WorldOnWire.setId(2L);
		descriptor2WorldOnWire.setName("World on a Wire");
		descriptor2WorldOnWire.setYear(1973);
		descriptor2WorldOnWire.setRemotePath("http://www.rottentomatoes.com/m/world_on_a_wire/");
		descriptor2WorldOnWire.setRemoteId("12346");
		descriptor2WorldOnWire.setSource(movieSource);
		descriptor2WorldOnWire
				.setSynopsis("A dystopic science-fiction epic, World on a Wire is German wunderkind Rainer Werner Fassbinder's gloriously cracked, boundlessly inventive take on future paranoia. With dashes of Kubrick, Vonnegut, and Dick, but a flavor entirely his own, Fassbinder tells the noir-spiked tale of reluctant action hero Fred Stiller (Klaus Lowitsch), a cybernetics engineer who uncovers a massive corporate and governmental conspiracy. At risk? Our entire (virtual) reality as we know it. This long unseen three-and-a-half-hour labyrinth is a satiric and surreal look at the weird world of tomorrow from one of cinema's kinkiest geniuses");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.TELEVISION);
		genres.add(Genre.ART_HOUSE_INTERNATIONAL);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor2WorldOnWire.setGenres(genres);
		movieDescriptorService.save(descriptor2WorldOnWire);

		descriptor3IronMan = new MovieDescriptor();
		descriptor3IronMan.setName("Iron Man");
		descriptor3IronMan.setId(3L);
		descriptor3IronMan.setYear(2008);
		descriptor3IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
		descriptor3IronMan.setRemoteId("12347");
		descriptor3IronMan.setSource(movieSource);
		descriptor3IronMan
				.setSynopsis("From Marvel Studios and Paramount Pictures comes Iron Man, an action-packed take on the tale of wealthy philanthropist Tony Stark (Robert Downey Jr.), who develops an invulnerable robotic suit to fight the throes of evil. In addition to being filthy rich, billionaire industrialist Tony Stark is also a genius inventor. When Stark is kidnapped and forced to build a diabolical weapon, he instead uses his intelligence and ingenuity to construct an indestructible suit of armor and escape his captors. Once free, Stark discovers a deadly conspiracy that could destabilize the entire globe, and dons his powerful new suit on a mission to stop the villains and save the world. Gwyneth Paltrow co-stars as his secretary, Virginia \"Pepper\" Potts, while Terrence Howard fills the role of Jim \"Rhodey\" Rhodes, one of Stark s colleagues, whose military background leads him to help in the formation of the suit. Jon Favreau directs, with Marvel movie veterans Avi Arad and Kevin Feige producing.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor3IronMan.setGenres(genres);
		movieDescriptorService.save(descriptor3IronMan);

		descriptor4HitchHiker = new MovieDescriptor();
		descriptor4HitchHiker.setName("The Hitchhiker's Guide to the Galaxy");
		descriptor4HitchHiker.setId(4L);
		descriptor4HitchHiker.setYear(2005);
		descriptor4HitchHiker.setRemotePath("http://www.rottentomatoes.com/m/hitchhikers_guide_to_the_galaxy/");
		descriptor4HitchHiker.setRemoteId("12348");
		descriptor4HitchHiker.setSource(movieSource);
		descriptor4HitchHiker
				.setSynopsis("Douglas Adams' oft-adapted tale of an normal guy making his way through the universe (it's already been presented as a novel, a radio serial, a television series, and a comic book) finally makes its way to the big screen in this endearingly goofy sci-fi comedy. Arthur Dent (Martin Freeman) is a very ordinary man who is having a truly unusual day -- after discovering that one of his best friends, Ford Prefect (Mos Def), is actually an alien, Ford tells him that the planet Earth is going to be destroyed so that otherworldly forces can make room for construction of a hyperspace bypass.");
		genres = new HashSet<Genre>();
		genres.add(Genre.COMEDY);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor4HitchHiker.setGenres(genres);
		movieDescriptorService.save(descriptor4HitchHiker);

		descriptor5Alien = new MovieDescriptor();
		descriptor5Alien.setName("Alien");
		descriptor5Alien.setId(5L);
		descriptor5Alien.setYear(1979);
		descriptor5Alien.setRemotePath("http://www.rottentomatoes.com/m/alien/");
		descriptor5Alien.setRemoteId("12349");
		descriptor5Alien.setSource(movieSource);
		descriptor5Alien
				.setSynopsis("'In space, no one can hear you scream.' A close encounter of the third kind becomes a Jaws-style nightmare when an alien invades a spacecraft in Ridley Scott's sci-fi horror classic. On the way home from a mission for the Company, the Nostromo's crew is woken up from hibernation by the ship's Mother computer to answer a distress signal from a nearby planet. Capt. Dallas's (Tom Skerritt) rescue team discovers a bizarre pod field, but things get even stranger when a face-hugging creature bursts out of a pod and attaches itself to Kane (John Hurt). Over the objections of Ripley (Sigourney Weaver), science officer Ash (Ian Holm) lets Kane back on the ship. The acid-blooded incubus detaches itself from an apparently recovered Kane, but an alien erupts from Kane's stomach and escapes. The alien starts stalking the humans, pitting Dallas and his crew (and cat) against a malevolent killing machine that also has a protector in the nefarious Company.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor5Alien.setGenres(genres);
		movieDescriptorService.save(descriptor5Alien);

		descriptor6DayAfter = new MovieDescriptor();
		descriptor6DayAfter.setName("The Day After");
		descriptor6DayAfter.setId(6L);
		descriptor6DayAfter.setYear(1983);
		descriptor6DayAfter.setRemotePath("http://www.rottentomatoes.com/m/day_after/");
		descriptor6DayAfter.setRemoteId("12350");
		descriptor6DayAfter.setSource(movieSource);
		descriptor6DayAfter
				.setSynopsis("A peaceful Midwestern city attempts to recover after it is destroyed by a nuclear missile strike in this powerful and deeply disturbing testament to the folly of pro-military hawks who believed that annihilation was a justifiable means of attaining power and control. The Day After originally aired on network television. At the end of the broadcast, many stations offered teams of counselors staffing 800 telephone numbers to help distraught viewers calm down.");
		genres = new HashSet<Genre>();
		genres.add(Genre.MYSTERY_SUSPENSE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor6DayAfter.setGenres(genres);
		movieDescriptorService.save(descriptor6DayAfter);

		descriptor7Prometheus = new MovieDescriptor();
		descriptor7Prometheus.setName("Prometheus");
		descriptor7Prometheus.setId(7L);
		descriptor7Prometheus.setYear(2012);
		descriptor7Prometheus.setRemotePath("http://www.rottentomatoes.com/m/prometheus_2012/");
		descriptor7Prometheus.setRemoteId("12351");
		descriptor7Prometheus.setSource(movieSource);
		descriptor7Prometheus
				.setSynopsis("Ridley Scott, director of Alien and Blade Runner, returns to the genre he helped define. With Prometheus, he creates a groundbreaking mythology, in which a team of explorers discover a clue to the origins of mankind on Earth, leading them on a thrilling journey to the darkest corners of the universe. There, they must fight a terrifying battle to save the future of the human race.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.HORROR);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor7Prometheus.setGenres(genres);
		movieDescriptorService.save(descriptor7Prometheus);

		descriptor8GhostShip = new MovieDescriptor();
		descriptor8GhostShip.setName("Ghost Ship");
		descriptor8GhostShip.setId(8L);
		descriptor8GhostShip.setYear(2002);
		descriptor8GhostShip.setRemotePath("http://www.rottentomatoes.com/m/ghost_ship/");
		descriptor8GhostShip.setRemoteId("12352");
		descriptor8GhostShip.setSource(movieSource);
		descriptor8GhostShip
				.setSynopsis("A salvage team think they've made the find of a lifetime, until they discover there's more on board than meets the eye in this supernatural thriller. Led by Captain Sean Murphy (Gabriel Byrne), the crew of the tugboat Arctic Warrior have discovered a sideline far more lucrative than hauling ships in and out of the harbor -- they locate missing or wrecked ships in international waters, repair them until they can be brought back to port, and then sell off the ship and its contents as salvage.");
		genres = new HashSet<Genre>();
		genres.add(Genre.HORROR);
		descriptor8GhostShip.setGenres(genres);
		movieDescriptorService.save(descriptor8GhostShip);

	}

	/**************************************************************************
	 ******************** Tests for Add entry to index ************************
	 **************************************************************************/

	/**
	 * Add 3 entries and check if the number of elements in the index is 3.
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testSimpleAdd() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		// execute
		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();
		Assert.assertTrue(invertedIndex.size() > 0);

		// verify
		// check that there's 3 entries as it results from the separate array
		assertEquals(3, invertedIndexEngine.getNumberOfDocuments().intValue());
	}

	/**
	 * Having an empty index, add a document and after that check the values for
	 * TF (Term Frequency) and IDF (Inverse Document Frequency)
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testAddSynopsisIntoEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// execute
		// add only the first one
		invertedIndexEngine.addEntry(descriptor1Serenity);

		// verify (check the words from the index, as well as, their idf/tf
		// values)
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		assertNotNull(invertedIndex);

		// compare number of words
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
				.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		assertEquals(indexWords, parsedWordsList);

		// analyze index tf/idfs
		for (String word : invertedIndex.keySet()) {
			IndexEntry indexEntry = invertedIndex.get(word);

			// check idf (must be 0 because there's only 1 document in the index
			// so log(1 / 1) = 0)
			assertEquals(0f, indexEntry.getIdf());

			// only one document must be present in the postings
			assertEquals(1, indexEntry.getPostings().size());

			// check tf
			if (word.startsWith("galax") || word.startsWith("independ")) {
				assertEquals(new Short((short) 2), indexEntry.getPostings().get(descriptor1Serenity.getId()).getTf());
			} else if (word.startsWith("allian")) {
				assertEquals(new Short((short) 3), indexEntry.getPostings().get(descriptor1Serenity.getId()).getTf());
			} else {
				assertEquals(new Short((short) 1), indexEntry.getPostings().get(descriptor1Serenity.getId()).getTf());
			}

			assertEquals(descriptor1Serenity.getGenres(), indexEntry.getPostings().get(descriptor1Serenity.getId())
					.getGenres());
		}
	}

	/**
	 * Given a non-empty index (with one entry added), let's see what happens
	 * when adding a new one.
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testAddSynopsisIntoNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// add only the first one
		invertedIndexEngine.addEntry(descriptor1Serenity);

		// execute
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptorGalaxyQuest = new MovieDescriptor();
		descriptorGalaxyQuest.setId(1L);
		descriptorGalaxyQuest.setName("Galaxy Quest");
		descriptorGalaxyQuest.setYear(1999);
		descriptorGalaxyQuest.setRemotePath("http://www.rottentomatoes.com/m/galaxy_quest/");
		descriptorGalaxyQuest.setRemoteId("18909");
		descriptorGalaxyQuest.setSource(movieSource);
		descriptorGalaxyQuest
				.setSynopsis("A team of intrepid adventurers travels through the outer reaches of the galaxy to team up with others");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.COMEDY);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptorGalaxyQuest.setGenres(genres);
		movieDescriptorService.save(descriptorGalaxyQuest);

		// add the new entry to the index
		invertedIndexEngine.addEntry(descriptorGalaxyQuest);

		// verify (check the words from the index, as well as, their idf/tf
		// values)
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		assertNotNull(invertedIndex);

		// compare number of words
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptorGalaxyQuest
				.getSynopsis() + " " + descriptor1Serenity.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		assertEquals(indexWords, parsedWordsList);

		// analyze index tf/idfs
		for (String word : invertedIndex.keySet()) {
			IndexEntry indexEntry = invertedIndex.get(word);

			// check idf (because we have 2 documents, some of the words will
			// have the idf 0, some 0.6931472. This is because words that appear
			// in both docs have log (2 / 2) = 0 and words that appear in only
			// one of the docs have log(2 / 1) = 0.6931472
			if (word.startsWith("adventur") || word.startsWith("galax") || word.startsWith("outer")) {
				assertEquals(0f, indexEntry.getIdf());
			} else {
				assertEquals(0.6931472f, indexEntry.getIdf());
			}

			// check tf
			for (Map.Entry<Long, Posting> postingEntry : indexEntry.getPostings().entrySet()) {
				if (postingEntry.getKey().equals(descriptor1Serenity.getId())) {
					if (word.startsWith("galax") || word.startsWith("independ")) {
						assertEquals(new Short((short) 2), postingEntry.getValue().getTf());
					} else if (word.equals("allianc")) {
						assertEquals(new Short((short) 3), postingEntry.getValue().getTf());
					} else {
						assertEquals(new Short((short) 1), postingEntry.getValue().getTf());
					}
				} else {
					if (word.startsWith("team")) {
						assertEquals(new Short((short) 2), postingEntry.getValue().getTf());
					} else {
						assertEquals(new Short((short) 1), postingEntry.getValue().getTf());
					}
				}
			}
		}
	}

	/**
	 * Given an empty index, add an empty synopsis to it.
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testAddEmptySynopsisIntoEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// execute
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptorGalaxyQuest = new MovieDescriptor();
		descriptorGalaxyQuest.setId(1L);
		descriptorGalaxyQuest.setName("Galaxy Quest");
		descriptorGalaxyQuest.setYear(1999);
		descriptorGalaxyQuest.setRemotePath("http://www.rottentomatoes.com/m/galaxy_quest/");
		descriptorGalaxyQuest.setRemoteId("18909");
		descriptorGalaxyQuest.setSource(movieSource);
		descriptorGalaxyQuest.setSynopsis("");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.COMEDY);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptorGalaxyQuest.setGenres(genres);
		movieDescriptorService.save(descriptorGalaxyQuest);

		// add the new entry to the index
		invertedIndexEngine.addEntry(descriptorGalaxyQuest);

		// verify (the index should contain no words)
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		assertEquals(0, invertedIndex.size());

	}

	/**
	 * Given a non-empty index, add a empty synopsis and make sure the index is
	 * unchanged
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testAddEmptySynopsisIntoNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// add only the first one
		invertedIndexEngine.addEntry(descriptor1Serenity);

		// execute
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptorGalaxyQuest = new MovieDescriptor();
		descriptorGalaxyQuest.setId(1L);
		descriptorGalaxyQuest.setName("Galaxy Quest");
		descriptorGalaxyQuest.setYear(1999);
		descriptorGalaxyQuest.setRemotePath("http://www.rottentomatoes.com/m/galaxy_quest/");
		descriptorGalaxyQuest.setRemoteId("18909");
		descriptorGalaxyQuest.setSource(movieSource);
		descriptorGalaxyQuest.setSynopsis("");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.COMEDY);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptorGalaxyQuest.setGenres(genres);
		movieDescriptorService.save(descriptorGalaxyQuest);

		// add the new entry to the index
		invertedIndexEngine.addEntry(descriptorGalaxyQuest);

		// verify (check the words from the index, as well as, their idf/tf
		// values)
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		assertNotNull(invertedIndex);

		// compare number of words
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
				.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		assertEquals(indexWords, parsedWordsList);

		// analyze index tf/idfs
		for (String word : invertedIndex.keySet()) {
			IndexEntry indexEntry = invertedIndex.get(word);

			// check idf (must be 0 because there's only 1 document in the index
			// so log(1 / 1) = 0)
			assertEquals(0f, indexEntry.getIdf());

			// only one document must be present in the postings
			assertEquals(1, indexEntry.getPostings().size());

			// check tf
			if (word.startsWith("galax") || word.startsWith("independ")) {
				assertEquals(new Short((short) 2), indexEntry.getPostings().get(descriptor1Serenity.getId()).getTf());
			} else if (word.startsWith("allian")) {
				assertEquals(new Short((short) 3), indexEntry.getPostings().get(descriptor1Serenity.getId()).getTf());
			} else {
				assertEquals(new Short((short) 1), indexEntry.getPostings().get(descriptor1Serenity.getId()).getTf());
			}

			if (indexEntry.getPostings().containsKey(descriptorGalaxyQuest.getId())) {
				assertEquals(descriptorGalaxyQuest.getGenres(),
						indexEntry.getPostings().get(descriptorGalaxyQuest.getId()).getGenres());
			}
		}
	}

	/**
	 * Tests adding entries that contain actors, directors and screenwriters
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testCastAndCrewIndexing_FullNames() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		// save the source
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1Serenity = new MovieDescriptor();
		descriptor1Serenity.setId(1L);
		descriptor1Serenity.setName("Serenity");
		descriptor1Serenity.setYear(2005);
		descriptor1Serenity.setRemotePath("http://www.rottentomatoes.com/m/serenity/");
		descriptor1Serenity.setRemoteId("12345");
		descriptor1Serenity.setSource(movieSource);
		descriptor1Serenity
				.setSynopsis("A band of renegades on the run in outer space get in more hot water than they anticipated in this sci-fi action-adventure adapted from the television series Firefly. In the 26th century, the galaxy has been colonized by a military force known as the Alliance, but its leadership has not gone unquestioned. The Alliance was once challenged by a league of rebels known as the Independents, but the Alliance emerged victorious after a brutal civil war, with the surviving Independents scattering around the galaxy.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1Serenity.setGenres(genres);

		Set<MovieCrewPerson> movieActorsSerenity = new HashSet<MovieCrewPerson>();
		// in reality, a name of an actor which appears in multiple movies would
		// be a common instance across movie descriptors
		MovieCrewPerson nathanFillion = new MovieCrewPerson("Nathan Fillion", MovieCrewPersonType.ACTOR);
		movieActorsSerenity.add(nathanFillion);
		movieActorsSerenity.add(new MovieCrewPerson("Gina Torres", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Alan Tudyk", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Adam Baldwin", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Morena Baccarin", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Jewel Staite", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Summer Glau", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Sean Maher", MovieCrewPersonType.ACTOR));

		MovieCrewPerson ronGlass = new MovieCrewPerson("Ron Glass", MovieCrewPersonType.ACTOR);
		movieActorsSerenity.add(ronGlass);

		movieActorsSerenity.add(new MovieCrewPerson("Chiwetel Ejiofor", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("David Krumholtz", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Michael Hitchcock", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Sarah Paulson", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Yan Feldman", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Rafael Feldman", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Nectar Rose", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Tamara Taylor", MovieCrewPersonType.ACTOR));

		descriptor1Serenity.setActors(movieActorsSerenity);

		Set<MovieCrewPerson> movieDirectorsSerenity = new HashSet<MovieCrewPerson>();
		movieDirectorsSerenity.add(new MovieCrewPerson("Joss Whedon", MovieCrewPersonType.DIRECTOR));

		descriptor1Serenity.setDirectors(movieDirectorsSerenity);

		Set<MovieCrewPerson> movieScreenwritersSerenity = new HashSet<MovieCrewPerson>();
		movieScreenwritersSerenity.add(new MovieCrewPerson("Joss Whedon", MovieCrewPersonType.SCREENWRITER));

		descriptor1Serenity.setScreenwriters(movieScreenwritersSerenity);

		movieDescriptorService.save(descriptor1Serenity);

		MovieDescriptor descriptor2SavingPrivateRyan = new MovieDescriptor();
		descriptor2SavingPrivateRyan.setId(2L);
		descriptor2SavingPrivateRyan.setName("Saving Private Ryan");
		descriptor2SavingPrivateRyan.setYear(1998);
		descriptor2SavingPrivateRyan.setRemotePath("http://www.rottentomatoes.com/m/saving_private_ryan/");
		descriptor2SavingPrivateRyan.setRemoteId("12346");
		descriptor2SavingPrivateRyan.setSource(movieSource);
		descriptor2SavingPrivateRyan
				.setSynopsis("Steven Spielberg directed this powerful, realistic re-creation of WWII's D-day invasion and the immediate aftermath. The story opens with a prologue in which a veteran brings his family to the American cemetery at Normandy, and a flashback then joins Capt. John Miller (Tom Hanks) and GIs in a landing craft making the June 6, 1944, approach to Omaha Beach to face devastating German artillery fire. This mass slaughter of American soldiers is depicted in a compelling, unforgettable 24-minute sequence. Miller's men slowly move forward to finally take a concrete pillbox. On the beach littered with bodies is one with the name 'Ryan' stenciled on his backpack. Army Chief of Staff Gen. George C. Marshall (Harve Presnell), learning that three Ryan brothers from the same family have all been killed in a single week, requests that the surviving brother, Pvt. James Ryan (Matt Damon), be located and brought back to the United States. Capt. Miller gets the assignment, and he chooses a translator, Cpl. Upham (Jeremy Davis), skilled in language but not in combat, to join his squad of right-hand man Sgt. Horvath (Tom Sizemore), plus privates Mellish (Adam Goldberg), Medic Wade (Giovanni Ribisi), cynical Reiben (Edward Burns) from Brooklyn, Italian-American Caparzo (Vin Diesel), and religious Southerner Jackson (Barry Pepper), an ace sharpshooter who calls on the Lord while taking aim. Having previously experienced action in Italy and North Africa, the close-knit squad sets out through areas still thick with Nazis. After they lose one man in a skirmish at a bombed village, some in the group begin to question the logic of losing more lives to save a single soldier. The film's historical consultant is Stephen E. Ambrose, and the incident is based on a true occurance in Ambrose's 1994 bestseller D-Day: June 6, 1944.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		descriptor2SavingPrivateRyan.setGenres(genres);

		Set<MovieCrewPerson> movieActorsSavingPrivateRyan = new HashSet<MovieCrewPerson>();

		MovieCrewPerson tomHanks = new MovieCrewPerson("Tom Hanks", MovieCrewPersonType.ACTOR);
		movieActorsSavingPrivateRyan.add(tomHanks);

		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Edward Burns", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Tom Sizemore", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Jeremy Davies", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Vin Diesel", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Adam Goldberg", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Barry Pepper ", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Giovanni Ribisi", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(ronGlass);
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Matt Damon", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Dennis Farina", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Ted Danson", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Harve Presnell", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Dale Dye", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Bryan Cranston", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("David Wohl", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Paul Giamatti", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Ryan Hurst", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Harrison Young", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Max Martini", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Dylan Bruno", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(nathanFillion);
		descriptor2SavingPrivateRyan.setActors(movieActorsSavingPrivateRyan);

		Set<MovieCrewPerson> movieDirectorsSavingPrivateRyan = new HashSet<MovieCrewPerson>();

		MovieCrewPerson stevenSpielberg = new MovieCrewPerson("Steven Spielberg", MovieCrewPersonType.DIRECTOR);
		movieDirectorsSavingPrivateRyan.add(stevenSpielberg);

		descriptor2SavingPrivateRyan.setDirectors(movieDirectorsSavingPrivateRyan);

		Set<MovieCrewPerson> movieScreenWritersSavingPrivateRyan = new HashSet<MovieCrewPerson>();
		movieScreenWritersSavingPrivateRyan.add(new MovieCrewPerson("Robert Rodat", MovieCrewPersonType.SCREENWRITER));
		descriptor2SavingPrivateRyan.setScreenwriters(movieScreenWritersSavingPrivateRyan);

		movieDescriptorService.save(descriptor2SavingPrivateRyan);

		MovieDescriptor descriptor3CastAway = new MovieDescriptor();
		descriptor3CastAway.setName("Cast Away");
		descriptor3CastAway.setId(3L);
		descriptor3CastAway.setYear(2000);
		descriptor3CastAway.setRemotePath("http://www.rottentomatoes.com/m/cast_away/");
		descriptor3CastAway.setRemoteId("12347");
		descriptor3CastAway.setSource(movieSource);
		descriptor3CastAway
				.setSynopsis("An exploration of human survival and the ability of fate to alter even the tidiest of lives with one major event, Cast Away tells the story of Chuck Noland (Tom Hanks), a Federal Express engineer who devotes most of his life to his troubleshooting job. His girlfriend Kelly (Helen Hunt) is often neglected by his dedication to work, and his compulsive personality suggests a conflicted man. But on Christmas Eve, Chuck proposes marriage to Kelly right before embarking on a large assignment. On the assignment, a plane crash strands Chuck on a remote island, and his fast-paced life is slowed to a crawl, as he is miles removed from any human contact. Finding solace only in a volleyball that he befriends, Chuck must now learn to endure the emotional and physical stress of his new life, unsure of when he may return to the civilization he knew before. Cast Away reunites star Hanks with director Robert Zemeckis, their first film together since 1994's Oscar-winning Forrest Gump.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		descriptor3CastAway.setGenres(genres);

		Set<MovieCrewPerson> movieActorsCastAway = new HashSet<MovieCrewPerson>();
		movieActorsCastAway.add(tomHanks);
		movieActorsCastAway.add(new MovieCrewPerson("Helen Hunt", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Chris Noth", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Nick Searcy", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Lari White", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Michael Forest", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Viveka Davis", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Ashley Edner", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Christopher Kriesa", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Elden Henson", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Geoffrey Blake", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Jay Acovone", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Jenifer Lewis", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Valerie Wildman", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Nan Martin", MovieCrewPersonType.ACTOR));
		descriptor3CastAway.setActors(movieActorsCastAway);

		Set<MovieCrewPerson> movieDirectorCastAway = new HashSet<MovieCrewPerson>();
		movieDirectorCastAway.add(new MovieCrewPerson("Robert Zemeckis", MovieCrewPersonType.DIRECTOR));

		descriptor3CastAway.setDirectors(movieDirectorCastAway);

		Set<MovieCrewPerson> movieScreenwritersCastAway = new HashSet<MovieCrewPerson>();
		movieScreenwritersCastAway.add(new MovieCrewPerson("William Broyles", MovieCrewPersonType.SCREENWRITER));

		descriptor3CastAway.setScreenwriters(movieScreenwritersCastAway);

		movieDescriptorService.save(descriptor3CastAway);

		MovieDescriptor descriptor4WarHorse = new MovieDescriptor();
		descriptor4WarHorse.setName("War Horse");
		descriptor4WarHorse.setId(4L);
		descriptor4WarHorse.setYear(2011);
		descriptor4WarHorse.setRemotePath("http://www.rottentomatoes.com/m/war_horse/");
		descriptor4WarHorse.setRemoteId("12348");
		descriptor4WarHorse.setSource(movieSource);
		descriptor4WarHorse
				.setSynopsis("Set against a sweeping canvas of rural England and Europe during the First World War, War Horse begins with the remarkable friendship between a horse named Joey and a young man called Albert, who tames and trains him. When they are forcefully parted, the film follows the extraordinary journey of the horse as he moves through the war, changing and inspiring the lives of all those he meets-British cavalry, German soldiers, and a French farmer and his granddaughter-before the story reaches its emotional climax in the heart of No Man's Land. The First World War is experienced through the journey of this horse-an odyssey of joy and sorrow, passionate friendship and high adventure.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		descriptor4WarHorse.setGenres(genres);

		Set<MovieCrewPerson> movieActorsWarHorse = new HashSet<MovieCrewPerson>();
		movieActorsWarHorse.add(new MovieCrewPerson("Jeremy Irvine", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("Emily Watson", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("Peter Mullan", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("David Thewlis", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("David Kross", MovieCrewPersonType.ACTOR));
		descriptor4WarHorse.setActors(movieActorsWarHorse);

		Set<MovieCrewPerson> movieDirectorsWarHorse = new HashSet<MovieCrewPerson>();
		movieDirectorsWarHorse.add(stevenSpielberg);
		descriptor4WarHorse.setDirectors(movieDirectorsWarHorse);

		Set<MovieCrewPerson> movieScreenwritersWarHorse = new HashSet<MovieCrewPerson>();
		movieScreenwritersWarHorse.add(new MovieCrewPerson("Lee Hall", MovieCrewPersonType.SCREENWRITER));
		descriptor4WarHorse.setScreenwriters(movieScreenwritersWarHorse);

		movieDescriptorService.save(descriptor4WarHorse);

		invertedIndexEngine.setIndexFullNamesForCastAndCrew(true);

		// execute
		invertedIndexEngine.addEntry(descriptor1Serenity);
		invertedIndexEngine.addEntry(descriptor2SavingPrivateRyan);
		invertedIndexEngine.addEntry(descriptor3CastAway);
		invertedIndexEngine.addEntry(descriptor4WarHorse);

		// verify (check the words from the index, as well as, their idf/tf
		// values)
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		assertNotNull(invertedIndex);

		// compare number of words
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
				.getSynopsis()
				+ descriptor2SavingPrivateRyan.getSynopsis()
				+ descriptor3CastAway.getSynopsis()
				+ descriptor4WarHorse.getSynopsis())));
		parsedWords.addAll(getStringList(movieActorsCastAway, true));
		parsedWords.addAll(getStringList(movieDirectorCastAway, true));
		parsedWords.addAll(getStringList(movieScreenwritersCastAway, true));

		parsedWords.addAll(getStringList(movieActorsSerenity, true));
		parsedWords.addAll(getStringList(movieDirectorsSerenity, true));
		parsedWords.addAll(getStringList(movieScreenwritersSerenity, true));

		parsedWords.addAll(getStringList(movieActorsSavingPrivateRyan, true));
		parsedWords.addAll(getStringList(movieDirectorsSavingPrivateRyan, true));
		parsedWords.addAll(getStringList(movieScreenWritersSavingPrivateRyan, true));

		parsedWords.addAll(getStringList(movieActorsWarHorse, true));
		parsedWords.addAll(getStringList(movieDirectorsWarHorse, true));
		parsedWords.addAll(getStringList(movieScreenwritersWarHorse, true));

		ArrayList<String> orderedList = new ArrayList<String>(parsedWords);
		Collections.sort(orderedList);

		assertEquals(orderedList, indexWords);

		invertedIndexEngine.setIndexFullNamesForCastAndCrew(false);

		// actors
		for (MovieCrewPerson movieCrewPerson : descriptor1Serenity.getActors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();
			if (lowerCaseFullName.equals("nathan fillion") || lowerCaseFullName.equals("ron glass")) {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting serenityPosting = indexEntry.getPostings().get(descriptor1Serenity.getId());
				assertEquals(new Short("1"), serenityPosting.getTf());

				Posting savingPrivateRyanPosting = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyanPosting.getTf());
			} else {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
				// it appears in 1 movie out of 4
				assertEquals((float) Math.log(4), indexEntry.getIdf());
				assertEquals(1, indexEntry.getPostings().size());
				Posting serenityPosting = indexEntry.getPostings().get(descriptor1Serenity.getId());
				assertEquals(new Short("1"), serenityPosting.getTf());
			}
		}

		for (MovieCrewPerson movieCrewPerson : descriptor2SavingPrivateRyan.getActors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();
			if (lowerCaseFullName.equals("tom hanks")) {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
				// it appears in 2 movies out of 4 (cast away and saving private
				// ryan)
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting serenityPosting = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), serenityPosting.getTf());

				Posting savingPrivateRyanPosting = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyanPosting.getTf());
			} else if (lowerCaseFullName.equals("nathan fillion") || lowerCaseFullName.equals("ron glass")) {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);

				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting serenityPosting = indexEntry.getPostings().get(descriptor1Serenity.getId());
				assertEquals(new Short("1"), serenityPosting.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyan.getTf());
			} else {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);

				// it appears in 1 movie out of 4
				assertEquals((float) Math.log(4), indexEntry.getIdf());
				assertEquals(1, indexEntry.getPostings().size());
				Posting savingPrivateRyanPosting = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyanPosting.getTf());
			}
		}

		for (MovieCrewPerson movieCrewPerson : descriptor3CastAway.getActors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();
			if (lowerCaseFullName.equals("tom hanks")) {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
				// it appears in 2 movies out of 4 (cast away and saving private
				// ryan)
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting serenityPosting = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), serenityPosting.getTf());

				Posting savingPrivateRyanPosting = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyanPosting.getTf());

				Posting castAwayPosting = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), castAwayPosting.getTf());
			} else {
				IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);

				// it appears in 1 movie out of 4
				assertEquals((float) Math.log(4), indexEntry.getIdf());
				assertEquals(1, indexEntry.getPostings().size());
				Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), castAway.getTf());
			}
		}

		for (MovieCrewPerson movieCrewPerson : descriptor4WarHorse.getActors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();
			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);

			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting warHorse = indexEntry.getPostings().get(descriptor4WarHorse.getId());
			assertEquals(new Short("1"), warHorse.getTf());
		}

		// directors
		for (MovieCrewPerson movieCrewPerson : descriptor1Serenity.getDirectors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting serenityPosting = indexEntry.getPostings().get(descriptor1Serenity.getId());
			// josh whedon is both a director and a screenwriter
			assertEquals(new Short("1"), serenityPosting.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor2SavingPrivateRyan.getDirectors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// spielberg appears in 2 movies out of 4 (this movie and
			// "war horse")
			assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
			assertEquals(2, indexEntry.getPostings().size());

			Posting savingsPrivateRyanPosting = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
			assertEquals(new Short("1"), savingsPrivateRyanPosting.getTf());

			Posting warHorsePosting = indexEntry.getPostings().get(descriptor4WarHorse.getId());
			assertEquals(new Short("1"), warHorsePosting.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor3CastAway.getDirectors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
			assertEquals(new Short("1"), castAway.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor4WarHorse.getDirectors()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 2 movies out of 4
			assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
			assertEquals(2, indexEntry.getPostings().size());

			Posting castAway = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
			assertEquals(new Short("1"), castAway.getTf());

			Posting warHorse = indexEntry.getPostings().get(descriptor4WarHorse.getId());
			assertEquals(new Short("1"), warHorse.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor1Serenity.getScreenWriters()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting castAway = indexEntry.getPostings().get(descriptor1Serenity.getId());
			// josh whedon is both a director and a screenwriter
			assertEquals(new Short("1"), castAway.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor2SavingPrivateRyan.getScreenWriters()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting castAway = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
			assertEquals(new Short("1"), castAway.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor3CastAway.getScreenWriters()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
			assertEquals(new Short("1"), castAway.getTf());
		}

		for (MovieCrewPerson movieCrewPerson : descriptor4WarHorse.getScreenWriters()) {
			String lowerCaseFullName = movieCrewPerson.getFullName().toLowerCase();

			IndexEntry indexEntry = invertedIndex.get(lowerCaseFullName);
			// it appears in 1 movie out of 4
			assertEquals((float) Math.log(4), indexEntry.getIdf());
			assertEquals(1, indexEntry.getPostings().size());
			Posting castAway = indexEntry.getPostings().get(descriptor4WarHorse.getId());
			assertEquals(new Short("1"), castAway.getTf());
		}
	}

	/**
	 * Tests cast and crew indexing for the case when non-full names are added
	 * into the index (i.e. instead they are tokenized and parsed like any
	 * regular word)
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testCastAndCrewIndexing_NotFullNames() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		// save the source
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor descriptor1Serenity = new MovieDescriptor();
		descriptor1Serenity.setId(1L);
		descriptor1Serenity.setName("Serenity");
		descriptor1Serenity.setYear(2005);
		descriptor1Serenity.setRemotePath("http://www.rottentomatoes.com/m/serenity/");
		descriptor1Serenity.setRemoteId("12345");
		descriptor1Serenity.setSource(movieSource);
		descriptor1Serenity
				.setSynopsis("A band of renegades on the run in outer space get in more hot water than they anticipated in this sci-fi action-adventure adapted from the television series Firefly. In the 26th century, the galaxy has been colonized by a military force known as the Alliance, but its leadership has not gone unquestioned. The Alliance was once challenged by a league of rebels known as the Independents, but the Alliance emerged victorious after a brutal civil war, with the surviving Independents scattering around the galaxy.");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.ACTION_ADVENTURE);
		genres.add(Genre.SCIENCE_FICTION_FANTASY);
		descriptor1Serenity.setGenres(genres);

		Set<MovieCrewPerson> movieActorsSerenity = new HashSet<MovieCrewPerson>();
		// in reality, a name of an actor which appears in multiple movies would
		// be a common instance across movie descriptors
		MovieCrewPerson nathanFillion = new MovieCrewPerson("Nathan Fillion", MovieCrewPersonType.ACTOR);
		movieActorsSerenity.add(nathanFillion);
		movieActorsSerenity.add(new MovieCrewPerson("Gina Torres", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Alan Tudyk", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Adam Baldwin", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Morena Baccarin", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Jewel Staite", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Summer Glau", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Sean Maher", MovieCrewPersonType.ACTOR));

		MovieCrewPerson ronGlass = new MovieCrewPerson("Ron Glass", MovieCrewPersonType.ACTOR);
		movieActorsSerenity.add(ronGlass);

		movieActorsSerenity.add(new MovieCrewPerson("Chiwetel Ejiofor", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("David Krumholtz", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Michael Hitchcock", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Sarah Paulson", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Yan Feldman", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Rafael Feldman", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Nectar Rose", MovieCrewPersonType.ACTOR));
		movieActorsSerenity.add(new MovieCrewPerson("Tamara Taylor", MovieCrewPersonType.ACTOR));

		descriptor1Serenity.setActors(movieActorsSerenity);

		Set<MovieCrewPerson> movieDirectorsSerenity = new HashSet<MovieCrewPerson>();
		movieDirectorsSerenity.add(new MovieCrewPerson("Joss Whedon", MovieCrewPersonType.DIRECTOR));

		descriptor1Serenity.setDirectors(movieDirectorsSerenity);

		Set<MovieCrewPerson> movieScreenwritersSerenity = new HashSet<MovieCrewPerson>();
		movieScreenwritersSerenity.add(new MovieCrewPerson("Joss Whedon", MovieCrewPersonType.SCREENWRITER));

		descriptor1Serenity.setScreenwriters(movieScreenwritersSerenity);

		movieDescriptorService.save(descriptor1Serenity);

		MovieDescriptor descriptor2SavingPrivateRyan = new MovieDescriptor();
		descriptor2SavingPrivateRyan.setId(2L);
		descriptor2SavingPrivateRyan.setName("Saving Private Ryan");
		descriptor2SavingPrivateRyan.setYear(1998);
		descriptor2SavingPrivateRyan.setRemotePath("http://www.rottentomatoes.com/m/saving_private_ryan/");
		descriptor2SavingPrivateRyan.setRemoteId("12346");
		descriptor2SavingPrivateRyan.setSource(movieSource);
		descriptor2SavingPrivateRyan
				.setSynopsis("Steven Spielberg directed this powerful, realistic re-creation of WWII's D-day invasion and the immediate aftermath. The story opens with a prologue in which a veteran brings his family to the American cemetery at Normandy, and a flashback then joins Capt. John Miller (Tom Hanks) and GIs in a landing craft making the June 6, 1944, approach to Omaha Beach to face devastating German artillery fire. This mass slaughter of American soldiers is depicted in a compelling, unforgettable 24-minute sequence. Miller's men slowly move forward to finally take a concrete pillbox. On the beach littered with bodies is one with the name 'Ryan' stenciled on his backpack. Army Chief of Staff Gen. George C. Marshall (Harve Presnell), learning that three Ryan brothers from the same family have all been killed in a single week, requests that the surviving brother, Pvt. James Ryan (Matt Damon), be located and brought back to the United States. Capt. Miller gets the assignment, and he chooses a translator, Cpl. Upham (Jeremy Davis), skilled in language but not in combat, to join his squad of right-hand man Sgt. Horvath (Tom Sizemore), plus privates Mellish (Adam Goldberg), Medic Wade (Giovanni Ribisi), cynical Reiben (Edward Burns) from Brooklyn, Italian-American Caparzo (Vin Diesel), and religious Southerner Jackson (Barry Pepper), an ace sharpshooter who calls on the Lord while taking aim. Having previously experienced action in Italy and North Africa, the close-knit squad sets out through areas still thick with Nazis. After they lose one man in a skirmish at a bombed village, some in the group begin to question the logic of losing more lives to save a single soldier. The film's historical consultant is Stephen E. Ambrose, and the incident is based on a true occurance in Ambrose's 1994 bestseller D-Day: June 6, 1944.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		descriptor2SavingPrivateRyan.setGenres(genres);

		Set<MovieCrewPerson> movieActorsSavingPrivateRyan = new HashSet<MovieCrewPerson>();

		MovieCrewPerson tomHanks = new MovieCrewPerson("Tom Hanks", MovieCrewPersonType.ACTOR);
		movieActorsSavingPrivateRyan.add(tomHanks);

		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Edward Burns", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Tom Sizemore", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Jeremy Davies", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Vin Diesel", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Adam Goldberg", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Barry Pepper ", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Giovanni Ribisi", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(ronGlass);
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Matt Damon", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Dennis Farina", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Ted Danson", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Harve Presnell", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Dale Dye", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Bryan Cranston", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("David Wohl", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Paul Giamatti", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Ryan Hurst", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Harrison Young", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Max Martini", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(new MovieCrewPerson("Dylan Bruno", MovieCrewPersonType.ACTOR));
		movieActorsSavingPrivateRyan.add(nathanFillion);
		descriptor2SavingPrivateRyan.setActors(movieActorsSavingPrivateRyan);

		Set<MovieCrewPerson> movieDirectorsSavingPrivateRyan = new HashSet<MovieCrewPerson>();

		MovieCrewPerson stevenSpielberg = new MovieCrewPerson("Steven Spielberg", MovieCrewPersonType.DIRECTOR);
		movieDirectorsSavingPrivateRyan.add(stevenSpielberg);

		descriptor2SavingPrivateRyan.setDirectors(movieDirectorsSavingPrivateRyan);

		Set<MovieCrewPerson> movieScreenWritersSavingPrivateRyan = new HashSet<MovieCrewPerson>();
		movieScreenWritersSavingPrivateRyan.add(new MovieCrewPerson("Robert Rodat", MovieCrewPersonType.SCREENWRITER));
		descriptor2SavingPrivateRyan.setScreenwriters(movieScreenWritersSavingPrivateRyan);

		movieDescriptorService.save(descriptor2SavingPrivateRyan);

		MovieDescriptor descriptor3CastAway = new MovieDescriptor();
		descriptor3CastAway.setName("Cast Away");
		descriptor3CastAway.setId(3L);
		descriptor3CastAway.setYear(2000);
		descriptor3CastAway.setRemotePath("http://www.rottentomatoes.com/m/cast_away/");
		descriptor3CastAway.setRemoteId("12347");
		descriptor3CastAway.setSource(movieSource);
		descriptor3CastAway
				.setSynopsis("An exploration of human survival and the ability of fate to alter even the tidiest of lives with one major event, Cast Away tells the story of Chuck Noland (Tom Hanks), a Federal Express engineer who devotes most of his life to his troubleshooting job. His girlfriend Kelly (Helen Hunt) is often neglected by his dedication to work, and his compulsive personality suggests a conflicted man. But on Christmas Eve, Chuck proposes marriage to Kelly right before embarking on a large assignment. On the assignment, a plane crash strands Chuck on a remote island, and his fast-paced life is slowed to a crawl, as he is miles removed from any human contact. Finding solace only in a volleyball that he befriends, Chuck must now learn to endure the emotional and physical stress of his new life, unsure of when he may return to the civilization he knew before. Cast Away reunites star Hanks with director Robert Zemeckis, their first film together since 1994's Oscar-winning Forrest Gump.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		descriptor3CastAway.setGenres(genres);

		Set<MovieCrewPerson> movieActorsCastAway = new HashSet<MovieCrewPerson>();
		movieActorsCastAway.add(tomHanks);
		movieActorsCastAway.add(new MovieCrewPerson("Helen Hunt", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Chris Noth", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Nick Searcy", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Lari White", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Michael Forest", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Viveka Davis", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Ashley Edner", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Christopher Kriesa", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Elden Henson", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Geoffrey Blake", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Jay Acovone", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Jenifer Lewis", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Valerie Wildman", MovieCrewPersonType.ACTOR));
		movieActorsCastAway.add(new MovieCrewPerson("Nan Martin", MovieCrewPersonType.ACTOR));
		descriptor3CastAway.setActors(movieActorsCastAway);

		Set<MovieCrewPerson> movieDirectorCastAway = new HashSet<MovieCrewPerson>();
		movieDirectorCastAway.add(new MovieCrewPerson("Robert Zemeckis", MovieCrewPersonType.DIRECTOR));

		descriptor3CastAway.setDirectors(movieDirectorCastAway);

		Set<MovieCrewPerson> movieScreenwritersCastAway = new HashSet<MovieCrewPerson>();
		movieScreenwritersCastAway.add(new MovieCrewPerson("William Broyles", MovieCrewPersonType.SCREENWRITER));

		descriptor3CastAway.setScreenwriters(movieScreenwritersCastAway);

		movieDescriptorService.save(descriptor3CastAway);

		MovieDescriptor descriptor4WarHorse = new MovieDescriptor();
		descriptor4WarHorse.setName("War Horse");
		descriptor4WarHorse.setId(4L);
		descriptor4WarHorse.setYear(2011);
		descriptor4WarHorse.setRemotePath("http://www.rottentomatoes.com/m/war_horse/");
		descriptor4WarHorse.setRemoteId("12348");
		descriptor4WarHorse.setSource(movieSource);
		descriptor4WarHorse
				.setSynopsis("Set against a sweeping canvas of rural England and Europe during the First World War, War Horse begins with the remarkable friendship between a horse named Joey and a young man called Albert, who tames and trains him. When they are forcefully parted, the film follows the extraordinary journey of the horse as he moves through the war, changing and inspiring the lives of all those he meets-British cavalry, German soldiers, and a French farmer and his granddaughter-before the story reaches its emotional climax in the heart of No Man's Land. The First World War is experienced through the journey of this horse-an odyssey of joy and sorrow, passionate friendship and high adventure.");
		genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ACTION_ADVENTURE);
		descriptor4WarHorse.setGenres(genres);

		Set<MovieCrewPerson> movieActorsWarHorse = new HashSet<MovieCrewPerson>();
		movieActorsWarHorse.add(new MovieCrewPerson("Jeremy Irvine", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("Emily Watson", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("Peter Mullan", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("David Thewlis", MovieCrewPersonType.ACTOR));
		movieActorsWarHorse.add(new MovieCrewPerson("David Kross", MovieCrewPersonType.ACTOR));
		descriptor4WarHorse.setActors(movieActorsWarHorse);

		Set<MovieCrewPerson> movieDirectorsWarHorse = new HashSet<MovieCrewPerson>();
		movieDirectorsWarHorse.add(stevenSpielberg);
		descriptor4WarHorse.setDirectors(movieDirectorsWarHorse);

		Set<MovieCrewPerson> movieScreenwritersWarHorse = new HashSet<MovieCrewPerson>();
		movieScreenwritersWarHorse.add(new MovieCrewPerson("Lee Hall", MovieCrewPersonType.SCREENWRITER));
		descriptor4WarHorse.setScreenwriters(movieScreenwritersWarHorse);

		movieDescriptorService.save(descriptor4WarHorse);

		invertedIndexEngine.setIndexFullNamesForCastAndCrew(false);

		// execute
		invertedIndexEngine.addEntry(descriptor1Serenity);
		invertedIndexEngine.addEntry(descriptor2SavingPrivateRyan);
		invertedIndexEngine.addEntry(descriptor3CastAway);
		invertedIndexEngine.addEntry(descriptor4WarHorse);

		List<String> movieCrewMembers = new ArrayList<String>();
		movieCrewMembers.addAll(getStringList(movieActorsCastAway, false));
		movieCrewMembers.addAll(getStringList(movieDirectorCastAway, false));
		movieCrewMembers.addAll(getStringList(movieScreenwritersCastAway, false));

		movieCrewMembers.addAll(getStringList(movieActorsSerenity, false));
		movieCrewMembers.addAll(getStringList(movieDirectorsSerenity, false));
		movieCrewMembers.addAll(getStringList(movieScreenwritersSerenity, false));

		movieCrewMembers.addAll(getStringList(movieActorsSavingPrivateRyan, false));
		movieCrewMembers.addAll(getStringList(movieDirectorsSavingPrivateRyan, false));
		movieCrewMembers.addAll(getStringList(movieScreenWritersSavingPrivateRyan, false));

		movieCrewMembers.addAll(getStringList(movieActorsWarHorse, false));
		movieCrewMembers.addAll(getStringList(movieDirectorsWarHorse, false));
		movieCrewMembers.addAll(getStringList(movieScreenwritersWarHorse, false));

		String[] movieCrewMembersNamesParsed = TextParsingHelper.parseText(movieCrewMembers.toString());
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();
		for (String name : movieCrewMembersNamesParsed) {
			IndexEntry indexEntry = invertedIndex.get(name);

			if (name.startsWith("tom")) {

				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), castAway.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("2"), savingPrivateRyan.getTf());
			} else if (name.startsWith("hank")) {
				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("2"), castAway.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyan.getTf());
			} else if (name.startsWith("michael")) {
				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting serenity = indexEntry.getPostings().get(descriptor1Serenity.getId());
				assertEquals(new Short("1"), serenity.getTf());

				Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), castAway.getTf());
			} else if (name.equals("davi") || name.startsWith("robert")) {
				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting castAway = indexEntry.getPostings().get(descriptor3CastAway.getId());
				assertEquals(new Short("1"), castAway.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyan.getTf());
			} else if (name.equals("david")) {
				// it appears in 3 movies out of 4
				assertEquals((float) Math.log((float) 4 / 3), indexEntry.getIdf());
				assertEquals(3, indexEntry.getPostings().size());
				Posting serenity = indexEntry.getPostings().get(descriptor1Serenity.getId());
				assertEquals(new Short("1"), serenity.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyan.getTf());

				Posting warHorse = indexEntry.getPostings().get(descriptor4WarHorse.getId());
				assertEquals(new Short("1"), warHorse.getTf());
			} else if (name.startsWith("adam") || name.startsWith("ron") || name.startsWith("glass")
					|| name.startsWith("nathan") || name.startsWith("fillion")) {
				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting serenity = indexEntry.getPostings().get(descriptor1Serenity.getId());
				assertEquals(new Short("1"), serenity.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyan.getTf());
			} else if (name.equals("young") || name.equals("jeremi") || name.equals("steven")
					|| name.equals("spielberg")) {
				// it appears in 2 movies out of 4
				assertEquals((float) Math.log(4 / 2), indexEntry.getIdf());
				assertEquals(2, indexEntry.getPostings().size());
				Posting warHorse = indexEntry.getPostings().get(descriptor4WarHorse.getId());
				assertEquals(new Short("1"), warHorse.getTf());

				Posting savingPrivateRyan = indexEntry.getPostings().get(descriptor2SavingPrivateRyan.getId());
				assertEquals(new Short("1"), savingPrivateRyan.getTf());
			} else {
				assertEquals((float) Math.log(4), indexEntry.getIdf());
				assertEquals(1, indexEntry.getPostings().size());
			}
		}

	}

	/**
	 * Given a movie with a synopsis containing UTF8 characters, test adding it
	 * into the index
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testAddSynopsisContainingUTF8() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// execute
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor kungFuDescriptor = new MovieDescriptor();
		kungFuDescriptor.setId(1L);
		kungFuDescriptor.setName("Kung Fu Hustle");
		kungFuDescriptor.setYear(2005);
		kungFuDescriptor.setRemotePath("http://www.rottentomatoes.com/m/kung_fu_hustle/");
		kungFuDescriptor.setRemoteId("080809");
		kungFuDescriptor.setSource(movieSource);
		String synopsis = "After the gang busts the ancient kung fu king known as The Beast "
				+ "(Leung Siu Lung, Liáng Xiǎolóng) out of jail, tensions reach a boiling point as Pig Sty Alley's "
				+ "landlady (Yuen Qiu, Yuán Hépíng) leads an all-out attack against the gang and Sing discovers his "
				+ "true heroic fate.";
		kungFuDescriptor.setSynopsis(synopsis);
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ART_HOUSE_INTERNATIONAL);
		genres.add(Genre.ROMANCE);
		genres.add(Genre.COMEDY);
		kungFuDescriptor.setGenres(genres);
		movieDescriptorService.save(kungFuDescriptor);

		// execute
		invertedIndexEngine.addEntry(kungFuDescriptor);

		// verify (check the words from the index, as well as, their idf/tf
		// values)
		Map<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		assertNotNull(invertedIndex);

		// compare number of words
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(kungFuDescriptor
				.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		// check explicitly the UTF8 words
		assertTrue(invertedIndex.keySet().contains("liáng"));
		assertTrue(invertedIndex.keySet().contains("xiǎolóng"));
		assertTrue(invertedIndex.keySet().contains("yuán"));
		assertTrue(invertedIndex.keySet().contains("hépíng"));

		// analyze index tf/idfs
		for (String word : invertedIndex.keySet()) {
			IndexEntry indexEntry = invertedIndex.get(word);

			// check idf (must be 0 because there's only 1 document in the index
			// so log(1 / 1) = 0)
			assertEquals(0f, indexEntry.getIdf());

			// only one document must be present in the postings
			assertEquals(1, indexEntry.getPostings().size());

			// check tf
			if (word.startsWith("gang")) {
				assertEquals(new Short((short) 2), indexEntry.getPostings().get(kungFuDescriptor.getId()).getTf());
			} else {
				assertEquals(new Short((short) 1), indexEntry.getPostings().get(kungFuDescriptor.getId()).getTf());
			}

			assertEquals(kungFuDescriptor.getGenres(), indexEntry.getPostings().get(kungFuDescriptor.getId())
					.getGenres());
		}
	}

	/**
	 * Add a null movie into the index and check that an exception is thrown
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void testAddEmptyMovie() throws InvalidMovieDescriptorException {
		try {
			invertedIndexEngine.addEntry(null);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_IS_NULL, ex.getReason());

			throw ex;
		}
	}

	/**************************************************************************
	 ******************** Tests for Remove entry from index *******************
	 **************************************************************************/

	/**
	 * Test the removeEntry() method of the index to see if the number of
	 * entries in the index is 2.
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRemoveMovieFromNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute
		invertedIndexEngine.removeEntry(descriptor1Serenity.getId());

		// verify
		HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

		// check that there's 2 entries as it results from the separate array
		assertEquals(2, invertedIndexEngine.getNumberOfDocuments().intValue());

		// check number of words from inverted index
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor2WorldOnWire
				.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		assertEquals(indexWords, parsedWordsList);

		// analyze index tf/idfs
		for (String word : invertedIndex.keySet()) {
			IndexEntry indexEntry = invertedIndex.get(word);

			// check idf (because we have 2 movies left, some of the words will
			// have the idf 0, some 0.6931472. This is because words that appear
			// in both docs have log (2 / 2) = 0 and words that appear in only
			// one of the docs have log(2 / 1) = 0.6931472
			if (word.startsWith("entir") || word.startsWith("world") || word.startsWith("action")
					|| word.startsWith("genius") || word.startsWith("tale") || word.startsWith("conspiraci")) {
				assertEquals(0f, indexEntry.getIdf());
			} else {
				assertEquals(0.6931472f, indexEntry.getIdf());
			}

			// check tf
			for (Map.Entry<Long, Posting> postingEntry : indexEntry.getPostings().entrySet()) {
				if (postingEntry.getKey().equals(descriptor1Serenity.getId())) {
					if (word.startsWith("marvel") || word.startsWith("toni") || word.startsWith("world")
							|| word.startsWith("fassbind")) {
						assertEquals(new Short((short) 2), postingEntry.getValue().getTf());
					} else {
						assertEquals(new Short((short) 1), postingEntry.getValue().getTf());
					}
				}
			}
		}
	}

	/**
	 * Given an empty index, try removing a movie that was saved into the
	 * database
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void testRemoveMovieFromEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// execute
		try {
			invertedIndexEngine.removeEntry(descriptor1Serenity.getId());
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST, ex.getReason());

			throw ex;
		}

	}

	/**
	 * Given an empty index, try removing a movie
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void testRemoveInexistentMovieFromEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		invertedIndexEngine.clearIndex();

		// execute
		try {
			invertedIndexEngine.removeEntry(123456L);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST, ex.getReason());

			throw ex;
		}

	}

	/**
	 * Test removing a non-existing movie from a non-empty index
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void testRemoveNonExistingMovieFromNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute
		try {
			invertedIndexEngine.removeEntry(1234L);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST, ex.getReason());

			// verify
			HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

			// check that there's 2 entries as it results from the separate
			// array
			assertEquals(3, invertedIndexEngine.getNumberOfDocuments().intValue());

			// check number of words from inverted index
			ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
			Collections.sort(indexWords);

			Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
					.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
			ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
			Collections.sort(parsedWordsList);

			assertEquals(indexWords, parsedWordsList);

			throw ex;
		}
	}

	/**
	 * Test removing a non-existing movie from a non-empty index
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void testRemoveNullMovieFromNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute
		try {
			invertedIndexEngine.removeEntry(null);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST, ex.getReason());

			// verify
			HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

			// check that there's 2 entries as it results from the separate
			// array
			assertEquals(3, invertedIndexEngine.getNumberOfDocuments().intValue());

			// check number of words from inverted index
			ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
			Collections.sort(indexWords);

			Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
					.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
			ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
			Collections.sort(parsedWordsList);

			assertEquals(indexWords, parsedWordsList);

			throw ex;
		}
	}

	/**************************************************************************
	 ******************** Tests for Update entry from index *******************
	 **************************************************************************/

	/**
	 * Test the updateEntry() with a regular synopsis into a non-empty index and
	 * check the tf-idfs afterwards
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testRegularUpdateIntoNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute (update the second movie; set synopsis to 'ABCD')
		String[] descriptor2InitialSynopsis = TextParsingHelper.parseText(descriptor2WorldOnWire.getSynopsis());
		descriptor2WorldOnWire.setSynopsis("ABCD");
		invertedIndexEngine.updateEntry(descriptor2WorldOnWire);

		// verify (movie details)
		HashMap<Long, MovieDetailsDTO> movieDetails = invertedIndexEngine.getMovieDetails();
		MovieDetailsDTO fDet = movieDetails.get(descriptor2WorldOnWire.getId());
		Assert.assertEquals("ABCD", fDet.getDesc());

		// verify words from index
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndexEngine.getInvertedIndex().keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
				.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		assertEquals(indexWords, parsedWordsList);

		// analyze index tf/idfs
		for (String word : invertedIndexEngine.getInvertedIndex().keySet()) {
			IndexEntry indexEntry = invertedIndexEngine.getInvertedIndex().get(word);

			// check that no previous word from the updated document exists and
			// is linked to only the updated document
			for (String initialWord : descriptor2InitialSynopsis) {
				if (initialWord.equals(word)) {
					if (indexEntry.getPostings().keySet().contains(descriptor2WorldOnWire.getId())) {
						fail("The update was not performed");
					}
				}
			}

			// check idf
			if (word.startsWith("forc") || word.startsWith("militari") || word.startsWith("action")) {
				assertEquals(0.4054651f, indexEntry.getIdf());
			} else {
				assertEquals(1.0986123f, indexEntry.getIdf());
			}

			// check tf
			for (Map.Entry<Long, Posting> postingEntry : indexEntry.getPostings().entrySet()) {
				if (postingEntry.getKey().equals(descriptor1Serenity.getId())) {
					if (word.startsWith("marvel") || word.startsWith("toni") || word.startsWith("independ")
							|| word.startsWith("galax")) {
						assertEquals(new Short((short) 2), postingEntry.getValue().getTf());
					} else if (word.startsWith("allianc")) {
						assertEquals(new Short((short) 3), postingEntry.getValue().getTf());
					} else {
						assertEquals(new Short((short) 1), postingEntry.getValue().getTf());
					}
				}
			}
		}
	}

	/**
	 * Test the updateEntry() with an empty synopsis into a non-empty index and
	 * check the tf-idfs afterwards
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test
	public void testUpdateUsingEmptySynopsisIntoNonEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute (update the second movie; set synopsis to empty string)
		String[] descriptor2InitialSynopsis = TextParsingHelper.parseText(descriptor1Serenity.getSynopsis());
		descriptor2WorldOnWire.setSynopsis(MovieFinderConstants.STR_EMPTY);

		invertedIndexEngine.updateEntry(descriptor2WorldOnWire);

		// verify (movie details)
		HashMap<Long, MovieDetailsDTO> movieDetails = invertedIndexEngine.getMovieDetails();
		MovieDetailsDTO fDet = movieDetails.get(descriptor2WorldOnWire.getId());
		assertNull(fDet);

		// verify words from index
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndexEngine.getInvertedIndex().keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
				.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		// analyze index tf/idfs
		for (String word : invertedIndexEngine.getInvertedIndex().keySet()) {
			IndexEntry indexEntry = invertedIndexEngine.getInvertedIndex().get(word);

			// check that no previous word from the updated document exists
			// and is linked to only the updated document
			for (String initialWord : descriptor2InitialSynopsis) {
				if (initialWord.equals(word)) {
					if (indexEntry.getPostings().keySet().contains(descriptor2WorldOnWire.getId())) {
						fail("The update was not performed");
					}
				}
			}

			// check idf
			if (word.startsWith("forc") || word.startsWith("militari") || word.startsWith("action")) {
				assertEquals(0.0f, indexEntry.getIdf());
			} else {
				assertEquals(0.6931472f, indexEntry.getIdf());
			}

			// check tf
			for (Map.Entry<Long, Posting> postingEntry : indexEntry.getPostings().entrySet()) {
				if (postingEntry.getKey().equals(descriptor1Serenity.getId())) {
					if (word.startsWith("marvel") || word.startsWith("toni") || word.startsWith("independ")
							|| word.startsWith("galax")) {
						assertEquals(new Short((short) 2), postingEntry.getValue().getTf());
					} else if (word.startsWith("allianc")) {
						assertEquals(new Short((short) 3), postingEntry.getValue().getTf());
					} else {
						assertEquals(new Short((short) 1), postingEntry.getValue().getTf());
					}
				}
			}
		}
	}

	/**
	 * Tests updateEntry method with a synopsis containing UTF8 characters
	 */
	@Test
	public void testUpdateUsingUTF8Synopsis() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute (update the second movie; set synopsis to empty string)
		String[] descriptor2InitialSynopsis = TextParsingHelper.parseText(descriptor1Serenity.getSynopsis());
		descriptor2WorldOnWire.setSynopsis("Françoise Dorléac");
		invertedIndexEngine.updateEntry(descriptor2WorldOnWire);

		// verify (movie details)
		HashMap<Long, MovieDetailsDTO> movieDetails = invertedIndexEngine.getMovieDetails();
		MovieDetailsDTO fDet = movieDetails.get(descriptor2WorldOnWire.getId());
		Assert.assertEquals("Françoise Dorléac", fDet.getDesc());

		// verify words from index
		ArrayList<String> indexWords = new ArrayList<String>(invertedIndexEngine.getInvertedIndex().keySet());
		Collections.sort(indexWords);

		Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
				.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
		ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
		Collections.sort(parsedWordsList);

		assertEquals(indexWords, parsedWordsList);

		assertTrue(invertedIndexEngine.getInvertedIndex().keySet().contains("françois"));
		assertTrue(invertedIndexEngine.getInvertedIndex().keySet().contains("dorléac"));

		// analyze index tf/idfs
		for (String word : invertedIndexEngine.getInvertedIndex().keySet()) {
			IndexEntry indexEntry = invertedIndexEngine.getInvertedIndex().get(word);

			// check that no previous word from the updated document exists and
			// is linked to only the updated document
			for (String initialWord : descriptor2InitialSynopsis) {
				if (initialWord.equals(word)) {
					if (indexEntry.getPostings().keySet().contains(descriptor2WorldOnWire.getId())) {
						fail("The update was not performed");
					}
				}
			}

			// check idf
			if (word.startsWith("forc") || word.startsWith("militari") || word.startsWith("action")) {
				assertEquals(0.4054651f, indexEntry.getIdf());
			} else {
				assertEquals(1.0986123f, indexEntry.getIdf());
			}

			// check tf
			for (Map.Entry<Long, Posting> postingEntry : indexEntry.getPostings().entrySet()) {
				if (postingEntry.getKey().equals(descriptor1Serenity.getId())) {
					if (word.startsWith("marvel") || word.startsWith("toni") || word.startsWith("independ")
							|| word.startsWith("galax")) {
						assertEquals(new Short((short) 2), postingEntry.getValue().getTf());
					} else if (word.startsWith("allianc")) {
						assertEquals(new Short((short) 3), postingEntry.getValue().getTf());
					} else {
						assertEquals(new Short((short) 1), postingEntry.getValue().getTf());
					}
				}
			}
		}
	}

	/**
	 * Update the synopsis of a non existent document
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void updateNonExistingDocument() throws InvalidMovieDescriptorException {
		// setup
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// save a movie into the database and after that try to use the update
		// method from the index
		MovieSource movieSource = new MovieSource();
		movieSource.setLocation("http://rottentomatoes.com");
		movieSource.setName("Rotten Tomatoes");
		movieSourceService.save(movieSource);

		MovieDescriptor kungFuDescriptor = new MovieDescriptor();
		kungFuDescriptor.setId(1L);
		kungFuDescriptor.setName("Kung Fu Hustle");
		kungFuDescriptor.setYear(2005);
		kungFuDescriptor.setRemotePath("http://www.rottentomatoes.com/m/kung_fu_hustle/");
		kungFuDescriptor.setRemoteId("080809");
		kungFuDescriptor.setSource(movieSource);
		kungFuDescriptor.setSynopsis("abc");
		Set<Genre> genres = new HashSet<Genre>();
		genres.add(Genre.DRAMA);
		genres.add(Genre.ART_HOUSE_INTERNATIONAL);
		genres.add(Genre.ROMANCE);
		genres.add(Genre.COMEDY);
		kungFuDescriptor.setGenres(genres);
		movieDescriptorService.save(kungFuDescriptor);

		// execute
		try {
			invertedIndexEngine.updateEntry(kungFuDescriptor);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST, ex.getReason());

			// verify
			HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

			// check that there's 2 entries as it results from the separate
			// array
			assertEquals(3, invertedIndexEngine.getNumberOfDocuments().intValue());

			// check number of words from inverted index
			ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
			Collections.sort(indexWords);

			Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
					.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
			ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
			Collections.sort(parsedWordsList);

			assertEquals(indexWords, parsedWordsList);

			throw ex;
		}
	}

	/**
	 * Given an empty index, try to update a movie and test that it fails with
	 * an exception
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void updateMovieFromEmptyIndex() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		// execute
		try {
			invertedIndexEngine.updateEntry(descriptor1Serenity);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST, ex.getReason());

			// verify
			HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

			// check that there's 2 entries as it results from the separate
			// array
			assertEquals(0, invertedIndexEngine.getNumberOfDocuments().intValue());

			// check number of words from inverted index
			ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
			assertEquals(0, indexWords.size());

			throw ex;
		}
	}

	/**
	 * Try to update a null movie and check that it fails with an exception
	 * 
	 * @throws InvalidMovieDescriptorException
	 */
	@Test(expected = InvalidMovieDescriptorException.class)
	public void updateNullMovie() throws InvalidMovieDescriptorException {
		// setup
		// save all 8 descriptors to database
		saveDescriptorsIntoDatabase();

		invertedIndexEngine.clearIndex();

		invertedIndexEngine.addEntry(descriptor1Serenity);

		invertedIndexEngine.addEntry(descriptor2WorldOnWire);

		invertedIndexEngine.addEntry(descriptor3IronMan);

		// execute
		try {
			invertedIndexEngine.updateEntry(null);
		} catch (InvalidMovieDescriptorException ex) {
			assertEquals(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_IS_NULL, ex.getReason());

			// verify
			HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();

			// check that there's 2 entries as it results from the separate
			// array
			assertEquals(3, invertedIndexEngine.getNumberOfDocuments().intValue());

			// check number of words from inverted index
			ArrayList<String> indexWords = new ArrayList<String>(invertedIndex.keySet());
			Collections.sort(indexWords);

			Set<String> parsedWords = new HashSet<String>(Arrays.asList(TextParsingHelper.parseText(descriptor1Serenity
					.getSynopsis() + descriptor2WorldOnWire.getSynopsis() + " " + descriptor3IronMan.getSynopsis())));
			ArrayList<String> parsedWordsList = new ArrayList<String>(parsedWords);
			Collections.sort(parsedWordsList);

			assertEquals(indexWords, parsedWordsList);

			throw ex;
		}
	}

	/**
	 * Given a list of {@link MovieCrewPerson}s it extracts the full name of
	 * each and constructs an array list
	 * 
	 * @param movieCrewPersons
	 *            the list of actors, directors and screenwriters that were
	 *            involved in a movie
	 * @return the full names in lowercase for actors, directors and
	 *         screenwriters
	 */
	private List<String> getStringList(Set<MovieCrewPerson> movieCrewPersons, boolean turnToLowerCase) {
		List<String> movieCrewPersonsToReturn = new ArrayList<String>();

		for (MovieCrewPerson movieCrewPerson : movieCrewPersons) {
			if (turnToLowerCase) {
				movieCrewPersonsToReturn.add(movieCrewPerson.getFullName().toLowerCase());
			} else {
				movieCrewPersonsToReturn.add(movieCrewPerson.getFullName());
			}
		}

		return movieCrewPersonsToReturn;
	}
}
