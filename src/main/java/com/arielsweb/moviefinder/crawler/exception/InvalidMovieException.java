package com.arielsweb.moviefinder.crawler.exception;

/**
 * Denotes a movie that couldn't be added into the DB
 * 
 * @author Ariel
 * 
 */
public class InvalidMovieException extends Exception {

	private static final long serialVersionUID = -8405642458855372939L;

	public enum Reason {
		MOVIE_NOT_RELEASED
	}

	private Reason reason;

	public InvalidMovieException(String msg) {
		super(msg);
	}

	public InvalidMovieException(Reason reason) {
		this.reason = reason;
	}

	/**
	 * @return the reason
	 */
	public Reason getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(Reason reason) {
		this.reason = reason;
	}
}
