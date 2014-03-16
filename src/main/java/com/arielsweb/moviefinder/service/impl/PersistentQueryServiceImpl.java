package com.arielsweb.moviefinder.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.index.util.TextParsingHelper;
import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.PersistentQueryService;
import com.arielsweb.moviefinder.utilities.Utils;

/**
 * Extracts {@link PersistentQuery}ies from the datastore
 * 
 * @author Ariel
 * 
 */
@Repository("persistentQueryService")
public class PersistentQueryServiceImpl extends GenericServiceImpl<PersistentQuery> implements PersistentQueryService {

    private static final String queryColumns = "query.id, query.query_string, query.owner, query.interv ";

    /**
     * Gets the queries that belong to users that are online
     * */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PersistentQuery> getQueriesForOnlineUsers() {
	SQLQuery query = createSQLQuery(
		"SELECT " + queryColumns + " FROM " + getTableName() + " query INNER JOIN "
			+ Utils.getTableForEntity(User.class) + " ON query.owner = user.id "
			+ "WHERE user.is_online = " + 1)
		.addEntity(PersistentQuery.class);

	List<PersistentQuery> queries = query.list();
	return queries;
    }

    /**
     * Gets the list of {@PersistentQuery}ies for a specific
     * {@link User} (given by its userId)
     **/
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PersistentQuery> getQueriesForUser(Long userId) {
	List<PersistentQuery> queries = sessionFactory.getCurrentSession().createCriteria(PersistentQuery.class)
		.add(Restrictions.eq("owner.id", userId))
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

	return queries;
    }

    @Override
    public List<PersistentQueryToken> getQueryTokens(PersistentQuery persistentQuery) {
	String[] queryTokens = TextParsingHelper.parseText(persistentQuery.getQueryString());

	Map<String, Float> queryWeights = TextParsingHelper.getQueryWeights(queryTokens);

	List<PersistentQueryToken> persistentQueryTokens = new ArrayList<PersistentQueryToken>();

	for (Map.Entry<String, Float> entry : queryWeights.entrySet()) {
	    PersistentQueryToken persistentQueryToken = new PersistentQueryToken();
	    persistentQueryToken.setToken(entry.getKey());
	    persistentQueryToken.setWeight(entry.getValue());
	    persistentQueryToken.setParentQuery(persistentQuery);

	    persistentQueryTokens.add(persistentQueryToken);
	}
	return persistentQueryTokens;
    }

    @Override
    protected String getTableName() {
	return Utils.getTableForEntity(PersistentQuery.class);
    }
}
