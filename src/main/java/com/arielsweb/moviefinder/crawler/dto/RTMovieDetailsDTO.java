package com.arielsweb.moviefinder.crawler.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents details related to a movie that are taken using
 * 
 * <pre>
 * http://api.rottentomatoes.com/api/public/v1.0/movies/770672122.json?apikey=[your_api_key]
 * </pre>
 * 
 * @author Ariel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTMovieDetailsDTO {
    private String[] genres;
    
    private RTPosterLinks posters;

    @JsonProperty("alternate_ids")
    private RTAlternateIds alternateIds;

    /**
     * Default constructor
     */
    public RTMovieDetailsDTO() {

    }

    /**
     * Getters & setters
     */
    public String[] getGenres() {
	return genres;
    }

    public void setGenres(String[] genres) {
	this.genres = genres;
    }

    public RTPosterLinks getPosters() {
	return posters;
    }

    public void setPosters(RTPosterLinks posters) {
	this.posters = posters;
    }

    public RTAlternateIds getAlternateIds() {
	return alternateIds;
    }

    public void setAlternateIds(RTAlternateIds alternateIds) {
	this.alternateIds = alternateIds;
    }
}
