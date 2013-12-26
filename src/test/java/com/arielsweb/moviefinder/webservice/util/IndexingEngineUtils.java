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

/**
 * @author Ariel
 * @date 11/11/2012
 */
public class IndexingEngineUtils {
    public static Long shareResource(String uniqueDescription, String uniqueResourceTitle, String uniqueRemotePath) {
	Long resourceId = 0L;
	try {
	    URL url = new URL("https://localhost:8443/IRAgentServer/index/add/");

	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "json/application");
	    conn.setRequestProperty("Authorization", "Ion:abcd");

	    String input = "{\"resourceName\":\"" + uniqueResourceTitle + "\"," + "\"remotePath\":\"" + uniqueRemotePath
		    + "\"," + "\"resourceDescription\":\"" + uniqueDescription + "\"}";

	    OutputStream os = conn.getOutputStream();
	    os.write(input.getBytes());
	    os.flush();

	    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	    }

	    // verify the success of the request
	    assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
	    System.out.println(conn.getResponseCode() + ", " + conn.getResponseMessage());

	    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {
		System.out.println(output);

		resourceId = Long.parseLong(output);
	    }

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	} catch (IOException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	}

	return resourceId;
    }

    public static void removeResourceFromShare(Long resourceId) {
	try {
	    URL removeUrl = new URL("https://localhost:8443/IRAgentServer/index/remove/" + resourceId.toString());

	    HttpURLConnection removeConn = (HttpURLConnection) removeUrl.openConnection();
	    removeConn.setRequestMethod("DELETE");
	    removeConn.setRequestProperty("Authorization", "Ion:abcd");

	    if (removeConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("Failed : HTTP error code : " + removeConn.getResponseCode());
	    }

	    // verify the success of the request
	    assertEquals(HttpURLConnection.HTTP_OK, removeConn.getResponseCode());
	    System.out.println(removeConn.getResponseCode() + ", " + removeConn.getResponseMessage());

	    BufferedReader br = new BufferedReader(new InputStreamReader((removeConn.getInputStream())));
	    System.out.println("Output from Server .... \n");
	    String output = null;
	    while ((output = br.readLine()) != null) {
		System.out.println(output);
	    }

	    removeConn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	} catch (IOException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	}
    }

    public static void updateSharedResource(Long resourceId, String uniqueDescription, String uniqueResourceTitle,
	    String uniqueRemotePath) {
	try {
	    URL updateURL = new URL("https://localhost:8443/IRAgentServer/index/update/");

	    HttpURLConnection updateConn = (HttpURLConnection) updateURL.openConnection();
	    updateConn.setDoOutput(true);
	    updateConn.setRequestMethod("POST");
	    updateConn.setRequestProperty("Authorization", "Ion:abcd");
	    updateConn.setRequestProperty("Content-Type", "json/application");

	    String input = "{\"id\":" + resourceId + ", "
		    	 + "\"resourceName\":\"" + uniqueResourceTitle + "\","
		         + "\"remotePath\":\"" + uniqueRemotePath + "\"," 
	    		 + "\"resourceDescription\":\"" + uniqueDescription + "\"}";

	    OutputStream os = updateConn.getOutputStream();
	    os.write(input.getBytes());
	    os.flush();

	    if (updateConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		throw new RuntimeException("Failed : HTTP error code : " + updateConn.getResponseCode());
	    }

	    // verify the success of the request
	    assertEquals(HttpURLConnection.HTTP_OK, updateConn.getResponseCode());
	    System.out.println(updateConn.getResponseCode() + ", " + updateConn.getResponseMessage());

	    BufferedReader br = new BufferedReader(new InputStreamReader((updateConn.getInputStream())));
	    System.out.println("Output from Server .... \n");
	    String output = null;
	    while ((output = br.readLine()) != null) {
		System.out.println(output);
	    }

	    updateConn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	} catch (IOException e) {
	    e.printStackTrace();

	    fail("Exception occurred");
	}
    }

}
