package com.arielsweb.moviefinder.index.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.Posting;
import com.arielsweb.moviefinder.index.util.RelevanceFeedbackUtil;

/**
 * Improves an existing query vector by adding to it terms along with respective
 * weights.
 * 
 * @author Ariel
 * 
 */
@Component("relevanceFeedbackEngine")
public class RelevanceFeedbackEngine {

    @Value("${rocchio.queryWeight}")
    private Float queryWeight;

    @Value("${rocchio.relevantDocumentsWeight}")
    private Float relevantDocumentsWeight;

    @Value("${rocchio.nonRelevantDocumentsWeight}")
    private Float nonRelevantDocumentsWeight;

    private IndexEngine invertedIndexEngine;

    /**
     * Creates a new stateless instance for refining the query
     */
    public RelevanceFeedbackEngine() {

    }

    /**
     * Stating from an initial query (or no initial query), this method adds
     * word weights to it. The additional word weights come from relevant
     * documents.
     * 
     * @param query
     *            the parsed query tokens
     * @param relevantEntries
     *            the relevant document ids
     * @return the resulted refined query
     */
    public Map<String, Float> getRefinedQuery(Map<String, Float> queryTokens, Long[] relevantEntries) {
	// if this service was called using an empty or null map of query tokens
	// or an empty or null list of relevant entries
	// then the query should remain untouched
	if (relevantEntries == null || relevantEntries.length == 0) {
	    return queryTokens;
	}

	HashMap<String, IndexEntry> invertedIndex = invertedIndexEngine.getInvertedIndex();
	
	// this is quite impossible but if somehow the memory-based index was
	// cleared, we risk losing previous information related to what the user
	// liked so just return the previous query
	if (invertedIndex.size() == 0) {
	    return queryTokens;
	}

	Float[] query = new Float[invertedIndex.size()];
	Float[] relevantDocuments = new Float[invertedIndex.size()];
	Float[] nonRelevantDocuments = new Float[invertedIndex.size()];
	
	String[] wordEntries = invertedIndex.keySet().toArray(new String[0]);
	for (int i = 0; i < wordEntries.length; i++) {
	    relevantDocuments[i] = 0f;
	    nonRelevantDocuments[i] = 0f;
	    
	    if (queryTokens != null && queryTokens.containsKey(wordEntries[i])) {
		query[i] = queryTokens.get(wordEntries[i]);
	    } else {
		query[i] = 0f;
	    }

	    IndexEntry indexEntry = invertedIndex.get(wordEntries[i]);
	    float idf = indexEntry.getIdf();
	    for (Map.Entry<Long, Posting> postingsEntry : indexEntry.getPostings().entrySet()) {
		Short tf = postingsEntry.getValue().getTf();

		if (isRelevantDoc(postingsEntry.getKey(), relevantEntries)) {
		    relevantDocuments[i] += (tf.equals(1)) ? idf : tf * idf;
		    nonRelevantDocuments[i] += 0f;
		} else {
		    nonRelevantDocuments[i] += (tf.equals(1)) ? idf : tf * idf;
		    relevantDocuments[i] += 0f;
		}
	    }
	}
	
	// query component
	Float[] normalizedQuery = RelevanceFeedbackUtil.multiplyVectorByScalar(query, queryWeight);

	// relevant entries component
	float scalar = relevantDocumentsWeight
		* ((float) 1 / ((relevantEntries.length == 0) ? 1 : relevantEntries.length));
	Float[] relevantDjEntries = RelevanceFeedbackUtil.multiplyVectorByScalar(relevantDocuments, scalar);

	// non-relevant entries component
	scalar = nonRelevantDocumentsWeight
		* ((float) 1 / (invertedIndexEngine.getNumberOfDocuments() - relevantEntries.length));
	Float[] nonRelevantDkEntries = RelevanceFeedbackUtil.multiplyVectorByScalar(nonRelevantDocuments, scalar);

	// final calculation: normalizedQueryVector + relevantEntriesVector -
	// nonRelevantEntriesVector
	Float[] result = RelevanceFeedbackUtil.addVectors(normalizedQuery, relevantDjEntries);
	result = RelevanceFeedbackUtil.substractVectors(result, nonRelevantDkEntries);

	Map<String, Float> resultingQueryTokens = new HashMap<String, Float>();
	for (int i = 0; i < wordEntries.length; i++) {
	    if (result[i] > 0) {
		resultingQueryTokens.put(wordEntries[i], result[i]);
	    }
	}

	return resultingQueryTokens;
    }

    /**
     * Checks if the entry passed in is among the relevant ones.
     * 
     * @param entry
     *            the entry to check it it's relevant or not
     * @return
     */
    private boolean isRelevantDoc(Long entryId, Long[] relevantEntries) {
	for (int i = 0; i < relevantEntries.length; i++) {
	    if (relevantEntries[i].compareTo(entryId) == 0)
		return true;
	}

	return false;
    }

    @Autowired
    public void setIndexEngine(IndexEngine invertedIndexEngine) {
	this.invertedIndexEngine = invertedIndexEngine;
    }
}
