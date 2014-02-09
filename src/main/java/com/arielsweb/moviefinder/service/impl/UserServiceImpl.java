package com.arielsweb.moviefinder.service.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.arielsweb.moviefinder.model.MovieDescriptor;
import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.UserService;
import com.arielsweb.moviefinder.service.exceptions.DataAccessException;
import com.arielsweb.moviefinder.utilities.MovieFinderConstants;
import com.arielsweb.moviefinder.utilities.Utils;

/**
 * Deals with reading and writing the datastore for users
 * 
 * @author Ariel
 * @date 1/12/2011
 */
@Repository("userService")
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    @Override
    public void setOnline(Long id) {
	if (id > 0) {
	    SQLQuery query = createSQLQuery("UPDATE " + tableName + " SET "
		    + MovieFinderConstants.USER_IS_ONLINE + "=1" + " WHERE id=" + id
		    + ";");
	    query.executeUpdate();
	} else {
	    log.debug(this.getClass() + ":"
		    + MovieFinderConstants.INCORRECT_ID_UPDATE_ERROR);
	    throw new DataAccessException(this.getClass() + ":"
		    + MovieFinderConstants.INCORRECT_ID_UPDATE_ERROR);
	}
    }
    
    @Override
    @Transactional
    public User getUserByUsername(String username) {
	if (username != null && !username.isEmpty()) {
	    SQLQuery query = createSQLQuery("SELECT * from " + tableName + " WHERE username='" + username + "';").addEntity(entityBeanType);;
	    @SuppressWarnings("unchecked")
	    List<User> users = query.list();
	    if (users.size() == 1) {
		return users.get(0);
	    } else {
		return null;
	    }
	} else {
	    log.debug(this.getClass() + ":"
		    + MovieFinderConstants.INCORRECT_GET_BY_USERNAME);
	    throw new DataAccessException(this.getClass() + ":"
		    + MovieFinderConstants.INCORRECT_GET_BY_USERNAME);
	}
    }

    @Override
    protected String getTableName() {
	return Utils.getTableForEntity(User.class);
    }

    @Override
    @Transactional
    public void saveRelevantResult(Long userId, MovieDescriptor movieDescriptor) {
	User user = this.find(userId);
	user.getRelevantMovies().add(movieDescriptor);

	super.save(user);
    }

}
