package com.arielsweb.moviefinder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @JsonIgnore
    public Long getInterval() {
	return interval;
    }

    @JsonIgnore
    public void setInterval(Long interval) {
	this.interval = interval;
    }

    public String getQueryString() {
	return queryString;
    }

    public void setQueryString(String queryString) {
	this.queryString = queryString;
    }
}
