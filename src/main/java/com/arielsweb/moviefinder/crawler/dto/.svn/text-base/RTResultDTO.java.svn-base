package com.arielsweb.moviefinder.crawler.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Abstract a result object that contains the number of total results, the
 * results returned and total number. This object is populated using
 * 
 * <pre>
 * http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=[your_api_key]&q=Jack&page_limit=1
 * Result:
 * {
 * total:591,
 * results:[{movie1}, {movie2}, .... , {movie591}]
 * }
 * 
 * <pre>
 * 
 * @author Ariel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTResultDTO {
    private Integer total;
    private List<RTMovieDTO> movies;
    private RTResultLinksDTO links;

    /**
     * Default constructor
     */
    public RTResultDTO() {
    }

    /**
     * Getters and setters
     */
    public Integer getTotal() {
	return total;
    }

    public void setTotal(Integer total) {
	this.total = total;
    }

    public List<RTMovieDTO> getMovies() {
	return movies;
    }

    public void setMovies(List<RTMovieDTO> movies) {
	this.movies = movies;
    }

    public RTResultLinksDTO getLinks() {
	return links;
    }

    public void setLinks(RTResultLinksDTO links) {
	this.links = links;
    }
}
