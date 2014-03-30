package com.arielsweb.moviefinder.index.util;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;

import com.arielsweb.moviefinder.model.PersistentQueryToken;

/**
 * Tests the {@link TestParsingHelper}
 * 
 * @author Ariel
 * 
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class TextParsingHelperTest {

    /**
     * Tests the parsing of text
     */
    @Test
    public void testTextParsing() {
	// setup
	String testToParse = "The languages of Germanic peoples gave rise to the English language (the best known are the Angles, Saxons, Frisii, Jutes and possibly some Franks, who traded, fought with and lived alongside the Latin-speaking peoples of the Roman Empire in the centuries-long process of the Germanic peoples' expansion into Western Europe during the Migration Period). Latin loan words such as wine, cup, and bishop entered the vocabulary of these Germanic peoples before their arrival in Britain and the subsequent formation of England.";

	// execute
	String[] wordsParsed = TextParsingHelper.parseText(testToParse);

	// verify
	String[] expectedWords = { "languag", "german", "peopl", "gave", "rise", "english", "languag", "angles",
		"saxon", "frisii", "jute", "possibl", "frank", "trade", "fought", "live", "alongsid", "latin", "speak",
		"peopl", "roman", "empire", "centuri", "long", "process", "german", "peopl", "expans", "western",
		"europ", "migrat", "period", "latin", "loan", "word", "wine", "cup", "bishop", "enter", "vocabulari",
		"german", "peopl", "arriv", "britain", "subsequ", "format", "england" };


	assertEquals(47, wordsParsed.length);

	for (int i = 0; i < wordsParsed.length; i++) {
	    assertTrue(expectedWords[i].equalsIgnoreCase(wordsParsed[i]));
	}

    }

    /**
     * Tests the parsing of text
     */
    @Test
    public void testRTSynopsisParsing() {
	// setup
	String rottenTomatoesSynopsis = "17-year-old Tal has emigrated from France to Jerusalem with her family. She writes a letter expressing her refusal to accept that only hatred can reign between Israelis and Palestinians. She slips the letter into a bottle, and her brother throws it into the sea near Gaza, where he is carrying out his military service. A few weeks later, Tal receives an e-mail response from a mysterious 'Gazaman,' a young Palestinian named Nam. Thus begins a turbulent but tender long-distance friendship between two young people that are separated by a history they are trying both to understand and change. This engrossing and hopeful drama starring Hiam Abbas is based on the award-winning novel by Valrie Zenatti.";
	
	// execute
	String[] wordsParsed = TextParsingHelper.parseText(rottenTomatoesSynopsis);

	// verify
	String[] expectedWords = { "year", "tal", "emigr", "franc", "jerusalem", "famili", "write", "letter",
		"express", "refus", "accept", "hatr", "reign", "israeli", "palestinian", "slip", "letter", "bottl",
		"brother", "throw", "sea", "gaza", "carri", "militari", "servic", "week", "tal", "receiv", "mail",
		"respons", "mysteri", "gazaman", "young", "palestinian", "name", "nam", "begin", "turbul", "tender",
		"long", "distanc", "friendship", "young", "peopl", "separ", "histori", "understand", "chang",
		"engross", "hope", "drama", "star", "hiam", "abbas", "base", "award", "win", "valri", "zenatti" };

	assertEquals(59, wordsParsed.length);

	for (int i = 0; i < wordsParsed.length; i++) {
	    assertTrue(expectedWords[i].equalsIgnoreCase(wordsParsed[i]));
	}
    }

    /**
     * Test parsing UTF-8 description
     */
    @Test
    public void testUTF8Description() {
	// setup
	String synopsis = "After the gang busts the ancient kung fu king known as The Beast "
		+ "(Leung Siu Lung, Liáng Xiǎolóng, 梁小龙) out of jail, tensions reach a boiling point as Pig Sty Alley's "
		+ "landlady (Yuen Qiu, Yuán Hépíng, 袁和平) leads an all-out attack against the gang and Sing discovers his "
		+ "true heroic fate.";

	// execute
	String[] wordsParsed = TextParsingHelper.parseText(synopsis);

	// verify
	String[] expectedWords = { "gang", "bust", "ancient", "kung", "fu", "king", "beast", "leung", "siu", "lung",
		"liáng", "xiǎolóng", "梁小龙", "jail", "tension", "reach", "boil", "point", "pig", "sti", "alley",
		"landladi", "yuen", "qiu", "yuán", "hépíng", "袁和平", "lead", "attack", "gang", "sing", "discov", "true",
		"heroic", "fate" };

	assertEquals(35, wordsParsed.length);

	for (int i = 0; i < wordsParsed.length; i++) {
	    assertTrue(expectedWords[i].equalsIgnoreCase(wordsParsed[i]));
	}
    }

    @Test
    public void testGetQueryWeightsFromPersistentQueryTokens() {
	// setup
	List<PersistentQueryToken> persistentQueryTokens = new ArrayList<PersistentQueryToken>();
	PersistentQueryToken token1 = new PersistentQueryToken();
	token1.setToken("token1");
	token1.setWeight(1.2f);
	persistentQueryTokens.add(token1);
	
	PersistentQueryToken token2 = new PersistentQueryToken();
	token2.setToken("token2");
	token2.setWeight(1.3f);
	persistentQueryTokens.add(token2);

	// execute
	Map<String, Float> queryWeights = TextParsingHelper.getQueryWeights(persistentQueryTokens);

	// verify
	Float weight1 = queryWeights.get("token1");
	Assert.assertEquals(1.2f, weight1);
	    
	Float weight2 = queryWeights.get("token2");
	Assert.assertEquals(1.3f, weight2);
    }
}
