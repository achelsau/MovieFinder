package com.arielsweb.moviefinder.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * The table name is hardcoded whereas the column names are written in a
 * constants class because they are needed also in the SQL statements in their
 * specific Services and it's desired to avoid expensive and repetitive
 * reflection methods.
 * 
 * @author Ariel
 * @date 23/11/2011
 * 
 */
@Entity
@XmlRootElement
public class User {
	@Id
	@GeneratedValue
	private Long id;
	private String username;
	private String password;
	private String location;
	private String role;
	@Column(name = MovieFinderConstants.USER_REAL_NAME)
	private String realName;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "RELEVANT_MOVIES", joinColumns = { @JoinColumn(name = MovieFinderConstants.USER_ID) }, inverseJoinColumns = { @JoinColumn(name = MovieFinderConstants.MOVIE_ID) })
	private List<MovieDescriptor> relevantMovies;
	@Column(name = MovieFinderConstants.USER_IS_ONLINE)
	private Boolean isOnline;

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public List<MovieDescriptor> getRelevantMovies() {
		return relevantMovies;
	}

	public void setRelevantMovies(List<MovieDescriptor> relevantMovies) {
		this.relevantMovies = relevantMovies;
	}

	public Boolean getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		if (username == null) {
			if (other.getUsername() != null)
				return false;
		} else if (!username.equals(other.getUsername()))
			return false;
		return true;
	}
}
