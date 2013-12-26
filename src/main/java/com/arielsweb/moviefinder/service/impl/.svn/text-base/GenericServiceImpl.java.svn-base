package com.arielsweb.moviefinder.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.service.GenericService;
import com.arielsweb.moviefinder.service.exceptions.DataAccessException;
import com.arielsweb.moviefinder.utilities.MovieFinderConstants;

/**
 * The Class GenericService that is later deriver in other DAOs
 * 
 * @param <T>
 *            the generic type
 */
@Repository("genericDao")
public abstract class GenericServiceImpl<T> implements GenericService<T> {

    /** The hibernate template. */
    protected SessionFactory sessionFactory;

    /** The table name. */
    protected String tableName = MovieFinderConstants.STR_EMPTY;

    /** The logger. */
    protected org.apache.log4j.Logger log = Logger.getLogger(GenericServiceImpl.class);
    protected Class<?> entityBeanType;

    /**
     * Instantiates a new generic dao.
     */
    public GenericServiceImpl() {
	tableName = getTableName();
	this.entityBeanType = ((Class<?>) ((ParameterizedType) getClass()
		.getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    /**
     * Sets the session factory.
     * 
     * @param sessionFactory
     *            the new session factory
     */
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    /**
     * Creates the SQL query using a shorthand method
     * 
     * @param sqlQuery
     *            the actual SQL query to create
     * @return the SQLQuery object created
     */
    protected SQLQuery createSQLQuery(String sqlQuery) {
	return sessionFactory.getCurrentSession()
		.createSQLQuery(sqlQuery);
    }

    @Transactional(readOnly = true)
    public T find(Long id) {
	try {
	    SQLQuery query = createSQLQuery(
		    "SELECT * from " + tableName + " WHERE id = " + id + ";")
		    .addEntity(entityBeanType);
	    @SuppressWarnings("unchecked")
	    List<T> results = query.list();
	    if (results.size() == 1) {
		return results.get(0);
	    } else {
		return null;
	    }
	} catch (EntityNotFoundException entityNotFoundEx) {
	    return null;
	}
    }

    @Transactional(readOnly = true)
    public List<T> list() {
	SQLQuery query = createSQLQuery("SELECT * from " + tableName).addEntity(entityBeanType);
	@SuppressWarnings("unchecked")
	List<T> results = query.list();
	return results;
    }

    @Transactional(readOnly = false)
    public void update(T uq) {
	if (uq != null) {
	    sessionFactory.getCurrentSession().update(uq);
	} else {
	    log.error(MovieFinderConstants.UPDATE_NULL_ERROR);
	    throw new DataAccessException(MovieFinderConstants.UPDATE_NULL_ERROR);
	}
    }

    @Override
    @Transactional(readOnly = false)
    public Serializable save(T entity) {
	if (entity != null) {
	    return sessionFactory.getCurrentSession().save(entity);
	} else {
	    log.error(MovieFinderConstants.SAVE_NULL_ERROR);
	    throw new DataAccessException(MovieFinderConstants.SAVE_NULL_ERROR);
	}
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(T entity) {
	if (entity != null) {
	    sessionFactory.getCurrentSession().delete(entity);
	} else {
	    log.error(MovieFinderConstants.SAVE_NULL_ERROR);
	    throw new DataAccessException(MovieFinderConstants.SAVE_NULL_ERROR);
	}
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
	if (id > 0) {
	    SQLQuery query = createSQLQuery("DELETE from " + tableName
		    + " WHERE id =" + id + ";");
	    query.executeUpdate();
	} else {
	    log.error(MovieFinderConstants.DELETE_NULL_ERROR);
	    throw new DataAccessException(MovieFinderConstants.DELETE_NULL_ERROR);
	}
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteAll() {
	SQLQuery query = createSQLQuery("DELETE from " + tableName);
	query.executeUpdate();
    }

    /**
     * Must be overridden by the concrete DAOs in order to expose the table name
     * that is mapped on the specific entity.
     * 
     * @return the String representing the table name
     */
    protected abstract String getTableName();
}
