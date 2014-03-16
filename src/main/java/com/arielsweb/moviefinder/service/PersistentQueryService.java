package com.arielsweb.moviefinder.service;

import java.util.List;

import com.arielsweb.moviefinder.model.PersistentQuery;
import com.arielsweb.moviefinder.model.PersistentQueryToken;

/**
 * Service dealing with database operations related to {@link PersistentQuery}
 * 
 * @author Ariel
 * 
 */
public interface PersistentQueryService extends GenericService<PersistentQuery> {
    /**
     * Gets the queries that belong to users that are online
     * 
     * @return the {@link List} of {@link PersistentQuery}ies
     */
    List<PersistentQuery> getQueriesForOnlineUsers();

    /**
     * Gets the list of {@PersistentQuery}ies for a specific
     * {@link User} (given by its userId)
     * 
     * @param userId
     *            the id of the user to get the list of            {@PersistentQuery
     * }ies
     * @return the {@link List} of {@link PersistentQuery}ies
     */
    List<PersistentQuery> getQueriesForUser(Long userId);
    
    /**
     * Gets query tokens from a query string
     * 
     * @return the list of {@link PersistentQueryToken}
     */
    List<PersistentQueryToken> getQueryTokens(PersistentQuery persistentQuery);
}
