SELECT P1.name AS name1, P2.name AS name2, title
FROM movies
JOIN credits AS C1 ON C1.movie_id = movies.id
JOIN credits AS C2 ON C2.movie_id = C1.movie_id
JOIN people AS P1 ON P1.id = C1.person_id
JOIN people AS P2 ON P2.id = C2.person_id
WHERE (C1.position = 1) AND (C2.position = 1) AND (P1.name < P2.name)
ORDER BY name1, name2, title;
