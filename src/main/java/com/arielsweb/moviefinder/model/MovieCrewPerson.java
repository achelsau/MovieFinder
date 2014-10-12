package com.arielsweb.moviefinder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * Models a person that's involved in directing, writing or acting in a movie.
 * Eg: Steven Spielberg can be represented by this object. Brad Pitt can be as
 * well. etc. Usually crew person is different to actor but since we're going to
 * store the full name only this class will accommodate both.
 * 
 * @author Ariel
 * 
 */
@Entity
public class MovieCrewPerson implements Serializable {

	private static final long serialVersionUID = 6066058019869498075L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = MovieFinderConstants.FULL_NAME)
	private String fullName;

	@Column(name = MovieFinderConstants.TYPE)
	@Enumerated(EnumType.ORDINAL)
	private MovieCrewPersonType crewPersonType;

	/**
	 * Default constructor
	 */
	public MovieCrewPerson() {
	}

	/**
	 * Creates a new actor, director or screenwriter entity
	 * 
	 * @param fullName
	 *            the name of the crew or cast member
	 */
	public MovieCrewPerson(String fullName, MovieCrewPersonType crewPersonType) {
		this.fullName = fullName;
		this.crewPersonType = crewPersonType;
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the crewPersonType
	 */
	public MovieCrewPersonType getCrewPersonType() {
		return crewPersonType;
	}

	/**
	 * @param crewPersonType
	 *            the crewPersonType to set
	 */
	public void setCrewPersonType(MovieCrewPersonType crewPersonType) {
		this.crewPersonType = crewPersonType;
	}

	public String toString() {
		return this.fullName;
	}

	public int hashCode() {
		return this.fullName.hashCode();
	}

	/**
	 * Full name column is set as UNIQUE at the DB level
	 */
	public boolean equals(Object object) {
		if (object instanceof MovieCrewPerson) {
			return (((MovieCrewPerson) object).getFullName().equals(this.fullName));
		}

		return false;
	}
}
