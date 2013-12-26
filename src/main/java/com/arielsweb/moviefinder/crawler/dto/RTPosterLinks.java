package com.arielsweb.moviefinder.crawler.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * The poster links (images) for a movie. Nice to have when in need to display
 * the movie in a list of results
 * 
 * @author Ariel
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RTPosterLinks {
    private String thumbnail;

    public RTPosterLinks() {
    }

    public String getThumbnail() {
	return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
	this.thumbnail = thumbnail;
    }
}