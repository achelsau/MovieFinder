package com.arielsweb.moviefinder.index.dto;

import java.io.Serializable;
import java.util.Set;

import com.arielsweb.moviefinder.model.Genre;

/**
 * <pre>
 * The index is represented as <String, IndexEntry> where:
 *    1. String represents the word token from the synopsis to be indexed
 *    2. {@link IndexEntry} offers a description of the word token meaning a IDF (inverse document frequency), 
 *       and a map of Posting(s) in the form <id, {tf, genres}>
 * The pair <tf, genres> is represented by this object.
 * </pre>
 * 
 * @since 2/05/2010
 * @author Ariel
 */
public class Posting implements Serializable {

    private static final long serialVersionUID = -2542266694500967683L;

    /** the frequency of the term appearing in a movie synopsis (posting) **/
    private Short tf;
    /** the genres of the movie described by the synopsis being indexed **/
    private Set<Genre> genres;

    public Posting() {
	// default constructor
    }

    /**
     * Creates a new posting for a word token. The posting is of the form {tf,
     * genres}
     * 
     * @param tf
     *            the term frequency of the word token
     * @param genre
     *            the genre of the movie's synopsis to which the word token
     *            belongs
     */
    public Posting(Short tf, Set<Genre> genres) {
	super();
	this.tf = tf;
	this.genres = genres;
    }

    /**
     * @return the tf
     */
    public Short getTf() {
	return tf;
    }

    /**
     * @param tf
     *            the tf to set
     */
    public void setTf(Short tf) {
	this.tf = tf;
    }

    public Set<Genre> getGenres() {
	return genres;
    }

    public void setGenres(Set<Genre> genres) {
	this.genres = genres;
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("[ ");
	for (Genre genre : genres) {
	    sb.append(genre + " ");
	}
	sb.append("]");
	
	return "{ " + tf + ", " + sb.toString() + " }";
    }

}
