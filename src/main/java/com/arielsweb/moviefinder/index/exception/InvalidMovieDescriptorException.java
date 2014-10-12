package com.arielsweb.moviefinder.index.exception;

import com.arielsweb.moviefinder.index.impl.InvertedIndexEngine;
import com.arielsweb.moviefinder.model.MovieDescriptor;

/**
 * Exception thrown when a {@link MovieDescriptor} to be indexed by
 * {@link InvertedIndexEngine#addEntry(MovieDescriptor)} is invalid.
 * 
 * @author Ariel
 * 
 */
public class InvalidMovieDescriptorException extends Exception {

	private static final long serialVersionUID = 6522391925784456445L;

	/**
	 * Denotes the reason why InvalidMovieDescriptor exception ocurred
	 * 
	 * @author Ariel
	 * 
	 */
	public enum Reason {
		MOVIE_DESCRIPTOR_IS_NULL,

		MOVIE_DESCRIPTOR_DOES_NOT_EXIST,

		MOVIE_DESCRIPTOR_WITH_EMPTY_SYNOPSIS
	}

	private Reason reason;

	public InvalidMovieDescriptorException(Reason reason) {
		this.reason = reason;
	}

	/**
	 * Getter and setter for the reason
	 */

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}
}
