select *
from employee
where
    first_name like '%y%'
    or first_name like '%w%'
order by id