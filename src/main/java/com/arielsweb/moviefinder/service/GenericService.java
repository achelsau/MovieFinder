package com.arielsweb.moviefinder.service;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for the generic dao parameterized by T
 * 
 * @author Ariel
 * @date 1/12/2011
 * @version 1.0
 */
public interface GenericService<T> {
	/**
	 * Selects the entity of type T from the database
	 * 
	 * @param id
	 *            the id of the entity to get
	 * @return the object mapping to the row in the database
	 */
	T find(Long id);

	/**
	 * Updates an entity already existing in the database
	 * 
	 * @param entity
	 *            the entity to update
	 */
	void update(T entity);

	/**
	 * Saves (Inserts) an entity into the database
	 * 
	 * @param entity
	 *            the entity to save into the database
	 * @return the generated identifier
	 */
	Serializable save(T entity);

	/**
	 * Deletes an entity from the database based on a reference
	 * 
	 * @param entity
	 *            the entity to delete
	 */
	void delete(T entity);

	/**
	 * Deletes an entity from the database based on its Id
	 * 
	 * @param id
	 *            the id of the entity to delete
	 */
	void delete(Long id);

	/**
	 * Lists all the entities from the database
	 * 
	 * @return the list of entities present in the table
	 */
	List<T> list();

	/**
	 * Deletes all the rows from the table
	 */
	void deleteAll();

}
