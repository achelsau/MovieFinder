package com.arielsweb.moviefinder.index.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.MovieDetailsDTO;
import com.arielsweb.moviefinder.index.dto.Posting;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException;
import com.arielsweb.moviefinder.index.exception.InvalidMovieDescriptorException.Reason;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.Genre;
import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * Singleton class to expose indexing functionality into memory. This
 * implementation is a standard inverted index.
 * 
 * @author Ariel
 * @data 27/12/2011
 */
@Component("invertedIndexEngine")
@Scope("singleton")
public class InvertedIndexEngine implements IndexEngine {

    private HashMap<String, IndexEntry> invertedIndex;
    private HashMap<Long, MovieDetailsDTO> movieDetails;
    private Integer count; // number of documents from the online index

    /**
     * Whether to index the names of the cast and crew as full names or split
     * them by words. This is a system-level property and it's taken from
     * moviefinder.properties
     */
    @Value("${index.indexFullNamesForCastAndCrew}")
    private Boolean indexFullNamesForCastAndCrew;

    /* Prevent direct access to the constructor */
    private InvertedIndexEngine() {
	super();

	invertedIndex = new HashMap<String, IndexEntry>();
	movieDetails = new HashMap<Long, MovieDetailsDTO>();
	count = 0;
    }

    /**
     * Used to recalculate Idfs from the index when a new movie synopsis (along
     * with actors, directors and screenwriters) is added
     */
    private void reCalcIdfs() {
	String[] wordKeys = new String[0];
	wordKeys = invertedIndex.keySet().toArray(wordKeys);
	for (int i = 0; i < wordKeys.length; i++) {
	    IndexEntry ind = invertedIndex.get(wordKeys[i]);
	    int postings = ind.getPostings().size(); // |d|

	    // count is already |D| + 1
	    float newIdf = (float) Math.log((float) count / postings);
	    ind.setIdf(newIdf);
	}
    }

    /**
     * This is called before reCalcIdfs and that's why count is already |D| + 1
     * there(if the document is new).
     * 
     * @param entryName
     *            the name of the movie synopsis
     * @return true if the user decided to overwrite it or if it is new
     * @throws InvalidMovieDescriptorException
     */
    private void addMovieDescriptorEntry(MovieDescriptor movieDescriptor) throws InvalidMovieDescriptorException {
	boolean synopsisNotPresent = movieDescriptor.getSynopsis() == null
		|| movieDescriptor.getSynopsis().length() == 0;

	String entryDesc = synopsisNotPresent ? movieDescriptor.getAlternateSynopsis() : movieDescriptor.getSynopsis();

	// it's pointless to add an empty synopsis movie at this point
	if (isMovieMetadataEmpty(movieDescriptor, entryDesc)) {
	    throw new InvalidMovieDescriptorException(Reason.MOVIE_DESCRIPTOR_WITH_EMPTY_SYNOPSIS);
	}

	createNewMovieDescriptorEntry(movieDescriptor);
    }

    /**
     * Checks if movie metadata is empty (primary synopsis, alternate synopsis,
     * cast and crew)
     * 
     * @param movieDescriptor the {@link MovieDescriptor
     * @param entryDesc the primary or alternate synopsis,depending if primary exists or not
     * @return true if the movie can be indexed, false the other way
     */
    private boolean isMovieMetadataEmpty(MovieDescriptor movieDescriptor, String entryDesc) {
	return (entryDesc == null || entryDesc.isEmpty()) && movieDescriptor.getActors().size() == 0
		&& movieDescriptor.getScreenWriters().size() == 0 && movieDescriptor.getDirectors().size() == 0;
    }

    /**
     * Creates new Movie Descriptor and reinitializes the count
     * 
     * @param movieDescriptor
     *            the {@link MovieDescriptor} to add
     */
    private void createNewMovieDescriptorEntry(MovieDescriptor movieDescriptor) {
	MovieDetailsDTO movieDTO = new MovieDetailsDTO(movieDescriptor.getName(), movieDescriptor.getSynopsis(),
		movieDescriptor.getSource().getName(), movieDescriptor.getRemotePath(),
		movieDescriptor.getReleaseDate());
	movieDetails.put(movieDescriptor.getId(), movieDTO); // reinitialize the
							     // term count
	count = movieDetails.size();
    }

    /**
     * Adds a new entry into the index.
     * 
     * <pre>
     * 
     * At this point the movie has either synopsis or critics consensus, otherwise it wouldn't have been added into
     * the database. Both description and consensus have value for retrieval and
     * feedback so we'll make no distinction at this point between the two.
     * 
     * <pre>
     * @throws InvalidMovieDescriptorException
     */
    @Override
    public synchronized void addEntry(MovieDescriptor movieDescriptor) throws InvalidMovieDescriptorException {
	if (movieDescriptor == null) {
	    throw new InvalidMovieDescriptorException(Reason.MOVIE_DESCRIPTOR_IS_NULL);
	}

	addMovieDescriptorEntry(movieDescriptor);

	Long movieId = movieDescriptor.getId();

	String[] parsedMeta = parseMovieSynopsis(movieDescriptor);

	// only update the term count here; the movieId is added in the
	// isAddingOk method
	MovieDetailsDTO movieDetail = movieDetails.get(movieId);
	movieDetail.setTermCount((short) parsedMeta.length);

	for (String parsedWord : parsedMeta) {
	    indexToken(parsedWord, movieDescriptor, false);
	}

	if (indexFullNamesForCastAndCrew) {
	    // index actors
	    for (MovieCrewPerson moviePerson : movieDescriptor.getActors()) {
		indexToken(moviePerson.getFullName().toLowerCase(), movieDescriptor, true);
	    }

	    // index screenwriter's full name
	    for (MovieCrewPerson moviePerson : movieDescriptor.getScreenWriters()) {
		indexToken(moviePerson.getFullName().toLowerCase(), movieDescriptor, true);
	    }

	    // index director's full name
	    for (MovieCrewPerson moviePerson : movieDescriptor.getDirectors()) {
		indexToken(moviePerson.getFullName().toLowerCase(), movieDescriptor, true);
	    }
	} else {
	    StringBuilder castAndCrew = new StringBuilder();

	    castAndCrew.append(movieDescriptor.getActors().toString());
	    castAndCrew.append(movieDescriptor.getDirectors().toString());
	    castAndCrew.append(movieDescriptor.getScreenWriters().toString());

	    String[] parsedCastAndCrewNames = TextParsingHelper.parseText(castAndCrew.toString());
	    for (String parsedName : parsedCastAndCrewNames) {
		indexToken(parsedName, movieDescriptor, true);
	    }
	}

	reCalcIdfs(); // recompute IDFs at the end of the adding
    }

    /**
     * Indexes a token (either a word or the name of a cast and crew member)
     * 
     * @param parsedWord
     *            the word to be indexed
     * @param movieDescriptor
     *            the {@link MovieDescriptor}
     * @param isNameOfCastAndCrew
     *            whether the token to index belongs to the name of an actor,
     *            director or screenwriter or not
     */
    private void indexToken(String parsedWord, MovieDescriptor movieDescriptor, boolean isNameOfCastAndCrew) {
	Long movieId = movieDescriptor.getId();

	// skip empty words
	if (parsedWord.length() == 0) {
	    return;
	}

	if (invertedIndex.containsKey(parsedWord)) {
	    IndexEntry currIndex = invertedIndex.get(parsedWord);

	    // it makes sense to increment the term frequency only if the token
	    // is not belonging to the name of a person from the cast or crew.
	    // The main reason is that usually cast and crew is not taken from
	    // the synopsis. Term frequency is relevant only for tokens taken
	    // from the synopsis.
	    if (currIndex.getPostings().containsKey(movieId) && !isNameOfCastAndCrew) {
		Posting posting = currIndex.getPostings().get(movieId);
		Short tf = posting.getTf(); // increment
					    // tf
		tf++;
		posting.setTf(tf);
		currIndex.getPostings().put(movieId, posting);
	    } else if (!currIndex.getPostings().containsKey(movieId)) {
		Posting posting = new Posting((short) 1, movieDescriptor.getGenres());
		currIndex.addPosting(movieId, posting); // appears for the
							// 1st time in
							// this doc
	    }
	} else {
	    IndexEntry indexWord = new IndexEntry();
	    invertedIndex.put(parsedWord, indexWord);
	    indexWord.setIdf((float) Math.log(count / 1)); // count is
							   // already |D| +
							   // 1
	    Posting posting = new Posting((short) 1, movieDescriptor.getGenres());
	    indexWord.addPosting(movieId, posting);
	}
    }

    /**
     * Given a {@link MovieDescriptor} parses the synopsis to be ready for
     * indexing.
     * 
     * @param movieDescriptor
     *            the movie descriptor that will be indexed
     * @return the parsed synopsis
     */
    private String[] parseMovieSynopsis(MovieDescriptor movieDescriptor) {
	// concat synopsis and consensus and make no distinction
	StringBuilder movieDescription = new StringBuilder();

	String synopsis = (movieDescriptor.getSynopsis() == null) ? MovieFinderConstants.STR_EMPTY : movieDescriptor
		.getSynopsis();
	String alternateSynopsis = (movieDescriptor.getAlternateSynopsis() == null) ? MovieFinderConstants.STR_EMPTY
		: movieDescriptor.getAlternateSynopsis();

	movieDescription.append(synopsis);
	movieDescription.append(MovieFinderConstants.STR_SPACE);
	movieDescription.append(alternateSynopsis);
	String[] parsedMeta = TextParsingHelper.parseText(movieDescription.toString());
	return parsedMeta;
    }

    /**
     * Returns the no. of docs. The method has to be synchronized to prevent
     * updates to the index while interrogation is on going from the QueryEngine
     * 
     * @return no. of docs
     */
    public synchronized Integer getNumberOfDocuments() {
	return count;
    }

    /**
     * Gets the list of movie details as a {@link HashMap} of <Id, MovieDetails>
     * 
     * @return the <Id, MovieDetails> pair as a HashMap
     */
    public synchronized HashMap<Long, MovieDetailsDTO> getMovieDetails() {
	return movieDetails;
    }

    /**
     * Returns the corpus {@link HashMap}. The method has to be synchronized to
     * prevent updates to the index while interrogation is on going from the
     * QueryEngine
     * 
     * @return
     */
    public synchronized HashMap<String, IndexEntry> getInvertedIndex() {
	return invertedIndex;
    }

    /**
     * Iterates through index
     */
    public void outputIndex() {
	System.out.println("====================================");

	String[] corpusWords = new String[0];
	corpusWords = invertedIndex.keySet().toArray(corpusWords);
	for (int i = 0; i < corpusWords.length; i++) {
	    System.out.print("(" + corpusWords[i] + ", " + invertedIndex.get(corpusWords[i]).getIdf() + ") ->");
	    Long[] postings = new Long[0];
	    HashMap<Long, Posting> postMap = invertedIndex.get(corpusWords[i]).getPostings();
	    postings = postMap.keySet().toArray(postings);

	    for (int j = 0; j < postings.length; j++) {
		System.out.print("(" + postings[j] + ", " + postMap.get(postings[j]) + ") ");
	    }
	    System.out.println();
	}

	System.out.println("====================================");
    }

    public void outputIndexForEntry(Long id) {
	System.out.println("====================================");

	String[] corpusWords = new String[0];
	corpusWords = invertedIndex.keySet().toArray(corpusWords);
	for (int i = 0; i < corpusWords.length; i++) {

	    StringBuilder indexPerDocument = new StringBuilder();
	    indexPerDocument.append("(" + corpusWords[i] + ", " + invertedIndex.get(corpusWords[i]).getIdf() + ") ->");

	    Long[] postings = new Long[0];
	    HashMap<Long, Posting> postMap = invertedIndex.get(corpusWords[i]).getPostings();
	    postings = postMap.keySet().toArray(postings);

	    for (int j = 0; j < postings.length; j++) {
		if (postings[j].equals(id)) {
		    indexPerDocument.append("(" + postings[j] + ", " + postMap.get(postings[j]) + ") ");
		    System.out.println(indexPerDocument);
		    break;
		}
	    }
	}

	System.out.println("====================================");
    }

    /**
     * Writes the index to a file just for testing purposes.
     */
    public void writeIndexToFile() {
	String[] corpusWords = new String[0];
	corpusWords = invertedIndex.keySet().toArray(corpusWords);
	try {
	    FileWriter fw = new FileWriter(
		    "D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\index_outputed");
	    BufferedWriter out = new BufferedWriter(fw);

	    HashMap<Genre, Set<String>> genreToWords = new HashMap<Genre, Set<String>>();

	    out.write("Inverted Index: ");
	    out.newLine();

	    for (int i = 0; i < corpusWords.length; i++) {

		Long[] postings = new Long[0];
		HashMap<Long, Posting> postMap = invertedIndex.get(corpusWords[i]).getPostings();
		postings = postMap.keySet().toArray(postings);

		out.write(corpusWords[i] + " -> ");
		for (int j = 0; j < postings.length; j++) {
		    out.write("(" + postings[j] + ", " + postMap.get(postings[j]) + ") ");

		    for (Genre genre : postMap.get(postings[j]).getGenres()) {
			if (genreToWords.containsKey(genre)) {
			    Set<String> words = genreToWords.get(genre);
			    words.add(corpusWords[i]);
			} else {
			    Set<String> words = new HashSet<String>();
			    words.add(corpusWords[i]);
			    genreToWords.put(genre, words);
			}
		    }
		}
		out.newLine();

	    }

	    out.write("Histogram of genres: ");
	    out.newLine();

	    // output the genres to words map
	    for (Genre genre : genreToWords.keySet()) {
		out.write("Genre: " + genre + ": ");
		for (String words : genreToWords.get(genre)) {
		    out.write(words + ", ");
		}

		out.newLine();
	    }

	    out.close();
	} catch (IOException e) {
	    System.out.println("Exception ");
	}
    }

    /**
     * Clears the index to do a proper unit testing
     */
    public void clearIndex() {
	// clear the corpus
	String[] corpusWords = new String[0];
	corpusWords = invertedIndex.keySet().toArray(corpusWords);
	for (int i = 0; i < corpusWords.length; i++) {
	    HashMap<Long, Posting> postMap = invertedIndex.get(corpusWords[i]).getPostings();
	    postMap.clear();
	}
	invertedIndex.clear();

	// clear the movie details map
	movieDetails.clear();

	// reset count
	count = 0;
    }

    @Override
    public synchronized void updateEntry(MovieDescriptor rd) throws InvalidMovieDescriptorException {
	if (rd == null) {
	    throw new InvalidMovieDescriptorException(InvalidMovieDescriptorException.Reason.MOVIE_DESCRIPTOR_IS_NULL);
	}

	removeEntry(rd.getId());

	addEntry(rd);
    }

    @Override
    public synchronized void removeEntry(Long movieId) throws InvalidMovieDescriptorException {
	boolean isMovieSynopsisInCorpus = false;
	for (Long fId : movieDetails.keySet()) {
	    if (fId.equals(movieId)) {
		isMovieSynopsisInCorpus = true;

		movieDetails.remove(fId);
		break;
	    }
	}

	// throw an error if movie to be removed is not present in the corpus
	if (!isMovieSynopsisInCorpus) {
	    throw new InvalidMovieDescriptorException(Reason.MOVIE_DESCRIPTOR_DOES_NOT_EXIST);
	}

	String[] words = invertedIndex.keySet().toArray(new String[0]);
	// iterating the corpus
	for (String word : words) {
	    IndexEntry indexEntry = invertedIndex.get(word);
	    HashMap<Long, Posting> postings = indexEntry.getPostings();
	    // if the word appears only in the erased document
	    if (postings.containsKey(movieId) && postings.size() == 1) {
		postings.remove(movieId);
		invertedIndex.remove(word);
	    } else if (postings.containsKey(movieId)) {
		postings.remove(movieId);
	    }
	}

	count--;
	reCalcIdfs();
    }

    @Override
    public void setMovieDetails(HashMap<Long, MovieDetailsDTO> movieDetails) {
	this.movieDetails = movieDetails;
	this.count = movieDetails.size();
    }

    @Override
    public void setCorpus(HashMap<String, IndexEntry> corpus) {
	this.invertedIndex = corpus;
    }

    /**
     * Sets the flag that indicates whether to index or not full names or to
     * tokenize and parse them
     * 
     * @param indexFullNamesForCastAndCrew
     *            true if full names are wanted into the index, false the other
     *            way
     */
    @Override
    public void setIndexFullNamesForCastAndCrew(Boolean indexFullNamesForCastAndCrew) {
	this.indexFullNamesForCastAndCrew = indexFullNamesForCastAndCrew;
    }

}
