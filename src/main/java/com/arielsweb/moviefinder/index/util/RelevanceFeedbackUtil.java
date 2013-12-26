package com.arielsweb.moviefinder.index.util;

import com.arielsweb.moviefinder.index.IndexEngine;

/**
 * Provides some util methods for calculating the refined query
 * 
 * @author Ariel
 * 
 */
public class RelevanceFeedbackUtil {

    /**
     * Performs the classical multiplication by scalar for a vector. It takes
     * the values from the vector and multiplies each of them by the scalar.
     * 
     * @param vector
     *            the vector to be multiplied
     * @param scalar
     *            the scalar to multiply the vector to
     * @return the resulting vector after multiplication is done.
     */
    public static Float[] multiplyVectorByScalar(Float[] vector, float scalar) {
	for (int i = 0; i < vector.length; i++) {
	    vector[i] = vector[i] * scalar;
	}

	return vector;
    }

    /**
     * Modifies the first vector parameter by adding vector2.
     * 
     * @param vector1
     *            the first vector to add
     * @param vector2
     *            the second vector to add
     * @return vector1 as the some of vector1 and vector2
     */
    public static Float[] addVectors(Float[] vector1, Float[] vector2) {
	if (vector1.length != vector2.length) {
	    return null;
	}

	for (int i = 0; i < vector1.length; i++) {
	    vector1[i] += vector2[i];
	}

	return vector1;
    }

    /**
     * Modifies the first vector parameter by substracting vector2 from it
     * 
     * @param vector1
     *            the vector to substract from
     * @param vector2
     *            the vector to substract with
     * @return vector1 as the difference between vector1 and vector2
     */
    public static Float[] substractVectors(Float[] vector1, Float[] vector2) {
	if (vector1.length != vector2.length) {
	    return null;
	}

	for (int i = 0; i < vector1.length; i++) {
	    vector1[i] -= vector2[i];
	}

	return vector1;
    }

    /**
     * Gets the query vector
     * 
     * @param query
     * @param invertedIndexEngine
     * @return
     */
    public static Float[] getQueryVector(String query, IndexEngine invertedIndexEngine) {
	String[] queryTokens = TextParsingHelper.parseText(query);
	Float[] queryW = new Float[queryTokens.length];

	String[] corpusWords = new String[0];
	corpusWords = invertedIndexEngine.getInvertedIndex().keySet().toArray(corpusWords);
	// first query weights
	for (int i = 0; i < corpusWords.length; i++) {
	    boolean found = false;

	    for (int j = 0; j < queryTokens.length; j++) {
		if (corpusWords[i].compareTo(queryTokens[j]) == 0) {
		    queryW[i] = 1f;
		    found = true;
		    break;
		}
	    }

	    if (found == false)
		queryW[i] = 0f;
	}

	return queryW;
    }

}
