package com.arielsweb.moviefinder.service.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;

import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.PersistentQueryService;

/**
 * Tests the {@link PersistentQueryService}
 * 
 * @author Ariel
 * 
 */
@DataSet("AllDaoTest.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext-data-test.xml" })
@Transactional(TransactionMode.ROLLBACK)
public class PersistentQueryServiceTest implements IGenericServiceTest<PersistentQuery> {
    @SpringBeanByType
    private PersistentQueryService queryDao;
    private static final Long THREE = new Long(3);

    @Test
    @Override
    public void testFind() {
	String exMovieName = "Interstellar mission";
	PersistentQuery query = queryDao.find(1L);
	Assert.assertEquals(exMovieName, query.getQueryString());
    }

    @Test
    @Override
    public void testUpdate() {
	String newQuery = "Solar System around Alpha Centauri";
	PersistentQuery userQuery = new PersistentQuery();
	userQuery.setId(3L);
	userQuery.setQueryString(newQuery);
	userQuery.setInterval(2000L);

	User owner = new User();
	owner.setId(1L);
	userQuery.setOwner(owner);
	queryDao.update(userQuery);

	PersistentQuery actual = queryDao.find(3L);
	Assert.assertEquals(THREE, actual.getId());
	Assert.assertEquals(newQuery, actual.getQueryString());
    }

    @Test
    @Override
    public void testSave() {
	PersistentQuery query = new PersistentQuery();
	query.setInterval(1000L);
	query.setQueryString("Extraterrestrial life");
	User owner = new User();
	owner.setId(1L);
	query.setOwner(owner);

	queryDao.save(query);

	PersistentQuery actual = queryDao.find(query.getId());

	Assert.assertEquals(query, actual);
    }

    @Test
    @Override
    public void testDeleteByEntity() {
	queryDao.delete(1L);
	PersistentQuery actual = queryDao.find(1L);
	Assert.assertNull(actual);
    }

    @Test
    @Override
    public void testDeleteById() {
	PersistentQuery query = new PersistentQuery();
	query.setId(1L);

	queryDao.delete(query);
	PersistentQuery actual = queryDao.find(1L);
	Assert.assertNull(actual);
    }

    @Test
    public void testGetQueriesForUser() {
	Long[] expectedIds = { new Long(4), new Long(5) };
	List<PersistentQuery> queries = queryDao.getQueriesForUser(3L);
	Assert.assertEquals(2, queries.size());
	int i = 0;
	for (Long expId : expectedIds) {
	    Assert.assertEquals(expId, queries.get(i).getId());
	    i++;
	}
    }

    @Test
    public void testGetQueriesForOnlineUsers() {
	Long[] expectedIds = { new Long(3), new Long(6), new Long(7) };
	List<PersistentQuery> queries = queryDao.getQueriesForOnlineUsers();
	int i = 0;
	for (Long expId : expectedIds) {
	    Assert.assertEquals(expId, queries.get(i).getId());
	    i++;
	}
    }
}
