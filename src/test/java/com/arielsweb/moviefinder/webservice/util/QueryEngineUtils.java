package com.arielsweb.moviefinder.webservice.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;

import org.codehaus.jackson.map.ObjectMapper;

import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.webservice.dto.ResultInfoResponse;

/**
 * @author Ariel
 * @date 11/11/2012
 */
public class QueryEngineUtils {
    /**
     * Quick Query for a movie
     * 
     * @param query
     *            the quick query to send to the server
     * @return the result for the quick query
     */
    public static ResultInfoResponse quickQueryForMovie(String query) {
	try {
	    URL queryUrl = new URL("https://localhost:8443/MovieFinderServer/mf/query/quickQuery/");

	    HttpURLConnection queryConn = (HttpURLConnection) queryUrl.openConnection();
	    queryConn.setDoOutput(true);
	    queryConn.setRequestMethod("POST");
	    queryConn.setRequestProperty("Content-Type", "text/plain");
	    queryConn.setRequestProperty("Authorization", "Ion:abcd");

	    OutputStream os = queryConn.getOutputStream();
	    os.write(query.getBytes());
	    os.flush();

	    if (queryConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("Failed : HTTP error code : " + queryConn.getResponseCode());
	    }

	    // verify the success of the request
	    assertEquals(HttpURLConnection.HTTP_OK, queryConn.getResponseCode());
	    System.out.println(queryConn.getResponseCode() + ", " + queryConn.getResponseMessage());

	    BufferedReader br = new BufferedReader(new InputStreamReader((queryConn.getInputStream())));
	    System.out.println("Output from Server .... \n");
	    ObjectMapper mapper = new ObjectMapper();
	    String output = null;
	    while ((output = br.readLine()) != null) {
		System.out.println(output);

		ResultInfoResponse result = mapper.readValue(output, ResultInfoResponse.class);

		return result;
	    }

	    queryConn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	} catch (IOException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	}

	return null;
    }

    /**
     * Sends a persistent query to the server either to be saved for the first
     * time or to be edited
     * 
     * @param queryString
     *            the query string associated with the {@link PersistentQuery}
     * @param interval
     *            the interval of the {@link PersistentQuery}
     * @param uniqueDescription
     *            the unique description of the
     * @param id
     *            the id of the query to send to the server; if the query is to
     *            be persisted for the first time, this would be null, else it
     *            will be populated
     * @return the id of the stored/edited query
     */
    public static ResultInfoResponse sendQueryToServer(String queryString, Long interval, Long id) {
	try {
	    URL queryUrl = null;
	    if (id == null) { // then save it
		queryUrl = new URL("https://localhost:8443/MovieFinderServer/mf/query/storePersistentQuery/");
	    } else { // update it
		queryUrl = new URL("https://localhost:8443/MovieFinderServer/mf/query/updatePersistentQuery/");
	    }

	    HttpURLConnection queryConn = (HttpURLConnection) queryUrl.openConnection();
	    queryConn.setDoOutput(true);
	    queryConn.setRequestMethod("POST");
	    queryConn.setRequestProperty("Content-Type", "json/application");
	    queryConn.setRequestProperty("Authorization", "Ion:abcd");

	    String input = null;
	    if (id == null) { // then save it
		input = "{\"interval\" : " + interval + "," + "\"queryString\" : \"" + queryString + "\"}";
	    } else { // update it
		input = "{\"id\" : " + id + "," + "\"interval\" : " + interval + "," + "\"queryString\" : \""
			+ queryString + "\"}";
	    }

	    OutputStream os = queryConn.getOutputStream();
	    os.write(input.getBytes());
	    os.flush();

	    if (queryConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("Failed : HTTP error code : " + queryConn.getResponseCode());
	    }

	    // verify the success of the request
	    assertEquals(HttpURLConnection.HTTP_OK, queryConn.getResponseCode());
	    System.out.println(queryConn.getResponseCode() + ", " + queryConn.getResponseMessage());

	    BufferedReader br = new BufferedReader(new InputStreamReader((queryConn.getInputStream())));

	    // even though this operation is only persisting the query, a list
	    // of initial results are still returned from the server (as in
	    // doing a quick query)
	    System.out.println("Output from Server .... \n");
	    ObjectMapper mapper = new ObjectMapper();
	    String output = null;
	    while ((output = br.readLine()) != null) {
		System.out.println(output);

		ResultInfoResponse result = mapper.readValue(output, ResultInfoResponse.class);

		return result;
	    }

	    queryConn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	} catch (IOException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	}

	return null;
    }

    public static int removePersistentQuery(Long queryId) {
	int responseCode = -1;
	try {
	    URL queryUrl = new URL("https://localhost:8443/MovieFinderServer/mf/query/deletePersistentQuery/" + queryId);

	    HttpURLConnection queryConn = (HttpURLConnection) queryUrl.openConnection();
	    queryConn.setDoOutput(true);
	    queryConn.setRequestMethod("DELETE");
	    queryConn.setRequestProperty("Authorization", "Ion:abcd");

	    if (queryConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("Failed : HTTP error code : " + queryConn.getResponseCode());
	    }

	    // verify the success of the request
	    assertEquals(HttpURLConnection.HTTP_OK, queryConn.getResponseCode());
	    responseCode = queryConn.getResponseCode();
	    System.out.println(responseCode + ", " + queryConn.getResponseMessage());

	    BufferedReader br = new BufferedReader(new InputStreamReader((queryConn.getInputStream())));

	    // even though this operation is only persisting the query, a list
	    // of initial results are still returned from the server (as in
	    // doing a quick query)
	    System.out.println("Output from Server .... \n");
	    String output = null;
	    while ((output = br.readLine()) != null) {
		System.out.println(output);
	    }

	    queryConn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	} catch (IOException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	}

	return responseCode;
    }

    public class ResultInfoIdComparator implements Comparator<ResultInfo> {

	@Override
	public int compare(ResultInfo o1, ResultInfo o2) {
	    return o1.getId().compareTo(o2.getId());
	}

    }
}
