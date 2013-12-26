package com.arielsweb.moviefinder.crawler.dto;

/**
 * Object for abstracting links received as a JSON response from a paginated API
 * 
 * <pre>
 * "links": {
 *         "self": "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=10&country=us&page=1",
 *         "next": "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=10&country=us&page=2",
 *         "alternate": "http://www.rottentomatoes.com/movie/in_theaters.php"
 *     }
 * </pre>
 * 
 * @author Ariel
 * 
 */
public class RTResultLinksDTO {
    private String self, next, alternate, prev;

    /**
     * Default constructor
     */
    public RTResultLinksDTO() {
    }

    /**
     * Getters and setters
     */
    public String getSelf() {
	return self;
    }

    public void setSelf(String self) {
	this.self = self;
    }

    public String getNext() {
	return next;
    }

    public void setNext(String next) {
	this.next = next;
    }

    public String getAlternate() {
	return alternate;
    }

    public void setAlternate(String alternate) {
	this.alternate = alternate;
    }

    public String getPrev() {
	return prev;
    }

    public void setPrev(String prev) {
	this.prev = prev;
    }
}
