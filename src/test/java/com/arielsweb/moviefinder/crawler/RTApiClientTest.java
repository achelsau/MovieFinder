package com.arielsweb.moviefinder.crawler;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

/**
 * Tests the {@link RTApiClient}
 * 
 * @author Ariel
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.DISABLED)
public class RTApiClientTest {
    @SpringBeanByType
    private RTApiClient rtApiClient;
    
    @Test
    @Ignore
    public void testBrowseApi() throws IOException {
	rtApiClient.browseInTheaters();
    }
}
