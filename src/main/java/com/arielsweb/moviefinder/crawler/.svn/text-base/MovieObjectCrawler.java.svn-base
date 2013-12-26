package com.arielsweb.moviefinder.crawler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.crawler.dto.MovieDTO;
import com.arielsweb.moviefinder.crawler.exception.InvalidMovieException;
import com.arielsweb.moviefinder.crawler.exception.InvalidMovieException.Reason;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.model.MovieCrewPersonType;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.service.MovieCrewPersonService;

/**
 * Given an URL for a http://www.imdb.com webpage, this crawler parses the dom
 * structure and extracts relevant info about a movie such as:
 * 
 * <pre>
 * - movie name
 * - year
 * - remote path
 * - id 
 * - synopsis, consensus 
 * - image_path
 * - cast and crew (actors, directors and screenwriters)
 * </pre>
 * 
 * @author Ariel
 * 
 */
@Component("movieObjectCrawler")
public class MovieObjectCrawler {

    @Autowired
    private MovieCrewPersonService movieCrewPersonService;

    /**
     * Creates a new movie crawler to be used for http://www.imdb.com
     */
    public MovieObjectCrawler() {

    }

    /**
     * Gets a {@link MovieDTO} from the specified URL given that the specified
     * URL has a predictable structure.
     * 
     * @param movieURL
     *            the URL where the movie is located
     * @return the {@link MovieDTO}
     * @throws IOException
     * @throws ParseException
     * @throws InvalidMovieException
     */
    public MovieDescriptor getMovieObject(String baseURL, String movieId) throws IOException,
	    InvalidMovieException {
	StringBuilder movieURL = new StringBuilder();
	movieURL.append(baseURL);
	movieURL.append(movieId);
	
	Document doc = null;
	try {
	    doc = Jsoup.connect(movieURL.toString()).get();
	} catch (SocketTimeoutException ex) {
	    // retry if a Socket time out exception occured
	    this.getMovieObject(baseURL, movieId);

	    return null;
	}

	MovieDescriptor movieDescriptor = new MovieDescriptor();

	// get movie name
	getTitle(doc, movieDescriptor);

	movieDescriptor.setRemotePath(baseURL + movieId);
	movieDescriptor.setRemoteId(movieId);

	Elements image = doc.select("#img_primary img");
	movieDescriptor.setImagePath(image.attr("src"));

	// get movie release date
	getReleaseDate(doc, movieDescriptor);

	// set movie id and remote path
	movieDescriptor.setRemoteId(movieId);
	
	movieDescriptor.setRemotePath(movieURL.toString());

	// get and set actors
	getActors(doc, movieDescriptor);

	// get and set directors
	Set<MovieCrewPerson> directors = getMovieCrew(doc, "#overview-top>div[itemprop=director]>a[itemprop=url]",
		MovieCrewPersonType.DIRECTOR);

	movieDescriptor.setDirectors(directors);

	// get and set screenwriters
	Set<MovieCrewPerson> screenwriters = getMovieCrew(doc, "#overview-top>div[itemprop=creator]>a[itemprop=url]",
		MovieCrewPersonType.SCREENWRITER);

	movieDescriptor.setScreenwriters(screenwriters);

	// get synopsis
	String synopsis = doc.select("#titleStoryLine>div[itemprop=description]").text();
	movieDescriptor.setSynopsis(synopsis);

	// get Genres
	String genresString = doc.select("#titleStoryLine>div[itemprop=genre]").text();
	genresString = genresString.substring(genresString.indexOf(":") + 1, genresString.length());

	getGenres(movieDescriptor, genresString);

	// get alternate description
	Elements alternateDescription = doc.select("#overview-top>p[itemprop=description]");
	movieDescriptor.setAlternateSynopsis(alternateDescription.text());

	// get rating
	Double rating = getMovieRating(doc, "#overview-top>.star-box>.star-box-giga-star");
	movieDescriptor.setRating(rating);

	return movieDescriptor;
    }

    /**
     * Gets the title of the movie
     * 
     * @param doc
     * @param movieDescriptor
     */
    private void getTitle(Document doc, MovieDescriptor movieDescriptor) {
	Elements originalMovieName = doc.select("#overview-top h1>span.title-extra[itemprop=name]");
	if (originalMovieName.hasText()) {
	    String movieName = originalMovieName.text();

	    // the first paranthesis that starts "(Original Title)"
	    movieDescriptor.setName(movieName.substring(1, movieName.indexOf("\" (")));
	} else {
	    movieDescriptor.setName(doc.select("#overview-top h1.header>span[itemprop=name]").text());
	}
    }

    /**
     * Gets the genres for the specified movie descriptor
     * 
     * @param movieDescriptor
     *            the {@link MovieDescriptor} to populate with values
     * @param genresString
     *            the string to parse in order to set the genres
     */
    private void getGenres(MovieDescriptor movieDescriptor, String genresString) {
	int start = 0;
	int end = 0;

	// the split by "|" doesn't work so writting a custom algorithm will do
	// the job
	Set<Genre> genres = new HashSet<Genre>();
	while (end < genresString.length()) {
	    end = genresString.indexOf("|", start);

	    // if there is no ' | ' anymore break out of the loop
	    if (end == -1) {
		end = genresString.length();
	    }

	    String genre = genresString.substring(start, end);

	    Genre imdbGenre = getMovieGenre(genre);
	    genres.add(imdbGenre);

	    start = end + 1;
	}

	movieDescriptor.setGenres(genres);
    }

    /**
     * Gets actors for the specified movie descriptor
     * 
     * @param doc
     *            the html document from which to get the info
     * @param movieDescriptor
     *            the {@link MovieDescriptor} to populate with info
     */
    private void getActors(Document doc, MovieDescriptor movieDescriptor) {
	Elements movieCast = doc.select("#titleCast tr");
	Set<MovieCrewPerson> actors = new HashSet<MovieCrewPerson>();
	for (int i = 1; i < movieCast.size(); i++) {
	    Element movieActor = movieCast.get(i);

	    String actorName = movieActor.select("td.itemprop>a").text();

	    // TODO: delete this if after the full population of the DB is done
	    if (actorName.equals("Arda Seçgun")) {
		continue;
	    }

	    if (!actorName.isEmpty()) {
		MovieCrewPerson actor = getOrCreateMovieCrewPerson(actorName, MovieCrewPersonType.ACTOR);

		actors.add(actor);
	    }
	}

	movieDescriptor.setActors(actors);
    }

    /**
     * All {@link MovieCrewPerson}s are unique at the DB level so we either get
     * an existing one or create a new one
     * 
     * @param moviePersonFullName
     *            the full name of the person involved in the movie
     * @return an existing {@link MovieCrewPerson} or a new one
     */
    private MovieCrewPerson getOrCreateMovieCrewPerson(String moviePersonFullName, MovieCrewPersonType type) {
	MovieCrewPerson moviePerson = movieCrewPersonService.getMovieCrewPersonByName(moviePersonFullName);
	if (moviePerson == null) {
	    moviePerson = new MovieCrewPerson(moviePersonFullName, type);
	}

	return moviePerson;
    }

    /**
     * Gets the release date for the movie descriptor
     * 
     * @param doc
     *            the html document from which to get the info
     * @param movieDescriptor
     *            the {@link MovieDescriptor}
     * @throws ParseException
     * @throws InvalidMovieException
     */
    private void getReleaseDate(Document doc, MovieDescriptor movieDescriptor) throws InvalidMovieException {
	Elements movieDetailsDivs = doc.select("#titleDetails>div");
	for (int i = 0; i < movieDetailsDivs.size(); i++) {
	    if (movieDetailsDivs.get(i).text().startsWith("Release Date")) {
		String movieDetailsTitle = movieDetailsDivs.get(i).text();
		int indexOfColon = movieDetailsTitle.indexOf(":");
		int indexOfParanthesis = movieDetailsTitle.indexOf("(");

		String dateStr = movieDetailsTitle.substring(indexOfColon + 1, indexOfParanthesis)
			.replace(String.valueOf((char) 160), " ").trim();

		Date dateObject = null;
		try {
		    dateObject = (Date) new SimpleDateFormat("dd MMMMM yyyy").parseObject(dateStr);
		} catch (ParseException ex) {
		    try {
			dateObject = (Date) new SimpleDateFormat("MMMMM yyyy").parseObject(dateStr);
		    } catch (ParseException ex2) {
			try {
			    dateObject = (Date) new SimpleDateFormat("yyyy").parseObject(dateStr);
			} catch (ParseException ex3) {
			    throw new InvalidMovieException(Reason.MOVIE_NOT_RELEASED);
			}
		    }
		}

		DateMidnight releaseDateMidnight = new DateMidnight(dateObject);

		if (releaseDateMidnight.isAfter(DateMidnight.now())) {
		    throw new InvalidMovieException(Reason.MOVIE_NOT_RELEASED);
		} else {
		    movieDescriptor.setReleaseDate(dateObject);
		    movieDescriptor.setYear(releaseDateMidnight.getYear());
		}

		break;

	    }
	}
	
	// if no release date has been found take the year as a reference
	if (movieDescriptor.getReleaseDate() == null) {
	    Elements select = doc.select("#overview-top>h1>span.nobr");
	    if (select.size() == 1) {
		Element year = select.get(0);

		String yearString = year.text();
		Integer yearInt = Integer.parseInt(yearString.substring(1, yearString.length() - 1));
		movieDescriptor.setYear(yearInt);

		// set the date to 1st of january
		DateTime releaseDateMidnight = new DateTime(yearInt, 1, 1, 0, 0, 0, 0);
		movieDescriptor.setReleaseDate(releaseDateMidnight.toDate());

		if (releaseDateMidnight.isAfter(DateMidnight.now())) {
		    throw new InvalidMovieException(Reason.MOVIE_NOT_RELEASED);
		}
	    }
	}
    }

    /**
     * Gets the {@link Genre} for a movie given a string
     * 
     * @param genre
     *            the genre as a sting
     */
    private Genre getMovieGenre(String genre) {
	Genre imdbGenre = null;
	String trimmedGenre = genre.replace(String.valueOf((char) 160), " ").trim();

	if (trimmedGenre.equals("Film-Noir")) {
	    imdbGenre = Genre.FILM_NOIR;
	} else if (trimmedGenre.equals("Game-Show")) {
	    imdbGenre = Genre.GAME_SHOW;
	} else if (trimmedGenre.equals("Reality-TV")) {
	    imdbGenre = Genre.REALITY_TV;
	} else if (trimmedGenre.equals("Sci-Fi")) {
	    imdbGenre = Genre.SCI_FI;
	} else if (trimmedGenre.equals("Talk-Show")) {
	    imdbGenre = Genre.TALK_SHOW;
	} else {
	    try {
		imdbGenre = Genre.valueOf(trimmedGenre.toUpperCase());
	    } catch (IllegalArgumentException ex) {
		// only if the structure of the html from imdb changes
		throw new RuntimeException("The genre string " + genre + " is not a valid one");
	    }
	}

	return imdbGenre;
    }

    /**
     * Gets movie crew set
     * 
     * @param doc
     *            the document to query
     * @param selector
     *            the selector from the document
     * @return the set of {@link MovieCrewPerson}
     */
    private Set<MovieCrewPerson> getMovieCrew(Document doc, String selector, MovieCrewPersonType type) {
	Elements movieCrew = doc.select(selector);

	Set<MovieCrewPerson> crewPersons = new HashSet<MovieCrewPerson>();
	for (Element movieCrewPerson : movieCrew) {
	    String fullName = movieCrewPerson.text();

	    if (!fullName.isEmpty()) {
		MovieCrewPerson moviePerson = getOrCreateMovieCrewPerson(fullName, type);

		crewPersons.add(moviePerson);
	    }
	}
	return crewPersons;
    }

    /**
     * Gets movie rating
     * 
     * @param doc
     * @param selector
     * @return
     */
    private Double getMovieRating(Document doc, String selector) {
	Element ratingDiv = (doc.select(selector).size() > 0) ? doc.select(selector).get(0) : null;

	if (ratingDiv == null) {
	    return Double.valueOf(0);
	}
	return Double.parseDouble(ratingDiv.text().replace(",", "."));
    }

}
