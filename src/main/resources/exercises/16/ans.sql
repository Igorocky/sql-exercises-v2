select *
from employee
where
    (dep_id = 1 or dep_id=4)
    and (first_name like 'A%'
                or first_name like 'E%'
                or first_name like 'I%'
                or first_name like 'O%'
                or first_name like 'U%'
                or first_name like 'Y%')
order by id