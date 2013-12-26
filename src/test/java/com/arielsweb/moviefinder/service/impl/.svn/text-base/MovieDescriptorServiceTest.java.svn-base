package com.arielsweb.moviefinder.service.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.model.MovieCrewPersonType;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Tests the {@link MovieDescriptorService}
 * 
 * @author Ariel
 * 
 */
@DataSet("AllDaoTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class MovieDescriptorServiceTest implements IGenericServiceTest<MovieDescriptor> {
    @SpringBeanByType
    private MovieDescriptorService movieDescriptorService;
    @SpringBeanByType
    private MovieSourceService movieSourceService;
    private static final Long ONE = new Long(1);

    @Test
    @Override
    public void testFind() {
	String exMovieName = "Ion movie";
	MovieDescriptor movieDescriptor = movieDescriptorService.find(1L);

	Assert.assertEquals(exMovieName, movieDescriptor.getName());
	Assert.assertEquals(ONE, movieDescriptor.getId());
    }

    @Test
    @Override
    public void testUpdate() {
	String newName = "My Movie, Not Ion's";
	String newDesc = "Not Long!";

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setId(1L);
	movieDescriptor.setName(newName);
	movieDescriptor.setYear(2012);
	movieDescriptor.setSynopsis(newDesc);
	movieDescriptor.setRemoteId("98765");
	movieDescriptor.setRemotePath("remove_path");

	MovieSource source = new MovieSource();
	source.setId(1L);
	movieDescriptor.setSource(source);
	movieDescriptorService.update(movieDescriptor);

	MovieDescriptor actual = movieDescriptorService.find(1L);
	Assert.assertEquals(ONE, actual.getId());
	Assert.assertEquals(newName, actual.getName());
	Assert.assertEquals(newDesc, actual.getSynopsis());
    }

    /**
     * Tests saving a {@link MovieDescriptor} along with a set of tags
     */
    @Test
    public void testSaveMovieAndGenres() {
	// setup
	String name = "Terminator 4";
	
	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName(name);
	movieDescriptor.setYear(2012);
	movieDescriptor.setSynopsis("A new movie from director McG");
	movieDescriptor.setRemoteId("98766");
	movieDescriptor.setRemotePath("remove_path");
	
	Genre[] tags = new Genre[3];
	tags[0] = Genre.DRAMA;
	tags[1] = Genre.MYSTERY_SUSPENSE;
	tags[2] = Genre.SCIENCE_FICTION_FANTASY;
	
	movieDescriptor.setGenres(new HashSet<Genre>(Arrays.asList(tags)));

	MovieSource source = new MovieSource();
	source.setId(1L);
	movieDescriptor.setSource(source);

	// execute
	movieDescriptorService.save(movieDescriptor);

	// verify
	MovieDescriptor movieWithTags = movieDescriptorService.find(movieDescriptor.getId());
	assertEquals(movieDescriptor.getRemotePath(), movieWithTags.getRemotePath());
	assertEquals(movieDescriptor.getName(), movieWithTags.getName());
	assertEquals(movieDescriptor.getSynopsis(), movieWithTags.getSynopsis());
	assertEquals(new HashSet<Genre>(Arrays.asList(tags)), movieWithTags.getGenres());
    }

    /**
     * Tests saving a {@link MovieDescriptor} along with a set of genres
     */
    @Test
    public void testUpdateMovieWithGenres() {
	// setup
	String name = "Terminator 4";

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName(name);
	movieDescriptor.setYear(2012);
	movieDescriptor.setSynopsis("A new movie from director McG");
	movieDescriptor.setRemoteId("98767");
	movieDescriptor.setRemotePath("remove_path");

	MovieSource source = new MovieSource();
	source.setId(1L);
	movieDescriptor.setSource(source);
	movieDescriptorService.save(movieDescriptor);

	// execute
	movieDescriptor = movieDescriptorService.find(movieDescriptor.getId());

	Genre[] genres = new Genre[1];
	genres[0] = Genre.SCIENCE_FICTION_FANTASY;
	movieDescriptor.setGenres(new HashSet<Genre>(Arrays.asList(genres)));

	// verify
	MovieDescriptor movieWithGenres = movieDescriptorService.find(movieDescriptor.getId());
	assertEquals(movieDescriptor.getRemotePath(), movieWithGenres.getRemotePath());
	assertEquals(movieDescriptor.getName(), movieWithGenres.getName());
	assertEquals(movieDescriptor.getSynopsis(), movieWithGenres.getSynopsis());
	assertEquals(new HashSet<Genre>(Arrays.asList(genres)), movieWithGenres.getGenres());
    }

    @Test
    @Override
    public void testSave() {
	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName("Booogiiee");
	movieDescriptor.setYear(2012);
	movieDescriptor.setRemoteId("98768");
	movieDescriptor.setSynopsis("Maaannnnn");

	MovieSource source = new MovieSource();
	source.setId(1L);
	movieDescriptor.setSource(source);
	movieDescriptor.setRemotePath("/usrs/linux/kernel");

	movieDescriptorService.save(movieDescriptor);
	MovieDescriptor actual = movieDescriptorService.find(movieDescriptor.getId());

	Assert.assertEquals(movieDescriptor, actual);
    }

    @Test
    @Override
    public void testDeleteByEntity() {
	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setId(1L);

	movieDescriptorService.delete(movieDescriptor);

	MovieDescriptor actual = movieDescriptorService.find(1L);
	Assert.assertNull(actual);
    }

    @Test
    @Override
    public void testDeleteById() {
	movieDescriptorService.delete(1L);

	MovieDescriptor actual = movieDescriptorService.find(1L);
	Assert.assertNull(actual);
    }

    /**
     * Tests the {@link MovieDescriptorService#isUnique(MovieDescriptor)} method
     * - for a duplicated movie
     */
    @Test
    public void testIsUniqueForDuplicatedMovie() {
	MovieSource source = movieSourceService.find(1L);

	// setup (try to add a duplicated movie)
	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName("Ion's movie");
	movieDescriptor.setYear(2012);
	movieDescriptor.setRemoteId("12345");
	movieDescriptor.setSource(source);
	movieDescriptor.setSynopsis("Testing with a different description");
	movieDescriptor.setRemotePath("http://www.rottentomatoes.com/Ion");

	// execute
	boolean isUnique = movieDescriptorService.isUnique(movieDescriptor.getRemoteId(), source.getId(), movieDescriptor.getName(), movieDescriptor
		.getYear().toString());

	// verify
	// it's not unique because there's already one in the db
	assertFalse(isUnique);
    }

    /**
     * Tests the {@link MovieDescriptorService#isUnique(MovieDescriptor)} method
     * - for a brand new movie without a remote id populated. The uniqueness
     * should be checked against name and year.
     */
    @Test
    public void testIsUniqueForNewMovie() {
	// setup (try to add a brand new movie)
	MovieSource source = movieSourceService.find(1L);

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName("Ion movie new");
	movieDescriptor.setYear(2012); // same year
	movieDescriptor.setSynopsis("Testing with a different description");
	movieDescriptor.setRemotePath("http://www.rottentomatoes.com/Ion");
	movieDescriptor.setSource(source);

	// execute
	boolean isUnique = movieDescriptorService.isUnique(movieDescriptor.getRemoteId(), source.getId(), movieDescriptor.getName(), movieDescriptor
		.getYear().toString());

	// verify
	// it's unique because there's no other one in the db
	assertTrue(isUnique);
    }

    /**
     * Tests the {@link MovieDescriptorService#isUnique(MovieDescriptor)} method
     * - for a brand new movie with special characters in its name and without a
     * remote id populated. The uniqueness should be checked against name and
     * year.
     */
    @Test
    public void testIsUniqueForNewMovie_SpecialChars() {
	// setup (try to add a brand new movie)
	MovieSource source = movieSourceService.find(1L);

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName("Ion's movie new"); // notice the " ' "
	movieDescriptor.setYear(2012); // some year
	movieDescriptor.setSynopsis("Testing with a different description");
	movieDescriptor.setRemotePath("http://www.rottentomatoes.com/Ion");
	movieDescriptor.setSource(source);

	// execute
	boolean isUnique = movieDescriptorService.isUnique(movieDescriptor.getRemoteId(), source.getId(), movieDescriptor.getName(), movieDescriptor
		.getYear().toString());

	// verify
	// it's unique because there's already one in the db
	assertTrue(isUnique);
    }

    /**
     * Tests the {@link MovieDescriptorService#isUnique(MovieDescriptor)} method
     * - for a brand new movie with double quotes in its name
     */
    @Test
    public void testIsUniqueForNewMovie_DoubleQuotes() {
	MovieSource source = movieSourceService.find(1L);

	// setup (try to add a brand new movie)
	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName("Ion \"movie new\""); // notice the " ' "
	movieDescriptor.setYear(2012); // some year
	movieDescriptor.setSynopsis("Testing with a different description");
	movieDescriptor.setRemotePath("http://www.rottentomatoes.com/Ion");
	movieDescriptor.setSource(source);

	// execute
	boolean isUnique = movieDescriptorService.isUnique(movieDescriptor.getRemoteId(), source.getId(), movieDescriptor.getName(), movieDescriptor
		.getYear().toString());

	// verify
	// it's unique because there's already one in the db
	assertTrue(isUnique);
    }
    
    /**
     * Tests inserting a movie with cast and crew (actors, directors and
     * screenwriters) into the database
     */
    @Test
    public void testInsertMovieWithCastAndCrew() {
	String name = "Terminator 4";

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	movieDescriptor.setName(name);
	movieDescriptor.setYear(2012);
	movieDescriptor.setSynopsis("A new movie from director McG");
	movieDescriptor.setRemoteId("98766");
	movieDescriptor.setRemotePath("remove_path");

	Genre[] tags = new Genre[3];
	tags[0] = Genre.DRAMA;
	tags[1] = Genre.MYSTERY_SUSPENSE;
	tags[2] = Genre.SCIENCE_FICTION_FANTASY;

	movieDescriptor.setGenres(new HashSet<Genre>(Arrays.asList(tags)));
	
	Set<MovieCrewPerson> actors = new HashSet<MovieCrewPerson>();
	actors.add(new MovieCrewPerson("Christian Bale", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Sam Worthington", MovieCrewPersonType.ACTOR));
	actors.add(new MovieCrewPerson("Moon Bloodgood", MovieCrewPersonType.ACTOR));
	
	movieDescriptor.setActors(actors);

	Set<MovieCrewPerson> directors = new HashSet<MovieCrewPerson>();
	directors.add(new MovieCrewPerson("McG", MovieCrewPersonType.DIRECTOR));

	movieDescriptor.setActors(actors);
	movieDescriptor.setDirectors(directors);
	
	Set<MovieCrewPerson> screenwriters = new HashSet<MovieCrewPerson>();
	screenwriters.add(new MovieCrewPerson("Michael Ferris", MovieCrewPersonType.SCREENWRITER));
	screenwriters.add(new MovieCrewPerson("John Brancato", MovieCrewPersonType.SCREENWRITER));
	
	movieDescriptor.setScreenwriters(screenwriters);

	MovieSource source = new MovieSource();
	source.setId(1L);
	source.setLocation("rotten tomatoes");
	source.setName("Rotten");
	movieDescriptor.setSource(source);
	movieSourceService.save(source);

	movieDescriptorService.save(movieDescriptor);

	// verify
	MovieDescriptor movieWithCastAndCrew = movieDescriptorService.find(movieDescriptor.getId());

	assertEquals(movieDescriptor.getRemotePath(), movieWithCastAndCrew.getRemotePath());
	assertEquals(movieDescriptor.getName(), movieWithCastAndCrew.getName());
	assertEquals(movieDescriptor.getSynopsis(), movieWithCastAndCrew.getSynopsis());
	assertEquals(new HashSet<Genre>(Arrays.asList(tags)), movieWithCastAndCrew.getGenres());

	assertEquals(movieDescriptor.getActors(), movieWithCastAndCrew.getActors());
	assertEquals(movieDescriptor.getDirectors(), movieWithCastAndCrew.getDirectors());
	assertEquals(movieDescriptor.getScreenWriters(), movieWithCastAndCrew.getScreenWriters());

	Set<MovieCrewPerson> crewAndCast = new HashSet<MovieCrewPerson>();
	crewAndCast.addAll(actors);
	crewAndCast.addAll(directors);
	crewAndCast.addAll(screenwriters);
	
	assertEquals(crewAndCast, movieWithCastAndCrew.getCastAndCrew());
    }

    /**
     * Tests inserting a movie with cast and crew (actors, directors and
     * screenwriters) into the database. In this case actor names are duplicate.
     */
    @Test
    public void testInsertMovieWithDuplicateCastAndCrew() {
	String name1 = "Terminator 4";
	String name2 = "Equilibrium";

	MovieDescriptor movieDescriptor1 = new MovieDescriptor();
	movieDescriptor1.setName(name1);
	movieDescriptor1.setYear(2012);
	movieDescriptor1.setSynopsis("A new movie from director McG");
	movieDescriptor1.setRemoteId("98766");
	movieDescriptor1.setRemotePath("remove_path");

	Genre[] genresTerminator = new Genre[3];
	genresTerminator[0] = Genre.DRAMA;
	genresTerminator[1] = Genre.MYSTERY_SUSPENSE;
	genresTerminator[2] = Genre.SCIENCE_FICTION_FANTASY;

	movieDescriptor1.setGenres(new HashSet<Genre>(Arrays.asList(genresTerminator)));

	Set<MovieCrewPerson> actorsTerminator4 = new HashSet<MovieCrewPerson>();
	MovieCrewPerson christianBale = new MovieCrewPerson("Christian Bale", MovieCrewPersonType.ACTOR);
	actorsTerminator4.add(christianBale);
	actorsTerminator4.add(new MovieCrewPerson("Sam Worthington", MovieCrewPersonType.ACTOR));
	actorsTerminator4.add(new MovieCrewPerson("Moon Bloodgood", MovieCrewPersonType.ACTOR));

	movieDescriptor1.setActors(actorsTerminator4);

	Set<MovieCrewPerson> directorsTerminator4 = new HashSet<MovieCrewPerson>();
	directorsTerminator4.add(new MovieCrewPerson("McG", MovieCrewPersonType.DIRECTOR));

	movieDescriptor1.setActors(actorsTerminator4);
	movieDescriptor1.setDirectors(directorsTerminator4);

	Set<MovieCrewPerson> screenwritersTerminator4 = new HashSet<MovieCrewPerson>();
	screenwritersTerminator4.add(new MovieCrewPerson("Michael Ferris", MovieCrewPersonType.SCREENWRITER));
	screenwritersTerminator4.add(new MovieCrewPerson("John Brancato", MovieCrewPersonType.SCREENWRITER));

	movieDescriptor1.setScreenwriters(screenwritersTerminator4);

	MovieDescriptor movieDescriptor2 = new MovieDescriptor();
	movieDescriptor2.setName(name2);
	movieDescriptor2.setYear(2002);
	movieDescriptor2.setSynopsis("A movie about the future of humanity");
	movieDescriptor2.setRemoteId("tt0238380");
	movieDescriptor2.setRemotePath("remove_path");

	Genre[] genresEquilibrum = new Genre[3];
	genresEquilibrum[0] = Genre.DRAMA;
	genresEquilibrum[1] = Genre.SCI_FI;
	genresEquilibrum[2] = Genre.ACTION;

	movieDescriptor2.setGenres(new HashSet<Genre>(Arrays.asList(genresEquilibrum)));

	Set<MovieCrewPerson> actorsEquilibrum = new HashSet<MovieCrewPerson>();
	actorsEquilibrum.add(christianBale);
	actorsEquilibrum.add(new MovieCrewPerson("Dominic Purcell", MovieCrewPersonType.ACTOR));
	actorsEquilibrum.add(new MovieCrewPerson("Sean Bean", MovieCrewPersonType.ACTOR));

	movieDescriptor2.setActors(actorsEquilibrum);

	Set<MovieCrewPerson> directorsEquilibrum = new HashSet<MovieCrewPerson>();
	MovieCrewPerson kurtWimmer = new MovieCrewPerson("Kurt Wimmer", MovieCrewPersonType.DIRECTOR);
	directorsEquilibrum.add(kurtWimmer);

	movieDescriptor2.setActors(actorsEquilibrum);
	movieDescriptor2.setDirectors(directorsEquilibrum);

	Set<MovieCrewPerson> screenwritersEquilibrum = new HashSet<MovieCrewPerson>();
	screenwritersEquilibrum.add(kurtWimmer);

	movieDescriptor2.setScreenwriters(screenwritersEquilibrum);

	MovieSource source = new MovieSource();
	source.setId(1L);
	source.setLocation("rotten tomatoes");
	source.setName("Rotten");
	movieDescriptor1.setSource(source);
	movieDescriptor2.setSource(source);
	movieSourceService.save(source);

	movieDescriptorService.save(movieDescriptor1);
	movieDescriptorService.save(movieDescriptor2);

	// verify
	MovieDescriptor equilibrumWithCastAndCrew = movieDescriptorService.find(movieDescriptor2.getId());

	assertEquals(movieDescriptor2.getRemotePath(), equilibrumWithCastAndCrew.getRemotePath());
	assertEquals(movieDescriptor2.getName(), equilibrumWithCastAndCrew.getName());
	assertEquals(movieDescriptor2.getSynopsis(), equilibrumWithCastAndCrew.getSynopsis());
	assertEquals(new HashSet<Genre>(Arrays.asList(genresEquilibrum)), equilibrumWithCastAndCrew.getGenres());

	assertEquals(movieDescriptor2.getActors(), equilibrumWithCastAndCrew.getActors());
	assertEquals(movieDescriptor2.getDirectors(), equilibrumWithCastAndCrew.getDirectors());
	assertEquals(movieDescriptor2.getScreenWriters(), equilibrumWithCastAndCrew.getScreenWriters());

	Set<MovieCrewPerson> crewAndCast = new HashSet<MovieCrewPerson>();
	crewAndCast.addAll(actorsEquilibrum);
	crewAndCast.addAll(directorsEquilibrum);
	crewAndCast.addAll(screenwritersEquilibrum);

	assertEquals(crewAndCast, equilibrumWithCastAndCrew.getCastAndCrew());

	// verify
	MovieDescriptor terminatorWithCastAndCrew = movieDescriptorService.find(movieDescriptor1.getId());

	assertEquals(movieDescriptor1.getRemotePath(), terminatorWithCastAndCrew.getRemotePath());
	assertEquals(movieDescriptor1.getName(), terminatorWithCastAndCrew.getName());
	assertEquals(movieDescriptor1.getSynopsis(), terminatorWithCastAndCrew.getSynopsis());
	assertEquals(new HashSet<Genre>(Arrays.asList(genresTerminator)), terminatorWithCastAndCrew.getGenres());

	assertEquals(movieDescriptor1.getActors(), terminatorWithCastAndCrew.getActors());
	assertEquals(movieDescriptor1.getDirectors(), terminatorWithCastAndCrew.getDirectors());
	assertEquals(movieDescriptor1.getScreenWriters(), terminatorWithCastAndCrew.getScreenWriters());

	Set<MovieCrewPerson> crewAndCastTerminator = new HashSet<MovieCrewPerson>();
	crewAndCastTerminator.addAll(actorsTerminator4);
	crewAndCastTerminator.addAll(directorsTerminator4);
	crewAndCastTerminator.addAll(screenwritersTerminator4);

	assertEquals(crewAndCastTerminator, terminatorWithCastAndCrew.getCastAndCrew());

    }
}
