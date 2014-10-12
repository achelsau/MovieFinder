package com.arielsweb.moviefinder.service.impl;

import org.springframework.stereotype.Repository;

import com.arielsweb.moviefinder.model.MovieSource;
import com.arielsweb.moviefinder.service.MovieSourceService;

/**
 * Service that deals with {@link MovieSource} entities
 * 
 * @author Ariel
 * @data 24/11/2012
 * 
 */
@Repository("movieSourceServiceImpl")
public class MovieSourceServiceImpl extends GenericServiceImpl<MovieSource> implements MovieSourceService {

	@Override
	protected String getTableName() {
		return MovieSource.class.getSimpleName().toUpperCase();
	}

}
