package com.arielsweb.moviefinder.index;

import java.util.HashMap;

import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.MovieDetailsDTO;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.model.MovieDescriptor;

/**
 * Defines the contract that the indexing engine will have to fulfill. This is
 * because various implementations of the engine can be swapped easily.
 * 
 * @author Ariel
 * @date 27/12/2011
 * 
 */
public interface IndexEngine {
    /**
     * Adds an entry to the index
     * 
     * @param movieDescriptor
     *            the descriptor for the movie to add into the index (synopsis
     *            and critics consensus and/or any other useful textual
     *            information such as cast, director year, or others)
     * @return true if everything went ok
     * @throws InvalidMovieDescriptorException
     */
    void addEntry(MovieDescriptor movieDescriptor) throws InvalidMovieDescriptorException;

    /**
     * Updates the entry in the index if it exists
     * 
     * @param movieDescriptor
     *            the descriptor for the movie to update into the index
     * @return true if everything went ok
     * @throws InvalidMovieDescriptorException
     */
    void updateEntry(MovieDescriptor movieDescriptor) throws InvalidMovieDescriptorException;

    /**
     * Removes an entry from the index
     * 
     * @param movieId
     *            the DB id of the {@link MovieDescriptor} - it is compulsory
     *            for the descriptor to be persisted in order to be removed from
     *            MBI
     * @return true if everything went ok; false the other way
     * @throws InvalidMovieDescriptorException
     */
    void removeEntry(Long movieId) throws InvalidMovieDescriptorException;

    void outputIndex();
    
    /**
     * Given a specific id, output the index corresponding to that document only
     */
    void outputIndexForEntry(Long id);

    void writeIndexToFile();

    /**
     * Gets the list of movie details as a {@link HashMap} of <Id, MovieDetails>
     * 
     * @return the <Id, MovieDetails> pair as a HashMap
     */
    HashMap<Long, MovieDetailsDTO> getMovieDetails();

    /**
     * Sets a map of <movieId, {@link MovieDetailsDTO}
     * 
     * @param movieDetails
     *            the map of movie details
     */
    void setMovieDetails(HashMap<Long, MovieDetailsDTO> movieDetails);

    /**
     * Gets the exact memory representation of the inverted index
     * 
     * @return the pair <keyword, entries> of the inverted index
     */
    HashMap<String, IndexEntry> getInvertedIndex();

    /**
     * Sets the current inverted index
     * 
     * @param corpus
     *            the actual inverted index data structure
     */
    void setCorpus(HashMap<String, IndexEntry> corpus);

    /**
     * Clears the contents of the whole index
     */
    void clearIndex();

    /**
     * Gets the number of docs present in the index
     * 
     * @return
     */
    Integer getNumberOfDocuments();

    /**
     * Sets the flag that indicates whether to index or not full names or to
     * tokenize and parse them
     * 
     * @param indexFullNamesForCastAndCrew
     *            true if full names are wanted into the index, false the other
     *            way
     */
    void setIndexFullNamesForCastAndCrew(Boolean indexFullNamesForCastAndCrew);
}
