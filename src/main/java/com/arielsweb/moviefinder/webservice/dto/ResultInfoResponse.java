package com.arielsweb.moviefinder.webservice.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.arielsweb.moviefinder.index.dto.ResultInfo;

/**
 * Wrapper for the result info prepared for being serialized to JSON and sent to
 * the client
 * 
 * @author Ariel
 */
@XmlRootElement(name = "resultResponseMap")
public class ResultInfoResponse {

	/**
	 * holds a set of results returned by the query: <score, result info>;
	 * <score, result info>; etc...
	 **/
	@XmlElement(name = "resultsMap")
	public List<ResultInfo> results;

	/**
	 * In case the query is persisted -> return its id from the database
	 */
	@XmlElement(name = "queryId")
	public Long queryId;

	/**
	 * Default constructor
	 */
	public ResultInfoResponse() {

	}

	/**
	 * Response returned after a quick query is performed. After this operation
	 * only the results are the one that we are interested in. The query is not
	 * persisted.
	 * 
	 * @param results
	 *            the map of <score, result> for the query that was sent
	 */
	public ResultInfoResponse(List<ResultInfo> results) {
		this.results = results;
	}

	/**
	 * Response returned after the operation of persisting a query has been
	 * performed. In this case, besides an initial set of results, the id of the
	 * query that was persisted into the database is returned
	 * 
	 * @param results
	 *            the ordered list of results for the query that was sent
	 * @param queryId
	 *            the id of the query that was sent
	 */
	public ResultInfoResponse(List<ResultInfo> results, Long queryId) {
		this.results = results;
		this.queryId = queryId;
	}

	/**
	 * Getters and Setters
	 */
	public List<ResultInfo> getResults() {
		return results;
	}

	public void setResults(List<ResultInfo> results) {
		this.results = results;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}
}
