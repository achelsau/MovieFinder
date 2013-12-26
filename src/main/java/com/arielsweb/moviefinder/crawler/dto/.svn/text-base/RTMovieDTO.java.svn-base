package com.arielsweb.moviefinder.crawler.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a movie taken from the Rotten Tomatoes API by issuing a query
 * using
 * 
 * <pre>
 * http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=[your_api_key]&q=Jack&page_limit=1
 * </pre>
 * 
 * @author Ariel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTMovieDTO {
    /** the RT id **/
    private String id;
    /** the title of the movie that comes from the Rotten Tomatoes API **/
    private String title;
    /** the synopsis of the movies from the Rotten Tomatoes API **/
    private String synopsis;
    /** the consensus of the critics **/
    @JsonProperty("critics_consensus")
    private String criticsConsensus;
    /** the movie year of apparition **/
    private Integer year;
    /**
     * the object representing a bunch of links related to the current movie
     * (self information, reviews, etc.)
     **/
    private RTMovieLinksDTO links;

    /**
     * Default constructor
     */
    public RTMovieDTO() {
    }

    /**
     * Getters and setters
     */
    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getSynopsis() {
	return synopsis;
    }

    public void setSynopsis(String synopsis) {
	this.synopsis = synopsis;
    }

    public RTMovieLinksDTO getLinks() {
	return links;
    }

    public void setLinks(RTMovieLinksDTO links) {
	this.links = links;
    }

    public String getCriticsConsensus() {
	return criticsConsensus;
    }

    public void setCriticsConsensus(String criticsConsensus) {
	this.criticsConsensus = criticsConsensus;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

}
