SELECT name, count(*) AS total
FROM people
JOIN credits ON credits.person_id = people.id
JOIN genres ON genres.movie_id = credits.movie_id
WHERE genre LIKE 'Drama' AND type LIKE 'writer'
GROUP BY name
ORDER BY total desc, name
LIMIT 10;
