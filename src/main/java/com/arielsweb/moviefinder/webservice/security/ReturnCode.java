package com.arielsweb.moviefinder.webservice.security;

/**
 * Defines some generic return codes for each WS operation
 * 
 * When a problem occurs for the WS it returns a HTTP code and another return
 * code for displaying the proper i18n error message.
 * 
 * @author Ariel
 * 
 */
public class ReturnCode {
    
    // 1xx - Indexing Engine Errors

    // 2xx - Query Engine Errors
    public static final String QUERY_NULL_OR_EMPTY = "201"; // query is null or
							    // empty
    public static final String MALFORMED_QUERY_ID = "202"; // query id is
							    // malformed

    // 3xx - Relevance Feedback Errors

}
