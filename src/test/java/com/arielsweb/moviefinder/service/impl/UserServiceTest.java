package com.arielsweb.moviefinder.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.UserService;

@DataSet("AllDaoTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class UserServiceTest implements IGenericServiceTest<User> {
	@SpringBeanByType
	private UserService userDao;
	private final String usernameExpected = "Ion";

	@Test
	@Override
	public void testFind() {
		User userQuery = null;
		userQuery = userDao.find(1L);
		Assert.assertEquals(new Long(1), userQuery.getId());
		Assert.assertEquals(usernameExpected, userQuery.getUsername());
	}

	@Test
	@Override
	public void testUpdate() {
		String changedName = "Gigi";

		User user = new User();
		user.setId(1L);
		user.setRealName(changedName);
		user.setIsOnline(true);
		user.setPassword("qwerty");
		user.setUsername("testName");

		/** change the name **/
		userDao.update(user);

		User actual = userDao.find(1L);
		Assert.assertEquals(changedName, actual.getRealName());

	}

	@Test
	@Override
	public void testSave() {
		User newUser = new User();
		newUser.setUsername("New User");
		newUser.setPassword("obfuscated");
		newUser.setRealName("Real Name");
		newUser.setIsOnline(true);

		userDao.save(newUser);

		User actual = userDao.find(newUser.getId());
		Assert.assertEquals(actual, newUser);
	}

	@Test
	public void testSetIsOnline() {
		userDao.setOnline(1L);

		User actual = userDao.find(1L);
		Assert.assertEquals(true, actual.getIsOnline());
	}

	@Test
	@Override
	public void testDeleteByEntity() {
		User usr = new User();
		usr.setId(3L);
		userDao.delete(usr);
		User actual = userDao.find(3L);
		Assert.assertNull(actual);

	}

	/**
	 * <tab_users id="1" username="Ion" password="abcd" real_name="abcd"
	 * location="No" is_online="0" />
	 */
	@Test
	public void testGetUserByUsername() {
		User user = userDao.getUserByUsername("Ion");

		Assert.assertEquals("Ion", user.getUsername());
		Assert.assertEquals("abcd", user.getRealName());
		Assert.assertEquals("abcd", user.getPassword());
		Assert.assertEquals("No", user.getLocation());
		Assert.assertEquals(false, user.getIsOnline());
	}

	@Test
	@Override
	public void testDeleteById() {
		userDao.delete(3L);
		User actual = userDao.find(3L);
		Assert.assertNull(actual);
	}
}
