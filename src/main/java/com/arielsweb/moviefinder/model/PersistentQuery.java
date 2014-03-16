package com.arielsweb.moviefinder.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Represents a query that is persisted into the database. It contains a
 * reference to its owner, which is the {@link User}.
 * 
 * @author Ariel
 * 
 */
@Entity()
@XmlRootElement(name = "PersistentQuery")
public class PersistentQuery {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    @Column(name = "interv")
    private Long interval;

    @Column(name = "query_string")
    private String queryString;

    @OneToMany(mappedBy = "parentQuery", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PersistentQueryToken> tokens = new ArrayList<PersistentQueryToken>();

    public PersistentQuery() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @JsonIgnore
    public User getOwner() {
	return owner;
    }

    @JsonIgnore
    public void setOwner(User owner) {
	this.owner = owner;
    }

    public Long getInterval() {
	return interval;
    }

    public void setInterval(Long interval) {
	this.interval = interval;
    }

    public String getQueryString() {
	return queryString;
    }

    public void setQueryString(String queryString) {
	this.queryString = queryString;
    }

    public List<PersistentQueryToken> getTokens() {
	return tokens;
    }

    public void setTokens(List<PersistentQueryToken> tokens) {
	this.tokens = tokens;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((interval == null) ? 0 : interval.hashCode());
	result = prime * result + ((queryString == null) ? 0 : queryString.hashCode());
	result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
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
	PersistentQuery other = (PersistentQuery) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (interval == null) {
	    if (other.interval != null)
		return false;
	} else if (!interval.equals(other.interval))
	    return false;
	if (queryString == null) {
	    if (other.queryString != null)
		return false;
	} else if (!queryString.equals(other.queryString))
	    return false;
	if (tokens == null) {
	    if (other.tokens != null)
		return false;
	} else {
	    if (tokens.size() != other.tokens.size()) {
		return false;
	    }

	    for (PersistentQueryToken persistentQueryToken : tokens) {
		if (!other.tokens.contains(persistentQueryToken)) {
		    return false;
		}
	    }
	}
	return true;
    }
}
