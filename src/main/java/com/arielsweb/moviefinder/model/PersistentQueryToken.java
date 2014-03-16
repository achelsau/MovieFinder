package com.arielsweb.moviefinder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ariel
 *
 */
@Entity
@XmlRootElement
public class PersistentQueryToken {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_query")
    private PersistentQuery parentQuery;

    @Column
    private String token;

    @Column
    private Double weight;

    /**
     * @return the token
     */
    public String getToken() {
	return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
	this.token = token;
    }

    /**
     * @return the weight
     */
    public Double getWeight() {
	return weight;
    }

    /**
     * @param weight
     *            the weight to set
     */
    public void setWeight(Double weight) {
	this.weight = weight;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @return the parentQuery
     */
    public PersistentQuery getParentQuery() {
	return parentQuery;
    }

    /**
     * @param parentQuery
     *            the parentQuery to set
     */
    public void setParentQuery(PersistentQuery parentQuery) {
	this.parentQuery = parentQuery;
    }
}
