package com.arielsweb.moviefinder.crawler.dto;

import java.util.Date;
import java.util.List;

/**
 * Models a movie object
 * 
 * @author Ariel
 *
 */
public class MovieDTO {
    private String id;
    
    /** the name of the movie taken from the website **/
    private String name;
    
    /** the year of the movie taken from the website **/
    private Date releaseDate;
    
    /** the path from the website where the movie can be found **/
    private String remotePath;
    
    /** the description provided by the website **/
    private String synopsis;
    
    /** the url to the image/poster of the movie **/
    private String imagePath;
    
    /** the full names of actors of this movie **/
    private List<String> actors;
    
    /** the list of full names of directors for this movie **/
    private List<String> directors;

    /** the list **/
    private List<String> screenwriters;

    /**
     * Creates a new movie dto
     */
    public MovieDTO() {

    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the year
     */
    public Date getReleaseDate() {
	return releaseDate;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setReleaseDate(Date year) {
	this.releaseDate = year;
    }

    /**
     * @return the remotePath
     */
    public String getRemotePath() {
	return remotePath;
    }

    /**
     * @param remotePath
     *            the remotePath to set
     */
    public void setRemotePath(String remotePath) {
	this.remotePath = remotePath;
    }

    /**
     * @return the synopsis
     */
    public String getSynopsis() {
	return synopsis;
    }

    /**
     * @param synopsis
     *            the synopsis to set
     */
    public void setSynopsis(String synopsis) {
	this.synopsis = synopsis;
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

    /**
     * @return the actors
     */
    public List<String> getActors() {
	return actors;
    }

    /**
     * @param actors
     *            the actors to set
     */
    public void setActors(List<String> actors) {
	this.actors = actors;
    }

    /**
     * @return the directors
     */
    public List<String> getDirectors() {
	return directors;
    }

    /**
     * @param directors
     *            the directors to set
     */
    public void setDirectors(List<String> directors) {
	this.directors = directors;
    }

    /**
     * @return the screenwriters
     */
    public List<String> getScreenwriters() {
	return screenwriters;
    }

    /**
     * @param screenwriters
     *            the screenwriters to set
     */
    public void setScreenwriters(List<String> screenwriters) {
	this.screenwriters = screenwriters;
    }

}
