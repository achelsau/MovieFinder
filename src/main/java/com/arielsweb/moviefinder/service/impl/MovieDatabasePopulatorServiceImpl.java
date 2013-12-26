package com.arielsweb.moviefinder.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arielsweb.moviefinder.crawler.dto.RTAlternateIds;
import com.arielsweb.moviefinder.crawler.dto.RTMovieDTO;
import com.arielsweb.moviefinder.crawler.dto.RTMovieDetailsDTO;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDatabasePopulatorService;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Populates the database with {@link MovieDescriptor}s. Once it gets the
 * info from the APIs, it populates the model object and stores it into the
 * database.
 * 
 * @author Ariel
 * 
 */
@Service("movieDatabasePopulatorServiceImpl")
public class MovieDatabasePopulatorServiceImpl implements MovieDatabasePopulatorService {

    @Autowired
    private MovieSourceService movieSourceService;

    @Autowired
    private MovieDescriptorService movieDescriptorService;

    /** The logger. */
    protected org.apache.log4j.Logger log = Logger.getLogger(MovieDatabasePopulatorServiceImpl.class);

    private static int movieNo = 0;

    /**
     * For a specific population run, either take a fresh copy of the
     * {@link MovieSource} from the database or insert a new one.
     * 
     * @param sourceName
     *            the name of the source from where to get the movies
     * @param url
     *            the url of the source (http://www.imdb.com,
     *            http://www.rottentomatoes.com, etc)
     * 
     * @return the movie source to which the movie is linked to
     */
    @Override
    public MovieSource getMovieSource(String sourceName, String url) {
	List<MovieSource> sources = movieSourceService.list();
	MovieSource source = null;
	if (sources.size() == 1) {
	    source = sources.get(0);
	} else {
	    source = new MovieSource();
	    source.setLocation(url);
	    source.setName(sourceName);
	    movieSourceService.save(source);
	}

	return source;
    }

    /**
     * Inserts the movie into the database
     * 
     * @param rtMovieDTO
     *            high-level details about the movie (name, year, synopsis, etc)
     * @param rtMovieDetail
     *            more in depth details about the movie (genre, posters, cast,
     *            etc)
     * @param source
     *            the movie source (website) to be inserted
     */
    @Override
    public void insertMovieIntoDb(RTMovieDTO rtMovieDTO, RTMovieDetailsDTO rtMovieDetails, MovieSource source) {
	boolean hasSynopsis = rtMovieDTO.getSynopsis() != null && !rtMovieDTO.getSynopsis().isEmpty();
	boolean hasCriticsConsensus = rtMovieDTO.getCriticsConsensus() != null
		&& !rtMovieDTO.getCriticsConsensus().isEmpty();
	if (!hasSynopsis && !hasCriticsConsensus) {
	    log.warn("Didn't add movie " + rtMovieDTO.getTitle() + " because of no info");

	    return; // skips those without a synopsis or critics
		    // consensus; they have no value for relevance
		    // feedback (or even search)
	}

	MovieDescriptor movieDescriptor = new MovieDescriptor();
	if (hasSynopsis && hasCriticsConsensus) {
	    movieDescriptor.setSynopsis(rtMovieDTO.getSynopsis());
	    movieDescriptor.setAlternateSynopsis(rtMovieDTO.getCriticsConsensus());
	} else if (hasSynopsis) {
	    movieDescriptor.setSynopsis(rtMovieDTO.getSynopsis());
	} else if (hasCriticsConsensus) {
	    movieDescriptor.setSynopsis(rtMovieDTO.getCriticsConsensus());
	}
	movieDescriptor.setName(rtMovieDTO.getTitle());
	movieDescriptor.setRemotePath(rtMovieDTO.getLinks().getAlternate());
	movieDescriptor.setYear(rtMovieDTO.getYear());
	movieDescriptor.setRemoteId(rtMovieDTO.getId());
	movieDescriptor.setSource(source);

	populateMovieDetails(movieDescriptor, rtMovieDetails);
	movieDescriptorService.save(movieDescriptor);

	log.warn("Added movie " + rtMovieDTO.getTitle() + ", no. " + (++movieNo));
    }

    /**
     * Populates the details of a found movie
     * 
     * @param movieDescriptor
     *            the database entity {@link MovieDescriptor}
     * @param rtMovieDetailsDTO
     *            the DTO received from the API
     */
    private void populateMovieDetails(MovieDescriptor movieDescriptor, RTMovieDetailsDTO rtMovieDetailsDTO) {
	Genre[] genres = new Genre[rtMovieDetailsDTO.getGenres().length];
	int i = 0;
	for (String genre : rtMovieDetailsDTO.getGenres()) {
	    if (genre.equals("Action & Adventure")) {
		genres[i] = Genre.ACTION_ADVENTURE;
	    } else if (genre.equals("Anime & Manga")) {
		genres[i] = Genre.ANIME_MANGA;
	    } else if (genre.equalsIgnoreCase("ART HOUSE & INTERNATIONAL")) {
		genres[i] = Genre.ART_HOUSE_INTERNATIONAL;
	    } else if (genre.equalsIgnoreCase("Cult Movies")) {
		genres[i] = Genre.CULT_MOVIES;
	    } else if (genre.equalsIgnoreCase("Faith & Spirituality")) {
		genres[i] = Genre.FAITH_SPIRITUALITY;
	    } else if (genre.equalsIgnoreCase("Gay & Lesbian")) {
		genres[i] = Genre.GAY_LESBIAN;
	    } else if (genre.equals("Kids & Family")) {
		genres[i] = Genre.KIDS_FAMILY;
	    } else if (genre.equalsIgnoreCase("MUSICAL & PERFORMING ARTS")) {
		genres[i] = Genre.MUSICAL_PERFORMING_ARTS;
	    } else if (genre.equals("Mystery & Suspense")) {
		genres[i] = Genre.MYSTERY_SUSPENSE;
	    } else if (genre.equals("Science Fiction & Fantasy")) {
		genres[i] = Genre.SCIENCE_FICTION_FANTASY;
	    } else if (genre.equalsIgnoreCase("SPECIAL INTEREST")) {
		genres[i] = Genre.SPECIAL_INTEREST;
	    } else if (genre.equalsIgnoreCase("SPORTS & FITNESS")) {
		genres[i] = Genre.SPORTS_FITNESS;
	    } else {
		genres[i] = Genre.valueOf(genre.toUpperCase());
	    }

	    i++;
	}

	movieDescriptor.setGenres(new HashSet<Genre>(Arrays.asList(genres)));
	RTAlternateIds alternateIds = rtMovieDetailsDTO.getAlternateIds();
	if (alternateIds != null) {
	    movieDescriptor.setAlternateId(alternateIds.getImdb());
	}
	movieDescriptor.setImagePath(rtMovieDetailsDTO.getPosters().getThumbnail());
    }

    @Override
    public void setMovieSourceService(MovieSourceService movieSourceService) {
	this.movieSourceService = movieSourceService;
    }

    @Override
    public void setMovieDescriptorService(MovieDescriptorService movieDescriptorService) {
	this.movieDescriptorService = movieDescriptorService;
    }
}
