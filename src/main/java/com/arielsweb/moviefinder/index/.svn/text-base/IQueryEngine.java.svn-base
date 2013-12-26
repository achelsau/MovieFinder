package com.arielsweb.moviefinder.index;

import java.util.List;
import java.util.Map;

import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.exception.InvalidQueryException;

/**
 * Defines generic methods for the query engine. Methods such as computing query
 * suggestions and for correcting spelling errors
 * 
 * @author Ariel
 * @date 27/12/2011
 * 
 */
public interface IQueryEngine {

    /**
     * Runs the query against the index
     * 
     * @param queryWeights
     *            the map containing <word, weight> pairs
     * @return a list of pairs of <Id of the movie, {@link ResultInfo}> for the
     *         results
     */
    List<ResultInfo> queryIndex(Map<String, Float> queryWeights) throws InvalidQueryException;
    
    /**
     * Runs the query against the index
     * 
     * @param query
     *            the query in natural language; for example: "moon landing"
     * @return a list of pairs of <Id of the movie, {@link ResultInfo}> for the
     *         results
     */
    List<ResultInfo> queryIndex(String query) throws InvalidQueryException;

    /**
     * Sets the flag indicating that the score should be normalized against the length of the document
     * 
     * @param normalizeScoreToDocumentLength 
     * 		whether to divide the score by the length of the document or not
     */
    void setNormalizeScoreToDocumentLength(String normalizeScoreToDocumentLength);

   /**
     * Gets the query tokens as a map of <'token', score>
     * @return the map of <'token', score>
     */
    Map<String, Float> getQueryWeights();
}
