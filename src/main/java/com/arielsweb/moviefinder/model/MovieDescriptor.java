package com.arielsweb.moviefinder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * Offers a textual description of a movie from a web-site or another location.
 * It is connected to a {@link MovieSource} and can be marked as being relevant
 * (by the user). The movies won't be saved into the DB unless they have either
 * a synopsis or, at least, a critics consensus.
 * 
 * @author Ariel
 * 
 */
@Entity
@XmlRootElement
public class MovieDescriptor implements Serializable {

	private static final long serialVersionUID = -8564266363632340747L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * the id from the API/database this movie was taken; by using this, further
	 * details can be obtained
	 **/
	@Column(name = "remote_id")
	private String remoteId;

	/**
	 * It's always good to offer alternate sites for this movie if possible. For
	 * instance, when taking a movie from Rotten Tomatoes API, they offer a link
	 * to the imdb movie so that's a valuable extra info.
	 */
	@Column(name = "alternate_id")
	private String alternateId;

	/**
	 * The actual name of the movie/article/news that was taken from a remote
	 * service and saved into the local database
	 */
	private String name;

	/**
	 * Year of aparition of the movie
	 */
	private Integer year;

	/**
	 * The release date the movie (in US mainly)
	 */
	@Column(name = "release_date")
	private Date releaseDate;

	/**
	 * The remote path (URL) of the movie
	 */
	@Column(name = "remote_path")
	private String remotePath;

	/**
	 * The website/API owner from which the movie was taken
	 */
	// no operations are cascaded here by default
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source")
	@XmlElement
	private MovieSource source;

	/**
	 * The main body of textual description of this movie (the movie won't be
	 * saved into the DB unless they have either a synopsis or, at least, a
	 * critics consensus)
	 */
	private String synopsis;

	/**
	 * The secondary body of textual description of this movie (the movie won't
	 * be saved into the DB unless they have either a synopsis or, at least, an
	 * alternate synopsis)
	 */
	@Column(name = "alternate_synopsis")
	private String alternateSynopsis;

	/** A nice to have item would be a thumbnail for this movie **/
	@Column(name = "image_path")
	private String imagePath;

	/**
	 * Movie genres such as: ACTION, THRILLER, SCI-FI, etc.
	 */
	@ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "GENRE", joinColumns = @JoinColumn(name = "movie_descriptor_id"))
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "genre_name")
	private Set<Genre> genres;

	/**
	 * It's best that actors, directors and screenwriters are kept on Lazy
	 * Loading because you might need a movie descriptor when marking it
	 * relevant and then you don't need to drag all the cast and crew. Also,
	 * it's easier to Cascade all operations.
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "CAST_CREW_IN_MOVIE", joinColumns = { @JoinColumn(name = MovieFinderConstants.MOVIE_ID) }, inverseJoinColumns = { @JoinColumn(name = MovieFinderConstants.CAST_AND_CREW_ID) })
	private Set<MovieCrewPerson> castAndCrew = new HashSet<MovieCrewPerson>();

	/**
	 * The rating from the source: such as IMDB rating
	 */
	private Double rating;

	/**
	 * For optimization reasons these 3 hashsets are populated when cast and
	 * crew is set and then returned whenever the getters of each is called.
	 */
	@Transient
	private Set<MovieCrewPerson> movieActors = new HashSet<MovieCrewPerson>();

	@Transient
	private Set<MovieCrewPerson> movieDirectors = new HashSet<MovieCrewPerson>();

	@Transient
	private Set<MovieCrewPerson> movieScreenwriters = new HashSet<MovieCrewPerson>();

	public MovieDescriptor() {

	}

	/**
	 * Getters & Setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public MovieSource getSource() {
		return source;
	}

	public void setSource(MovieSource source) {
		this.source = source;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getAlternateSynopsis() {
		return alternateSynopsis;
	}

	public void setAlternateSynopsis(String alternateSynopsis) {
		this.alternateSynopsis = alternateSynopsis;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	/**
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public String getAlternateId() {
		return alternateId;
	}

	public void setAlternateId(String alternateId) {
		this.alternateId = alternateId;
	}

	public Set<MovieCrewPerson> getActors() {
		if (movieActors.size() == 0) {
			for (MovieCrewPerson movieCastAndCrew : castAndCrew) {
				switch (movieCastAndCrew.getCrewPersonType()) {
				case ACTOR:
					movieActors.add(movieCastAndCrew);
					break;
				}
			}
		}

		return movieActors;
	}

	/**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}

	/**
	 * @return the castAndCrew
	 */
	public Set<MovieCrewPerson> getCastAndCrew() {
		return castAndCrew;
	}

	/**
	 * @param castAndCrew
	 *            the castAndCrew to set
	 */
	public void setCastAndCrew(Set<MovieCrewPerson> castAndCrew) {
		for (MovieCrewPerson movieCastAndCrew : castAndCrew) {
			switch (movieCastAndCrew.getCrewPersonType()) {
			case ACTOR:
				movieActors.add(movieCastAndCrew);
				break;
			case DIRECTOR:
				movieDirectors.add(movieCastAndCrew);
				break;
			case SCREENWRITER:
				movieScreenwriters.add(movieCastAndCrew);
				break;
			}
		}

		this.castAndCrew = castAndCrew;
	}

	public void setActors(Set<MovieCrewPerson> actors) {
		this.movieActors = actors;

		this.castAndCrew.addAll(movieActors);
	}

	public Set<MovieCrewPerson> getDirectors() {
		if (movieDirectors.size() == 0) {
			for (MovieCrewPerson movieCastAndCrew : castAndCrew) {
				switch (movieCastAndCrew.getCrewPersonType()) {
				case DIRECTOR:
					movieDirectors.add(movieCastAndCrew);
					break;
				}
			}
		}

		return movieDirectors;
	}

	public void setDirectors(Set<MovieCrewPerson> directors) {
		this.movieDirectors = directors;

		this.castAndCrew.addAll(movieDirectors);
	}

	public Set<MovieCrewPerson> getScreenWriters() {
		if (movieScreenwriters.size() == 0) {
			for (MovieCrewPerson movieCastAndCrew : castAndCrew) {
				switch (movieCastAndCrew.getCrewPersonType()) {
				case SCREENWRITER:
					movieScreenwriters.add(movieCastAndCrew);
					break;
				}
			}
		}

		return this.movieScreenwriters;
	}

	public void setScreenwriters(Set<MovieCrewPerson> screenwriters) {
		this.movieScreenwriters = screenwriters;

		this.castAndCrew.addAll(movieScreenwriters);
	}

	public boolean equals(Object movieDescriptor) {
		if (movieDescriptor instanceof MovieDescriptor) {
			// if both instances are persisted then compare them by database id
			if (((MovieDescriptor) movieDescriptor).getId() != null && this.id != null) {
				return this.id.equals(((MovieDescriptor) movieDescriptor).getId());
			}

			// if one or both instances aren't persisted compare them by source
			// && remoteId (which is also a compoud unique key at the DB level)
			return ((MovieDescriptor) movieDescriptor).getSource().getId().equals(this.getSource().getId())
					&& ((MovieDescriptor) movieDescriptor).getRemoteId().equals(this.remoteId);
		}

		return false;
	}

}
