package com.arielsweb.moviefinder.service.impl;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
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
import com.arielsweb.moviefinder.model.PersistentQueryToken;
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

	List<PersistentQueryToken> tokens = new ArrayList<PersistentQueryToken>();

	PersistentQueryToken token1 = new PersistentQueryToken();
	token1.setToken("solar");
	token1.setWeight(1.25f);
	token1.setParentQuery(userQuery);
	tokens.add(token1);

	PersistentQueryToken token2 = new PersistentQueryToken();
	token2.setToken("system");
	token2.setWeight(0.25f);
	token2.setParentQuery(userQuery);
	tokens.add(token2);

	userQuery.setTokens(tokens);

	PersistentQueryToken token3 = new PersistentQueryToken();
	token3.setToken("alpha");
	token3.setWeight(0.35f);
	token3.setParentQuery(userQuery);
	tokens.add(token3);

	PersistentQueryToken token4 = new PersistentQueryToken();
	token4.setToken("cenatur");
	token4.setWeight(0.55f);
	token4.setParentQuery(userQuery);
	tokens.add(token4);

	userQuery.setTokens(tokens);

	User owner = new User();
	owner.setId(1L);
	userQuery.setOwner(owner);
	queryDao.update(userQuery);

	PersistentQuery actual = queryDao.find(3L);
	Assert.assertEquals(THREE, actual.getId());
	Assert.assertEquals(newQuery, actual.getQueryString());
	Assert.assertEquals(userQuery.getTokens().size(), actual.getTokens().size());

	Assert.assertEquals(userQuery.getTokens().get(0).getToken(), actual.getTokens().get(0).getToken());
	Assert.assertEquals(userQuery.getTokens().get(0).getWeight(), actual.getTokens().get(0).getWeight());

	Assert.assertEquals(userQuery.getTokens().get(1).getToken(), actual.getTokens().get(1).getToken());
	Assert.assertEquals(userQuery.getTokens().get(1).getWeight(), actual.getTokens().get(1).getWeight());

	Assert.assertEquals(userQuery.getTokens().get(2).getToken(), actual.getTokens().get(2).getToken());
	Assert.assertEquals(userQuery.getTokens().get(2).getWeight(), actual.getTokens().get(2).getWeight());

	Assert.assertEquals(userQuery.getTokens().get(3).getToken(), actual.getTokens().get(3).getToken());
	Assert.assertEquals(userQuery.getTokens().get(3).getWeight(), actual.getTokens().get(3).getWeight());
    }

    @Test
    @Override
    public void testSave() {
	PersistentQuery query = new PersistentQuery();
	query.setInterval(1000L);
	query.setQueryString("Extraterrestrial life");

	List<PersistentQueryToken> tokens = new ArrayList<PersistentQueryToken>();

	PersistentQueryToken token1 = new PersistentQueryToken();
	token1.setToken("extraterrest");
	token1.setWeight(1.25f);
	token1.setParentQuery(query);
	tokens.add(token1);

	PersistentQueryToken token2 = new PersistentQueryToken();
	token2.setToken("life");
	token2.setWeight(0.25f);
	token2.setParentQuery(query);
	tokens.add(token2);

	query.setTokens(tokens);

	User owner = new User();
	owner.setId(1L);
	query.setOwner(owner);

	queryDao.save(query);

	PersistentQuery actual = queryDao.find(query.getId());

	Assert.assertEquals(query, actual);
	Assert.assertEquals(query.getTokens().size(), actual.getTokens().size());

	Assert.assertEquals(query.getTokens().get(0).getToken(), actual.getTokens().get(0).getToken());
	Assert.assertEquals(query.getTokens().get(0).getWeight(), actual.getTokens().get(0).getWeight());

	Assert.assertEquals(query.getTokens().get(1).getToken(), actual.getTokens().get(1).getToken());
	Assert.assertEquals(query.getTokens().get(1).getWeight(), actual.getTokens().get(1).getWeight());
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
    public void testGetQueriesForUserWithTokens() {
	// setup
	PersistentQuery query = new PersistentQuery();
	query.setInterval(1000L);
	query.setQueryString("Extraterrestrial life");

	List<PersistentQueryToken> tokens = new ArrayList<PersistentQueryToken>();

	PersistentQueryToken token1 = new PersistentQueryToken();
	token1.setToken("extraterrest");
	token1.setWeight(1.25f);
	token1.setParentQuery(query);
	tokens.add(token1);

	PersistentQueryToken token2 = new PersistentQueryToken();
	token2.setToken("life");
	token2.setWeight(0.25f);
	token2.setParentQuery(query);
	tokens.add(token2);

	query.setTokens(tokens);

	User owner = new User();
	owner.setId(1L);
	query.setOwner(owner);

	queryDao.save(query);

	// execute
	List<PersistentQuery> queries = queryDao.getQueriesForUser(1L);

	// verify
	Assert.assertEquals(3, queries.size());

	boolean containsNewQuery = false;
	for (PersistentQuery persistentQuery : queries) {
	    if (persistentQuery.getId().equals(query.getId())) {
		containsNewQuery = true;
	    }
	}
	
	Assert.assertTrue(containsNewQuery);

	int i = 0;
	for (PersistentQueryToken token : queries.get(0).getTokens()) {
	    assertEquals(tokens.get(i).getId(), token.getId());

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
