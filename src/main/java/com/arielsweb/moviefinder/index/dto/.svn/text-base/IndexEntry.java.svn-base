package com.arielsweb.moviefinder.index.dto;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <pre>
 * The index is represented as <String, IndexEntry> where:
 *    1. String represents the word token from the synopsis to be indexed
 *    2. {@link IndexEntry} offers a description of the word token meaning a IDF (inverse document frequency), 
 *       and a map of Posting(s) in the form <id, {tf, genre}>
 * </pre>
 * 
 * @since 2/05/2010
 * @author Ariel
 */
public class IndexEntry implements Serializable {

    private static final long serialVersionUID = -459172595470854744L;
    /**
     * a mapping between the id of the document and the tf of the term
     **/
    private HashMap<Long, Posting> postings;
    private float idf;

    public IndexEntry() {
	postings = new HashMap<Long, Posting>();
    }

    /**
     * Links a posting to a word.
     * 
     * @param movieId
     *            the id of the document being linked to a specific word
     * @param posting
     *            the {@link Posting} containing the <tf and genre>
     */
    public void addPosting(Long movieId, Posting posting) {
	postings.put(movieId, posting);
    }

    /**
     * Gets a mapping between the id of the document and the tf of the term
     * 
     * @return the postings
     */
    public HashMap<Long, Posting> getPostings() {
	return postings;
    }

    /**
     * Sets the Map of <movieId, {tf, genre}>
     * 
     * @param postings
     *            the postings to set
     */
    public void setPostings(HashMap<Long, Posting> postings) {
	this.postings = postings;
    }

    /**
     * @return the inverse document frequency
     */
    public float getIdf() {
	return idf;
    }

    /**
     * @param idf
     *            the inverse document frequency to set
     */
    public void setIdf(float wf) {
	this.idf = wf;
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(this.idf + "\n");

	String[] docKeys = new String[1];
	docKeys = this.postings.keySet().toArray(docKeys);
	for (int i = 0; i < docKeys.length; i++) {
	    sb.append(docKeys[i] + "->" + this.postings.get(docKeys[i]) + " ");
	}
	return sb.toString();
    }
}
