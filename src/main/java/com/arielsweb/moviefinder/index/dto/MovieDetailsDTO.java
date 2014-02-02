package com.arielsweb.moviefinder.index.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * The index is represented as <String, IndexEntry> where:
 *    1. String represents the word token from the synopsis to be indexed
 *    2. {@link IndexEntry} offers a description of the work token meaning a IDF (inverse document frequency), 
 *       and a map of Posting(s) in the form <id, {tf, genre}>
 * 
 * This object offers details for a movie descriptor. Because all those details can't be put into the index, 
 * an additional list of these objects is kept into memory to avoid calling the database.
 * </pre>
 * 
 * @author Ariel
 * @data 28/12/2011
 * 
 */
public class MovieDetailsDTO implements Serializable {

    private static final long serialVersionUID = 5787075779598588947L;

    private short termCount;
    private float score;
    private String title, desc, source, path, imagePath;
    private Date releaseDate;

    /**
     * Creates the details about a movie from the online index
     * 
     * @param desc
     *            the textual description
     * @param source
     *            the name of the source (website) from which the movie was
     *            taken
     * @param path
     *            the path from the owner's computers
     */
    public MovieDetailsDTO(String title, String desc, String source, String path, Date releaseDate, String imagePath) {
	super();
	this.title = title;
	this.desc = desc;
	this.source = source;
	this.path = path;
	this.releaseDate = releaseDate;
	this.imagePath = imagePath;
    }

    public short getTermCount() {
	return termCount;
    }

    public void setTermCount(short termCount) {
	this.termCount = termCount;
    }

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String owner) {
	this.source = owner;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public float getScore() {
	return score;
    }

    public void setScore(float score) {
	this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
	return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
	this.releaseDate = releaseDate;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
	return imagePath;
    }

    /**
     * @param imagePath
     *            the imagePath to set
     */
    public void setImagePath(String imagePath) {
	this.imagePath = imagePath;
    }
}
