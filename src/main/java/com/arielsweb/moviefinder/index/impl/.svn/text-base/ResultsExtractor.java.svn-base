package com.arielsweb.moviefinder.index.impl;

import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.index.dto.ResultInfo;

/**
 * Extracts the top K results from the whole list before sending it to the
 * client. Implements a priority queue using a heap
 * 
 * @author Ariel
 */
@Component
public class ResultsExtractor {

    private int listLength = -1;

    public ResultsExtractor() {
	super();
    }

    /**
     * Recreates the max heap
     * 
     * @param results
     *            the results list
     * @param i
     *            the current index from which to restore the heap property
     * @param length
     *            the length of the array (it's passed in because the
     *            results.length never modifies, just has some elements set to
     *            null. It's always faster to send the length before hand than
     *            to traverse the list and calculate it each time.
     */
    private void maxHeapify(ResultInfo[] results, int i, int length) {
	int l = 2 * i + 1;
	int r = 2 * i + 2;
	int largest = 0;
	if (l < length && results[l].getScore() > results[i].getScore()) {
	    largest = l;
	} else {
	    largest = i;
	}

	if (r < length && results[r].getScore() > results[largest].getScore()) {
	    largest = r;
	}

	if (largest != i) {
	    ResultInfo aux = results[i];
	    results[i] = results[largest];
	    results[largest] = aux;
	    maxHeapify(results, largest, length);
	}
    }

    public void buildMaxHeap(ResultInfo[] results) {
	for (int i = results.length / 2; i >= 0; i--) {
	    maxHeapify(results, i, results.length);
	}
    }

    /**
     * Gets the maximum element from the heap
     * 
     * @param results
     *            the results list
     * @return the result info which is maximum
     */
    public ResultInfo heapExtractMax(ResultInfo[] results) {
	// decrease the length by one because we will be extracting one element
	if (listLength == -1) {
	    listLength = results.length;
	    listLength--;
	} else {
	    listLength--;
	}
	

	// perform the heap extract operation
	ResultInfo max = results[0];
	results[0] = results[listLength];
	results[listLength] = null;

	// reconstruct the heap
	maxHeapify(results, 0, listLength);
	return max;
    }
}
