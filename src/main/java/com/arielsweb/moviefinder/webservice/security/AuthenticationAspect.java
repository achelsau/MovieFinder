package com.arielsweb.moviefinder.webservice.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arielsweb.moviefinder.model.User;
import com.arielsweb.moviefinder.service.UserService;
import com.arielsweb.moviefinder.webservice.security.exception.InvalidAuthenticationCredentials;

/**
 * Performs authentication for each request that goes to the controllers of the
 * web-service <code>(com.arielsweb.moviefinder.webservice.controllers.*)</code>
 * 
 * @author Ariel
 *
 */
@Aspect
@Component
public class AuthenticationAspect {

	@Autowired
	private UserService userService;

	/** The log. */
	private final org.apache.log4j.Logger LOGGER = Logger.getLogger(AuthenticationAspect.class);

	@Before(value = "execution(* com.arielsweb.moviefinder.webservice.controllers.*.*(..)) && args(..,user)")
	public void authenticate(JoinPoint joinPoint, User user) throws InvalidAuthenticationCredentials {
		LOGGER.warn("authentication is being done");
		LOGGER.warn("hijacked : " + joinPoint.getSignature().getName());

		HttpServletRequest request = null;
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}

		String credentials = (String) request.getHeader("Authorization");
		String[] usernameAndPasswd = credentials.split(":");
		String username = usernameAndPasswd[0];
		String password = usernameAndPasswd[1];

		User retrievedUser = userService.getUserByUsername(username);

		if (retrievedUser == null || !retrievedUser.getPassword().equals(password)) {
			LOGGER.error("Ooops! Bad credentials!");
			throw new InvalidAuthenticationCredentials("The username and password combination is not correct");
		}

		copyUserInformation(retrievedUser, user);

		LOGGER.warn("Good to go! Authentication succeded.");
	}

	/**
	 * Copies the info taken from the database about the user currently
	 * executing the WS operation
	 * 
	 * @param retrievedUser
	 *            the user retrieved from the DB
	 * @param parameterUser
	 *            the user passed as a parameter to the WS operation
	 */
	private void copyUserInformation(User retrievedUser, User parameterUser) {
		parameterUser.setId(retrievedUser.getId());
		parameterUser.setIsOnline(retrievedUser.getIsOnline());
		parameterUser.setLocation(retrievedUser.getLocation());
		parameterUser.setPassword(retrievedUser.getPassword());
		parameterUser.setRealName(retrievedUser.getRealName());
		parameterUser.setRelevantMovies(retrievedUser.getRelevantMovies());
		parameterUser.setRole(retrievedUser.getRole());
		parameterUser.setUsername(retrievedUser.getUsername());
	}
}
