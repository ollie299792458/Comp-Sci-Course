match (p1:Person)-[:ACTS_IN]->(m:Movie)<-[:ACTS_IN]-(p2:Person)
where p1.name = 'Lawrence, Jennifer (III)'
  and p2.name <> 'Lawrence, Jennifer (III)'
with p2.name as name, count(m) as total
where total > 1
return name, total
order by name;
