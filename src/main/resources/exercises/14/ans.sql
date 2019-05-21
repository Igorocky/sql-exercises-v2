select *
from employee
where
    last_name like '%l'
    or last_name like '%t'
    or last_name like '%y'
    or last_name like '%e'
order by id