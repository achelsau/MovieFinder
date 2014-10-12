package com.arielsweb.moviefinder.index.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.tartarus.snowball.StemWord;
import org.tartarus.snowball.Stopwords;

/**
 * Utility class that, given an arbitrary text, maintains only alphanumeric
 * characters and, after that, eliminates stopwords and does stemming. On top of
 * that, it ignores numbers (years, code-numbers etc) to make indexing only word
 * oriented for the moment
 * 
 * @author Ariel
 * 
 */
public class TextParsingProperNounsHelper {

	private static StemWord stemmer = new StemWord();
	private static Pattern patt = Pattern.compile("[0-9]+"); // numbers

	/** The logger. */
	private static org.apache.log4j.Logger log = Logger.getLogger(TextParsingProperNounsHelper.class);

	/**
	 * Checks what chars can be kept by the keepOnly method
	 * 
	 * @param c
	 *            the char
	 * @return true->can be kept; false->the other way
	 */
	private static boolean canKeep(char c) {
		if (c >= 'a' && c <= 'z') {
			return true;
		}
		if (c >= 'A' && c <= 'Z') {
			return true;
		}
		if (c >= '0' && c <= '9') {
			return true;
		}
		if (c == ' ') {
			return true;
		}

		// character is not wanted but it will be found
		// if there are proper nouns in the text
		if (c == '_') {
			return true;
		}
		return false;
	}

	/**
	 * Keeps only the alpha-numeric values along with spaces
	 * 
	 * @param input
	 *            the string to be parsed
	 * @return the string cleaned from special characters
	 */
	private static StringBuilder keepAlphaOnly(StringBuilder input) {
		for (int i = 0; i < input.toString().length(); i++) {
			if (!canKeep(input.charAt(i))) {
				input.replace(i, i + 1, " "); // add space in place of the
				// previous unwanted char
			}
		}
		return input;
	}

	/**
	 * Preserves nouns that start with a capital letter by adding a "_" to
	 * prevent them being split by space. When the stopwords will be eliminated,
	 * 
	 * @return the previous text with an "_" added between capital letter words
	 */
	private static Set<String> preserveProperNouns(StringBuilder input) {
		Pattern wordsStartingWithCapital = Pattern.compile("(?:\\s*\\b([A-Z][a-z]+)\\b)+");

		Set<String> properNouns = new HashSet<String>();
		Matcher matcher = wordsStartingWithCapital.matcher(input);
		while (matcher.find()) {
			StringBuilder capitalLetterGroup = new StringBuilder(matcher.group());

			char[] letterGroupCharArray = capitalLetterGroup.toString().toCharArray();

			// start from 1 to avoid adding "_" at the begining of a proper noun
			// (it makes more sense for those to be stemmed if possible: i.e.
			// Germanic -> german)
			for (int i = 1; i < letterGroupCharArray.length; i++) {
				if (letterGroupCharArray[i] == ' ') {
					capitalLetterGroup.replace(i, i + 1, "_");
				}
			}

			// maintain the original form of proper nouns
			properNouns.add(capitalLetterGroup.toString().trim());

			input.replace(matcher.start(), matcher.end(), capitalLetterGroup.toString());
		}

		return properNouns;
	}

	/**
	 * Parses the raw metadata received from the database entities
	 * 
	 * @param text
	 *            the text to be parsed
	 * @return the array of parsed elements of the description
	 */
	public static String[] parseText(String text) {

		Matcher match;
		StringBuilder textToParse = new StringBuilder(text);

		Set<String> properNouns = preserveProperNouns(textToParse);
		textToParse = keepAlphaOnly(textToParse);

		// split by space separator assuming western languages
		String[] words = textToParse.toString().split("\\s");

		List<String> tokens = new LinkedList<String>();
		for (int i = 0; i < words.length; i++) {
			match = patt.matcher(words[i]);
			if (match.matches()) {
				continue;
			}

			if (words[i].equals(" ") || words[i].equals("")) {
				continue;
			}

			if (properNouns.contains(words[i])) {
				// revert the '_' to ' ' as it was before
				words[i] = words[i].replace('_', ' ');

				// extract stopwords from compounds such as
				// "The Great Piramid"
				StringBuilder parsedProperNoun = new StringBuilder();
				for (String properNounToken : words[i].split(" ")) {
					if (!Stopwords.isStopword(properNounToken)) {
						parsedProperNoun.append(properNounToken + " ");
					}
				}

				if (parsedProperNoun.toString() == null || parsedProperNoun.toString().equals(" ")
						|| parsedProperNoun.toString().equals("")) {
					continue;
				}

				// Stems single words (e.g. Germanic -> German, Saxons ->
				// Saxon). It might be possible to stem Huygens to
				// Huygen (which is a proper noun) but we'll live with that
				if (parsedProperNoun.toString().split(" ").length == 1) {
					stemmer.setWord(parsedProperNoun.toString().trim());
					String parsedWord = stemmer.getAttribute().toLowerCase();
					tokens.add(parsedWord.toString());
				} else { // avoid stemming for tokens such as Roman Empire,
					// United Nations, etc
					tokens.add(parsedProperNoun.toString().trim());
				}
			} else {
				if (Stopwords.isStopword(words[i]) == false) {
					stemmer.setWord(words[i].trim());
					String parsedWord = stemmer.getAttribute().toLowerCase();

					tokens.add(parsedWord.toString());
				}
			}
		}

		return tokens.toArray(new String[0]);
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String[] stemmedTokens = parseText("Director Ang Lee creates a groundbreaking movie event about a young man who survives a disaster at sea and is hurtled into an epic journey of adventure and discovery. While cast away, he forms an amazing and unexpected connection with another survivor...a fearsome Bengal tiger.");
		long end = System.currentTimeMillis();

		log.info(end - start);
		log.info(stemmedTokens.length);
		for (String str : stemmedTokens) {
			log.info(str);
		}
	}

}