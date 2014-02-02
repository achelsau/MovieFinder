package com.arielsweb.moviefinder.index.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wraps up information related to a result obtained after running a query.
 * 
 * @since 24/05/2010
 * @author Ariel
 */
@XmlRootElement(name = "resultInfo")
public class ResultInfo implements Comparable<ResultInfo> {
    /** the score of the result **/
    private Float score;
    /**
     * the title of the movie, its description (synopsis), its source (website)
     * and the URL where it can be accessed
     **/
    private String movieTitle, description, source, remotePath, remotePicture;

    private Date releaseDate;

    /**
     * the database id of the movie
     */
    private Long movieId;

    public ResultInfo() {
	// default ctor
    }

    public ResultInfo(String title, String description) {
	this.movieTitle = title;
	this.description = description;
    }

    public Float getScore() {
	return score;
    }

    public void setScore(Float score) {
	this.score = score;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTitle() {
	return movieTitle;
    }

    public void setTitle(String movieTitle) {
	this.movieTitle = movieTitle;
    }

    public Long getId() {
	return movieId;
    }

    public void setId(Long movieId) {
	this.movieId = movieId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String owner) {
        this.source = owner;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    /**
     * @return the releaseDate
     */
    public Date getReleaseDate() {
	return releaseDate;
    }

    /**
     * @param releaseDate
     *            the releaseDate to set
     */
    public void setReleaseDate(Date releaseDate) {
	this.releaseDate = releaseDate;
    }

    /**
     * @return the remotePicture
     */
    public String getRemotePicture() {
	return remotePicture;
    }

    /**
     * @param remotePicture
     *            the remotePicture to set
     */
    public void setRemotePicture(String remotePicture) {
	this.remotePicture = remotePicture;
    }

    @Override
    public int compareTo(ResultInfo o) {
	if (this.score > o.score) {
	    return -1;
	} else if (this.score < o.score) {
	    return 1;
	}
	return 0;
    }
    
    public String toString() {
	return score + "";
    }
}
