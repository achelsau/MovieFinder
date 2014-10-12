package com.arielsweb.moviefinder.service.impl;

/**
 * Generic interface for enforcing the test for basic methods
 * 
 * @author Ariel
 * @date 1/12/2011
 */
public interface IGenericServiceTest<T> {

	void testFind();

	void testUpdate();

	void testSave();

	void testDeleteByEntity();

	void testDeleteById();
}
