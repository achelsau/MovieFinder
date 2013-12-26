package com.arielsweb.moviefinder.crawler.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Holds the alternative ids of a movie (such as IMDB id)
 * 
 * @author Ariel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTAlternateIds {
    /** the imdb id of a RT movie **/
    private String imdb;

    public RTAlternateIds() {
    }

    public String getImdb() {
	return imdb;
    }

    public void setImdb(String imdb) {
	this.imdb = imdb;
    }
}