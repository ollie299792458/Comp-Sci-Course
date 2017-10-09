match (g:Genre)
match path = allshortestpaths((g:Genre)-[:HAS_GENRE*]-(g2:Genre))
where g.genre < g2.genre
return distinct g.genre as genre1, g2.genre as genre2, length(path)/2 as length
order by length desc, genre1, genre2;
