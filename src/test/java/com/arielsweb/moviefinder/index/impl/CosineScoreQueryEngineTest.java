package com.arielsweb.moviefinder.index.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateMidnight;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.index.IQueryEngine;
import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.exception.InvalidQueryException;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.model.MovieCrewPersonType;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Tests the {@link CosineScoreQueryEngine}
 * 
 * @author Ariel
 * 
 */
@DataSet("ClearDescriptorsGenresAndSources.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class CosineScoreQueryEngineTest {
    @SpringBeanByType
    private MovieDescriptorService movieDescriptorService;
    @SpringBeanByType
    private IQueryEngine cosineQueryEngine;
    @SpringBeanByType
    private IndexEngine invertedIndexEngine;
    private MovieDescriptor descriptor1Serenity, descriptor2WorldOnWire, descriptor3IronMan, descriptor4HitchHiker,
	    descriptor5Alien, descriptor6DayAfter, descriptor7Prometheus, descriptor8GhostShip;
    @SpringBeanByType
    private MovieSourceService movieSourceService;

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
	descriptor1Serenity.setReleaseDate(new DateMidnight(1980, 9, 30).toDate());
	movieDescriptorService.save(descriptor1Serenity);

	descriptor2WorldOnWire = new MovieDescriptor();
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
	descriptor2WorldOnWire.setReleaseDate(new DateMidnight(1990, 9, 30).toDate());
	movieDescriptorService.save(descriptor2WorldOnWire);

	descriptor3IronMan = new MovieDescriptor();
	descriptor3IronMan.setName("Iron Man");
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
	descriptor3IronMan.setReleaseDate(new DateMidnight(2000, 9, 30).toDate());
	movieDescriptorService.save(descriptor3IronMan);

	descriptor4HitchHiker = new MovieDescriptor();
	descriptor4HitchHiker.setName("The Hitchhiker's Guide to the Galaxy");
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
	descriptor4HitchHiker.setReleaseDate(new DateMidnight(2001, 9, 30).toDate());
	movieDescriptorService.save(descriptor4HitchHiker);

	descriptor5Alien = new MovieDescriptor();
	descriptor5Alien.setName("Alien");
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
	descriptor5Alien.setReleaseDate(new DateMidnight(2002, 9, 30).toDate());
	movieDescriptorService.save(descriptor5Alien);

	descriptor6DayAfter = new MovieDescriptor();
	descriptor6DayAfter.setName("The Day After");
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
	descriptor6DayAfter.setReleaseDate(new DateMidnight(2003, 9, 30).toDate());
	movieDescriptorService.save(descriptor6DayAfter);

	descriptor7Prometheus = new MovieDescriptor();
	descriptor7Prometheus.setName("Prometheus");
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
	descriptor7Prometheus.setReleaseDate(new DateMidnight(2004, 9, 30).toDate());
	movieDescriptorService.save(descriptor7Prometheus);

	descriptor8GhostShip = new MovieDescriptor();
	descriptor8GhostShip.setName("Ghost Ship");
	descriptor8GhostShip.setYear(2002);
	descriptor8GhostShip.setRemotePath("http://www.rottentomatoes.com/m/ghost_ship/");
	descriptor8GhostShip.setRemoteId("12352");
	descriptor8GhostShip.setSource(movieSource);
	descriptor8GhostShip
		.setSynopsis("A salvage team think they've made the find of a lifetime, until they discover there's more on board than meets the eye in this supernatural thriller. Led by Captain Sean Murphy (Gabriel Byrne), the crew of the tugboat Arctic Warrior have discovered a sideline far more lucrative than hauling ships in and out of the harbor -- they locate missing or wrecked ships in international waters, repair them until they can be brought back to port, and then sell off the ship and its contents as salvage.");
	genres = new HashSet<Genre>();
	genres.add(Genre.HORROR);
	descriptor8GhostShip.setGenres(genres);
	descriptor8GhostShip.setReleaseDate(new DateMidnight(2005, 9, 30).toDate());
	movieDescriptorService.save(descriptor8GhostShip);

    }

    /**
     * Saves {@link MovieDescriptor}s into database
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void saveDescriptorsWithCastIntoDatabase() throws InvalidMovieDescriptorException {
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
	descriptor1Serenity.setReleaseDate(new DateMidnight(2005, 9, 30).toDate());

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
	descriptor2SavingPrivateRyan.setReleaseDate(new DateMidnight(1988, 11, 6).toDate());

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
	descriptor3CastAway.setReleaseDate(new DateMidnight(2000, 12, 22).toDate());

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
	descriptor4WarHorse.setReleaseDate(new DateMidnight(2011, 2, 10).toDate());
	movieDescriptorService.save(descriptor4WarHorse);

	invertedIndexEngine.setIndexFullNamesForCastAndCrew(false);

	// execute
	invertedIndexEngine.addEntry(descriptor1Serenity);
	invertedIndexEngine.addEntry(descriptor2SavingPrivateRyan);
	invertedIndexEngine.addEntry(descriptor3CastAway);
	invertedIndexEngine.addEntry(descriptor4WarHorse);

	// execute
	List<ResultInfo> results = cosineQueryEngine.queryIndex("Steven Spielberg");

	// verify
	assertNotNull(results);

	Assert.assertEquals(2, results.size());

	// temporarily sort results with equal score based on their id
	Collections.sort(results, new Comparator<ResultInfo>() {
	    @Override
	    public int compare(ResultInfo o1, ResultInfo o2) {
		return o2.getId().compareTo(o1.getId());
	    }
	});

	assertEquals(descriptor4WarHorse.getId(), results.get(0).getId());
	assertEquals(descriptor4WarHorse.getName(), results.get(0).getTitle());
	assertEquals(descriptor4WarHorse.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor4WarHorse.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor4WarHorse.getSource().getName(), results.get(0).getSource());

	// idf("Steven") * tf("Steven", doc4) + idf("Spielberg") *
	// tf("Spielberg", doc4) = 0.69314718056 + 0.69314718056
	assertEquals(Float.valueOf("1.3862944"), results.get(0).getScore());

	// verify the order and scores
	assertEquals(descriptor2SavingPrivateRyan.getId(), results.get(1).getId());
	assertEquals(descriptor2SavingPrivateRyan.getName(), results.get(1).getTitle());
	assertEquals(descriptor2SavingPrivateRyan.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor2SavingPrivateRyan.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor2SavingPrivateRyan.getSource().getName(), results.get(1).getSource());

	// idf("Steven") * tf("Steven", doc2) + idf("Spielberg") *
	// tf("Spielberg", doc2) = 0.69314718056 + 0.69314718056
	assertEquals(Float.valueOf("1.3862944"), results.get(1).getScore());
    }

    /**
     * Tests the results returned by the query method from the
     * {@link IQueryEngine#queryIndex(String)}. The algorithm won't normalize
     * the scores according to document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForSuccessfulQuery_WithNoCastAndCrew() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute
	List<ResultInfo> results = cosineQueryEngine.queryIndex("ship computer");

	// verify
	assertNotNull(results);

	Assert.assertEquals(2, results.size());

	// verify the order and scores
	assertEquals(descriptor5Alien.getId(), results.get(0).getId());
	assertEquals(descriptor5Alien.getName(), results.get(0).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(0).getSource());

	// idf(ship) * tf(ship, doc5) + idf(computer) * tf(computer, doc5) =
	// 1.3862944 * 2 + 2.0794415 * 1 = 4.8520303
	assertEquals(Float.valueOf("4.8520303"), results.get(0).getScore());

	assertEquals(descriptor8GhostShip.getId(), results.get(1).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(1).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(1).getSource());

	// idf (ship) * tf(ship, doc8) = 1.3862944 * 3 = 4.1588832
	assertEquals(Float.valueOf("4.158883"), results.get(1).getScore());

    }

    /**
     * Tests the results returned by the query method from the
     * {@link IQueryEngine#queryIndex(String)}. The algorithm won't normalize
     * the scores according to document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForSuccessfulQuery_WithCastAndCrew() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute
	List<ResultInfo> results = cosineQueryEngine.queryIndex("ship computer");

	// verify
	assertNotNull(results);

	Assert.assertEquals(2, results.size());

	// verify the order and scores
	assertEquals(descriptor5Alien.getId(), results.get(0).getId());
	assertEquals(descriptor5Alien.getName(), results.get(0).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(0).getSource());

	// idf(ship) * tf(ship, doc5) + idf(computer) * tf(computer, doc5) =
	// 1.3862944 * 2 + 2.0794415 * 1 = 4.8520303
	assertEquals(Float.valueOf("4.8520303"), results.get(0).getScore());

	assertEquals(descriptor8GhostShip.getId(), results.get(1).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(1).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(1).getSource());

	// idf (ship) * tf(ship, doc8) = 1.3862944 * 3 = 4.1588832
	assertEquals(Float.valueOf("4.158883"), results.get(1).getScore());

    }

    /**
     * Tests the results returned by {@link IQueryEngine#queryIndex(String)} for
     * query that matches all the documents. Here, matches all documents means
     * that each document contains one of the words. The algorithm won't
     * normalize the scores according to document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForQueryMatchingAllDocuments() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute (the text parsing method makes discovery -> discoveri and
	// discovered -> discov)
	List<ResultInfo> results = cosineQueryEngine
		.queryIndex("discover mythology world space ship universe nuclear destroy");

	// verify
	assertNotNull(results);

	Assert.assertEquals(8, results.size());

	assertEquals(descriptor8GhostShip.getId(), results.get(0).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(0).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(0).getSource());

	// idf(discovery) * tf(discovery, doc8) + idf(ship) * tf(ship,
	// doc8) = 0.47000363 * 2 + 1.3862944 * 3 = 5.09889046
	assertEquals(Float.valueOf("5.09889046"), results.get(0).getScore());

	assertEquals(descriptor5Alien.getId(), results.get(1).getId());
	assertEquals(descriptor5Alien.getName(), results.get(1).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(1).getSource());

	// idf(discovery) * tf(discovery, doc5) + idf(space) * tf(space,
	// doc5) + idf(ship) * tf(ship, doc5) = 0.47000363 * 1 + 1.3862944 * 1 +
	// 1.3862944 * 2 = 4.62888683
	assertEquals(Float.valueOf("4.62888683"), results.get(1).getScore());

	assertEquals(descriptor7Prometheus.getId(), results.get(2).getId());
	assertEquals(descriptor7Prometheus.getName(), results.get(2).getTitle());
	assertEquals(descriptor7Prometheus.getSynopsis(), results.get(2).getDescription());
	assertEquals(descriptor7Prometheus.getRemotePath(), results.get(2).getRemotePath());
	assertEquals(descriptor7Prometheus.getSource().getName(), results.get(2).getSource());

	// idf(discovery) * tf(discovery, doc7) + idf(mythology) * tf(mythology,
	// doc7) + idf(universe) * tf(universe, doc7) = 0.47000363 * 1 +
	// 2.0794415 * 1 + 1.3862944 * 1 = 3.9357395
	assertEquals(Float.valueOf("3.9357395"), results.get(2).getScore());

	assertEquals(descriptor6DayAfter.getId(), results.get(3).getId());
	assertEquals(descriptor6DayAfter.getName(), results.get(3).getTitle());
	assertEquals(descriptor6DayAfter.getSynopsis(), results.get(3).getDescription());
	assertEquals(descriptor6DayAfter.getRemotePath(), results.get(3).getRemotePath());
	assertEquals(descriptor6DayAfter.getSource().getName(), results.get(3).getSource());

	// idf(world) * tf(world, doc6) + idf(destroy) * tf(destroy, doc6) =
	// 2.0794415 * 1 + 1.3862944 * 1= 3.465736
	assertEquals(Float.valueOf("3.465736"), results.get(3).getScore());

	assertEquals(descriptor4HitchHiker.getId(), results.get(4).getId());
	assertEquals(descriptor4HitchHiker.getName(), results.get(4).getTitle());
	assertEquals(descriptor4HitchHiker.getSynopsis(), results.get(4).getDescription());
	assertEquals(descriptor4HitchHiker.getRemotePath(), results.get(4).getRemotePath());
	assertEquals(descriptor4HitchHiker.getSource().getName(), results.get(4).getSource());

	// idf(discovery) * tf(discovery, doc4) + idf(universe) * tf(universe,
	// doc4) + idf(destroy) * tf(destroy, doc4)= 0.47000363 * 1 + 1.3862944
	// * 1 + 1.3862944 * 1 = 3.2425923
	assertEquals(Float.valueOf("3.2425923"), results.get(4).getScore());

	assertEquals(descriptor2WorldOnWire.getId(), results.get(5).getId());
	assertEquals(descriptor2WorldOnWire.getName(), results.get(5).getTitle());
	assertEquals(descriptor2WorldOnWire.getSynopsis(), results.get(5).getDescription());
	assertEquals(descriptor2WorldOnWire.getRemotePath(), results.get(5).getRemotePath());
	assertEquals(descriptor2WorldOnWire.getSource().getName(), results.get(5).getSource());

	// idf(world) * tf(world, doc2) = 1.3862944 * 2 = 2.7725888
	assertEquals(Float.valueOf("2.7725888"), results.get(5).getScore());

	assertEquals(descriptor3IronMan.getId(), results.get(6).getId());
	assertEquals(descriptor3IronMan.getName(), results.get(6).getTitle());
	assertEquals(descriptor3IronMan.getSynopsis(), results.get(6).getDescription());
	assertEquals(descriptor3IronMan.getRemotePath(), results.get(6).getRemotePath());
	assertEquals(descriptor3IronMan.getSource().getName(), results.get(6).getSource());

	// idf(discovery) * tf(discovery, doc3) + idf(world) * tf(world,
	// doc3) = 0.47000363 * 1 + 1.3862944 * 1 = 1.856298
	assertEquals(Float.valueOf("1.856298"), results.get(6).getScore());

	assertEquals(descriptor1Serenity.getId(), results.get(7).getId());
	assertEquals(descriptor1Serenity.getName(), results.get(7).getTitle());
	assertEquals(descriptor1Serenity.getSynopsis(), results.get(7).getDescription());
	assertEquals(descriptor1Serenity.getRemotePath(), results.get(7).getRemotePath());
	assertEquals(descriptor1Serenity.getSource().getName(), results.get(7).getSource());

	// idf(space) * tf(space, doc1) = 1.3862944 * 1 = 1.3862944
	assertEquals(Float.valueOf("1.3862944"), results.get(7).getScore());

    }

    /**
     * Tests the results returned by {@link IQueryEngine#queryIndex(String)} for
     * null query. The algorithm won't normalize the scores according to
     * document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test(expected = InvalidQueryException.class)
    public void testEngineForNullQuery() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute with exception
	cosineQueryEngine.queryIndex((String) null);

    }

    /**
     * Tests the results returned by {@link IQueryEngine#queryIndex(String)} for
     * empty string query. The algorithm won't normalize the scores according to
     * document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test(expected = InvalidQueryException.class)
    public void testEngineForEmptyStringQuery() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute with exception
	cosineQueryEngine.queryIndex("");
    }

    /**
     * Tests the results returned by {@link IQueryEngine#queryIndex(String)} for
     * a query equal to the descriptor's 3 synopsis. The algorithm won't
     * normalize the scores according to document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForQueryEqualToSynopsis() throws InvalidMovieDescriptorException {

	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute with the synopsis of the whole 3rd descriptor
	List<ResultInfo> results = cosineQueryEngine
		.queryIndex("From Marvel Studios and Paramount Pictures comes Iron Man, an action-packed take on the tale of wealthy philanthropist Tony Stark (Robert Downey Jr.), who develops an invulnerable robotic suit to fight the throes of evil. In addition to being filthy rich, billionaire industrialist Tony Stark is also a genius inventor. When Stark is kidnapped and forced to build a diabolical weapon, he instead uses his intelligence and ingenuity to construct an indestructible suit of armor and escape his captors. Once free, Stark discovers a deadly conspiracy that could destabilize the entire globe, and dons his powerful new suit on a mission to stop the villains and save the world. Gwyneth Paltrow co-stars as his secretary, Virginia \"Pepper\" Potts, while Terrence Howard fills the role of Jim \"Rhodey\" Rhodes, one of Stark s colleagues, whose military background leads him to help in the formation of the suit. Jon Favreau directs, with Marvel movie veterans Avi Arad and Kevin Feige producing.");

	// verify (notice the 3rd descriptor is at the top)
	Assert.assertEquals(8, results.size());

	assertEquals(descriptor3IronMan.getId(), results.get(0).getId());
	assertEquals(descriptor3IronMan.getName(), results.get(0).getTitle());
	assertEquals(descriptor3IronMan.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor3IronMan.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor3IronMan.getSource().getName(), results.get(0).getSource());
	assertEquals(Float.valueOf("256.00565"), results.get(0).getScore());

	assertEquals(descriptor2WorldOnWire.getId(), results.get(1).getId());
	assertEquals(descriptor2WorldOnWire.getName(), results.get(1).getTitle());
	assertEquals(descriptor2WorldOnWire.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor2WorldOnWire.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor2WorldOnWire.getSource().getName(), results.get(1).getSource());
	assertEquals(Float.valueOf("8.89313"), results.get(1).getScore());

	assertEquals(descriptor4HitchHiker.getId(), results.get(2).getId());
	assertEquals(descriptor4HitchHiker.getName(), results.get(2).getTitle());
	assertEquals(descriptor4HitchHiker.getSynopsis(), results.get(2).getDescription());
	assertEquals(descriptor4HitchHiker.getRemotePath(), results.get(2).getRemotePath());
	assertEquals(descriptor4HitchHiker.getSource().getName(), results.get(2).getSource());
	assertEquals(Float.valueOf("5.2042513"), results.get(2).getScore());

	assertEquals(descriptor7Prometheus.getId(), results.get(3).getId());
	assertEquals(descriptor7Prometheus.getName(), results.get(3).getTitle());
	assertEquals(descriptor7Prometheus.getSynopsis(), results.get(3).getDescription());
	assertEquals(descriptor7Prometheus.getRemotePath(), results.get(3).getRemotePath());
	assertEquals(descriptor7Prometheus.getSource().getName(), results.get(3).getSource());
	assertEquals(Float.valueOf("4.6288867"), results.get(3).getScore());

	assertEquals(descriptor6DayAfter.getId(), results.get(4).getId());
	assertEquals(descriptor6DayAfter.getName(), results.get(4).getTitle());
	assertEquals(descriptor6DayAfter.getSynopsis(), results.get(4).getDescription());
	assertEquals(descriptor6DayAfter.getRemotePath(), results.get(4).getRemotePath());
	assertEquals(descriptor6DayAfter.getSource().getName(), results.get(4).getSource());
	assertEquals(Float.valueOf("3.753418"), results.get(4).getScore());

	assertEquals(descriptor5Alien.getId(), results.get(5).getId());
	assertEquals(descriptor5Alien.getName(), results.get(5).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(5).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(5).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(5).getSource());
	assertEquals(Float.valueOf("3.2425923"), results.get(5).getScore());

	assertEquals(descriptor1Serenity.getId(), results.get(6).getId());
	assertEquals(descriptor1Serenity.getName(), results.get(6).getTitle());
	assertEquals(descriptor1Serenity.getSynopsis(), results.get(6).getDescription());
	assertEquals(descriptor1Serenity.getRemotePath(), results.get(6).getRemotePath());
	assertEquals(descriptor1Serenity.getSource().getName(), results.get(6).getSource());
	assertEquals(Float.valueOf("2.942488"), results.get(6).getScore());

	assertEquals(descriptor8GhostShip.getId(), results.get(7).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(7).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(7).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(7).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(7).getSource());
	assertEquals(Float.valueOf("2.942488"), results.get(6).getScore());
    }

    /**
     * Tests the results returned by {@link IQueryEngine#queryIndex(String)} for
     * a query equal to the descriptor's 3 synopsis concatenated to descriptor's
     * 1 synopsis. The algorithm won't normalize the scores according to
     * document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForQueryEqualToTwoSynopses() throws InvalidMovieDescriptorException {

	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute with the synopsis of the whole 3rd descriptor concatenated to
	// the whole 1st descriptor
	List<ResultInfo> results = cosineQueryEngine
		.queryIndex("From Marvel Studios and Paramount Pictures comes Iron Man, an action-packed take on the tale of wealthy philanthropist Tony Stark (Robert Downey Jr.), who develops an invulnerable robotic suit to fight the throes of evil. In addition to being filthy rich, billionaire industrialist Tony Stark is also a genius inventor. When Stark is kidnapped and forced to build a diabolical weapon, he instead uses his intelligence and ingenuity to construct an indestructible suit of armor and escape his captors. Once free, Stark discovers a deadly conspiracy that could destabilize the entire globe, and dons his powerful new suit on a mission to stop the villains and save the world. Gwyneth Paltrow co-stars as his secretary, Virginia \"Pepper\" Potts, while Terrence Howard fills the role of Jim \"Rhodey\" Rhodes, one of Stark s colleagues, whose military background leads him to help in the formation of the suit. Jon Favreau directs, with Marvel movie veterans Avi Arad and Kevin Feige producing. A band of renegades on the run in outer space get in more hot water than they anticipated in this sci-fi action-adventure adapted from the television series Firefly. In the 26th century, the galaxy has been colonized by a military force known as the Alliance, but its leadership has not gone unquestioned. The Alliance was once challenged by a league of rebels known as the Independents, but the Alliance emerged victorious after a brutal civil war, with the surviving Independents scattering around the galaxy.");

	// verify (notice the 3rd descriptor is at the top and the 1st is
	// second)
	Assert.assertEquals(8, results.size());

	assertEquals(descriptor3IronMan.getId(), results.get(0).getId());
	assertEquals(descriptor3IronMan.getName(), results.get(0).getTitle());
	assertEquals(descriptor3IronMan.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor3IronMan.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor3IronMan.getSource().getName(), results.get(0).getSource());
	assertEquals(Float.valueOf("258.94812"), results.get(0).getScore());

	assertEquals(descriptor1Serenity.getId(), results.get(1).getId());
	assertEquals(descriptor1Serenity.getName(), results.get(1).getTitle());
	assertEquals(descriptor1Serenity.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor1Serenity.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor1Serenity.getSource().getName(), results.get(1).getSource());
	assertEquals(Float.valueOf("97.55032"), results.get(1).getScore());

	assertEquals(descriptor4HitchHiker.getId(), results.get(2).getId());
	assertEquals(descriptor4HitchHiker.getName(), results.get(2).getTitle());
	assertEquals(descriptor4HitchHiker.getSynopsis(), results.get(2).getDescription());
	assertEquals(descriptor4HitchHiker.getRemotePath(), results.get(2).getRemotePath());
	assertEquals(descriptor4HitchHiker.getSource().getName(), results.get(2).getSource());
	assertEquals(Float.valueOf("11.900157"), results.get(2).getScore());

	assertEquals(descriptor2WorldOnWire.getId(), results.get(3).getId());
	assertEquals(descriptor2WorldOnWire.getName(), results.get(3).getTitle());
	assertEquals(descriptor2WorldOnWire.getSynopsis(), results.get(3).getDescription());
	assertEquals(descriptor2WorldOnWire.getRemotePath(), results.get(3).getRemotePath());
	assertEquals(descriptor2WorldOnWire.getSource().getName(), results.get(3).getSource());
	assertEquals(Float.valueOf("9.87396"), results.get(3).getScore());

	assertEquals(descriptor5Alien.getId(), results.get(4).getId());
	assertEquals(descriptor5Alien.getName(), results.get(4).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(4).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(4).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(4).getSource());
	assertEquals(Float.valueOf("6.590545"), results.get(4).getScore());

	assertEquals(descriptor6DayAfter.getId(), results.get(5).getId());
	assertEquals(descriptor6DayAfter.getName(), results.get(5).getTitle());
	assertEquals(descriptor6DayAfter.getSynopsis(), results.get(5).getDescription());
	assertEquals(descriptor6DayAfter.getRemotePath(), results.get(5).getRemotePath());
	assertEquals(descriptor6DayAfter.getSource().getName(), results.get(5).getSource());
	assertEquals(Float.valueOf("5.7150764"), results.get(5).getScore());

	assertEquals(descriptor7Prometheus.getId(), results.get(6).getId());
	assertEquals(descriptor7Prometheus.getName(), results.get(6).getTitle());
	assertEquals(descriptor7Prometheus.getSynopsis(), results.get(6).getDescription());
	assertEquals(descriptor7Prometheus.getRemotePath(), results.get(6).getRemotePath());
	assertEquals(descriptor7Prometheus.getSource().getName(), results.get(6).getSource());
	assertEquals(Float.valueOf("4.6288867"), results.get(6).getScore());

	assertEquals(descriptor8GhostShip.getId(), results.get(7).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(7).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(7).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(7).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(7).getSource());
	assertEquals(Float.valueOf("2.3263016"), results.get(7).getScore());
    }

    /**
     * Tests the results returned by an unsuccessful query method from the
     * {@link IQueryEngine#queryIndex(String)}. The algorithm won't normalize
     * the scores according to document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForUnsuccesfulQuery() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute with the synopsis of the whole 3rd descriptor
	List<ResultInfo> results = cosineQueryEngine.queryIndex("romania europe");

	// verify
	Assert.assertEquals(0, results.size());
    }

    /**
     * Tests the results returned by {@link IQueryEngine#queryIndex(String)}
     * after a query containing special characters is passed in. The algorithm
     * won't normalize the scores according to document length.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testEngineForQueryWithSpecialChars() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	// execute
	List<ResultInfo> results = cosineQueryEngine
		.queryIndex("ship's computer ']['l' ]] \\&* 932342 Ġ ġ Ģ ģ Ĥ ĥ Ħ ħ Ĩ ĩ Ī ī Ĭ ĭ Į į İ ı Ĳ ĳ Ĵ ĵ Ķ ķ ĸ Ĺ ĺ Ļ ļ Ľ ľ Ŀ");

	// verify
	assertNotNull(results);

	Assert.assertEquals(2, results.size());

	// verify the order and scores
	assertEquals(descriptor5Alien.getId(), results.get(0).getId());
	assertEquals(descriptor5Alien.getName(), results.get(0).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(0).getSource());

	// idf(ship) * tf(ship, doc5) + idf(computer) * tf(computer, doc5) =
	// 1.3862944 * 2 + 2.0794415 * 1 = 4.8520303
	assertEquals(Float.valueOf("4.8520303"), results.get(0).getScore());

	assertEquals(descriptor8GhostShip.getId(), results.get(1).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(1).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(1).getSource());

	// idf (ship) * tf(ship, doc8) = 1.3862944 * 3 = 4.1588832
	assertEquals(Float.valueOf("4.158883"), results.get(1).getScore());
    }

    /**
     * Tests the {@link IQueryEngine#queryIndex(String)} by querying based on a
     * critics consensus.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testQueryForCriticsConsensus() throws InvalidMovieDescriptorException {
	// setup
	MovieSource movieSource = new MovieSource();
	movieSource.setLocation("http://rottentomatoes.com");
	movieSource.setName("Rotten Tomatoes");
	movieSourceService.save(movieSource);

	descriptor1Serenity = new MovieDescriptor();
	descriptor1Serenity.setName("Serenity");
	descriptor1Serenity.setYear(2005);
	descriptor1Serenity.setRemotePath("http://www.rottentomatoes.com/m/serenity/");
	descriptor1Serenity.setRemoteId("12345");
	descriptor1Serenity.setSource(movieSource);
	descriptor1Serenity
		.setSynopsis("A band of renegades on the run in outer space get in more hot water than they anticipated in this sci-fi action-adventure adapted from the television series Firefly. In the 26th century, the galaxy has been colonized by a military force known as the Alliance, but its leadership has not gone unquestioned. The Alliance was once challenged by a league of rebels known as the Independents, but the Alliance emerged victorious after a brutal civil war, with the surviving Independents scattering around the galaxy.");
	descriptor1Serenity
		.setAlternateSynopsis("Snappy dialogue and goofy characters make this Wild Wild West soap opera in space fun and adventurous.");
	Set<Genre> genres = new HashSet<Genre>();
	genres.add(Genre.ACTION_ADVENTURE);
	genres.add(Genre.SCIENCE_FICTION_FANTASY);
	descriptor1Serenity.setGenres(genres);
	movieDescriptorService.save(descriptor1Serenity);

	descriptor2WorldOnWire = new MovieDescriptor();
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
	descriptor3IronMan.setYear(2008);
	descriptor3IronMan.setRemotePath("http://www.rottentomatoes.com/m/iron_man/");
	descriptor3IronMan.setRemoteId("12347");
	descriptor3IronMan.setSource(movieSource);
	descriptor3IronMan
		.setSynopsis("From Marvel Studios and Paramount Pictures comes Iron Man, an action-packed take on the tale of wealthy philanthropist Tony Stark (Robert Downey Jr.), who develops an invulnerable robotic suit to fight the throes of evil. In addition to being filthy rich, billionaire industrialist Tony Stark is also a genius inventor. When Stark is kidnapped and forced to build a diabolical weapon, he instead uses his intelligence and ingenuity to construct an indestructible suit of armor and escape his captors. Once free, Stark discovers a deadly conspiracy that could destabilize the entire globe, and dons his powerful new suit on a mission to stop the villains and save the world. Gwyneth Paltrow co-stars as his secretary, Virginia \"Pepper\" Potts, while Terrence Howard fills the role of Jim \"Rhodey\" Rhodes, one of Stark s colleagues, whose military background leads him to help in the formation of the suit. Jon Favreau directs, with Marvel movie veterans Avi Arad and Kevin Feige producing.");
	descriptor3IronMan
		.setAlternateSynopsis("Director Jon Favreau and star Robert Downey make this smart, high impact superhero movie one that even non-comics fans can enjoy.");
	genres = new HashSet<Genre>();
	genres.add(Genre.DRAMA);
	genres.add(Genre.ACTION_ADVENTURE);
	genres.add(Genre.SCIENCE_FICTION_FANTASY);
	descriptor3IronMan.setGenres(genres);
	movieDescriptorService.save(descriptor3IronMan);

	descriptor4HitchHiker = new MovieDescriptor();
	descriptor4HitchHiker.setName("The Hitchhiker's Guide to the Galaxy");
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

	invertedIndexEngine.clearIndex();

	// add only first 4 descriptors
	invertedIndexEngine.addEntry(descriptor1Serenity);
	invertedIndexEngine.addEntry(descriptor2WorldOnWire);
	invertedIndexEngine.addEntry(descriptor3IronMan);
	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	// execute
	List<ResultInfo> results = cosineQueryEngine
		.queryIndex("Snappy dialogue and goofy characters make this Wild Wild West soap opera in space fun and adventurous.");

	// verify
	assertNotNull(results);

	Assert.assertEquals(3, results.size());

	// verify the order and scores
	assertEquals(descriptor1Serenity.getId(), results.get(0).getId());
	assertEquals(descriptor1Serenity.getName(), results.get(0).getTitle());
	assertEquals(descriptor1Serenity.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor1Serenity.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor1Serenity.getSource().getName(), results.get(0).getSource());

	assertEquals(Float.valueOf("21.775246"), results.get(0).getScore());

	assertEquals(descriptor4HitchHiker.getId(), results.get(1).getId());
	assertEquals(descriptor4HitchHiker.getName(), results.get(1).getTitle());
	assertEquals(descriptor4HitchHiker.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor4HitchHiker.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor4HitchHiker.getSource().getName(), results.get(1).getSource());

	assertEquals(Float.valueOf("1.5561936"), results.get(1).getScore());

	assertEquals(descriptor3IronMan.getId(), results.get(2).getId());
	assertEquals(descriptor3IronMan.getName(), results.get(2).getTitle());
	assertEquals(descriptor3IronMan.getSynopsis(), results.get(2).getDescription());
	assertEquals(descriptor3IronMan.getRemotePath(), results.get(2).getRemotePath());
	assertEquals(descriptor3IronMan.getSource().getName(), results.get(2).getSource());

	assertEquals(Float.valueOf("0.28768212"), results.get(2).getScore());

    }

    /**
     * Tests the {@link IQueryEngine#queryIndex(String)} by querying based on a
     * critics consensus.
     * 
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testQueryWithDocumentNormalization() throws InvalidMovieDescriptorException {
	// setup
	saveDescriptorsIntoDatabase();

	invertedIndexEngine.clearIndex();

	invertedIndexEngine.addEntry(descriptor1Serenity);

	invertedIndexEngine.addEntry(descriptor2WorldOnWire);

	invertedIndexEngine.addEntry(descriptor3IronMan);

	invertedIndexEngine.addEntry(descriptor4HitchHiker);

	invertedIndexEngine.addEntry(descriptor5Alien);

	invertedIndexEngine.addEntry(descriptor6DayAfter);

	invertedIndexEngine.addEntry(descriptor7Prometheus);

	invertedIndexEngine.addEntry(descriptor8GhostShip);

	cosineQueryEngine.setNormalizeScoreToDocumentLength("true");

	// execute
	List<ResultInfo> results = cosineQueryEngine.queryIndex("ship computer");

	// verify
	assertNotNull(results);

	Assert.assertEquals(2, results.size());

	// verify the order and scores
	assertEquals(descriptor8GhostShip.getId(), results.get(0).getId());
	assertEquals(descriptor8GhostShip.getName(), results.get(0).getTitle());
	assertEquals(descriptor8GhostShip.getSynopsis(), results.get(0).getDescription());
	assertEquals(descriptor8GhostShip.getRemotePath(), results.get(0).getRemotePath());
	assertEquals(descriptor8GhostShip.getSource().getName(), results.get(0).getSource());

	// idf(ship) * tf(ship, doc5) + idf(computer) * tf(computer, doc5) =
	// 1.3862944 * 2 + 2.0794415 * 1 = 4.8520303 / 41 * 100 = 10.143618
	assertEquals(Float.valueOf("10.143618"), results.get(0).getScore());

	assertEquals(descriptor5Alien.getId(), results.get(1).getId());
	assertEquals(descriptor5Alien.getName(), results.get(1).getTitle());
	assertEquals(descriptor5Alien.getSynopsis(), results.get(1).getDescription());
	assertEquals(descriptor5Alien.getRemotePath(), results.get(1).getRemotePath());
	assertEquals(descriptor5Alien.getSource().getName(), results.get(1).getSource());

	// idf (ship) * tf(ship, doc8) = 1.3862944 * 3 = 4.1588832 / 93 * 100 =
	// 5.217237
	assertEquals(Float.valueOf("5.217237"), results.get(1).getScore());

    }
}
