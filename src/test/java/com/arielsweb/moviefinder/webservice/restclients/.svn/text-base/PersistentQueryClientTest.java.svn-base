package com.arielsweb.moviefinder.webservice.restclients;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.webservice.controllers.QueryEngineController;
import com.arielsweb.moviefinder.webservice.controllers.ResultInfoResponse;
import com.arielsweb.moviefinder.webservice.util.QueryEngineUtils;

/**
 * Provides a test using a REST client for the {@link QueryEngineController}.
 * Tests like should only check if the API method returned successfully
 * 
 * @author Ariel
 * @date 11/11/2012
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
public class PersistentQueryClientTest {
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

    @SpringBeanByName
    private PersistentQueryService persistentQueryService;

    @Before
    public void setUp() {
	// nothing to setup
    }

    /**
     * Scenario 1:
     * 
     * <pre>
     * 1. Send a quick query
     * </pre>
     */
    @Test
    public void quickQuery() {
	String queryString = "Lunar landings in the 70s";

	// 1. Run a quick query
	ResultInfoResponse resultInfoResponse = QueryEngineUtils.quickQueryForMovie(queryString);

	assertNotNull(resultInfoResponse);
    }

    /**
     * Scenario 2:
     * 
     * <pre>
     * 1. Persist a query
     * 2. Remove the previously persisted query
     * 3. Check the existence of the query by doing a Service operation
     * </pre>
     */
    @Test
    public void persistQueryAndRemoveIt() {
	Long interval = 100000L;
	String queryString = "Lunar landings in the 70s";

	// 1. Persist a query
	ResultInfoResponse resultInfoResponse = QueryEngineUtils.sendQueryToServer(queryString, interval, null);
	// this means it was persisted
	assertNotNull(resultInfoResponse.getQueryId());

	// 2. Remove the persistent query
	int responseCode = QueryEngineUtils.removePersistentQuery(resultInfoResponse.getQueryId());
	assertEquals(200, responseCode);
    }

    /**
     * Scenario 3:
     * 
     * <pre>
     * 1. Persist a query
     * 2. Update it
     * 3. Check the that the update was successful by doing a Service operation
     * </pre>
     */
    @Test
    public void persistQueryAndUpdateIt() {
	Long interval = 100000L;
	String queryString = "Lunar landings in the 70s";

	// 1. Persist a query
	ResultInfoResponse resultInfoResponse = QueryEngineUtils.sendQueryToServer(queryString, interval, null);

	queryString = "Icarus ship designed by mankind";

	Long idAfterStorage = resultInfoResponse.getQueryId();
	resultInfoResponse = QueryEngineUtils.sendQueryToServer(queryString, interval, idAfterStorage);

	// verify (that query id is not null and, furthermore, equal to the
	// one after storage)
	assertNotNull(resultInfoResponse.getQueryId());
	assertEquals(idAfterStorage, resultInfoResponse.getQueryId());
    }

}
