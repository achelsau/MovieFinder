package com.arielsweb.moviefinder.service.exceptions;

/**
 * Signals that the index population cannot be performed if the index already
 * contains data
 * 
 * @author Ariel
 * 
 */
public class InvalidIndexPopulationException extends Exception {

	private static final long serialVersionUID = -2422320379942244297L;

	/**
	 * Creates a new exception
	 * 
	 * @param message
	 *            the message to display to the user
	 */
	public InvalidIndexPopulationException(String message) {
		super(message);
	}
}
