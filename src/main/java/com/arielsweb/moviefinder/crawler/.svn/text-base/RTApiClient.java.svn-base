package com.arielsweb.moviefinder.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.crawler.dto.RTMovieDTO;
import com.arielsweb.moviefinder.crawler.dto.RTMovieDetailsDTO;
import com.arielsweb.moviefinder.crawler.dto.RTResultDTO;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDatabasePopulatorService;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * Rotten Tomatoes API client to list all movies and index their synopsis
 * 
 * @author Ariel
 * 
 */
@Component("rtApiClient")
public class RTApiClient {

    private static final int MOVIE_LIMIT = 5000;

    /** The logger. */
    protected org.apache.log4j.Logger log = Logger.getLogger(RTApiClient.class);

    @Autowired
    private MovieDatabasePopulatorService databasePopulatorService;
    @Autowired
    private MovieDescriptorService movieDescriptorService;

    private ObjectMapper mapper = new ObjectMapper();
    private int movieNo = 0;

    public RTApiClient() {
    }

    /**
     * Browses all the paginated API with movies that are "In theaters".
     * 
     * @throws IOException
     */
    public void browseInTheaters() throws IOException {
	MovieSource source = databasePopulatorService
		.getMovieSource("Rotten Tomatoes", "http://www.rottentomatoes.com");

	RTResultDTO rtResultDTO = null;
	long start = System.currentTimeMillis();
	String[] queries = {
		"	Jaws ",
		"	Seven Samurai (Shichinin no Samurai) ",
		"	The Terminator ",
		"	The Adventures of Robin Hood ",
		"	The Treasure of the Sierra Madre ",
		"	The Searchers ",
		"	The African Queen ",
		"	Mad Max : The Road Warrior ",
		"	Stagecoach ",
		"	Rio Bravo ",
		"	Henry V ",
		"	The Killer (Dip huet seung hung) ",
		"	The Taking of Pelham One Two Three ",
		"	Sullivan's Travels ",
		"	Un condamn� � mort s'est �chapp� ou Le vent souffle o� il veut (A Man Escaped) ",
		"	White Heat ",
		"	Hell and Back Again ",
		"	One False Move ",
		"	The Public Enemy ",
		"	Captain Blood ",
		"	Henry V (The Chronicle History of King Henry the Fift with His Battell Fought at Agincourt in France ",
		"	Homicide: The Movie ", "	National Velvet ", "	Racing Dreams ", "	The Ipcress File ",
		"	The Thief of Bagdad ", "	Kiki's Delivery Service ", "	My Darling Clementine ",
		"	The Hidden Fortress (kakushi-toride No San-akunin) ", "	Sanjuro (Tsubaki Sanj�r�) ",
		"	To Have and Have Not ", "	Angels with Dirty Faces ", "	Metropolis ", "	Apocalypse Now ",
		"	The Twilight Samurai (Tasogare Seibei) ", "	Up ", "	Lawrence of Arabia ", "	Ten Canoes ", "	Babe ",
		"	King Kong ", "	Once Upon a Time in the West ", "	Aliens ", "	The French Connection ", "	Blindsight ",
		"	Mountain Patrol: Kekexili ", "	Who Framed Roger Rabbit ", "	Dr. No ", "	Terminator : Judgment Day ",
		"	Badlands ", "	A Fistful of Dollars (Per un Pugno di Dollari) ", "	The  Steps ",
		"	The Secret of Roan Inish ", "	The Hurt Locker ", "	Crouching Tiger, Hidden Dragon ",
		"	Star Wars: Episode V - The Empire Strikes Back ", "	Apollo  ",
		"	The Good, the Bad and the Ugly (Il Buono, il Brutto, il Cattivo.) ", "	Back to the Future ",
		"	Full Metal Jacket ", "	Ran ", "	The Right Stuff ", "	The Wild Bunch ",
		"	Kumonosu J� (Throne of Blood) (Macbeth) ", "	Yojimbo ", "	Cell  (Celda ) ",
		"	The Last of the Mohicans ", "	Assault on Precinct  ", "	Bullitt ",
		"	The Man Who Shot Liberty Valance ", "	Aguirre, der Zorn Gottes (Aguirre, the Wrath of God) ",
		"	Wings ", "	Major Dundee ", "	WarGames (War Games) ", "	Bob le Flambeur (Bob the Gambler) ",
		"	No Way Out ", "	Stalag  ", "	Harry Potter and the Deathly Hallows - Part  ", "	True Grit ",
		"	WALL-E ", "	The Lord of the Rings: The Two Towers ", "	Catch Me If You Can ", "	Chicken Run ",
		"	 Assassins ", "	Goldfinger ", "	The Princess Bride ", "	Reservoir Dogs ", "	Spartacus ",
		"	The Bridge on the River Kwai ", "	From Russia With Love ",
		"	Triad Election (Hak se wui yi wo wai kwai) ", "	Yellow Submarine ", "	Diva ",
		"	Gun Crazy (Deadly Is the Female) ", "	Johnny Guitar ",
		"	Mesrine: Part  (Killer Instinct) and Part  (Public Enemy #) ", "	Little Big Man ", "	Midnight Run ",
		"	Shifty ", "	The Man Who Would Be King ", "	The Big Easy ", };

	// get 50 * 100 pages = 5000 movies
	for (String q : queries) {
	    String link = MovieFinderConstants.RT_BASE_URL + "movies.json?q=" + q.trim() + "&page_limit=50&apikey="
		    + MovieFinderConstants.RT_API_KEY;
	    int page = 1;
	    // do {
	    link = link.replaceAll(" ", "%20");
	    link = link.replaceAll("#", "%23");
	    rtResultDTO = connectAndBrowseApi(link);

	    if (rtResultDTO == null || rtResultDTO.getLinks() == null) {
		break;
	    }

	    link = rtResultDTO.getLinks().getNext();

	    for (RTMovieDTO rtMovieDTO : rtResultDTO.getMovies()) {
		insertMoviesIntoDb(rtMovieDTO, source);
	    }

	    if (link != null) {
		link = link.concat("&apikey=" + MovieFinderConstants.RT_API_KEY);

		log.warn("Brought page no " + page);
		page++;
	    }

	    // } while (page < 26 && link != null);
	}

	long stop = System.currentTimeMillis();
	log.warn(stop - start);
    }

    /**
     * Takes a movie DTO, extracts further details and then populates and saves
     * the DB model
     * 
     * @param rtMovieDTO
     *            the movie returned as a result of a query
     * @param source
     *            the owner (website) of the movie
     * @throws IOException
     */
    private void insertMoviesIntoDb(RTMovieDTO rtMovieDTO, MovieSource source) throws IOException {
	String movieTitle = rtMovieDTO.getTitle();
	String movieYear = (rtMovieDTO.getYear() == null) ? null : rtMovieDTO.getYear().toString();
	boolean isUnique = movieDescriptorService.isUnique(rtMovieDTO.getId(), source.getId(),
		movieTitle, movieYear);

	if (!isUnique) {
	    log.warn("Didn't add movie " + rtMovieDTO.getTitle() + ", because it's aleady added");
	    return; // skip duplicates
	}

	RTMovieDetailsDTO rtMovieDetailsDTO = connectToGetMovieDetails(rtMovieDTO.getLinks().getSelf() + "?apikey="
		+ MovieFinderConstants.RT_API_KEY);

	databasePopulatorService.insertMovieIntoDb(rtMovieDTO, rtMovieDetailsDTO, source);

	movieNo++;
	if (movieNo > MOVIE_LIMIT) {
	    return;
	}
    }



    /**
     * Connects to the list movies API to create a baseline for the index
     * 
     * @throws IOException
     */
    private RTResultDTO connectAndBrowseApi(String url) throws IOException {
	URL queryUrl = new URL(url);

	HttpURLConnection listMoviesConnection = (HttpURLConnection) queryUrl.openConnection();
	listMoviesConnection.setRequestMethod("GET");

	if (listMoviesConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new RuntimeException("Failed : " + url + " HTTP error code : "
		    + listMoviesConnection.getResponseCode());
	}

	// verify the success of the request
	log.info(listMoviesConnection.getResponseCode() + ", " + listMoviesConnection.getResponseMessage());

	return browseApi(listMoviesConnection);
    }

    /**
     * Connects to the list movies API to create a baseline for the index
     * 
     * @throws IOException
     */
    private RTMovieDetailsDTO connectToGetMovieDetails(String url) throws IOException {
	URL queryUrl = new URL(url);

	HttpURLConnection getMovieDetailsConnection = (HttpURLConnection) queryUrl.openConnection();
	getMovieDetailsConnection.setRequestMethod("GET");

	if (getMovieDetailsConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    throw new RuntimeException("Failed : " + url + " HTTP error code : "
		    + getMovieDetailsConnection.getResponseCode());
	}

	// verify the success of the request
	log.info(getMovieDetailsConnection.getResponseCode() + ", " + getMovieDetailsConnection.getResponseMessage());

	return getMovieDetailsInfo(getMovieDetailsConnection);
    }

    /**
     * Browses the listing of movies
     * 
     * @throws IOException
     */
    private RTMovieDetailsDTO getMovieDetailsInfo(HttpURLConnection getMovieDetailsConnection) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader((getMovieDetailsConnection.getInputStream())));
	log.info("Output from Server .... \n");
	String output = null;
	RTMovieDetailsDTO movieDetails = null;
	while ((output = br.readLine()) != null) {
	    if (output == null || output.length() == 0) {
		continue;
	    }

	    log.info(output);
	    movieDetails = mapper.readValue(output, RTMovieDetailsDTO.class);
	    break;
	}

	getMovieDetailsConnection.disconnect();

	return movieDetails;
    }

    /**
     * Browses the listing of movies
     * 
     * @throws IOException
     */
    private RTResultDTO browseApi(HttpURLConnection listMoviesConnection) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader((listMoviesConnection.getInputStream())));
	log.info("Output from Server .... \n");
	String output = null;
	RTResultDTO result = null;
	while ((output = br.readLine()) != null) {
	    if (output == null || output.length() == 0) {
		continue;
	    }

	    log.info(output);
	    result = mapper.readValue(output, RTResultDTO.class);
	    break;
	}

	listMoviesConnection.disconnect();

	return result;
    }
}
