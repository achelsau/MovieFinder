package com.arielsweb.moviefinder.index.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.index.IQueryEngine;
import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.MovieDetailsDTO;
import com.arielsweb.moviefinder.index.dto.Posting;
import com.arielsweb.moviefinder.index.dto.ResultInfo;
import com.arielsweb.moviefinder.index.exception.InvalidQueryException;
import com.arielsweb.moviefinder.index.util.TextParsingHelper;

/**
 * Cosine Score implementation of the query engine.
 * 
 * @author Ariel
 * 
 */
@Component("cosineScoreQueryEngine")
@Scope("prototype")
public class CosineScoreQueryEngine implements IQueryEngine {

    public static int K = 15;
    private static int MINIMUM_TERM_COUNT_FOR_NORMALIZATION = 30;
    private static IndexEngine indexingEngine;
    
    /** The logger. */
    private org.apache.log4j.Logger log = Logger.getLogger(CosineScoreQueryEngine.class);

    /** whether to normalize the scores or not relative to document
     *            length. for example, if we have a score of 10 for a document
     *            that's 100 words, the score will become 0.1. If the same
     *            document has 10 words, the score is higher: 1. **/
    @Value("${index.useScoreNormalization}")
    private String normalizeScoreToDocumentLength;

    private static int sumOfAll, count;


    public CosineScoreQueryEngine() {
	// Default ctor
    }

    @SuppressWarnings("unused")
    private void assignQueryWeights(String query, Float[] queryW,
	    String[] queryTokens) {
	// see if this is needed for the future
    }

    /**
     * Given an index entry in the form {idf, <movieId, {tf, genres}>},
     * calculate the score for the current token by iterating over the Map of
     * <movieId, {tf, genres}> and multiplying the idf of the word with each tf.
     * 
     * @param results
     *            the results to be populated
     * @param indexEntry
     *            the index entry used to calculate the score of the result
     */
    private void getScoresForToken(HashMap<Long, ResultInfo> results, IndexEntry indexEntry, 
	    Float queryTokenWeight) {
	HashMap<Long, Posting> postings = indexEntry.getPostings();

	Long[] movieIds = new Long[0];
	movieIds = (Long[]) postings.keySet().toArray(movieIds);

	// iterate through postings
	for (int i = 0; i < movieIds.length; i++) {
	    ResultInfo resInfo = null;
	    Float score = 0f;
	    Posting posting = postings.get(movieIds[i]);

	    if (results.containsKey(movieIds[i])) {
		resInfo = results.get(movieIds[i]);
		score = resInfo.getScore();
		score += queryTokenWeight * posting.getTf() * indexEntry.getIdf(); // tfIdf

		// set the new score
		resInfo.setScore(score);
	    } else {
		resInfo = new ResultInfo();
		score = queryTokenWeight * posting.getTf() * indexEntry.getIdf(); // tfIdf
		resInfo.setScore(score);
	    }

	    resInfo.setId(movieIds[i]);
	    results.put(movieIds[i], resInfo);
	}
    }

    /**
     * After the scores have been calculated and normalized, get the first K out
     * of a priority queue
     * 
     * @param results
     *            the results map from which to extract the first K relevant
     *            results
     * @return the first K results properly ordered
     */
    private List<ResultInfo> getResultList(HashMap<Long, ResultInfo> results) {
	HashMap<Long, MovieDetailsDTO> movieDetails = indexingEngine.getMovieDetails();

	ResultsExtractor extractor = new ResultsExtractor();
	ResultInfo[] resultValues = new ResultInfo[0];
	resultValues = results.values().toArray(resultValues);
	for (Map.Entry<Long, ResultInfo> pair : results.entrySet()) {
	    MovieDetailsDTO movieDetail = movieDetails.get(pair.getKey());

	    ResultInfo result = pair.getValue();
	    result.setReleaseDate(movieDetail.getReleaseDate());
	}

	// build max heap only after release dates have been set
	extractor.buildMaxHeap(resultValues);

	List<ResultInfo> orderedResults = new ArrayList<ResultInfo>();
	for (int i = 0; i < K && i < results.size(); i++) {
	    ResultInfo result = extractor.heapExtractMax(resultValues);

	    MovieDetailsDTO movieDetail = movieDetails.get(result.getId());
	    result.setDescription(movieDetail.getDesc());
	    result.setTitle(movieDetail.getTitle());
	    result.setSource(movieDetail.getSource());
	    result.setRemotePath(movieDetail.getPath());
	    result.setReleaseDate(movieDetail.getReleaseDate());
	    result.setRemotePicture(movieDetail.getImagePath());
	    log.info(result.getScore() + ", " + result.getTitle() + ", " + result.getDescription());

	    orderedResults.add(result);
	}

	return orderedResults;
    }

    /**
     * Gets the similarity between the query tokens and the documents that are
     * indexed.
     * 
     * <pre>
     * 1. For each query token, grab an index entry and iterate through
     * postings. For example when searching "moon landings", the token
     * "moon" will give a list of results L1, each with a score given by IDF *
     * TF. The token "landing" will give another list L2. Both of those lists will 
     * be merged into a single one in which there will be docs that contain both words and 
     * also docs containing only one word.
     * 2. After the full list is compiled, the scores calculated at the previous step are
     * normalized (based on the length of the documents). 
     * 3. After this, the first K results are ordered and returned.
     * </pre>
     * 
     * @param queryTokens
     *            the array of parsed query tokens
     * @return the first <code>K</code> {@link ResultInfo}s
     * @throws InvalidQueryException
     */
    private List<ResultInfo> getCosineSimilarity(Map<String, Float> queryWeights)
	    throws InvalidQueryException {
	String[] queryTokens = queryWeights.keySet().toArray(new String[0]);
	HashMap<String, IndexEntry> corpus = indexingEngine.getInvertedIndex();
	HashMap<Long, ResultInfo> results = new HashMap<Long, ResultInfo>();

	for (int i = 0; i < queryTokens.length; i++) {
	    IndexEntry indexEntry = corpus.get(queryTokens[i]);
	    if (indexEntry != null) {
		getScoresForToken(results, indexEntry, queryWeights.get(queryTokens[i]));
	    }
	}

	if (results.size() > 0) {
	    // only if the explicit option is set
	    if (Boolean.valueOf(normalizeScoreToDocumentLength)) {
		normalizeScores(results);
	    }

	    // System.out.println("Average: " + ((double) sumOfAll / count));
	    return getResultList(results);
	} else {
	    return new ArrayList<ResultInfo>(); // empty {@link ArrayList}
	}
    }

    /**
     * Divides the obtained scores by the length of the document. If a score of
     * 1.5 was found for a document with 100 words and the same score was found
     * for a document with 20 words, than the 20-word doc is in advantage.
     * 
     * @param query
     *            the query
     * @param limit
     *            limit of results
     * @param foundDocs
     *            the already matching documents
     * @param scores
     *            the already computed scores
     * @param docNames
     *            the already read document names
     * @return a mapping <doc_name, doc_info> ordered in ascendent order
     */
    private static void normalizeScores(HashMap<Long, ResultInfo> results) {
	HashMap<Long, MovieDetailsDTO> movieDetails = indexingEngine.getMovieDetails();
	float scoreMax = -1;

	Long[] movieIds = new Long[0];
	movieIds = results.keySet().toArray(movieIds);
	for (int i = 0; i < movieIds.length; i++) {
	    ResultInfo resultInfo = results.get(movieIds[i]);
	    
	    sumOfAll += movieDetails.get(movieIds[i]).getTermCount();
	    count++;
	    
	    if (movieDetails.get(movieIds[i]).getTermCount() > MINIMUM_TERM_COUNT_FOR_NORMALIZATION) {
		float normalizedScore = (resultInfo.getScore() / movieDetails.get(movieIds[i]).getTermCount()) * 100;
		resultInfo.setScore(normalizedScore);

		if (normalizedScore > scoreMax) {
		    scoreMax = normalizedScore;
		}
	    }
	}

    }

    /**
     * Query the inverted index.
     * 
     * @param query
     *            the map containing <word, weight> pairs
     * 
     * @return the list of {@link ResultInfo} objects
     * @throws InvalidQueryException
     */
    @Override
    public List<ResultInfo> queryIndex(Map<String, Float> queryWeights)
	    throws InvalidQueryException {

	return getCosineSimilarity(queryWeights);
    }

    @Override
    public List<ResultInfo> queryIndex(String query) throws InvalidQueryException {
	validateQuery(query);

	String[] queryTokens = TextParsingHelper.parseText(query);

	Map<String, Float> queryWeights = TextParsingHelper.getQueryWeights(queryTokens);

	return getCosineSimilarity(queryWeights);
    }

    /**
     * Validates the query
     * 
     * @param query
     *            the query to be validated
     */
    private void validateQuery(String query) {
	// it is impossible to query the index using null or empty string
	if (query == null) {
	    throw new InvalidQueryException(InvalidQueryException.Reason.QUERY_CANNOT_BE_NULL);
	} else if (query.isEmpty()) {
	    throw new InvalidQueryException(InvalidQueryException.Reason.QUERY_CANNOT_BE_EMPTY);
	}
    }

    @Autowired
    public void setIndexingEngine(IndexEngine indexingEngine) {
	CosineScoreQueryEngine.indexingEngine = indexingEngine;
    }

    /**
     * Sets the normalizeScoreToDocumentLength option
     * 
     * @param normalizeScoreToDocumentLength
     *            whether to divide the obtained score by the document length
     */
    public void setNormalizeScoreToDocumentLength(String normalizeScoreToDocumentLength) {
	this.normalizeScoreToDocumentLength = normalizeScoreToDocumentLength;
    }
}
