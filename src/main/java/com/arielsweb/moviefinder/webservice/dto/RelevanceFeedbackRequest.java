package com.arielsweb.moviefinder.webservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Request wrapping the persistent query and also the id of the liked movie
 * 
 * @author Ariel
 */
@XmlRootElement
public class RelevanceFeedbackRequest {

    private String persistentQueryId;

    private String relevantMovieId;

    public RelevanceFeedbackRequest() {

    }

    public RelevanceFeedbackRequest(String persistentQueryId, String relevantMovieId) {
	this.persistentQueryId = persistentQueryId;
	this.relevantMovieId = relevantMovieId;
    }

    /**
     * @return the persistentQuery
     */
    public String getPersistentQueryId() {
	return persistentQueryId;
    }

    /**
     * @param persistentQuery
     *            the persistentQuery to set
     */
    public void setPersistentQueryId(String persistentQueryId) {
	this.persistentQueryId = persistentQueryId;
    }

    /**
     * @return the relevantMovieId
     */
    public String getRelevantMovieId() {
	return relevantMovieId;
    }

    /**
     * @param relevantMovieId
     *            the relevantMovieId to set
     */
    public void setRelevantMovieId(String relevantMovieId) {
	this.relevantMovieId = relevantMovieId;
    }
}
