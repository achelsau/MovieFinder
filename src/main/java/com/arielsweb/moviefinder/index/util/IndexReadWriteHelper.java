/**
 * 
 */
package com.arielsweb.moviefinder.index.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.arielsweb.moviefinder.index.IndexEngine;
import com.arielsweb.moviefinder.index.dto.IndexEntry;
import com.arielsweb.moviefinder.index.dto.MovieDetailsDTO;

/**
 * @author Ariel
 *
 */
public class IndexReadWriteHelper {

    public static boolean serializedIndexExists(String indexFile) throws FileNotFoundException {
	File file = new File("D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\" + indexFile);
	
	return file.exists() && !file.isDirectory();
    }

    public static void setCorpusAndMovieDetails(IndexEngine indexEngine, String fileName) throws IOException,
	    ClassNotFoundException {
	FileInputStream inStream = new FileInputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\" + fileName));
	ObjectInputStream ois = new ObjectInputStream(inStream);

	HashMap<String, IndexEntry> invertedIndex = readInvertedIndex(ois);
	HashMap<Long, MovieDetailsDTO> movieDetails = readMovieDetails(ois);

	indexEngine.setCorpus(invertedIndex);
	indexEngine.setMovieDetails(movieDetails);
    }

    @SuppressWarnings("unchecked")
    private static HashMap<String, IndexEntry> readInvertedIndex(ObjectInputStream ois) throws IOException,
	    ClassNotFoundException {
	HashMap<String, IndexEntry> invertedIndex = (HashMap<String, IndexEntry>) ois.readObject();

	return invertedIndex;
    }

    @SuppressWarnings("unchecked")
    private static HashMap<Long, MovieDetailsDTO> readMovieDetails(ObjectInputStream ois) throws IOException,
	    ClassNotFoundException {

	HashMap<Long, MovieDetailsDTO> movieDetails = (HashMap<Long, MovieDetailsDTO>) ois.readObject();

	return movieDetails;
    }

    public static void serializeIndex(IndexEngine indexEngine) throws FileNotFoundException, IOException {
	FileOutputStream outStream = new FileOutputStream(new File(
		"D:\\.facultate\\dizertatie\\MovieFinderServer_git\\dbscript\\index_serialized"));
	ObjectOutputStream oos = new ObjectOutputStream(outStream);
	oos.writeObject(indexEngine.getInvertedIndex());
	oos.writeObject(indexEngine.getMovieDetails());
    }

}
