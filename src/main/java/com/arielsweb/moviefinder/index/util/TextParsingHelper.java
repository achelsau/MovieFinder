package com.arielsweb.moviefinder.index.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tartarus.snowball.StemWord;
import org.tartarus.snowball.Stopwords;

import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;
import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * Utility class that, given an arbitrary text, maintains only alphanumeric
 * characters and, after that, eliminates stopwords and does stemming. On top of
 * that, it ignores numbers (years, code-numbers etc) to make indexing only word
 * oriented for the moment.
 * 
 * @author Ariel
 * 
 */
public class TextParsingHelper {
    private static StemWord stemmer = new StemWord();
    private static Pattern patt = Pattern.compile("[0-9]+"); // numbers

    // any non letter symbol, non numeric and non space character will be
    // converted to space ' '
    private static Pattern anyNonLetterNonNumberNonSpace = Pattern.compile("[^\\p{L}\\p{N}\\s]");

    /**
     * Keeps only the alpha-numeric values along with spaces
     * 
     * @param input
     *            the string to be parsed
     * @return the string cleaned from special characters
     */
    private static String keepAlphaOnly(String input) {
	Matcher matcher = anyNonLetterNonNumberNonSpace.matcher(input);
	String alphaNumericUTF8Chars = matcher.replaceAll(" ");

	return alphaNumericUTF8Chars.toString();
    }

    /**
     * Parses the raw metadata received from the database entities
     * 
     * @param text
     *            the text to be parsed
     * @return the array of parsed elements of the description
     */
    public static String[] parseText(String text) {
	if (text == null) {
	    // then return an empty string
	    return new String[0];
	}

	Matcher match;
	String parsedMeta = keepAlphaOnly(text.trim());
	String[] words = parsedMeta.toString().split("\\s"); // split by space
							     // separator
							     // assuming western
							     // languages
	StringBuilder parsedText = new StringBuilder();
	for (int i = 0; i < words.length; i++) {
	    match = patt.matcher(words[i]);
	    if (match.matches()) {
		continue;
	    }

	    if (words[i].compareTo(MovieFinderConstants.STR_SPACE) != 0 && words[i].compareTo(MovieFinderConstants.STR_EMPTY) != 0) {
		if (Stopwords.isStopword(words[i]) == false) {
		    stemmer.setWord(words[i]);
		    String parsedWord = stemmer.getAttribute().toLowerCase();
		    parsedText.append(parsedWord + MovieFinderConstants.STR_SPACE);
		}
	    }
	}

	return parsedText.toString().split(MovieFinderConstants.STR_SPACE); // return
									    // //
									    // words
    }

    public static Map<String, Float> getQueryWeights(String[] queryTokens) {
	// get default query weights (tf * idf), where idf is, by default, 1
	Map<String, Short> queryTfs = new HashMap<String, Short>();
	for (String queryToken : queryTokens) {
	    short tf = (queryTfs.get(queryToken) == null) ? 0 : queryTfs.get(queryToken);

	    queryTfs.put(queryToken, ++tf);
	}

	Map<String, Float> queryWeights = new HashMap<String, Float>();
	for (String queryToken : queryTokens) {
	    queryWeights.put(queryToken, 1f * queryTfs.get(queryToken));
	}
	return queryWeights;
    }

    public static Map<String, Float> getQueryWeights(List<PersistentQueryToken> persistentQueryTokens) {
	Map<String, Float> queryWeights = new HashMap<String, Float>();

	for (PersistentQueryToken persistentQueryToken : persistentQueryTokens) {
	    queryWeights.put(persistentQueryToken.getToken(), persistentQueryToken.getWeight());
	}

	return queryWeights;
    }

    public static List<PersistentQueryToken> getQueryTokensListFromMap(PersistentQuery persistentQuery,
	    Map<String, Float> queryWeights) {
	List<PersistentQueryToken> persistentQueryTokens = new ArrayList<PersistentQueryToken>();
	for (Map.Entry<String, Float> queryWeightEntry : queryWeights.entrySet()) {
	    PersistentQueryToken persistentQueryToken = new PersistentQueryToken();
	    persistentQueryToken.setParentQuery(persistentQuery);
	    persistentQueryToken.setToken(queryWeightEntry.getKey());
	    persistentQueryToken.setWeight(queryWeightEntry.getValue());

	    persistentQueryTokens.add(persistentQueryToken);
	}
	return persistentQueryTokens;
    }
}

