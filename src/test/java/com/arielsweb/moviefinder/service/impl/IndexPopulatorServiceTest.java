package com.arielsweb.moviefinder.service.impl;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieDescriptorService;
import com.arielsweb.moviefinder.service.exceptions.InvalidIndexPopulationException;

/**
 * Test the {@link IndexPopulatorService}
 * 
 * @author Ariel
 * 
 */
@DataSet("IndexPopulatorTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class IndexPopulatorServiceTest {
    @SpringBeanByType
    private IndexEngine indexEngine;
    @SpringBeanByType
    private MovieDescriptorService rdService;

    /**
     * Tests the proper population of the inverted index
     * 
     * @throws InvalidIndexPopulationException
     * @throws InvalidMovieDescriptorException
     */
    @Test
    public void testPopulateIndex() throws InvalidIndexPopulationException, InvalidMovieDescriptorException {
	// setup
	indexEngine.clearIndex();// make sure the index is clean here

	// execute
	rdService.populateIndex();

	// verify
	HashMap<String, IndexEntry> corpus = indexEngine.getInvertedIndex();
	int noOfDocs = indexEngine.getNumberOfDocuments();

	assertEquals(16, corpus.size()); // how many words are present in the
					 // corpus
	assertEquals(3, noOfDocs);
    }
    
    @Test(expected = InvalidIndexPopulationException.class)
    public void testPopulateIndex_errorExpected() throws InvalidIndexPopulationException, InvalidMovieDescriptorException {
	// setup
	MovieSource source = new MovieSource();
	source.setName("John");

	MovieDescriptor rDescriptor = new MovieDescriptor();
	rDescriptor.setSource(source);
	rDescriptor.setRemotePath("new_path");
	rDescriptor.setSynopsis("Test this");
	rDescriptor.setName("Res Name");
	
	indexEngine.addEntry(rDescriptor);
	
	// execute
	rdService.populateIndex();
    }
}
