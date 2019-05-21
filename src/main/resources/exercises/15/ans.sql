select *
from employee
where first_name like '%d%' or first_name like 'D%'
order by id