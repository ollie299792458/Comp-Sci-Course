match gpath = (m1:Movie)-[:HAS_GENRE*2]-(m2:Movie),
      kpath = (m1:Movie)-[:HAS_KEYWORD*2]-(m2:Movie),
      apath = (m1:Movie)-[:ACTS_IN*2]-(m2:Movie)
where m1.title = 'Skyfall (2012)'
  and m2.title <> 'Skyfall (2012)'
with count(distinct(kpath)) + 10*count(distinct(apath)) as score,
     m2.title as title
return title, score
order by score desc;
