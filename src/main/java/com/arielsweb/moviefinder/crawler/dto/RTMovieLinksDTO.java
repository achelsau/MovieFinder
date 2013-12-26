package com.arielsweb.moviefinder.crawler.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Encompanses the links
 * 
 * <pre>
 * "links": {
 * 	"self": "http://api.rottentomatoes.com/api/public/v1.0/movies/771305539.json",
 *      "alternate": "http://www.rottentomatoes.com/m/silent_hill_revelation/",
 *      "cast": "http://api.rottentomatoes.com/api/public/v1.0/movies/771305539/cast.json",
 *      "clips": "http://api.rottentomatoes.com/api/public/v1.0/movies/771305539/clips.json",
 *      "reviews": "http://api.rottentomatoes.com/api/public/v1.0/movies/771305539/reviews.json",
 *      "similar": "http://api.rottentomatoes.com/api/public/v1.0/movies/771305539/similar.json"
 * }
 * </pre>
 * 
 * @author Ariel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTMovieLinksDTO {
    /** the link to details about this movie (genre, cast, etc) **/
    private String self;

    /** the link pointing to the HTML page of this movie **/
    private String alternate;

    /** Default constructor **/
    public RTMovieLinksDTO() {
    }

    /**
     * Gets more JSON information about this movie such as cast, genre, etc
     * 
     * @return the URL to the API that returns detailed info
     */
    public String getSelf() {
	return self;
    }

    /**
     * Sets the URL returning JSON information about this movie such as cast,
     * genre, etc.
     * 
     * @param self
     *            the URL for the HTML page
     */
    public void setSelf(String self) {
	this.self = self;
    }

    /**
     * Gets the URL for the HTML page of this movie
     * 
     * @return the URL for the HTML page
     */
    public String getAlternate() {
	return alternate;
    }

    /**
     * Sets the URL for the HTML page of this movie
     * 
     * @param alternate
     *            the URL for the HTML page
     */
    public void setAlternate(String alternate) {
	this.alternate = alternate;
    }
}
