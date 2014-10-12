package com.arielsweb.moviefinder.utilities;

public class MovieFinderConstants {
	public static final String UPDATE_NULL_ERROR = "You can't update a null entity";
	public static final String SAVE_NULL_ERROR = "You can't save a null entity";
	public static final String DELETE_NULL_ERROR = "You can't delete a null entity";
	public static final String INCORRECT_ID_UPDATE_ERROR = "You can't update an entity having an Id <= 0";
	public static final String INCORRECT_GET_BY_USERNAME = "You can't select an user by a null or empty username";

	/** Database column name constants **/
	public static final String USER_IS_ONLINE = "is_online";
	public static final String USER_REAL_NAME = "real_name";
	public static final String USER_ID = "user_id";
	public static final String CAST_AND_CREW_ID = "cast_crew_id";
	public static final String MOVIE_ID = "movie_id";
	public static final String FULL_NAME = "full_name";
	public static final String TYPE = "type";

	public static final String STR_EMPTY = "";
	public static final String STR_SPACE = " ";

	public static final String RT_API_KEY = "t7ks4968aka9q96g42wm3z4b";
	public static final String RT_BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0/";
}
