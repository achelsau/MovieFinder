package com.arielsweb.moviefinder.webservice.restclients;

import static junit.framework.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.arielsweb.moviefinder.index.dto.ResultInfoResponse;
import com.arielsweb.moviefinder.webservice.util.IndexingEngineUtils;
import com.arielsweb.moviefinder.webservice.util.QueryEngineUtils;

/**
 * Deprecated test from the time when movie descriptor was an API entry-point.
 * 
 * @author Ariel
 * 
 */
public class MovieDescriptorClientTest {

    static {
	// for localhost testing only
	javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

	    public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
		if (hostname.equals("localhost")) {
		    return true;
		}
		return false;
	    }
	});
    }

    /**
     * For this test to be running, the server for the main app. has to be
     * started.
     * 
     * <pre>
     * 1. Share a resource
     * 2. Query for it based on a word from the description
     * 3. Remove the resource from share - to maintain the index as in the initial condition
     * </pre>
     */
    @Test
    @Ignore
    public void shareAndQueryForResourceDescriptor() {
	String uniqueDescription = "Unique description 1q2w3e4r5t6y";
	String uniqueResourceTitle = "Something cool";
	String uniqueRemotePath = "uniqueRemotePath";

	// 1. Share a resource
	Long resourceId = IndexingEngineUtils.shareResource(uniqueDescription, uniqueResourceTitle, uniqueRemotePath);

	// 2. Query for it based on a word from the description
	String query = "1q2w3e4r5t6y";
	com.arielsweb.moviefinder.index.dto.ResultInfoResponse resultInfoResponse = QueryEngineUtils.quickQueryForMovie(query);

	assertEquals(resourceId, resultInfoResponse.getQueryId());

	// 3. Remove the resource from share - to maintain the index as in the
	// initial condition
	IndexingEngineUtils.removeResourceFromShare(resourceId);
    }

    /**
     * For this test to be running, the server for the main app. has to be
     * started.
     * 
     * <pre>
     * 1. Share resource
     * 2. Update resource's description
     * 3. Query for the updated resource
     * 4. Unshare the resources
     * </pre>
     */
    @Test
    @Ignore
    public void shareAndUpdateResourceDescriptor() {
	String uniqueDescription = "Unique description 1q2w3e4r5t6y";
	String uniqueResourceTitle = "Something cool";
	String uniqueRemotePath = "uniqueRemotePath";

	// 1. Share a resource
	Long resourceId = IndexingEngineUtils.shareResource(uniqueDescription, uniqueResourceTitle, uniqueRemotePath);

	// 2. Update resource details
	String updatedDescription = "Updated description 0p9o8i7u6y";
	IndexingEngineUtils.updateSharedResource(resourceId, updatedDescription, uniqueResourceTitle, uniqueRemotePath);

	// 3. Query for the updated resource (and pass the updated description also
	// to be sure the correct id will be picked up)
	String query = "0p9o8i7u6y";
	ResultInfoResponse resultInfoResponse = QueryEngineUtils.quickQueryForMovie(query);

	assertEquals(resourceId, resultInfoResponse.getQueryId());

	// 3. Unshare the resource (to preserve the way the index was)
	IndexingEngineUtils.removeResourceFromShare(resourceId);
    }

    /**
     * For this test to be running, the server for the main app. has to be
     * started.
     * 
     * <pre>
     * 1. Share resource
     * 2. Unshare the resources
     * 3. Query for the unshared resource
     * </pre>
     */
    @Test
    @Ignore
    public void shareResourceUnshareItAndQueryForIt() {
	String uniqueDescription = "Unique description 1q2w3e4r5t6y";
	String uniqueResourceTitle = "Something cool";
	String uniqueRemotePath = "uniqueRemotePath";

	// 1. Share a resource
	Long resourceId = IndexingEngineUtils.shareResource(uniqueDescription, uniqueResourceTitle, uniqueRemotePath);

	// 2. Unshare the resource (to preserve the way the index was)
	IndexingEngineUtils.removeResourceFromShare(resourceId);

	// 3. Query for it based on a word from the description
	String query = "1q2w3e4r5t6y";
	com.arielsweb.moviefinder.index.dto.ResultInfoResponse resultInfoResponse = QueryEngineUtils.quickQueryForMovie(query);

	assertEquals(resourceId, resultInfoResponse.getQueryId());
    }

}
