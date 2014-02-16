package com.arielsweb.moviefinder.service.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.model.PersistentQuery;
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
	SQLQuery query = createSQLQuery(
		"SELECT " + queryColumns + " FROM " + getTableName() + " query INNER JOIN "
			+ Utils.getTableForEntity(User.class) + " ON query.owner = user.id WHERE user.id = "
			+ userId).addEntity(PersistentQuery.class);

	List<PersistentQuery> queries = query.list();
	return queries;
    }

    @Override
    protected String getTableName() {
	return Utils.getTableForEntity(PersistentQuery.class);
    }
}
