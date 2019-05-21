select *
from employee
where
    first_name like '%i%'
    and length(last_name) < 5
    and '2004-01-01' <= works_since
order by id