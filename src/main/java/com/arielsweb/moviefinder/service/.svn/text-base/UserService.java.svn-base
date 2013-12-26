package com.arielsweb.moviefinder.service;

import com.arielsweb.moviefinder.model.User;

public interface UserService extends GenericService<User> {
    /**
     * Sets the is_online flag into the database
     * 
     * @param id
     *            the id of the user for which to set the flag
     */
    void setOnline(Long id);

    /**
     * Gets the user from the database by his username
     * 
     * @param username
     *            the username
     * @return the User entity having username
     */
    public User getUserByUsername(String username);
}
