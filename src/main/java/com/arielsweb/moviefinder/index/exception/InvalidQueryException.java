package com.arielsweb.moviefinder.index.exception;

/**
 * Custom exception when trying to retrieve results from an empty list
 * 
 * @author Ariel
 *
 */
public class InvalidQueryException extends RuntimeException {

	private static final long serialVersionUID = 2673687504388938550L;

	/**
	 * Reason for which a query can be invalid
	 * 
	 * @author Ariel
	 * 
	 */
	public enum Reason {
		/** Reason thrown when a query string is null **/
		QUERY_CANNOT_BE_NULL,

		/** Reason thrown when a query string is empty **/
		QUERY_CANNOT_BE_EMPTY
	}

	/** reason of the exception **/
	private Reason reason;

	public InvalidQueryException(Reason reason) {
		this.reason = reason;
	}
}
