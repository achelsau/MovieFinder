package com.arielsweb.moviefinder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a web-site or another owner on the web of a specific movie. A
 * source may contain many {@link MovieDescriptor}. But a
 * {@link MovieDescriptor} can be assigned to only one source.
 * 
 * @author Ariel
 * 
 */
@Entity
@XmlRootElement
public class MovieSource implements Serializable {

	private static final long serialVersionUID = -6689879649964736112L;

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@Column
	private String location;

	public MovieSource() {
	}

	/**
	 * Getters and setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean equals(Object source) {
		if (source instanceof MovieSource) {
			if (this.id != null && ((MovieSource) source).getId() != null) {
				return this.id.equals(((MovieSource) source).getId());
			}

			return this.location.equals(((MovieSource) source).getLocation())
					&& this.name.equals(((MovieSource) source).getName());
		}

		return false;
	}
}
