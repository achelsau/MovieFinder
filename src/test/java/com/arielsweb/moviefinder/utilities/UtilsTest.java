/**
 * 
 */
package com.arielsweb.moviefinder.utilities;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;

import com.arielsweb.moviefinder.model.User;

/**
 * @author Ariel
 * 
 */

@RunWith(UnitilsJUnit4TestClassRunner.class)
public class UtilsTest {

	@Test
	public void testGetTableForEntity() {
		Class<? extends Object> clazz = User.class;
		String tableName = Utils.getTableForEntity(clazz);

		Assert.assertEquals(User.class.getSimpleName().toUpperCase(), tableName);
	}
}
