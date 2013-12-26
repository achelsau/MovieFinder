package com.arielsweb.moviefinder.service.impl;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.model.MovieCrewPerson;
import com.arielsweb.moviefinder.service.MovieCrewPersonService;

/**
 * Manipulates {@link MovieCrewPerson} entities
 * 
 * @author Ariel
 * 
 */
@Repository("movieCrewPersonServiceImpl")
public class MovieCrewPersonServiceImpl extends GenericServiceImpl<MovieCrewPerson> implements MovieCrewPersonService {

    @Override
    @Transactional(readOnly = true)
    public MovieCrewPerson getMovieCrewPersonByName(String fullName) {
	MovieCrewPerson movieCrewPerson = (MovieCrewPerson) sessionFactory.getCurrentSession()
		.createCriteria(MovieCrewPerson.class)
		.add(Restrictions.eq("fullName", fullName)).uniqueResult();

	return movieCrewPerson;
    }

    @Override
    protected String getTableName() {
	return "TAB_MOVIE_CREW_PERSON";
    }

}
