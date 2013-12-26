SELECT 
    DISTINCT *
FROM
    tab_movie_crew_person
        INNER JOIN
    moviefinder_scalability_test.tab_cast_crew_in_movie ON tab_movie_crew_person.id = tab_cast_crew_in_movie.cast_and_crew_id
        INNER JOIN
    tab_movie_descriptors ON tab_cast_crew_in_movie.movie_id = tab_movie_descriptors.id
		INNER JOIN
	tab_movie_genres ON tab_movie_genres.movie_descriptor_id = tab_movie_descriptors.id
where
    movie_id = 715;